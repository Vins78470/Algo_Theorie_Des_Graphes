package Controller;

import Helpers.FileHelper;

import Modele.Graphe;
import javafx.fxml.FXML;
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

    private void onOpenGraphClicked() {
        // Récupère la fenêtre parent
        Stage stage = (Stage) IdOpenGraph.getParentPopup().getOwnerWindow();

        // Ouvre le FileChooser et récupère le chemin
        String filepath = FileHelper.chooseFile(stage);
        if (filepath != null) {
            try {
                // Charge le graphe depuis le fichier
                Graphe g = FileHelper.loadGraphFromFile(filepath);
                g.printMatrix(); // ou autre traitement
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
