package Modele;

import java.util.*;

/**
 * Algorithme de Dijkstra :
 * Calcule le plus court chemin entre deux sommets dans un graphe pondéré.
 * Retourne une String affichable dans la console ou le front.
 */
public class Dijkstra {

    private static List<Integer> finalPath = new ArrayList<>();

    public static List<Integer> getFinalPath() {
        return new ArrayList<>(finalPath);
    }

    public static String getResult(Graphe g, int start, int end) {
        StringBuilder sb = new StringBuilder();

        int[][] mat = g.getMatrix();
        int n = mat.length;

        // Initialisation
        int[] dist = new int[n];
        int[] parent = new int[n];
        boolean[] visited = new boolean[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        dist[start] = 0;

        sb.append("Sommet de départ : ").append(g.getVertexName(start)).append("\n");
        sb.append("Sommet d'arrivée : ").append(g.getVertexName(end)).append("\n\n");

        // --- Algorithme principal ---
        for (int count = 0; count < n - 1; count++) {
            int u = minDistance(dist, visited);
            if (u == -1) break; // tous les sommets atteignables ont été traités
            visited[u] = true;

            // Mise à jour des distances
            for (int v = 0; v < n; v++) {
                if (!visited[v] && mat[u][v] != 0 && dist[u] != Integer.MAX_VALUE
                        && dist[u] + mat[u][v] < dist[v]) {
                    dist[v] = dist[u] + mat[u][v];
                    parent[v] = u;
                }
            }
        }

        // --- Construction du chemin final ---
        finalPath.clear();
        if (dist[end] == Integer.MAX_VALUE) {
            sb.append("Aucun chemin trouvé entre ")
                    .append(g.getVertexName(start))
                    .append(" et ")
                    .append(g.getVertexName(end))
                    .append(".\n");
        } else {
            // Reconstruire le chemin
            for (int v = end; v != -1; v = parent[v]) {
                finalPath.add(v);
            }
            Collections.reverse(finalPath);

            // Vérifier que le chemin commence bien par le sommet de départ
            if (!finalPath.isEmpty() && finalPath.get(0) == start) {
                sb.append("Distance minimale : ").append(dist[end]).append("\n");
                sb.append("Chemin le plus court : ").append(reconstructPath(g, finalPath)).append("\n");
            } else {
                finalPath.clear();
                sb.append("Aucun chemin disponible.\n");
            }
        }

        return sb.toString();
    }

    // Trouve le sommet non visité avec la plus petite distance
    private static int minDistance(int[] dist, boolean[] visited) {
        int min = Integer.MAX_VALUE;
        int minIndex = -1;
        for (int v = 0; v < dist.length; v++) {
            if (!visited[v] && dist[v] <= min) {
                min = dist[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    // Reconstruit le chemin à partir de la liste d'indices
    private static String reconstructPath(Graphe g, List<Integer> path) {
        List<String> names = new ArrayList<>();
        for (int v : path) names.add(g.getVertexName(v));
        return String.join(" → ", names);
    }
}