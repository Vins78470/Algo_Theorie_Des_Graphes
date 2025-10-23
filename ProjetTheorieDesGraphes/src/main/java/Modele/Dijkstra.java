package Modele;

import java.util.*;

/**
 * Implémentation de l’algorithme de Dijkstra.
 *
 * Cet algorithme calcule le plus court chemin entre un sommet source
 * et un sommet destination dans un graphe pondéré à poids positifs.
 *
 * Le principe repose sur une exploration progressive :
 * à chaque étape, le sommet non encore visité ayant la plus petite distance
 * estimée est sélectionné, puis ses voisins sont mis à jour si un chemin
 * plus court est trouvé.
 *
 * L’algorithme ne fonctionne que pour des graphes sans arêtes de poids négatif.
 *
 * Cette classe fournit :
 * - un calcul du plus court chemin entre deux sommets
 * - un texte explicatif détaillant les résultats
 * - la liste du chemin final (pour affichage graphique)
 */
public class Dijkstra {

    /** Liste représentant le chemin final trouvé par l’algorithme. */
    private static List<Integer> finalPath = new ArrayList<>();

    /**
     * Retourne le chemin final calculé sous forme d’une liste d’indices de sommets.
     * Cette liste est utilisée pour surligner les arêtes correspondantes
     * lors de l’affichage graphique du résultat.
     *
     * @return une nouvelle liste contenant les indices du plus court chemin
     */
    public static List<Integer> getFinalPath() {
        return new ArrayList<>(finalPath);
    }

    /**
     * Exécute l’algorithme de Dijkstra entre deux sommets du graphe.
     * Calcule les distances minimales et reconstruit le chemin optimal.
     *
     * @param g graphe sur lequel exécuter l’algorithme
     * @param start indice du sommet source
     * @param end indice du sommet destination
     * @return une chaîne de caractères décrivant le résultat du calcul
     */
    public static String getResult(Graphe g, int start, int end) {
        StringBuilder sb = new StringBuilder();

        int[][] mat = g.getMatrix();
        int n = mat.length;

        // --- Initialisation des structures ---
        int[] dist = new int[n];       // Tableau des distances minimales
        int[] parent = new int[n];     // Tableau des prédécesseurs
        boolean[] visited = new boolean[n];  // Marquage des sommets visités
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        dist[start] = 0;

        sb.append("Sommet de départ : ").append(g.getVertexName(start)).append("\n");
        sb.append("Sommet d'arrivée : ").append(g.getVertexName(end)).append("\n\n");

        // --- Boucle principale (n-1 itérations) ---
        for (int count = 0; count < n - 1; count++) {
            int u = minDistance(dist, visited);
            if (u == -1) break; // Tous les sommets atteignables ont été traités
            visited[u] = true;

            // Relaxation des arêtes sortantes du sommet u
            for (int v = 0; v < n; v++) {
                if (!visited[v] && mat[u][v] != 0 && dist[u] != Integer.MAX_VALUE
                        && dist[u] + mat[u][v] < dist[v]) {
                    dist[v] = dist[u] + mat[u][v];
                    parent[v] = u;
                }
            }
        }

        // --- Reconstruction du chemin final ---
        finalPath.clear();
        if (dist[end] == Integer.MAX_VALUE) {
            sb.append("Aucun chemin trouvé entre ")
                    .append(g.getVertexName(start))
                    .append(" et ")
                    .append(g.getVertexName(end))
                    .append(".\n");
        } else {
            // On remonte les parents depuis le sommet d’arrivée
            for (int v = end; v != -1; v = parent[v]) {
                finalPath.add(v);
            }
            Collections.reverse(finalPath);

            // Vérification de cohérence
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

    /**
     * Recherche le sommet non visité avec la plus petite distance courante.
     *
     * @param dist tableau des distances
     * @param visited tableau des sommets visités
     * @return l’indice du sommet le plus proche non encore visité
     */
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

    /**
     * Construit une représentation textuelle du chemin trouvé,
     * en remplaçant les indices par les noms des sommets.
     *
     * @param g graphe utilisé pour obtenir les noms
     * @param path liste d’indices représentant le chemin
     * @return chaîne formatée du type "A → B → C"
     */
    private static String reconstructPath(Graphe g, List<Integer> path) {
        List<String> names = new ArrayList<>();
        for (int v : path) names.add(g.getVertexName(v));
        return String.join(" → ", names);
    }
}
