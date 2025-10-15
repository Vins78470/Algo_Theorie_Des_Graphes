package Helpers;

import Modele.Graphe;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileHelper {

    /**
     * Ouvre un FileChooser et retourne le chemin du fichier choisi.
     */
    public static String chooseFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir un fichier texte");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Fichiers TXT (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) return file.getAbsolutePath();
        return null;
    }

    /**
     * Charge un graphe à partir du chemin d'un fichier texte.
     * Peut être utilisé après chooseFile().
     */
    public static Graphe loadGraphFromFile(String filepath) throws IOException {
        return loadGraphFromMatrix(filepath);
    }

    /**
     * Charge un graphe à partir d'un fichier texte de type "liste d'arêtes pondérées".
     */
    public static Graphe loadGraphFromMatrix(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        List<String> cityNames = new ArrayList<>();
        List<int[]> rows = new ArrayList<>();

        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length < 2) continue; // ignorer les lignes vides ou invalides

            cityNames.add(parts[0]); // nom de la ville

            int[] distances = new int[parts.length - 1];
            for (int i = 1; i < parts.length; i++) {
                String value = parts[i].trim().replace("–", "-").replace("−", "-");
                distances[i - 1] = Integer.parseInt(value);
            }
            rows.add(distances);
        }
        br.close();

        int n = cityNames.size();

        // --- Détection automatique si le graphe est orienté ---
        boolean oriented = false;
        for (int i = 0; i < n; i++) {
            int[] rowI = rows.get(i);
            for (int j = 0; j < n; j++) {
                if (rowI[j] != rows.get(j)[i]) { // asymétrie détectée
                    oriented = true;
                    break;
                }
            }
            if (oriented) break;
        }

        Graphe g = new Graphe(n, oriented, true, cityNames.toArray(new String[0]));

        for (int i = 0; i < n; i++) {
            int[] row = rows.get(i);
            for (int j = 0; j < row.length; j++) {
                if (row[j] != 0) {
                    g.addEdge(i, j, row[j]);
                }
            }
        }

        return g;
    }


}