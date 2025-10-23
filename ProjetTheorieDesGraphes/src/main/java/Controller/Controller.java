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

/**
 * Contrôleur principal de l’application.
 *
 * Cette classe joue le rôle d’intermédiaire entre la vue JavaFX (interface graphique)
 * et le modèle (algorithmes et structures de graphes). Elle permet à l’utilisateur de :
 * - Charger un graphe depuis un fichier texte (.txt)
 * - Sélectionner un algorithme à exécuter (BFS, DFS, Dijkstra, Kruskal, Prim, Bellman-Ford, Floyd-Warshall)
 * - Choisir les sommets de départ et d’arrivée
 * - Visualiser dynamiquement le résultat des algorithmes grâce à la librairie SmartGraph
 *
 * Le contrôleur suit le modèle de conception MVC :
 * - Modèle : algorithmes et structures de données
 * - Vue : interface FXML avec JavaFX
 * - Contrôleur : coordination des interactions entre les deux
 */
public class Controller implements Initializable {

    /** Élément du menu permettant de charger un graphe depuis un fichier texte. */
    @FXML
    private MenuItem IdOpenGraph;

    /** Panneau principal de la fenêtre, séparant la zone du graphe et les zones de texte. */
    @FXML
    private SplitPane mainSplitPane;

    /** Zone d’affichage des étapes détaillées d’un algorithme. */
    @FXML
    private TextArea stepsTextArea;

    /** Conteneur où est affiché le graphe. */
    @FXML
    private AnchorPane canvasAnchorPane;

    /** Zone de texte affichant le résultat final d’un algorithme. */
    @FXML
    private TextArea resultTextArea;

    /** Liste déroulante permettant de sélectionner un algorithme. */
    @FXML
    private ComboBox<String> algorithmComboBox;

    /** Liste déroulante pour choisir le sommet de départ. */
    @FXML
    private ComboBox<String> startComboBox;

    /** Liste déroulante pour choisir le sommet d’arrivée. */
    @FXML
    private ComboBox<String> endComboBox;

    /** Référence vers l’algorithme actuellement sélectionné. */
    private Object currentAlgo;

    /** Graphe actuellement chargé dans l’application. */
    private Graphe currentGraph;

    /** Tableau contenant le résultat et les étapes de l’exécution d’un algorithme. */
    private String[] res;

    /** Panneau graphique utilisé pour afficher le graphe à l’aide de SmartGraph. */
    private SmartGraphPanel<String, String> smartGraphPanel;

    /** Indique si le SmartGraphPanel a déjà été lié à l’AnchorPane (évite les doublons). */
    private boolean bound = false;

    /**
     * Méthode appelée automatiquement à l’ouverture de l’application.
     * Elle initialise la vue, charge un graphe par défaut si disponible,
     * configure les ComboBox et prépare les interactions utilisateur.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(() -> {
            String defaultGraphFile = "../graphes/graphe.txt";
            File f = new File(defaultGraphFile);

            // Si un graphe par défaut existe, on le charge, sinon on crée un graphe vide
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

    /**
     * Lie le panneau graphique SmartGraph à l’AnchorPane pour qu’il s’adapte automatiquement
     * lors du redimensionnement de la fenêtre.
     */
    private void bindSmartGraphToPane() {
        if (bound) return; // Empêche un double bind
        bound = true;
        smartGraphPanel.prefWidthProperty().bind(canvasAnchorPane.widthProperty());
        smartGraphPanel.prefHeightProperty().bind(canvasAnchorPane.heightProperty());
    }

    /**
     * Affiche un graphe dans la fenêtre grâce à la librairie SmartGraph.
     * Configure les styles des sommets et arêtes pour une meilleure lisibilité.
     *
     * @param g le graphe à afficher
     */
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

    /**
     * Permet à l’utilisateur de choisir un fichier texte (.txt) et de charger
     * un graphe à partir de la matrice d’adjacence contenue dans le fichier.
     */
    @FXML
    private void onOpenGraphClicked() {
        Stage stage = (Stage) IdOpenGraph.getParentPopup().getOwnerWindow();
        String filepath = Helpers.FileHelper.chooseFile(stage);
        if (filepath != null) {
            loadAndDisplayGraph(filepath);
            updateAlgorithmAvailability();
        }
    }

    /**
     * Charge un graphe depuis un fichier texte, l’affiche dans la fenêtre
     * et initialise les zones de texte associées.
     *
     * @param filepath chemin du fichier texte contenant la matrice d’adjacence
     */
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

    /**
     * Sélectionne et exécute l’algorithme choisi par l’utilisateur.
     * Vérifie également la compatibilité avec la présence de poids négatifs.
     */
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

    /**
     * Exécute l’algorithme choisi et met à jour la vue.
     * Affiche les étapes, le résultat final et colore le chemin trouvé.
     */
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

        // Récupère et met en évidence le chemin final
        List<Integer> finalPath = List.of();
        if (currentAlgo instanceof DFS dfsAlgo) finalPath = dfsAlgo.getFinalPath();
        else if (currentAlgo instanceof BFS bfsAlgo) finalPath = bfsAlgo.getFinalPath();
        else if (currentAlgo instanceof Kruskal kruskalAlgo) finalPath = kruskalAlgo.getFinalPath();
        else if (currentAlgo instanceof Prim) finalPath = Prim.getFinalPath();
        else if (currentAlgo instanceof Dijkstra) finalPath = Dijkstra.getFinalPath();
        else if (currentAlgo instanceof BellmanFord bellmanFord) finalPath = bellmanFord.getFinalPath();
        else if (currentAlgo instanceof FloydWarshall fw) finalPath = FloydWarshall.getFinalPath();

        GraphManager.highlightPathAnimated(smartGraphPanel, currentGraph, finalPath, 1000);
    }

    /**
     * Met à jour dynamiquement la liste des algorithmes disponibles
     * selon le type de graphe (orienté/non orienté, poids négatifs/positifs).
     */
    private void updateAlgorithmAvailability() {
        if (currentGraph == null || algorithmComboBox == null) return;

        boolean hasNeg = GraphManager.hasNegativeWeight(currentGraph);
        boolean isDirected = currentGraph.isOriented();

        algorithmComboBox.getItems().clear();

        if (isDirected) {
            if (hasNeg) algorithmComboBox.getItems().addAll("Bellman-Ford", "Floyd");
            else algorithmComboBox.getItems().addAll(
                    "Parcours en profondeur (DFS)",
                    "Parcours en largeur (BFS)",
                    "Dijkstra",
                    "Bellman-Ford",
                    "Floyd"
            );
        } else {
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

        boolean hasAlgo = !algorithmComboBox.getItems().isEmpty();
        startComboBox.setDisable(!hasAlgo);
        endComboBox.setDisable(!hasAlgo);
    }

    /**
     * Initialise les listes déroulantes de sommets pour la sélection du départ et de l’arrivée.
     *
     * @param graphe graphe actuellement chargé
     */
    private void initVertexComboBoxes(Graphe graphe) {
        List<String> vertexNames = graphe.getAllVertexNames();
        if (vertexNames != null && !vertexNames.isEmpty()) {
            startComboBox.getItems().setAll(vertexNames);
            endComboBox.getItems().setAll(vertexNames);
            startComboBox.getSelectionModel().select(0);
            endComboBox.getSelectionModel().select(0);
        }
    }

    /**
     * Met à jour l’état (activé ou désactivé) des ComboBox de sommets
     * en fonction de l’algorithme sélectionné.
     */
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
