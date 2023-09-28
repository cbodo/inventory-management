package com.cbodo.inventorymanagement.controller;

import com.cbodo.inventorymanagement.model.InHouse;
import com.cbodo.inventorymanagement.model.Inventory;
import com.cbodo.inventorymanagement.model.Part;
import com.cbodo.inventorymanagement.model.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * MainController class handles the logic for the main screen of the application.
 *
 * FUTURE ENHANCEMENTS:
 *  Allow multiple items to be selected in a table view to allow multiple deletions at once, while throwing error if
 *  user attempts to modify multiple selected items.
 *  Implement undo/redo functionality for adding, updating, and deleting items.
 *
 *  RUNTIME ERROR:
 *   User was not prompted with an alert before clicking the close button in the title bar of application. Added
 *   setOnCloseRequest listener to addPart, modifyPart, addProduct, modifyProduct methods to alert user before
 *   windows are closed via the title bar.
 *
 * @author Craig Bodo
 */

public class MainController implements Initializable {
    /**
     *  Parts TableView
     */
    @FXML private TableView<Part> partsTableView;

    /**
     * Parts Table ID Column
     */
    @FXML private TableColumn<Part, Integer> partIdColumn;
    /**
     * Parts Table Name Column
     */
    @FXML private TableColumn<Part, String> partNameColumn;
    /**
     * Parts Table Stock Column
     */
    @FXML private TableColumn<Part, Integer> partInvColumn;
    /**
     * Parts Table Price Column
     */
    @FXML private TableColumn<Part, Double> partPriceColumn;

    /**
     * Parts Search TextField
     */
    @FXML private TextField partSearchField;

    /**
     * Products TableView
     */
    @FXML private TableView<Product> productsTableView;

    /**
     * Products Table ID Column
     */
    @FXML private TableColumn<Product, Integer> productIdColumn;
    /**
     * Products Table Name Column
     */
    @FXML private TableColumn<Product, String> productNameColumn;
    /**
     * Products Table Stock Column
     */
    @FXML private TableColumn<Product, Integer> productInvColumn;
    /**
     * Products Table Price Column
     */
    @FXML private TableColumn<Product, Double> productPriceColumn;
    /**
     * Products Search TextField
     */
    @FXML private TextField productSearchField;
    /**
     * Exit Scene Button component
     */
    @FXML private Button exitButton;

    /**
     * Checks if TextField input contains an integer.
     * @param input String input to be compared.
     * @return Boolean that indicates if input is numeric.
     */
    public boolean isNotNumeric(String input) {
        return !input.matches("(|[0-9][0-9]*)?");
    }

    /**
     * Shows Label alert based on validation status.
     * @param label Validation label for current field.
     * @param color Color of label text for alert.
     * @param value Value of setText for label.
     */
    public void showLabel(Label label, String color, String value) {
        label.setText(value);
        label.setStyle("-fx-font-family: 'sans-serif';");
        label.setTextFill(Paint.valueOf(color));
        label.setVisible(true);
    }

    /**
     * Removes leading zeros from input for text fields requiring an integer input.
     * @param input Input to be checked.
     * @return String with leading zeros removed or null if input is empty.
     */
    public String removeLeadingZero(String input) {
        if (!input.isEmpty()) {
            if (input.charAt(0) == '0' && input.length() > 1) {
                if (input.charAt(1) != '.') {
                    return input.substring(1);
                }
            }
            return input;
        }
        return null;
    }

    /**
     * Formats TextFields expecting only integers by removing any whitespace or non-integer characters.
     * @param input Input to be formatted.
     * @return String that has all non-integer characters removed.
     */
    public String formatIntegerInput(String input) {
        input = input.replaceAll("[^\\d]", "");
        input = removeLeadingZero(input);
        return input;
    }

