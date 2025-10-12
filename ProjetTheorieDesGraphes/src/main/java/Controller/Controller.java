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
    private TextArea stepsTextArea; // pour les étapes (anciennement textArea)

    @FXML
    private TextArea resultTextArea; // pour le résultat final (anciennement stepsTextArea)


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

        String[] res;

        switch (selectedAlgo) {
            case "Parcours en profondeur (DFS)":
                res = GraphManager.runDFS(currentGraph, 0);
                break;
            case "Parcours en largeur (BFS)":
                res = GraphManager.runBFS(currentGraph, 0);
                break;
            case "Kruskal":
                res = GraphManager.runKruskal(currentGraph);
                break;
            case "Prim":
                res = GraphManager.runPrim(currentGraph, 0);
                break;
            case "Dijkstra":
                res = GraphManager.runDijkstra(currentGraph, 0, 9);
                break;
            case "Bellman-Ford":
                stepsTextArea.setText("Bellman-Ford non implémenté");
                return;
            default:
                stepsTextArea.setText("Sélection invalide !");
                return;
        }

        // ✅ On met les étapes dans textArea, et le résultat final dans stepsTextArea
        if (res != null) {
            stepsTextArea.setText(res[0]);       // Étapes
            resultTextArea.setText(res[1]);  // Résultat final
        }
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

            if (stepsTextArea != null) {
                stepsTextArea.clear();
                stepsTextArea.setText(currentGraph.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
