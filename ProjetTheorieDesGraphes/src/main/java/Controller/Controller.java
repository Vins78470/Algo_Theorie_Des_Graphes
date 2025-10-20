package Controller;

import Modele.*;
import Modele.GraphManager;

import com.brunomnsilva.smartgraph.graphview.SmartArrow;
import com.brunomnsilva.smartgraph.graphview.SmartGraphEdgeNode;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
    private ComboBox<String> startComboBox;

    @FXML
    private ComboBox<String> endComboBox;

    private Object currentAlgo;
    private Graphe currentGraph;
    private String[] res;

    private SmartGraphPanel<String, String> smartGraphPanel;
    private boolean bound = false; // pour binder le resize une seule fois

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(() -> {
            String defaultGraphFile = "../graphes/graphe.txt";
            File f = new File(defaultGraphFile);
            if (f.exists()) {
                loadAndDisplayGraph(defaultGraphFile);
            } else {
                currentGraph = GraphManager.initDefaultGraph(null, stepsTextArea);
                displaySmartGraph(currentGraph);
            }

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

    /** --- Bind automatique pour redraw lors du resize --- */
    private void bindSmartGraphToPane() {
        if (bound) return; // bind une seule fois
        bound = true;

        // Ne pas redraw à chaque resize, juste resize le panel
        smartGraphPanel.prefWidthProperty().bind(canvasAnchorPane.widthProperty());
        smartGraphPanel.prefHeightProperty().bind(canvasAnchorPane.heightProperty());
    }

    /** --- Convertit et affiche le Graphe avec SmartGraphPanel --- */
    /** --- Convertit et affiche le Graphe avec SmartGraphPanel --- */
    private void displaySmartGraph(Graphe g) {
        smartGraphPanel = GraphManager.buildSmartGraph(g);

        canvasAnchorPane.getChildren().clear();
        canvasAnchorPane.getChildren().add(smartGraphPanel);

        smartGraphPanel.prefWidthProperty().bind(canvasAnchorPane.widthProperty());
        smartGraphPanel.prefHeightProperty().bind(canvasAnchorPane.heightProperty());

        Platform.runLater(() -> {
            smartGraphPanel.init();
            smartGraphPanel.setAutomaticLayout(false);

            // Style des sommets
            String vertexDefault = "-fx-fill: #2E5C8A; -fx-stroke: black;";
            smartGraphPanel.getModel().vertices().forEach(v ->
                    smartGraphPanel.getStylableVertex(v).setStyleInline(vertexDefault)
            );

            // Style des arêtes - tout en noir plein
            String edgeStyle = "-fx-stroke: black; -fx-stroke-width: 1.5; -fx-fill: transparent;";
            smartGraphPanel.getModel().edges().forEach(e -> {
                SmartGraphEdgeNode<?, ?> edgeNode =
                        (SmartGraphEdgeNode<?, ?>) smartGraphPanel.getStylableEdge(e);

                edgeNode.setStyle(edgeStyle);

                SmartArrow arrow = edgeNode.getAttachedArrow();
                if (arrow != null) {
                    arrow.setStyle("-fx-fill: black; -fx-stroke: black; -fx-stroke-width: 1.2;");
                }

                if (edgeNode.getAttachedLabel() != null) {
                    edgeNode.getAttachedLabel().setTranslateY(-8);
                    edgeNode.getAttachedLabel().setTranslateX(-8);
                }
            });
        });

        bindSmartGraphToPane();
    }
    @FXML
    private void onOpenGraphClicked() {
        Stage stage = (Stage) IdOpenGraph.getParentPopup().getOwnerWindow();
        String filepath = Helpers.FileHelper.chooseFile(stage);
        if (filepath != null) {
            loadAndDisplayGraph(filepath);
            updateAlgorithmAvailability();
        }
    }



    private void loadAndDisplayGraph(String filepath) {
        try {
            currentGraph = Helpers.FileHelper.loadGraphFromFile(filepath);
            displaySmartGraph(currentGraph);
            if (stepsTextArea != null) {
                stepsTextArea.clear();
                stepsTextArea.setText(currentGraph.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                int startIndex = currentGraph.getAllVertexNames().indexOf(startComboBox.getValue());
                res = GraphManager.runDFS((DFS) currentAlgo, currentGraph, startIndex);
            }
            case "Parcours en largeur (BFS)" -> {
                currentAlgo = new BFS();
                int startIndex = currentGraph.getAllVertexNames().indexOf(startComboBox.getValue());
                res = GraphManager.runBFS((BFS) currentAlgo, currentGraph, startIndex);
            }
            case "Kruskal" -> {
                currentAlgo = new Kruskal();
                res = GraphManager.runKruskal((Kruskal) currentAlgo, currentGraph);
            }
            case "Prim" -> {
                currentAlgo = new Prim();
                int startIndex = currentGraph.getAllVertexNames().indexOf(startComboBox.getValue());
                res = GraphManager.runPrim((Prim) currentAlgo, currentGraph, startIndex);
            }
            case "Dijkstra" -> {
                currentAlgo = new Dijkstra();
                int startIndex = currentGraph.getAllVertexNames().indexOf(startComboBox.getValue());
                int endIndex = currentGraph.getAllVertexNames().indexOf(endComboBox.getValue());
                res = GraphManager.runDijkstra((Dijkstra) currentAlgo, currentGraph, startIndex, endIndex);
            }
            case "Bellman-Ford" -> {
                currentAlgo = new BellmanFord();
                int startIndex = currentGraph.getAllVertexNames().indexOf(startComboBox.getValue());
                int endIndex = currentGraph.getAllVertexNames().indexOf(endComboBox.getValue());
                res = GraphManager.runBellmanFord((BellmanFord) currentAlgo, currentGraph, startIndex, endIndex);
            }
            case "Floyd" -> {
                currentAlgo = new FloydWarshall();
                int startIndex = currentGraph.getAllVertexNames().indexOf(startComboBox.getValue());
                int endIndex = currentGraph.getAllVertexNames().indexOf(endComboBox.getValue());
                res = GraphManager.runFloydWarshall((FloydWarshall) currentAlgo, currentGraph, startIndex, endIndex);
            }
            default -> res = null;
        }
    }

    @FXML
    private void onExecuteClicked() {
        if (currentGraph == null) {
            stepsTextArea.setText("Aucun graphe chargé !");
            return;
        }

        choiceAlgorithm();

        if (res != null) {
            stepsTextArea.setText(res[0]);
            resultTextArea.setText(res[1]);
        }

        // Colorer le chemin final
        List<Integer> finalPath = List.of();
        if (currentAlgo instanceof DFS dfsAlgo) finalPath = dfsAlgo.getFinalPath();
        else if (currentAlgo instanceof BFS bfsAlgo) finalPath = bfsAlgo.getFinalPath();
        else if (currentAlgo instanceof Kruskal kruskalAlgo) finalPath = kruskalAlgo.getFinalPath();
        else if (currentAlgo instanceof Prim) finalPath = Prim.getFinalPath();
        else if (currentAlgo instanceof Dijkstra) finalPath = Dijkstra.getFinalPath();
        else if (currentAlgo instanceof BellmanFord bellmanFord) finalPath = bellmanFord.getFinalPath();
        else if (currentAlgo instanceof FloydWarshall fw) finalPath = FloydWarshall.getFinalPath();

        GraphManager.highlightPathAnimated(smartGraphPanel, currentGraph, finalPath,1000);
    }

    private void updateAlgorithmAvailability() {
        if (currentGraph == null || algorithmComboBox == null) return;

        boolean hasNeg = GraphManager.hasNegativeWeight(currentGraph);
        boolean isDirected = currentGraph.isOriented();

        algorithmComboBox.getItems().clear();

        if (isDirected) {
            // Graphe orienté : désactiver Prim et Kruskal
            if (hasNeg) algorithmComboBox.getItems().addAll("Bellman-Ford", "Floyd");
            else algorithmComboBox.getItems().addAll(
                    "Parcours en profondeur (DFS)",
                    "Parcours en largeur (BFS)",
                    "Dijkstra",
                    "Bellman-Ford",
                    "Floyd"
            );
        } else {
            // Graphe non orienté : tout dispo
            if (hasNeg) algorithmComboBox.getItems().addAll("Bellman-Ford", "Floyd");
            else algorithmComboBox.getItems().addAll(
                    "Parcours en profondeur (DFS)",
                    "Parcours en largeur (BFS)",
                    "Kruskal",
                    "Prim",
                    "Dijkstra",
                    "Bellman-Ford",
                    "Floyd"
            );
        }

        // Désactiver les comboBox si pas d’algos compatibles
        boolean hasAlgo = !algorithmComboBox.getItems().isEmpty();
        startComboBox.setDisable(!hasAlgo);
        endComboBox.setDisable(!hasAlgo);
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
        if (selectedAlgo == null) return;

        switch (selectedAlgo) {
            case "Parcours en profondeur (DFS)", "Parcours en largeur (BFS)" -> {
                startComboBox.setDisable(false);
                endComboBox.setDisable(true);
            }
            case "Dijkstra", "Bellman-Ford", "Floyd" -> {
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
