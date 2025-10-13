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
    private TextArea stepsTextArea; // pour les étapes (anciennement textArea)

    @FXML
    private TextArea resultTextArea; // pour le résultat final (anciennement stepsTextArea)

    // Algorithme courant sélectionné
    private Object currentAlgo; // peut être DFS, BFS, Kruskal, Prim, Dijkstra, etc.

    // StepManager pour gérer les étapes du graphe
    private StepManager stepManager = new StepManager();


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
        if (currentGraph == null) {
            stepsTextArea.setText("Aucun graphe chargé !");
            return;
        }

        // Réinitialiser StepManager
        stepManager.reset();

        String selectedAlgo = algorithmComboBox.getValue();
        String[] res = null;

        switch (selectedAlgo) {
            case "Parcours en profondeur (DFS)" -> {
                currentAlgo = new DFS();
                // Récupère le StepManager de l'algo
                stepManager = ((DFS) currentAlgo).getStepManager(currentGraph,0);
                res = GraphManager.runDFS((DFS) currentAlgo, currentGraph, 0);
            }
            case "Parcours en largeur (BFS)" -> {
                currentAlgo = new BFS();
                res = GraphManager.runBFS((BFS) currentAlgo, currentGraph,  0);
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
            case "Bellman-Ford" -> {
                stepsTextArea.setText("Bellman-Ford non implémenté");
                return;
            }
            default -> {
                stepsTextArea.setText("Sélection invalide !");
                return;
            }
        }

        if (res != null) {
            stepsTextArea.setText(res[0]);       // Étapes
            resultTextArea.setText(res[1]);      // Résultat final
        }

        // Dessiner les étapes avec GraphDrawer
        GraphDrawer gD = new GraphDrawer(currentGraph);
        gD.drawGraph(graphCanvas);
        gD.drawStepManagerSequentially(graphCanvas,stepManager,500);

    }




    // ---------------------------
    // Initialisation automatique
    // ---------------------------
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
            // initDefaultGraph retourne maintenant le graphe et met à jour stepsTextArea
            currentGraph = GraphManager.initDefaultGraph(graphCanvas, stepsTextArea);
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
           // gd.drawStepManagerSequentially(graphCanvas,stepManager,500);

            if (stepsTextArea != null) {
                stepsTextArea.clear();
                stepsTextArea.setText(currentGraph.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
