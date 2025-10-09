package Modele;

import java.util.*;

public class Prim {

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
        return sb.toString();
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
