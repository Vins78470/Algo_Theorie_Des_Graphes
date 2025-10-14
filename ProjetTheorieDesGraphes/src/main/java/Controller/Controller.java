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
import java.util.List;

public class Controller {

    @FXML
    private MenuItem IdOpenGraph;

    @FXML
    private TextArea stepsTextArea;

    @FXML
    private TextArea resultTextArea;

    @FXML
    private ComboBox<String> algorithmComboBox;

    @FXML
    private Canvas graphCanvas;

    @FXML
    private ComboBox<String> startComboBox;

    @FXML
    private ComboBox<String> endComboBox;

    private Object currentAlgo;

    private StepManager stepManager = new StepManager();
    private Graphe currentGraph;
    private String[] res;  // <-- accessible dans tout le Controller

    @FXML
    public void initialize() {
        String defaultGraphFile = "../graphes/graphe.txt";
        File f = new File(defaultGraphFile);
        if (f.exists()) {
            loadAndDrawGraph(defaultGraphFile);
        } else {
            currentGraph = GraphManager.initDefaultGraph(graphCanvas, stepsTextArea);
        }

        if (currentGraph != null) {
            initVertexComboBoxes(currentGraph);
            updateVertexComboBoxes();
        }

        updateAlgorithmAvailability();

        // Listener pour détecter le changement d'algorithme
        if (algorithmComboBox != null) {
            algorithmComboBox.setOnAction(e -> updateVertexComboBoxes());
        }
    }

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
    // Prépare l'algorithme sélectionné
    // ---------------------------
    private void choiceAlgorithm() {
        if (currentGraph == null) return;

        String selectedAlgo = algorithmComboBox.getValue();
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
            }*/
            default -> res = null;
        }
    }

    // ---------------------------
    // Exécution
    // ---------------------------
    @FXML
    private void onExecuteClicked() {
        if (currentGraph == null) {
            stepsTextArea.setText("Aucun graphe chargé !");
            return;
        }

        stepManager.reset();
        choiceAlgorithm();

        if (res != null) {
            stepsTextArea.setText(res[0]);
            resultTextArea.setText(res[1]);
        }

        GraphDrawer gD = new GraphDrawer(currentGraph);
        gD.drawGraph(graphCanvas);
        gD.drawStepManagerSequentially(graphCanvas, stepManager, 500);
    }

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

    private void updateAlgorithmAvailability() {
        if (currentGraph == null || algorithmComboBox == null) return;

        boolean hasNeg = GraphManager.hasNegativeWeight(currentGraph);
        algorithmComboBox.getItems().clear();

        if (hasNeg) {
            algorithmComboBox.getItems().addAll("Bellman-Ford","Floyd");
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

    private void initVertexComboBoxes(Graphe graphe) {
        List<String> vertexNames = graphe.getAllVertexNames();
        if (vertexNames != null && !vertexNames.isEmpty()) {
            startComboBox.getItems().setAll(vertexNames);
            endComboBox.getItems().setAll(vertexNames);
            startComboBox.getSelectionModel().select(0);
            endComboBox.getSelectionModel().select(0);
        }
    }

    @FXML
    private void updateVertexComboBoxes() {
        if (currentGraph == null || startComboBox == null || endComboBox == null || algorithmComboBox == null)
            return;

        List<String> vertexNames = currentGraph.getAllVertexNames();
        if (vertexNames == null || vertexNames.isEmpty()) return;

        startComboBox.getItems().setAll(vertexNames);
        endComboBox.getItems().setAll(vertexNames);
        startComboBox.getSelectionModel().select(0);
        endComboBox.getSelectionModel().select(0);

        String selectedAlgo = algorithmComboBox.getValue();
        if (selectedAlgo == null) return;  // <-- Protection contre null

        switch (selectedAlgo) {
            case "Parcours en profondeur (DFS)", "Parcours en largeur (BFS)" -> {
                startComboBox.setDisable(false);
                endComboBox.setDisable(true);
            }
            case "Dijkstra" -> {
                startComboBox.setDisable(false);
                endComboBox.setDisable(false);
            }
            case "Bellman-Ford", "Floyd" -> {
                startComboBox.setDisable(false);
                endComboBox.setDisable(false);
            }
            case "Prim" -> {
                startComboBox.setDisable(false);
                endComboBox.setDisable(true);
            }
            case "Kruskal" -> {
                startComboBox.setDisable(true);
                endComboBox.setDisable(true);
            }
            default -> {
                startComboBox.setDisable(true);
                endComboBox.setDisable(true);
            }
        }
    }

}
