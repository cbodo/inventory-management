module inventory.management {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.cbodo.inventorymanagement to javafx.fxml;
    exports com.cbodo.inventorymanagement;
}