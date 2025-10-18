package Modele;

import java.util.*;

public class Prim {

    private static List<Integer> finalPath = new ArrayList<>();

    public static List<Integer> getFinalPath() {
        return new ArrayList<>(finalPath);
    }

    public static String getResult(Graphe g, int start) {
        StringBuilder sb = new StringBuilder();

        int[][] mat = g.getMatrix();
        int n = mat.length;
        boolean[] visited = new boolean[n];
        int[] parent = new int[n];
        int[] key = new int[n];
        Arrays.fill(key, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        key[start] = 0;

        for (int i = 0; i < n - 1; i++) {
            int u = minKey(key, visited, n);
            visited[u] = true;

            for (int v = 0; v < n; v++) {
                if (mat[u][v] != 0 && !visited[v] && mat[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = mat[u][v];
                }
            }
        }

        sb.append("Résultat de Prim (départ : ").append(g.getVertexName(start)).append(")\n");
        int totalCost = 0;
        for (int i = 0; i < n; i++) {
            if (parent[i] != -1) {
                sb.append(String.format("%s — %s : %d\n",
                        g.getVertexName(parent[i]), g.getVertexName(i), mat[i][parent[i]]));
                totalCost += mat[i][parent[i]];
            }
        }
        sb.append("Coût total = ").append(totalCost).append("\n");

        // Construire le finalPath pour la visualisation
        finalPath.clear();
        if (n > 0) {
            // Construire l'arbre comme adjacence à partir du tableau parent
            Map<Integer, List<Integer>> tree = new HashMap<>();
            for (int i = 0; i < n; i++) {
                if (parent[i] != -1) {
                    tree.computeIfAbsent(parent[i], k -> new ArrayList<>()).add(i);
                    tree.computeIfAbsent(i, k -> new ArrayList<>()).add(parent[i]);
                }
            }

            // Parcours DFS avec backtrack depuis le nœud de départ
            boolean[] visitedDFS = new boolean[n];
            generatePathWithBacktrack(start, tree, visitedDFS);
        }

        return sb.toString();
    }

    private static void generatePathWithBacktrack(int node, Map<Integer, List<Integer>> tree, boolean[] visited) {
        visited[node] = true;
        finalPath.add(node);

        List<Integer> neighbors = tree.getOrDefault(node, Collections.emptyList());
        for (int neighbor : neighbors) {
            if (!visited[neighbor]) {
                generatePathWithBacktrack(neighbor, tree, visited);
                // Backtrack : retour au nœud actuel
                finalPath.add(node);
            }
        }
    }

    private static int minKey(int[] key, boolean[] visited, int n) {
        int min = Integer.MAX_VALUE, minIndex = -1;
        for (int v = 0; v < n; v++) {
            if (!visited[v] && key[v] < min) {
                min = key[v];
                minIndex = v;
            }
        }
        return minIndex;
    }
}