    /**
     * Checks if the TextField is empty.
     * @param field TextField to check.
     * @param label Label to display error message.
     * @return Boolean that indicates if field contains input.
     */
    public boolean inputIsEmpty(TextField field, Label label) {
        Border errorBorder = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2),
                new BorderWidths(1.5)));

        if (field.getText() == null || field.getText().isEmpty()) {
            showLabel(label, "red", "Required");
            field.setBorder(errorBorder);
            return true;
        }
        return false;
    }

    /**
     * Checks for numeric input in TextFields requiring numeric input.
     * @param field TextField to check.
     * @param label Label to display error message.
     * @return Boolean that indicates if field contains numeric input.
     */
    public boolean inputIsNumeric(TextField field, Label label) {
        Border errorBorder = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2),
                new BorderWidths(1.5)));
        field.setText(formatIntegerInput(field.getText()));

        if (!field.getText().matches("(|[0-9][0-9]*)?")) {
            showLabel(label, "red", "Please enter a number.");
            field.setBorder(errorBorder);
            return false;
        }
        return true;
    }

    /**
     * Executes inputIsEmpty() and inputIsNumeric for TextFields requiring numeric input.
     * @param field TextField to check.
     * @param label Label to display error message.
     * @return Boolean that indicates if TextField is both not empty and numeric.
     */
    public boolean inputIsValid(TextField field, Label label) {
        return !inputIsEmpty(field, label) && inputIsNumeric(field, label);
    }

    /**
     * Capitalizes String input and removes extra whitespace.
     * @param input String to be trimmed and capitalized.
     * @return String input that has all whitespace removed and has been capitalized.
     */
    public String capitalize(TextField input) {
        String text = input.getText().trim();
        String[] words = text.split("\\s+");

        for (int i = 0; i < words.length; ++i) {
            words[i] = words[i].substring(0,1).toUpperCase() + words[i].substring(1).toLowerCase();
        }

        input.setText(String.join(" ", words));
        return String.join(" ", words);
    }

    /**
     * Shows alert for delete and cancel actions.
     * @param event WindowEvent object to handle cancel case and consume the event.
     * @param stage stage to be closed in confirm case.
     */
    public void alert(WindowEvent event, Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Canceling");
        alert.setHeaderText("Canceling");
        alert.setContentText("Are you sure you want to cancel?\nChanges will NOT be saved.");
        alert.getDialogPane().setPrefSize(350,200);
        alert.getDialogPane().setStyle("-fx-font-family: 'sans-serif';");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.orElse(null) == ButtonType.OK) {
            stage.close();
        }
        event.consume();
    }

    /**
     * Shows alert depending on action.
     * @param code Indicates which alert to display.
     * @param type Indicates type of alert to display.
     * @return ButtonType's showAndWait method to capture result if needed.
     */
    public Optional<ButtonType> alert(int code, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.getDialogPane().setPrefSize(350,200);
        alert.getDialogPane().setStyle("-fx-font-family: 'sans-serif';");

        switch(code) {
            case 0:
                alert.setTitle("No Item Selected");
                alert.setHeaderText("No Item Selected" );
                alert.setContentText("Please select an item.");
                break;
            case 1:
                alert.setTitle("Remove Item");
                alert.setHeaderText("Remove Item");
                alert.setContentText("Are you sure you want to remove this item?");
                break;
            case 2:
                alert.setTitle("Exit");
                alert.setHeaderText("Exit");
                alert.setContentText("Are you sure you want to exit?");
                break;
            case 3:
                alert.setTitle("Error");
                alert.setHeaderText("Error saving item.");
                alert.setContentText("Please make sure all fields are correct.");
                break;
            case 4:
                alert.setTitle("Cancel");
                alert.setHeaderText("Are you sure you want to cancel?");
                alert.setContentText("Changes to item will NOT be saved.");
                break;
            case 5:
                alert.setTitle("Cannot Delete Item");
                alert.setHeaderText("Cannot Delete Item");
                alert.setContentText("Product has associated parts and cannot be removed.");
        }
        return alert.showAndWait();
    }

    /**
     * Handles part filtering function for search field.
     */
    public void filterParts() {
        partsTableView.getSelectionModel().clearSelection();
        productsTableView.getSelectionModel().clearSelection();

        String input = partSearchField.getText();

        if (!input.isEmpty()) {
            if (isNotNumeric(input)) {
                partsTableView.setItems(Inventory.lookupPart(input));
            }
            else {
                int partId = Integer.parseInt(input);
                String name = Objects.requireNonNull(Inventory.lookupPart(partId)).getName();
                partsTableView.setItems(Inventory.lookupPart(name));
            }
            if (Inventory.lookupPart(input) == null) {
                partsTableView.setPlaceholder(new Label("No Parts found.\nPlease try again."));
            }
        }
        else {
            partsTableView.setItems(Inventory.getAllParts());
        }
    }

    /**
     * Handles add part button action.
     * @throws IOException Exception handling for FXML file loading.
     */
    public void addPartButtonAction() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/cbodo/inventorymanagement/view/add-part.fxml"));
        Parent parent = loader.load();

        AddPartController controller = loader.getController();
        controller.initData();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add New Part");
        stage.setScene(new Scene(parent));
        stage.getScene().getRoot().setStyle("-fx-font-family: 'sans-serif';");
        stage.show();

        stage.setOnCloseRequest(event -> alert(event, stage));
        partsTableView.getSelectionModel().clearSelection();
    }

    /**
     * Handles modify part button action.
     * @throws IOException Exception handling for FXML file loading.
     */
    @FXML public void modifyPartButtonAction() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/cbodo/inventorymanagement/view/modify-part.fxml"));
        Parent parent = loader.load();

        if(partsTableView.getSelectionModel().getSelectedItem() != null) {
            ModifyPartController controller = loader.getController();
            controller.initData(partsTableView.getSelectionModel().getSelectedItem());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modify Part");
            stage.setScene(new Scene(parent));
            stage.getScene().getRoot().setStyle("-fx-font-family: 'sans-serif';");
            stage.show();

            stage.setOnCloseRequest(event -> alert(event, stage));
            partsTableView.getSelectionModel().clearSelection();
        }
        else {
            alert(0, Alert.AlertType.ERROR);
        }
    }

    /**
     * Handles delete part button action.
     */
    @FXML public void deletePartButtonAction() {
        if(partsTableView.getSelectionModel().getSelectedItem() != null) {
            Part partSelected = partsTableView.getSelectionModel().getSelectedItem();

            Optional<ButtonType> result = alert(1, Alert.AlertType.CONFIRMATION);

            if(result.isPresent() && result.get() == ButtonType.OK) {
                if (Inventory.deletePart(partSelected)) {
                    System.out.println("Part deleted.");
                }
            }
            partsTableView.getSelectionModel().clearSelection();
        }
        else {
            alert(0, Alert.AlertType.ERROR);
        }
    }

    /**
     * Handles product search action.
     */
    public void filterProducts() {
        partsTableView.getSelectionModel().clearSelection();
        productsTableView.getSelectionModel().clearSelection();

        String input = productSearchField.getText();

        if (!input.isEmpty()) {
            if (isNotNumeric(input)) {
                productsTableView.setItems(Inventory.lookupProduct(input));
            }
            else {
                int productId = Integer.parseInt(input);
                String name = Objects.requireNonNull(Inventory.lookupProduct(productId)).getName();
                productsTableView.setItems(Inventory.lookupProduct(name));
            }
            if (Inventory.lookupProduct(input) == null) {
                productsTableView.setPlaceholder(new Label("No Products found.\nPlease try again."));
            }
        }
        else {
            productsTableView.setItems(Inventory.getAllProducts());
        }
    }

    /**
     * Handles add product button action.
     * @throws IOException Exception handling for FXML file loading.
     */
    @FXML public void addProductButtonAction() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/cbodo/inventorymanagement/view/add-product.fxml"));
        Parent parent = loader.load();

        AddProductController controller = loader.getController();
        controller.initData();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add New Product");
        stage.setScene(new Scene(parent));
        stage.getScene().getRoot().setStyle("-fx-font-family: 'sans-serif';");
        stage.show();

        stage.setOnCloseRequest(event -> alert(event, stage));
        productsTableView.getSelectionModel().clearSelection();
    }

    /**
     * Handles modify product button action.
     * @throws IOException Exception handling for FXML file loading.
     */
    @FXML public void modifyProductButtonAction() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/cbodo/inventorymanagement/view/modify-product.fxml"));
        Parent parent = loader.load();

        if(productsTableView.getSelectionModel().getSelectedItem() != null) {
            ModifyProductController controller = loader.getController();
            controller.initData(productsTableView.getSelectionModel().getSelectedItem());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modify Product");
            stage.setScene(new Scene(parent));
            stage.getScene().getRoot().setStyle("-fx-font-family: 'sans-serif';");
            stage.show();

            stage.setOnCloseRequest(event -> alert(event, stage));
            productsTableView.getSelectionModel().clearSelection();
        }
        else {
            alert(0, Alert.AlertType.ERROR);
        }
    }

    /**
     * Handles delete product button action.
     */
    @FXML public void deleteProductButtonAction() {
        if(productsTableView.getSelectionModel().getSelectedItem() != null) {
            Product productSelected = productsTableView.getSelectionModel().getSelectedItem();
            if (!productSelected.getAllAssociatedParts().isEmpty()) {
                alert(5, Alert.AlertType.ERROR);
            }
            else {
                Optional<ButtonType> result = alert(1, Alert.AlertType.CONFIRMATION);

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    if (Inventory.deleteProduct(productSelected)) {
                        System.out.println("Product deleted.");
                    }
                }
            }
            productsTableView.getSelectionModel().clearSelection();
        }
        else {
            alert(0, Alert.AlertType.ERROR);
        }
    }

    /**
     * Initializes Parts and Products tables and populates both with sample data.
     */
    public void initialize(URL url, ResourceBundle rb) {
        NumberFormat priceFormat = NumberFormat.getCurrencyInstance();

        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partPriceColumn.setCellFactory(cell -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
            super.updateItem(price, empty);
            if(empty) {
                setText(null);
            }
            else {
                setText(priceFormat.format(price));
            }
            }
        });

        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productPriceColumn.setCellFactory(cell -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
            super.updateItem(price, empty);
            if(empty) {
                setText(null);
            }
            else {
                setText(priceFormat.format(price));
            }
            }
        });

        Inventory.addPart(new InHouse(1,"Brakes", 15.00,10,0,20,0));
        Inventory.addPart(new InHouse(2,"Wheel",11.00,16,0,20,0));
        Inventory.addPart(new InHouse(3,"Seat",15.00,10,0,20,0));

        Inventory.addProduct(new Product(1000, "Giant Bike", 299.99,5,0,10));
        Inventory.addProduct(new Product(1001, "Tricycle", 99.99,3,0,10));

        partsTableView.setItems(Inventory.getAllParts());
        productsTableView.setItems(Inventory.getAllProducts());

        partsTableView.setOnMouseClicked(mouseEvent -> productsTableView.getSelectionModel().clearSelection());
        productsTableView.setOnMouseClicked(mouseEvent -> partsTableView.getSelectionModel().clearSelection());
    }

    /**
     * Handles exit button action.
     */
    @FXML public void handleExitButtonAction() {
        Stage stage = (Stage) exitButton.getScene().getWindow();

        Optional<ButtonType> result = alert(2, Alert.AlertType.CONFIRMATION);

        if(result.orElse(null) == ButtonType.OK) {
            stage.close();
        }
    }

}
