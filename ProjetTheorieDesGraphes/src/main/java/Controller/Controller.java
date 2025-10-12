package Controller;

import Helpers.FileHelper;
import Vue.GraphDrawer;
import Modele.Graphe;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    @FXML
    private MenuItem IdOpenGraph;

    @FXML
    private TextArea textArea;

    @FXML
    private Canvas graphCanvas;

    // ---------------------------
    // Chargement manuel via menu
    // ---------------------------
    @FXML
    private void onOpenGraphClicked() {
        Stage stage = (Stage) IdOpenGraph.getParentPopup().getOwnerWindow();
        String filepath = FileHelper.chooseFile(stage);
        if (filepath != null) {
            loadAndDrawGraph(filepath);
        }
    }

    // ---------------------------
    // Méthode d'initialisation automatique
    // ---------------------------
    @FXML
    public void initialize() {
        // Fichier de graphe par défaut
        String defaultGraphFile = "/graphes/graphe.txt"; // chemin relatif depuis le répertoire de lancement
        loadAndDrawGraph(defaultGraphFile);
    }

    // ---------------------------
    // Fonction commune pour charger et dessiner
    // ---------------------------
    private void loadAndDrawGraph(String filepath) {
        try {
            Graphe g = FileHelper.loadGraphFromFile(filepath);
            g.printMatrix(); // optionnel : affiche la matrice dans la console
            GraphDrawer gd = new GraphDrawer(g);
            gd.drawGraph(graphCanvas);

            // Optionnel : afficher le contenu dans la TextArea
            if (textArea != null) {
                textArea.clear();
                textArea.setText(g.toString()); // ou une méthode qui renvoie le graphe sous forme de texte
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
