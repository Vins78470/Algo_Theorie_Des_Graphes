package Modele;

import java.util.*;

public class Prim {

    private static final List<Integer> finalPath = new ArrayList<>();

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

        sb.append("Étape 1 : Exécution de Prim (départ : ").append(g.getVertexName(start)).append(")\n");

        // --- Algorithme de Prim ---
        for (int count = 0; count < n - 1; count++) {
            int u = minKey(key, visited);
            if (u == -1) break; // Graphe non connexe
            visited[u] = true;

            for (int v = 0; v < n; v++) {
                if (mat[u][v] > 0 && !visited[v] && mat[u][v] < key[v]) {
                    key[v] = mat[u][v];
                    parent[v] = u;
                }
            }
        }

        // --- Construction du MST ---
        List<int[]> mst = new ArrayList<>();
        int totalCost = 0;

        sb.append("\nÉtape 2 : Arbre couvrant minimal obtenu\n");
        for (int i = 0; i < n; i++) {
            if (parent[i] != -1) {
                mst.add(new int[]{parent[i], i, mat[parent[i]][i]});
                totalCost += mat[parent[i]][i];
                sb.append(String.format("%s — %s : %d\n",
                        g.getVertexName(parent[i]), g.getVertexName(i), mat[parent[i]][i]));
            }
        }
        sb.append("Coût total = ").append(totalCost).append("\n");

        // --- Construction du finalPath (même logique que Kruskal) ---
        finalPath.clear();
        if (!mst.isEmpty()) {
            // Construire l’arbre comme une table d’adjacence
            Map<Integer, List<Integer>> tree = new HashMap<>();
            for (int[] e : mst) {
                tree.computeIfAbsent(e[0], k -> new ArrayList<>()).add(e[1]);
                tree.computeIfAbsent(e[1], k -> new ArrayList<>()).add(e[0]);
            }

            boolean[] visitedDFS = new boolean[n];
            generatePathWithBacktrack(start, tree, visitedDFS);
        }

        return sb.toString();
    }

    private static int minKey(int[] key, boolean[] visited) {
        int min = Integer.MAX_VALUE, minIndex = -1;
        for (int v = 0; v < key.length; v++) {
            if (!visited[v] && key[v] < min) {
                min = key[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    private static void generatePathWithBacktrack(int node, Map<Integer, List<Integer>> tree, boolean[] visited) {
        visited[node] = true;
        finalPath.add(node);

        List<Integer> neighbors = tree.getOrDefault(node, Collections.emptyList());
        for (int neighbor : neighbors) {
            if (!visited[neighbor]) {
                generatePathWithBacktrack(neighbor, tree, visited);
                // backtrack (retour au parent)
                finalPath.add(node);
            }
        }
    }
}
