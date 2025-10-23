package org.example.projettheoriedesgraphes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Classe principale de l’application graphique JavaFX.
 *
 * Cette classe gère le cycle de vie de l’application :
 * elle charge l’interface utilisateur à partir du fichier FXML
 * et initialise la fenêtre principale (stage).
 *
 * Le fichier {@code vue.fxml} définit la structure de l’interface :
 * menus, boutons, zones de texte et zone d’affichage du graphe.
 *
 * L’interaction entre la vue (FXML) et la logique de l’application
 * est assurée par le contrôleur {@link Controller.Controller}.
 *
 */
public class VueApplication extends Application {

    /**
     * Point d’entrée graphique de l’application JavaFX.
     *
     * Cette méthode est automatiquement appelée par le moteur JavaFX
     * après l’initialisation du contexte graphique.
     * Elle charge la vue FXML, définit la scène principale et
     * affiche la fenêtre de l’application.
     *
     * @param stage la fenêtre principale (fourni automatiquement par JavaFX)
     * @throws IOException si le fichier FXML n’est pas trouvé ou invalide
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/org/example/projettheoriedesgraphes/vue.fxml")
        );

        // Chargement du layout FXML et création de la scène principale
        Scene scene = new Scene(fxmlLoader.load());

        // Configuration de la fenêtre
        stage.setTitle("Projet Théorie des Graphes");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
