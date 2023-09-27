package com.cbodo.inventorymanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

/**
 *
 * Main class handles launch function of application
 *
 * RUNTIME ERROR:
 * start() method threw an exception due to the use of FXMLLoader. Added IOException to the method in order to handle
 * related exceptions.
 *
 * @author Craig Bodo
 */
public class Main extends Application {
    /**
     * Handles loading the main-form FXML sheet for application.
     * @param stage stage being set
     * @throws IOException handles exceptions related to FXMLLoader
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cbodo/inventorymanagement/view/main-form.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Inventory Management");
        stage.setScene(scene);
        scene.getRoot().setStyle("-fx-font-family: 'sans-serif';");
        stage.show();

        stage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle("Exit");
            alert.setHeaderText("Exiting Program");
            alert.setContentText("Are you sure you want to exit?");
            alert.getDialogPane().setPrefSize(350,200);
            alert.getDialogPane().setStyle("-fx-font-family: 'sans-serif';");

            Optional<ButtonType> result = alert.showAndWait();

            if(result.orElse(null) == ButtonType.OK) {
                stage.close();
                System.exit(0);
            }

            event.consume();
        });
    }

    /**
     * Launches application
     */
    public static void main(String[] args) {
        launch();
    }
}