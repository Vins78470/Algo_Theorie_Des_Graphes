package org.example.projettheoriedesgraphes;

import Controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import java.io.IOException;

public class VueApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/org/example/projettheoriedesgraphes/vue.fxml")
        );

        // Charge la vue et récupère le root
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Projet Théorie des Graphes");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

}
