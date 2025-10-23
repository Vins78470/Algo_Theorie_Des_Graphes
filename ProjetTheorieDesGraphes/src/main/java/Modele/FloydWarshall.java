package Modele;

import java.util.*;

/**
 * Implémentation de l’algorithme de Floyd-Warshall.
 *
 * Cet algorithme calcule les plus courts chemins entre toutes les paires de sommets
 * d’un graphe pondéré, qu’il soit orienté ou non.
 *
 * Le principe repose sur la mise à jour progressive d’une matrice de distances :
 * à chaque itération, on introduit un sommet intermédiaire k et on vérifie si le
 * chemin i → k → j est plus court que le chemin direct i → j.
 *
 * Il peut gérer des poids négatifs, mais pas de cycles absorbants
 * (somme négative infinie).
 *
 * L’implémentation conserve :
 * - la matrice des distances (W)
 * - la matrice des prédécesseurs (P)
 * - le chemin final entre deux sommets spécifiques
 * - un texte descriptif détaillant chaque étape du calcul
 */
public class FloydWarshall {

    /** Matrice des distances (W[i][j] = distance minimale entre i et j). */
    private double[][] W;

    /** Matrice des prédécesseurs (P[i][j] = indice du prédécesseur de j sur le plus court chemin depuis i). */
    private int[][] P;

    /** Tableau contenant les noms des sommets pour l’affichage. */
    private String[] noms;

    /** Liste du chemin final utilisé pour la visualisation graphique. */
    private static List<Integer> finalPath = new ArrayList<>();

    /** Constructeur par défaut. */
    public FloydWarshall() {}

    /**
     * Exécute le calcul complet de l’algorithme de Floyd-Warshall
     * et génère un texte détaillant les étapes de mise à jour.
     *
     * @param g le graphe sur lequel exécuter l’algorithme
     * @return chaîne décrivant les différentes étapes du calcul
     */
    private String computeFloydWarshall(Graphe g) {
        int n = g.getMatrix().length;
        noms = new String[n];
        for (int i = 0; i < n; i++) noms[i] = g.getVertexName(i);

        W = new double[n][n];
        P = new int[n][n];
        int[][] M = g.getMatrix();

        // --- Initialisation des matrices ---
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) W[i][j] = 0;
                else if (M[i][j] != 0) W[i][j] = M[i][j];
                else W[i][j] = Double.POSITIVE_INFINITY;

                P[i][j] = (M[i][j] != 0) ? i : -1;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== Matrice initiale ===\n");
        appendMatrix(sb);

        // --- Étapes principales (introduction des sommets intermédiaires) ---
        for (int k = 0; k < n; k++) {
            sb.append("=== Étape k = ").append(k).append(" ===\n");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (W[i][k] != Double.POSITIVE_INFINITY && W[k][j] != Double.POSITIVE_INFINITY
                            && W[i][k] + W[k][j] < W[i][j]) {
                        W[i][j] = W[i][k] + W[k][j];
                        P[i][j] = P[k][j];
                    }
                }
            }
            appendMatrix(sb);
        }

        // --- Détection de cycle absorbant ---
        for (int i = 0; i < n; i++) {
            if (W[i][i] < 0) {
                sb.append("Cycle absorbant détecté (poids négatif).\n");
                return sb.toString();
            }
        }

        sb.append("=== Matrice finale des plus courts chemins ===\n");
        appendMatrix(sb);
        return sb.toString();
    }

    /**
     * Ajoute à la chaîne donnée une représentation lisible de la matrice courante.
     * Chaque cellule contient la distance minimale entre deux sommets, ou ∞ si aucun chemin n’existe.
     *
     * @param sb StringBuilder dans lequel la matrice est ajoutée
     */
    private void appendMatrix(StringBuilder sb) {
        int n = W.length;
        int cellWidth = 9; // Largeur fixe pour l’alignement visuel

        // Ligne de séparation
        String sep = "+";
        for (int i = 0; i <= n; i++) sep += "-".repeat(cellWidth) + "+";

        sb.append(sep).append("\n");

        // En-tête (noms des sommets)
        sb.append("|").append(String.format("%" + cellWidth + "s", ""));
        for (String nom : noms) sb.append("|").append(String.format("%" + cellWidth + "s", nom));
        sb.append("|\n").append(sep).append("\n");

        // Contenu (valeurs de la matrice)
        for (int i = 0; i < n; i++) {
            sb.append("|").append(String.format("%" + cellWidth + "s", noms[i]));
            for (int j = 0; j < n; j++) {
                String val = (W[i][j] == Double.POSITIVE_INFINITY) ? "∞" : String.valueOf((int) W[i][j]);
                sb.append("|").append(String.format("%" + cellWidth + "s", val));
            }
            sb.append("|\n").append(sep).append("\n");
        }
        sb.append("\n");
    }

    /**
     * Construit le chemin le plus court entre deux sommets
     * à partir de la matrice des prédécesseurs.
     *
     * @param start indice du sommet source
     * @param end indice du sommet destination
     */
    private void buildFinalPath(int start, int end) {
        finalPath.clear();

        if (W[start][end] == Double.POSITIVE_INFINITY) {
            return; // Aucun chemin n’existe
        }

        List<String> cheminStr = new ArrayList<>();
        int v = end;
        while (v != -1) {
            cheminStr.add(noms[v]);
            v = P[start][v];
        }
        Collections.reverse(cheminStr);

        // Conversion des noms en indices
        for (String nomVille : cheminStr) {
            for (int i = 0; i < noms.length; i++) {
                if (noms[i].equals(nomVille)) {
                    finalPath.add(i);
                    break;
                }
            }
        }
    }

    /**
     * Exécute l’algorithme complet et retourne un rapport contenant :
     * - les matrices à chaque étape
     * - le plus court chemin trouvé entre les deux sommets spécifiés
     *
     * @param g graphe à traiter
     * @param start indice du sommet de départ
     * @param end indice du sommet d’arrivée
     * @return chaîne de texte contenant le déroulement complet et le résultat final
     */
    public String getResult(Graphe g, int start, int end) {
        String etapes = computeFloydWarshall(g);

        if (start < 0 || start >= noms.length || end < 0 || end >= noms.length)
            return etapes + "Indice invalide.";

        buildFinalPath(start, end);

        if (W[start][end] == Double.POSITIVE_INFINITY)
            return etapes + "Aucun chemin n'existe.";

        etapes += "Chemin le plus court de " + noms[start] + " à " + noms[end] + " : ";

        if (!finalPath.isEmpty()) {
            StringBuilder cheminStr = new StringBuilder();
            for (int i = 0; i < finalPath.size(); i++) {
                if (i > 0) cheminStr.append(" → ");
                cheminStr.append(noms[finalPath.get(i)]);
            }
            etapes += cheminStr.toString();
        }

        etapes += "   →   Distance = " + (int) W[start][end] + "\n";

        return etapes;
    }

    /**
     * Retourne la liste des indices correspondant au plus court chemin calculé.
     * Cette liste est utilisée pour la mise en surbrillance dans l’affichage graphique.
     *
     * @return liste des indices du chemin final
     */
    public static List<Integer> getFinalPath() {
        return new ArrayList<>(finalPath);
    }
}
