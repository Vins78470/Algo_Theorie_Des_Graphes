package Controller;

import Helpers.FileHelper;
import Modele.*;
import Vue.GraphDrawer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private MenuItem IdOpenGraph;

    @FXML
    private SplitPane mainSplitPane;

    @FXML
    private TextArea stepsTextArea;

    @FXML
    private AnchorPane canvasAnchorPane;
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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // --- Canvas responsive ---
        graphCanvas.widthProperty().bind(canvasAnchorPane.widthProperty());
        graphCanvas.heightProperty().bind(canvasAnchorPane.heightProperty());

        Runnable redrawGraph = () -> {
            if (currentGraph != null) {
                GraphDrawer gd = new GraphDrawer(currentGraph);
                gd.drawGraph(graphCanvas);
               // gd.drawStepManagerSequentially(graphCanvas, stepManager, 500);
            }
        };

        graphCanvas.widthProperty().addListener((obs, oldVal, newVal) -> redrawGraph.run());
        graphCanvas.heightProperty().addListener((obs, oldVal, newVal) -> redrawGraph.run());

        // --- Charger graphe par défaut ---
        Platform.runLater(() -> {

            String defaultGraphFile = "../graphes/graphe.txt";
            File f = new File(defaultGraphFile);
            if (f.exists()) {
                loadAndDrawGraph(defaultGraphFile);
            } else {
                currentGraph = GraphManager.initDefaultGraph(graphCanvas, stepsTextArea);
                redrawGraph.run();
            }

            // Initialiser les ComboBox et l’interface
            if (currentGraph != null) {
                initVertexComboBoxes(currentGraph);
                updateVertexComboBoxes();
            }

            updateAlgorithmAvailability();

            startComboBox.setDisable(true);
            endComboBox.setDisable(true);
            if (algorithmComboBox != null) {
                algorithmComboBox.setOnAction(e -> updateVertexComboBoxes());
            }

        });


    }


    private void redrawGraph() {
        if (currentGraph != null) {
            GraphDrawer gd = new GraphDrawer(currentGraph);
            gd.drawGraph(graphCanvas);
            // Si tu veux aussi dessiner les étapes du stepManager :
            //gd.drawStepManagerSequentially(graphCanvas, stepManager, 500);
        }
    }


    private void drawInitialGraph() {
        if (currentGraph != null) {
            GraphDrawer gd = new GraphDrawer(currentGraph);
            gd.drawGraph(graphCanvas);
        }
    }
    @FXML
    private void onOpenGraphClicked()
    {
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

                // Récupérer le sommet de départ
                String startVertex = startComboBox.getValue();
                int startIndex = currentGraph.getAllVertexNames().indexOf(startVertex);


                res = GraphManager.runDFS((DFS) currentAlgo, currentGraph, startIndex);
            }

            case "Parcours en largeur (BFS)" -> {
                currentAlgo = new BFS();

                String startVertex = startComboBox.getValue();
                int startIndex = currentGraph.getAllVertexNames().indexOf(startVertex);

                res = GraphManager.runBFS((BFS) currentAlgo, currentGraph, startIndex);
            }

            case "Kruskal" -> {
                currentAlgo = new Kruskal();
                res = GraphManager.runKruskal((Kruskal) currentAlgo, currentGraph);
            }

            case "Prim" -> {
                currentAlgo = new Prim();

                String startVertex = startComboBox.getValue();
                int startIndex = currentGraph.getAllVertexNames().indexOf(startVertex);

                res = GraphManager.runPrim((Prim) currentAlgo, currentGraph, startIndex);
            }

            case "Dijkstra" -> {
                currentAlgo = new Dijkstra();

                String startVertex = startComboBox.getValue();
                String endVertex = endComboBox.getValue();

                int startIndex = currentGraph.getAllVertexNames().indexOf(startVertex);
                int endIndex = currentGraph.getAllVertexNames().indexOf(endVertex);

                res = GraphManager.runDijkstra((Dijkstra) currentAlgo, currentGraph, startIndex, endIndex);
            }


            case "Bellman-Ford" -> {
                currentAlgo = new BellmanFord();

                String startVertex = startComboBox.getValue();
                String endVertex = endComboBox.getValue();

                int startIndex = currentGraph.getAllVertexNames().indexOf(startVertex);
                int endIndex = currentGraph.getAllVertexNames().indexOf(endVertex);

                res = GraphManager.runBellmanFord((BellmanFord) currentAlgo, currentGraph, startIndex, endIndex);
            }

            case "Floyd" -> {
                currentAlgo = new FloydWarshall();
                String startVertex = startComboBox.getValue();
                String endVertex = endComboBox.getValue();
                int startIndex = currentGraph.getAllVertexNames().indexOf(startVertex);
                int endIndex = currentGraph.getAllVertexNames().indexOf(endVertex);
                res = GraphManager.runFloydWarshall((FloydWarshall) currentAlgo, currentGraph,startIndex,endIndex);
            }


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

        // Exécute l'algorithme choisi
        choiceAlgorithm(); // Cette méthode doit instancier currentAlgo et remplir res

        if (res != null) {
            stepsTextArea.setText(res[0]);
            resultTextArea.setText(res[1]);
        }

        GraphDrawer gD = new GraphDrawer(currentGraph);

        // Dessine le graphe de base
        gD.drawGraph(graphCanvas);

        // Récupérer le chemin final depuis l'algo
        List<Integer> finalPath = new ArrayList<>();
        if (currentAlgo instanceof DFS dfsAlgo) {
            finalPath = dfsAlgo.getFinalPath();
        }
        else if (currentAlgo instanceof BFS bfsAlgo) {
            finalPath = bfsAlgo.getFinalPath();
        }
        else if (currentAlgo instanceof Kruskal kruskalAlgo) {
            finalPath = kruskalAlgo.getFinalPath();
        }

        // Dessiner uniquement le chemin final (sommets violets, arêtes rouges)
        gD.drawFinalPath(graphCanvas, currentGraph, finalPath);
    }


    private void loadAndDrawGraph(String filepath) {
        try {
            currentGraph = FileHelper.loadGraphFromFile(filepath);
            currentGraph.printMatrix();
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

        //algorithmComboBox.getSelectionModel().selectFirst();
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
