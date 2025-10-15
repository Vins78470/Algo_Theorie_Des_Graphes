package Modele;

import java.util.*;

public class FloydWarshall {

    private double[][] W;
    private int[][] P;
    private String[] noms;

    public FloydWarshall() {}

    // Calcul complet avec affichage des étapes
    private String computeFloydWarshall(Graphe g) {
        int n = g.getMatrix().length;
        noms = new String[n];
        for (int i = 0; i < n; i++) noms[i] = g.getVertexName(i);

        W = new double[n][n];
        P = new int[n][n];
        int[][] M = g.getMatrix();

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

        for (int i = 0; i < n; i++) {
            if (W[i][i] < 0) {
                sb.append("⚠️ Cycle absorbant détecté (poids négatif).\n");
                return sb.toString();
            }
        }

        sb.append("=== Matrice finale des plus courts chemins ===\n");
        appendMatrix(sb);
        return sb.toString();
    }

    // Affichage de la matrice avec bordures et largeur adaptée
    private void appendMatrix(StringBuilder sb) {
        int n = W.length;
        int cellWidth = 9; // largeur fixe plus grande pour noms longs

        // Ligne de séparation
        String sep = "+";
        for (int i = 0; i <= n; i++) sep += "-".repeat(cellWidth) + "+";

        sb.append(sep).append("\n");

        // En-tête
        sb.append("|").append(String.format("%" + cellWidth + "s", ""));
        for (String nom : noms) sb.append("|").append(String.format("%" + cellWidth + "s", nom));
        sb.append("|\n").append(sep).append("\n");

        // Contenu
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

    // Méthode publique pour afficher étapes + chemin
    public String getResult(Graphe g, int start, int end) {
        String etapes = computeFloydWarshall(g);

        if (start < 0 || start >= noms.length || end < 0 || end >= noms.length)
            return etapes + "Indice invalide.";

        if (W[start][end] == Double.POSITIVE_INFINITY)
            return etapes + "Aucun chemin n’existe.";

        List<String> chemin = new ArrayList<>();
        int v = end;
        while (v != -1) {
            chemin.add(noms[v]);
            v = P[start][v];
        }
        Collections.reverse(chemin);

        etapes += "Chemin le plus court de " + noms[start] + " à " + noms[end] + " : " +
                String.join(" → ", chemin) + "   →   Distance = " + (int) W[start][end] + "\n";

        return etapes;
    }
}
