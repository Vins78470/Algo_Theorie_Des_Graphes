package Modele;

import java.util.*;

/**
 * Implémentation de l'algorithme de Prim pour trouver
 * l'arbre couvrant de poids minimal (MST) d'un graphe pondéré.
 *
 * On peut choisir le sommet de départ.
 */
public class Prim {

    /**
     * Applique l'algorithme de Prim sur le graphe.
     *
     * @param g le graphe pondéré (non orienté)
     * @param startIndex index du sommet de départ
     */
    public static void run(Graphe g, int startIndex) {
        int[][] mat = g.getMatrix();
        int n = mat.length;

        boolean[] visited = new boolean[n]; // sommets déjà inclus dans le MST
        int[] parent = new int[n];          // parent[i] = sommet relié à i dans le MST
        int[] key = new int[n];             // poids minimal pour relier chaque sommet

        // --- Initialisation ---
        Arrays.fill(key, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        key[startIndex] = 0; // point de départ

        // --- Construction du MST ---
        for (int count = 0; count < n - 1; count++) {
            int u = getMinKeyVertex(key, visited);
            visited[u] = true;

            // mise à jour des voisins
            for (int v = 0; v < n; v++) {
                if (mat[u][v] != 0 && !visited[v] && mat[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = mat[u][v];
                }
            }
        }

        // --- Affichage du résultat ---
        System.out.println("\n=== Arbre couvrant minimal (Prim) ===");
        int totalCost = 0;

        for (int i = 0; i < n; i++) {
            if (parent[i] != -1) {
                System.out.printf("%s — %s : %d%n",
                        g.getVertexName(parent[i]),
                        g.getVertexName(i),
                        mat[i][parent[i]]);
                totalCost += mat[i][parent[i]];
            }
        }

        System.out.println("Coût total de l'arbre couvrant = " + totalCost);
    }

    /**
     * Retourne le sommet non visité avec la plus petite clé.
     */
    private static int getMinKeyVertex(int[] key, boolean[] visited) {
        int min = Integer.MAX_VALUE;
        int minIndex = -1;
        for (int i = 0; i < key.length; i++) {
            if (!visited[i] && key[i] < min) {
                min = key[i];
                minIndex = i;
            }
        }
        return minIndex;
    }
}
