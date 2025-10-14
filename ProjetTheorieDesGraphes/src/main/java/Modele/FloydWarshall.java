package Modele;

/**
 * Algorithme de Floyd-Warshall (cours EFREI)
 * Affichage parfaitement aligné des matrices W et P.
 */
public class FloydWarshall {

    public String run(Graphe g) {
        int n = g.getMatrix().length;
        int[][] M = g.getMatrix();
        String[] noms = new String[n];
        for (int i = 0; i < n; i++) noms[i] = g.getVertexName(i);

        // === Initialisation ===
        double[][] W = new double[n][n];
        int[][] P = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) W[i][j] = 0;
                else if (M[i][j] != 0) W[i][j] = M[i][j];
                else W[i][j] = Double.POSITIVE_INFINITY;

                if (M[i][j] != 0) P[i][j] = i;
                else P[i][j] = -1;
            }
        }

        // === Triple boucle principale ===
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (W[i][k] + W[k][j] < W[i][j]) {
                        W[i][j] = W[i][k] + W[k][j];
                        P[i][j] = P[k][j];
                    }
                }
            }
        }

        // === Vérification de cycle absorbant ===
        for (int i = 0; i < n; i++) {
            if (W[i][i] < 0) {
                return "⚠️ Le graphe contient un cycle absorbant (poids négatif).";
            }
        }

        // === Construction du texte ===
        StringBuilder sb = new StringBuilder();
        sb.append("=== Algorithme de Floyd-Warshall ===\n\n");

        int cellWidth = 7; // largeur fixe de chaque cellule (parfait pour alignement console)

        // ---------- Matrice W ----------
        sb.append("Distances minimales (matrice W) :\n\n");

        sb.append(String.format("%-" + cellWidth + "s", "")); // coin vide
        for (String name : noms) sb.append(String.format("%-" + cellWidth + "s", name));
        sb.append("\n");

        for (int i = 0; i < n; i++) {
            sb.append(String.format("%-" + cellWidth + "s", noms[i]));
            for (int j = 0; j < n; j++) {
                String val;
                if (W[i][j] == Double.POSITIVE_INFINITY) val = "∞";
                else val = String.format("%.0f", W[i][j]);
                sb.append(String.format("%-" + cellWidth + "s", val));
            }
            sb.append("\n");
        }

        // ---------- Matrice P ----------
        sb.append("\nTable des prédécesseurs (matrice P) :\n\n");

        sb.append(String.format("%-" + cellWidth + "s", ""));
        for (String name : noms) sb.append(String.format("%-" + cellWidth + "s", name));
        sb.append("\n");

        for (int i = 0; i < n; i++) {
            sb.append(String.format("%-" + cellWidth + "s", noms[i]));
            for (int j = 0; j < n; j++) {
                String pred;
                if (P[i][j] == -1) pred = "-";
                else pred = noms[P[i][j]];
                sb.append(String.format("%-" + cellWidth + "s", pred));
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
