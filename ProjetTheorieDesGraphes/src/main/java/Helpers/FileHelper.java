package Helpers;

import Modele.Graphe;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

/**
 * Classe utilitaire regroupant les fonctions liées à la gestion
 * des fichiers dans l’application.
 *
 * Elle permet notamment :
 * - D’ouvrir un sélecteur de fichiers pour charger un graphe depuis un fichier texte
 * - De lire et d’analyser le contenu d’un fichier pour construire un objet Graphe
 * - De détecter automatiquement si le graphe est orienté ou non
 *
 * Cette classe centralise toutes les opérations d’entrée/sortie de fichiers
 * afin de séparer la logique de lecture du reste du programme.
 */
public class FileHelper {

    /**
     * Ouvre une fenêtre de sélection de fichier (FileChooser)
     * permettant à l’utilisateur de choisir un fichier texte (.txt).
     *
     * @param stage fenêtre principale de l’application JavaFX
     * @return le chemin absolu du fichier sélectionné, ou null si aucun fichier n’est choisi
     */
    public static String chooseFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir un fichier texte");

        // On restreint la sélection aux fichiers texte uniquement
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Fichiers TXT (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Affiche la boîte de dialogue et récupère le fichier choisi
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) return file.getAbsolutePath();
        return null;
    }

    /**
     * Charge un graphe à partir du chemin d’un fichier texte.
     * Cette méthode redirige vers la fonction de lecture adaptée au format attendu
     * (ici, une matrice d’adjacence pondérée).
     *
     * @param filepath chemin du fichier à charger
     * @return un objet Graphe construit à partir du fichier
     * @throws IOException si le fichier ne peut pas être lu
     */
    public static Graphe loadGraphFromFile(String filepath) throws IOException {
        return loadGraphFromMatrix(filepath);
    }

    /**
     * Lit un fichier texte contenant une matrice d’adjacence pondérée
     * et construit un objet Graphe correspondant.
     *
     * Le fichier doit suivre le format suivant :
     * Chaque ligne représente une ville (ou un sommet) et commence par son nom,
     * suivi des distances (ou poids) vers les autres villes.
     * Exemple :
     *
     * Paris 0 5 0 8
     * Lyon 5 0 3 0
     * Lille 0 3 0 2
     * Nice 8 0 2 0
     *
     * Cette méthode détecte automatiquement si le graphe est orienté
     * (en comparant la symétrie de la matrice).
     *
     * @param filename chemin du fichier texte contenant la matrice
     * @return un objet Graphe correctement initialisé
     * @throws IOException en cas d’erreur de lecture du fichier
     */
    public static Graphe loadGraphFromMatrix(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        List<String> cityNames = new ArrayList<>();
        List<int[]> rows = new ArrayList<>();

        String line;

        // Lecture du fichier ligne par ligne
        while ((line = br.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length < 2) continue; // on ignore les lignes vides ou invalides

            // Le premier élément correspond au nom de la ville
            cityNames.add(parts[0]);

            // On extrait les valeurs numériques (distances ou poids)
            int[] distances = new int[parts.length - 1];
            for (int i = 1; i < parts.length; i++) {
                // Remplacement des tirets spéciaux pour éviter les erreurs de parsing
                String value = parts[i].trim().replace("–", "-").replace("−", "-");
                distances[i - 1] = Integer.parseInt(value);
            }
            rows.add(distances);
        }
        br.close();

        int n = cityNames.size();

        // Détection automatique si le graphe est orienté (si la matrice n’est pas symétrique)
        boolean oriented = false;
        for (int i = 0; i < n; i++) {
            int[] rowI = rows.get(i);
            for (int j = 0; j < n; j++) {
                if (rowI[j] != rows.get(j)[i]) {
                    oriented = true;
                    break;
                }
            }
            if (oriented) break;
        }

        // Création du graphe avec les informations détectées
        Graphe g = new Graphe(n, oriented, true, cityNames.toArray(new String[0]));

        // Ajout des arêtes à partir de la matrice
        for (int i = 0; i < n; i++) {
            int[] row = rows.get(i);
            for (int j = 0; j < row.length; j++) {
                if (row[j] != 0) { // 0 = absence d’arête
                    g.addEdge(i, j, row[j]);
                }
            }
        }

        return g;
    }

}
