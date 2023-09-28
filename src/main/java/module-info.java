module inventory.management {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.cbodo.inventorymanagement to javafx.fxml;
    exports com.cbodo.inventorymanagement;
    exports com.cbodo.inventorymanagement.controller;
    opens com.cbodo.inventorymanagement.controller to javafx.fxml;
    exports com.cbodo.inventorymanagement.model;
    opens com.cbodo.inventorymanagement.model to javafx.base;
}