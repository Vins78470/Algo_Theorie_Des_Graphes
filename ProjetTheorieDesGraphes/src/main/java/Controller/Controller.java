package Controller;

import Helpers.FileHelper;
import Modele.*;
import Vue.GraphDrawer;
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
    private TextArea stepsTextArea; // pour les étapes

    @FXML
    private TextArea resultTextArea; // pour le résultat final

    @FXML
    private ComboBox<String> algorithmComboBox;

    @FXML
    private Canvas graphCanvas;

    // Algo courant
    private Object currentAlgo;

    // StepManager pour l’animation
    private StepManager stepManager = new StepManager();

    // Graphe courant
    private Graphe currentGraph;

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
            currentGraph = GraphManager.initDefaultGraph(graphCanvas, stepsTextArea);
        }

        updateAlgorithmAvailability();
    }

    // ---------------------------
    // Chargement manuel via menu
    // ---------------------------
    @FXML
    private void onOpenGraphClicked() {
        Stage stage = (Stage) IdOpenGraph.getParentPopup().getOwnerWindow();
        String filepath = FileHelper.chooseFile(stage);
        if (filepath != null) {
            loadAndDrawGraph(filepath);
            updateAlgorithmAvailability();
        }
    }

    // ---------------------------
    // Exécution de l’algorithme sélectionné
    // ---------------------------
    @FXML
    private void onExecuteClicked() {
        if (currentGraph == null) {
            stepsTextArea.setText("Aucun graphe chargé !");
            return;
        }

        stepManager.reset();
        String selectedAlgo = algorithmComboBox.getValue();
        String[] res = null;

        boolean hasNeg = GraphManager.hasNegativeWeight(currentGraph);
        if (hasNeg && !(selectedAlgo.equals("Bellman-Ford") || selectedAlgo.equals("Floyd"))) {
            stepsTextArea.setText("Algorithme non disponible pour graphe avec poids négatif !");
            return;
        }

        switch (selectedAlgo) {
            case "Parcours en profondeur (DFS)" -> {
                currentAlgo = new DFS();
                stepManager = ((DFS) currentAlgo).getStepManager(currentGraph, 0);
                res = GraphManager.runDFS((DFS) currentAlgo, currentGraph, 0);
            }
            case "Parcours en largeur (BFS)" -> {
                currentAlgo = new BFS();
                res = GraphManager.runBFS((BFS) currentAlgo, currentGraph, 0);
            }
            case "Kruskal" -> {
                currentAlgo = new Kruskal();
                res = GraphManager.runKruskal((Kruskal) currentAlgo, currentGraph);
            }
            case "Prim" -> {
                currentAlgo = new Prim();
                res = GraphManager.runPrim((Prim) currentAlgo, currentGraph, 0);
            }
            case "Dijkstra" -> {
                currentAlgo = new Dijkstra();
                res = GraphManager.runDijkstra((Dijkstra) currentAlgo, currentGraph, 0, 9);
            }
            /*case "Bellman-Ford" -> {
                currentAlgo = new BellmanFord();
                res = GraphManager.runBellmanFord((BellmanFord) currentAlgo, currentGraph, 0, 9);
            }
            case "Floyd" -> {
                currentAlgo = new Floyd();
                res = GraphManager.runFloyd((Floyd) currentAlgo, currentGraph);
            }
            default -> {
                stepsTextArea.setText("Sélection invalide !");
                return;
            }*/
        }

        if (res != null) {
            stepsTextArea.setText(res[0]);       // Étapes
            resultTextArea.setText(res[1]);      // Résultat final
        }

        GraphDrawer gD = new GraphDrawer(currentGraph);
        gD.drawGraph(graphCanvas);
        gD.drawStepManagerSequentially(graphCanvas, stepManager, 500);
    }

    // ---------------------------
    // Charger et dessiner un fichier
    // ---------------------------
    private void loadAndDrawGraph(String filepath) {
        try {
            currentGraph = FileHelper.loadGraphFromFile(filepath);
            GraphDrawer gd = new GraphDrawer(currentGraph);
            gd.drawGraph(graphCanvas);

            if (stepsTextArea != null) {
                stepsTextArea.clear();
                stepsTextArea.setText(currentGraph.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------
    // Active/désactive les algos selon le graphe
    // ---------------------------
    private void updateAlgorithmAvailability() {
        if (currentGraph == null || algorithmComboBox == null) return;

        boolean hasNeg = GraphManager.hasNegativeWeight(currentGraph);
        algorithmComboBox.getItems().clear();

        if (hasNeg) {
            algorithmComboBox.getItems().addAll(
                    "Bellman-Ford",
                    "Floyd"
            );

        } else {
            algorithmComboBox.getItems().addAll(
                    "Parcours en profondeur (DFS)",
                    "Parcours en largeur (BFS)",
                    "Kruskal",
                    "Prim",
                    "Dijkstra",
                    "Bellman-Ford",
                    "Floyd"
            );
        }

        algorithmComboBox.getSelectionModel().selectFirst();
    }
}
