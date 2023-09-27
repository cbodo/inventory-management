package com.cbodo.inventorymanagement.controller;

import com.cbodo.inventorymanagement.model.InHouse;
import com.cbodo.inventorymanagement.model.Inventory;
import com.cbodo.inventorymanagement.model.Outsourced;
import com.cbodo.inventorymanagement.model.Part;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

/**
 * AddPartController controls logic of Add Part form.
 *
 * FUTURE ENHANCEMENT:
 *  Due to their many similarities, both ModifyPartController and AddPartController can be abstracted into a single
 *  controller class that controls the logic for both form types.
 *
 * RUNTIME ERROR:
 *  Received NumberFormatException in the validateAllFields() method for input strings of Min/Max/Stock TextFields
 *  containing a space or a non-integer character. Created method formatIntegerInput() in MainController to handle
 *  reformatting TextField text in these cases.
 *
 * @author Craig Bodo
 */
public class AddPartController extends MainController {
    /**
     * In-House radio button
     */
    @FXML private RadioButton inHouseRadioButton;
    /**
     * Outsourced radio button
     */
    @FXML private RadioButton outsourcedRadioButton;
    /**
     * Toggle group for In-House and Outsourced radio buttons
     */
    private ToggleGroup inHouseOrOutsourcedGroup;
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
     * Machine ID / Company Name text field
     */
    @FXML private TextField inHouseOrOutsourcedTextField;
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
     * Machine ID / Company Name validation label
     */
    @FXML private Label inHouseOrOutsourcedValidationLabel;
    /**
     * Machine ID / Company Name label for text field
     */
    @FXML private Label inHouseOrOutsourcedLabel;
    /**
     * Save button
     */
    @FXML private Button saveButton;
    /**
     * Cancel button
     */
    @FXML private Button cancelButton;
    /**
     * Holds formatting to indicate text field errors
     */
    private final Border errorBorder = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(2),
            new BorderWidths(1.5)));

    /**
     * Populates ID text field with auto-generated ID.
     */
    public void initData() {
        final int[] id = {0};

        Inventory.getAllParts().forEach((part) -> {
            while (part.getId() >= id[0]) {
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
     * Handles RadioButton change.
     */
    public void handleRadioButtonAction() {
        Toggle currentToggle = inHouseOrOutsourcedGroup.getSelectedToggle();
        inHouseOrOutsourcedValidationLabel.setVisible(false);
        inHouseOrOutsourcedTextField.setBorder(null);

        if (currentToggle.equals(this.inHouseRadioButton)) {
            inHouseOrOutsourcedLabel.setText("Machine ID");
            inHouseOrOutsourcedTextField.setText(null);
            inHouseOrOutsourcedTextField.setPromptText("Machine ID");
            inHouseOrOutsourcedValidationLabel.setVisible(false);
        }
        if (currentToggle.equals(this.outsourcedRadioButton)) {
            inHouseOrOutsourcedLabel.setText("Company Name");
            inHouseOrOutsourcedTextField.setText(null);
            inHouseOrOutsourcedTextField.setPromptText("Company Name");
            inHouseOrOutsourcedValidationLabel.setVisible(false);
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
     * Validates machine ID input.
     * @return Boolean indicating if input passes validation.
     */
    public boolean validateMachineId() {
        if (!inputIsValid(inHouseOrOutsourcedTextField, inHouseOrOutsourcedValidationLabel)) {
            return false;
        }
        else if (!inHouseOrOutsourcedTextField.getText().matches("[1-9]")){
            showLabel(inHouseOrOutsourcedValidationLabel, "red", "Machine ID should not be 0.");
            inHouseOrOutsourcedTextField.setBorder(errorBorder);
            return false;
        }
        inHouseOrOutsourcedTextField.setBorder(null);
        inHouseOrOutsourcedValidationLabel.setVisible(false);
        return true;
    }

    /**
     * Validates company name input.
     * @return Boolean indicating if input passes validation.
     */
    public boolean validateCompanyName() {
        return !inputIsEmpty(inHouseOrOutsourcedTextField, inHouseOrOutsourcedValidationLabel);
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

        if (inHouseOrOutsourcedGroup.getSelectedToggle().equals(inHouseRadioButton)) {
            return validateMachineId() && !allFields.contains(false);
        }
        if (inHouseOrOutsourcedGroup.getSelectedToggle().equals(outsourcedRadioButton)) {
            return validateCompanyName() && !allFields.contains(false);
        }

        return false;
    }

    /**
     * Creates new part object.
     * @return The new part object.
     */
    public Part createPart() {
        Part part = null;

        if (inHouseOrOutsourcedGroup.getSelectedToggle().equals(inHouseRadioButton)) {
            part = new InHouse(
                    Integer.parseInt(idTextField.getText()),
                    capitalize(nameTextField),
                    Double.parseDouble(priceTextField.getText()),
                    Integer.parseInt(invTextField.getText()),
                    Integer.parseInt(minTextField.getText()),
                    Integer.parseInt(maxTextField.getText()),
                    Integer.parseInt(inHouseOrOutsourcedTextField.getText()));
        }
        else if (inHouseOrOutsourcedGroup.getSelectedToggle().equals(outsourcedRadioButton)) {
            part = new Outsourced(
                    Integer.parseInt(idTextField.getText()),
                    capitalize(nameTextField),
                    Double.parseDouble(priceTextField.getText()),
                    Integer.parseInt(invTextField.getText()),
                    Integer.parseInt(minTextField.getText()),
                    Integer.parseInt(maxTextField.getText()),
                    capitalize(inHouseOrOutsourcedTextField));
        }
        return part;
    }

    /**
     * Handles save button action.
     */
    public void handleSaveButtonAction() {
        Stage stage = (Stage) saveButton.getScene().getWindow();

        if (validateAllFields()) {
            Part part = createPart();
            Inventory.addPart(part);
            stage.close();
        }
        else {
            alert(3, Alert.AlertType.ERROR);
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
     * Initializes ToggleGroup and RadioButtons.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Configure RadioButton ToggleGroup
        inHouseOrOutsourcedGroup = new ToggleGroup();
        this.inHouseRadioButton.setToggleGroup(inHouseOrOutsourcedGroup);
        this.outsourcedRadioButton.setToggleGroup(inHouseOrOutsourcedGroup);
        this.inHouseOrOutsourcedGroup.selectToggle(inHouseRadioButton);

    }
}
