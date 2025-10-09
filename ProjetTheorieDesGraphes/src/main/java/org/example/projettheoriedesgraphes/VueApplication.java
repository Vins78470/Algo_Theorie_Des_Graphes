package org.example.projettheoriedesgraphes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class VueApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Chemin vers ton fichier FXML (dans resources)
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/org/example/projettheoriedesgraphes/vue.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Projet Théorie des Graphes");
        stage.setScene(scene);
        stage.show();
    }
}
