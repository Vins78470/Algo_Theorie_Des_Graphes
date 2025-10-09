package Modele;

import java.util.*;

/**
 * Algorithme de Dijkstra – version : plus court chemin entre deux villes données.
 */
public class Dijkstra {

    /**
     * Calcule et affiche le plus court chemin entre deux sommets du graphe.
     *
     * @param g le graphe pondéré non orienté
     * @param startIndex index du sommet de départ
     * @param endIndex index du sommet d'arrivée
     */
    public static void run(Graphe g, int startIndex, int endIndex) {
        int[][] mat = g.getMatrix();
        int n = mat.length;

        int[] dist = new int[n];      // distances minimales
        boolean[] visited = new boolean[n];
        int[] parent = new int[n];    // pour reconstruire le chemin

        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        dist[startIndex] = 0;

        // --- Boucle principale ---
        for (int count = 0; count < n - 1; count++) {
            int u = minDistance(dist, visited, n);
            if (u == -1) break;
            visited[u] = true;

            for (int v = 0; v < n; v++) {
                if (!visited[v] && mat[u][v] != 0 && dist[u] != Integer.MAX_VALUE
                        && dist[u] + mat[u][v] < dist[v]) {
                    dist[v] = dist[u] + mat[u][v];
                    parent[v] = u;
                }
            }
        }

        // --- Affichage du résultat ---
        System.out.println("\n=== Plus court chemin (Dijkstra) ===");
        if (dist[endIndex] == Integer.MAX_VALUE) {
            System.out.println("Aucun chemin entre " + g.getVertexName(startIndex)
                    + " et " + g.getVertexName(endIndex));
        } else {
            System.out.println("De " + g.getVertexName(startIndex) + " à " + g.getVertexName(endIndex) + " :");
            System.out.println("→ Distance minimale : " + dist[endIndex]);
            System.out.println("→ Chemin : " + getPath(g, parent, endIndex));
        }
    }

    /** Trouve le sommet non visité avec la plus petite distance */
    private static int minDistance(int[] dist, boolean[] visited, int n) {
        int min = Integer.MAX_VALUE, minIndex = -1;
        for (int v = 0; v < n; v++) {
            if (!visited[v] && dist[v] <= min) {
                min = dist[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    /** Construit le chemin complet */
    private static String getPath(Graphe g, int[] parent, int j) {
        List<String> path = new ArrayList<>();
        while (j != -1) {
            path.add(g.getVertexName(j));
            j = parent[j];
        }
        Collections.reverse(path);
        return String.join(" → ", path);
    }
}
