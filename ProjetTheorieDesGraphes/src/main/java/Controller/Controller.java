package Controller;

import Helpers.FileHelper;
import Modele.GraphManager;
import Vue.GraphDrawer;
import Modele.Graphe;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Controller {

    @FXML
    private MenuItem IdOpenGraph;

    @FXML
    private TextArea textArea; // steps

    @FXML
    private TextArea stepsTextArea; // résultat algo

    @FXML
    private ComboBox<String> algorithmComboBox;

    @FXML
    private Canvas graphCanvas;

    // On stocke le graphe courant ici
    private Graphe currentGraph;

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

    @FXML
    private void onExecuteClicked() {
        String selectedAlgo = algorithmComboBox.getValue(); // récupère la sélection

        if (currentGraph == null) {
            stepsTextArea.setText("Aucun graphe chargé !");
            return;
        }

        switch (selectedAlgo) {
            case "Parcours en profondeur (DFS)":
                stepsTextArea.setText(GraphManager.runDFS(currentGraph, 0));
                break;
            case "Parcours en largeur (BFS)":
                stepsTextArea.setText(GraphManager.runBFS(currentGraph, 0));
                break;
            case "Kruskal":
                stepsTextArea.setText(GraphManager.runKruskal(currentGraph));
                break;
            case "Prim":
                stepsTextArea.setText(GraphManager.runPrim(currentGraph, 0));
                break;
            case "Dijkstra":
                stepsTextArea.setText(GraphManager.runDijkstra(currentGraph, 0, 9));
                break;
            case "Bellman-Ford":
                stepsTextArea.setText("Bellman-Ford non implémenté");
                break;
            default:
                stepsTextArea.setText("Sélection invalide !");
        }
    }

    // ---------------------------
    // Initialisation automatique
    // ---------------------------
    @FXML
    public void initialize() {
        String defaultGraphFile = "../graphes/graphe.txt";

        File f = new File(defaultGraphFile);
        if (f.exists()) {
            loadAndDrawGraph(defaultGraphFile);
        } else {
            currentGraph = GraphManager.initDefaultGraph(graphCanvas, textArea);
        }
    }

    // ---------------------------
    // Charger et dessiner un fichier
    // ---------------------------
    private void loadAndDrawGraph(String filepath) {
        try {
            currentGraph = FileHelper.loadGraphFromFile(filepath);
            GraphDrawer gd = new GraphDrawer(currentGraph);
            gd.drawGraph(graphCanvas);

            if (textArea != null) {
                textArea.clear();
                textArea.setText(currentGraph.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
