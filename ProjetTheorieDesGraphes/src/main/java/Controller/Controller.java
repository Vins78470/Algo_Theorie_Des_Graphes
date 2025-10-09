package Controller;



import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Controller {

    @FXML
    private MenuItem IdOpenGraph;

    @FXML
    private TextArea textArea; // Assure-toi d'avoir ce TextArea dans ton FXML

    @FXML
    private void onOpenGraphClicked() {
        // Création d'un FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir un fichier texte");

        // Filtre pour n'afficher que les fichiers .txt
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers TXT (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Utilise la fenêtre principale si tu peux
        Stage stage = (Stage) textArea.getScene().getWindow();

        // Ouvre la boîte de dialogue
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {
                // Lire tout le contenu du fichier
                String content = Files.readString(Path.of(file.getAbsolutePath()));

                // Afficher le contenu dans le TextArea
                textArea.setText(content);

            } catch (IOException e) {
                e.printStackTrace();
                textArea.setText("Erreur lors de la lecture du fichier : " + e.getMessage());
            }
        }
    }
}

