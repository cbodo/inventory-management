package com.cbodo.inventorymanagement.controller;

import com.cbodo.inventorymanagement.model.Inventory;
import com.cbodo.inventorymanagement.model.Part;
import com.cbodo.inventorymanagement.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.text.NumberFormat;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * AddProductController controls logic of Add Product form.
 *
 * FUTURE ENHANCEMENT:
 *  Due to their many similarities, both AddProductController and ModifyControllerController can be abstracted into a
 *  single controller class that controls the logic for both form types.
 *  There is also the possibility of further abstracting both product controllers and both part controllers due to
 *  the many similarities they share.
 *
 *  RUNTIME ERROR:
 *   Received NumberFormatException in the validateAllFields() method for input strings of Min/Max/Stock TextFields
 *   containing a space or a non-integer character. Created method formatIntegerInput() in MainController to handle
 *   reformatting TextField text in these cases.
 *
 * @author Craig Bodo
 */
public class AddProductController extends MainController {
    /**
     * Holds ObservableList of all associated parts
     */
    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    /**
     * Holds formatting to indicate text field errors
     */
    private final Border errorBorder = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2),
            new BorderWidths(1.5)));
    /**
     * ID text field
     */
    @FXML private TextField idTextField;
    /**
     * Name text field
     */
    @FXML private TextField nameTextField;
    /**
     * Current stock text field
     */
    @FXML private TextField invTextField;
    /**
     * Price text field
     */
    @FXML private TextField priceTextField;
    /**
     * Max inventory text field
     */
    @FXML private TextField maxTextField;
    /**
     * Min inventory text field
     */
    @FXML private TextField minTextField;
    /**
     * Search field for parts table
     */
    @FXML private TextField partSearchField;
    /**
     * Name validation label
     */
    @FXML private Label nameValidationLabel;
    /**
     * Current stock validation label
     */
    @FXML private Label invValidationLabel;
    /**
     * Price validation label
     */
    @FXML private Label priceValidationLabel;
    /**
     * Max inventory validation label
     */
    @FXML private Label maxValidationLabel;
    /**
     * Min inventory validation label
     */
    @FXML private Label minValidationLabel;
    /**
     * Associated parts validation label
     */
    @FXML private Label associatedPartsLabel;
    /**
     * Table shows all parts in inventory
     */
    @FXML private TableView<Part> allPartsTable;
    /**
     * Table shows associated parts selected for product
     */
    @FXML private TableView<Part> associatedPartsTable;
    /**
     * ID column in all parts table
     */
    @FXML private TableColumn<Part, Integer> allPartsIdColumn;
    /**
     * Name column in all parts table
     */
    @FXML private TableColumn<Part, String> allPartsNameColumn;
    /**
     * Inventory column in all parts table
     */
    @FXML private TableColumn<Part, Integer> allPartsInvColumn;
    /**
     * Price column in all parts table
     */
    @FXML private TableColumn<Part, Double> allPartsPriceColumn;
    /**
     * ID column in associated parts table
     */
    @FXML private TableColumn<Part, Integer> associatedPartsIdColumn;
    /**
     * Name column in associated parts table
     */
    @FXML private TableColumn<Part, String> associatedPartsNameColumn;
    /**
     * Inventory column in associated parts table
     */
    @FXML private TableColumn<Part, Integer> associatedPartsInvColumn;
    /**
     * Price column in associated parts table
     */
    @FXML private TableColumn<Part, Double> associatedPartsPriceColumn;
    /**
     * Save button
     */
    @FXML private Button saveButton;
    /**
     * Cancel button
     */
    @FXML private Button cancelButton;

    /**
     * Populates ID text field with an auto-generated ID.
     */
    public void initData() {
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        final int[] id = {0};

        allPartsTable.setItems(Inventory.getAllParts());
        associatedPartsTable.setPlaceholder(new Label("Please add Associated Parts."));

        allProducts.forEach((product) -> {
            while (product.getId() >= id[0]) {
                id[0] = id[0] +1;
            }
        });

        idTextField.setText(String.valueOf(id[0]));
    }

    /**
     * Removes validation error when text field input changes.
     * @param event KeyEvent action passed by TextField.
     */
    public void clearError(KeyEvent event) {
        TextField field = (TextField) event.getSource();
        Scene scene = field.getScene();

        String s = "TextField";
        int l = s.length();
        String name = field.getId().substring(0,field.getId().length()-l);
        Label label = (Label) scene.lookup("#" + name + "ValidationLabel");

        if (label.isVisible()) {
            label.setVisible(false);
            field.setBorder(null);
        }
    }

    /**
     * Validates name input.
     * @return Boolean indicating if input passes validation.
     */
    public Boolean validateName() {
        return !inputIsEmpty(nameTextField, nameValidationLabel);
    }

    /**
     * Validates price input.
     * @return Boolean indicating if input passes validation.
     */
    public Boolean validatePrice() {
        String input = priceTextField.getText();

        if (inputIsEmpty(priceTextField, priceValidationLabel)) {
            return false;
        }

        input = input.replaceAll("[^\\d.]", "");
        input = removeLeadingZero(input);
        priceTextField.setText(input);

        if (input.matches("0([.]?0?+)*")) {
            showLabel(priceValidationLabel, "red", "Price should not be 0.");
            priceTextField.setBorder(errorBorder);
            return false;
        }
        else if (!input.matches("^[+-]?[0-9]{1,3}(?:,?[0-9]{3})*(?:\\.[0-9]{1,2})?$")) {
            showLabel(priceValidationLabel, "red", "Please enter a valid price (e.g. 1.00).");
            priceTextField.setBorder(errorBorder);
            return false;
        }
        priceValidationLabel.setVisible(false);
        priceTextField.setBorder(null);
        return true;
    }

    /**
     * Validates min input.
     * @return Boolean indicating if input passes validation.
     */
    public Boolean validateMin() {
        if (inputIsValid(minTextField, minValidationLabel)) {
            if (inputIsValid(maxTextField, maxValidationLabel) && inputIsValid(invTextField, invValidationLabel)) {
                int min = Integer.parseInt(minTextField.getText());
                int max = Integer.parseInt(maxTextField.getText());
                int inv = Integer.parseInt(invTextField.getText());

                if (min <= inv && min <= max) {
                    minValidationLabel.setVisible(false);
                    minTextField.setBorder(null);
                    return true;
                }
                if (min > inv && min > max) {
                    showLabel(minValidationLabel,
                            "red",
                            "Min should not be greater than max.\nMin should not be greater than inventory.");
                    minTextField.setBorder(errorBorder);
                }
                else if (min > inv) {
                    showLabel(minValidationLabel, "red", "Min should not be greater than inventory.");
                    minTextField.setBorder(errorBorder);
                }
                else {
                    showLabel(minValidationLabel, "red", "Min should not be greater than max.");
                    minTextField.setBorder(errorBorder);
                }
            }
        }
        return false;
    }

    /**
     * Validates max input.
     * @return Boolean indicating if input passes validation.
     */
    public Boolean validateMax() {
        if (inputIsValid(maxTextField, maxValidationLabel)) {
            if (inputIsValid(minTextField, minValidationLabel) && inputIsValid(invTextField, invValidationLabel)) {
                int min = Integer.parseInt(minTextField.getText());
                int max = Integer.parseInt(maxTextField.getText());
                int inv = Integer.parseInt(invTextField.getText());

                if (min <= inv && min <= max) {
                    maxValidationLabel.setVisible(false);
                    maxTextField.setBorder(null);
                    return true;
                }
                if (min > max && inv > max) {
                    showLabel(maxValidationLabel,
                            "red",
                            "Max should not be less than min.\nMax should not be less than inventory.");
                    maxTextField.setBorder(errorBorder);
                }
                else if (min > max) {
                    showLabel(maxValidationLabel, "red", "Max should not be less than min.");
                    maxTextField.setBorder(errorBorder);
                }
                else {
                    showLabel(maxValidationLabel, "red", "Max should not be less than inventory.");
                    maxTextField.setBorder(errorBorder);
                }
            }
        }
        return false;
    }

    /**
     * Validates inventory input.
     * @return Boolean indicating if input passes validation.
     */
    public boolean validateInv() {
        if (inputIsValid(invTextField, invValidationLabel)) {
            if (inputIsValid(minTextField, minValidationLabel) && inputIsValid(maxTextField, maxValidationLabel)) {
                int min = Integer.parseInt(minTextField.getText());
                int max = Integer.parseInt(maxTextField.getText());
                int inv = Integer.parseInt(invTextField.getText());

                if (min <= inv && inv <= max) {
                    invValidationLabel.setVisible(false);
                    invTextField.setBorder(null);
                    return true;
                }
                if (min > inv && inv > max) {
                    showLabel(invValidationLabel,
                            "red",
                            "Inventory should not be less than min.\nInventory should not be greater than max.");
                    invTextField.setBorder(errorBorder);
                }
                else if (min > inv) {
                    showLabel(invValidationLabel, "red", "Inventory should not be less than min.");
                    invTextField.setBorder(errorBorder);
                }
                else {
                    showLabel(invValidationLabel, "red", "Inventory should not be greater than max.");
                    invTextField.setBorder(errorBorder);
                }
            }
        }
        return false;
    }

    /**
     * Handles validation of all fields.
     * @return Boolean indicating if all fields pass validation.
     */
    public boolean validateAllFields() {
        ObservableList<Boolean> allFields = FXCollections.observableArrayList();

        allFields.add(validateName());
        allFields.add(validatePrice());
        allFields.add(validateMin());
        allFields.add(validateMax());
        allFields.add(validateInv());

        return !allFields.contains(false);
    }

    /**
     * Filters all parts table based on text input in search field.
     */
    public void filterParts() {
        String input = partSearchField.getText();

        if (!input.isEmpty()) {
            if (isNotNumeric(input)) {
                allPartsTable.setItems(Inventory.lookupPart(input));
            }
            else {
                int partId = Integer.parseInt(input);
                String name = Objects.requireNonNull(Inventory.lookupPart(partId)).getName();
                allPartsTable.setItems(Inventory.lookupPart(name));
            }
            if (Inventory.lookupPart(input) == null) {
                allPartsTable.setPlaceholder(new Label("No Parts found.\nPlease try again."));
            }
        }
        else {
            allPartsTable.setItems(Inventory.getAllParts());
        }
    }

    /**
     * Adds associated part to product's associated parts list.
     */
    public void handleAddAssociatedPartButtonAction() {
        if (allPartsTable.getSelectionModel().getSelectedItem() != null) {
            if (!associatedParts.contains(allPartsTable.getSelectionModel().getSelectedItem())) {
                associatedParts.add(allPartsTable.getSelectionModel().getSelectedItem());
                associatedPartsTable.setItems(associatedParts);
                associatedPartsLabel.setVisible(false);
            }
        }
        else {
            alert(0, Alert.AlertType.ERROR);
        }
    }

    /**
     * Removes associated part from product's associated parts list.
     */
    public void handleRemoveAssociatedPartButtonAction() {
        if (associatedPartsTable.getSelectionModel().getSelectedItem() != null) {
            Optional<ButtonType> result = alert(1, Alert.AlertType.CONFIRMATION);
            if(result.isPresent() && result.get() == ButtonType.OK) {
                associatedParts.remove(associatedPartsTable.getSelectionModel().getSelectedItem());
            }
        }
        else {
            alert(0, Alert.AlertType.ERROR);
        }
    }

    /**
     * Creates new product object.
     * @return The new product object.
     */
    public Product createProduct() {
        Product product = new Product(
                Integer.parseInt(idTextField.getText()),
                capitalize(nameTextField),
                Double.parseDouble(priceTextField.getText()),
                Integer.parseInt(invTextField.getText()),
                Integer.parseInt(minTextField.getText()),
                Integer.parseInt(maxTextField.getText()));
        associatedParts.forEach(product::addAssociatedPart);

        return product;
    }

    /**
     * Handles save button action.
     */
    public void handleSaveButtonAction() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        if (validateAllFields()) {
            Product product = createProduct();
            Inventory.addProduct(product);
            stage.close();
        }
        else {
            alert(0, Alert.AlertType.ERROR);
        }
    }

    /**
     * Handles cancel button action.
     */
    @FXML public void handleCancelButtonAction() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();

        Optional<ButtonType> result = alert(4, Alert.AlertType.CONFIRMATION);

        if (result.orElse(null) == ButtonType.OK) {
            stage.close();
        }
    }

    /**
     * Initializes parts table and associated parts table.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        NumberFormat priceFormat = NumberFormat.getCurrencyInstance();

        allPartsIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartsInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        allPartsPriceColumn.setCellFactory(cell -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(priceFormat.format(price));
                }
            }
        });

        associatedPartsIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartsInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        associatedPartsPriceColumn.setCellFactory(cell -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(priceFormat.format(price));
                }
            }
        });
    }
}
