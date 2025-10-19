package Modele;

import java.util.*;

public class Prim {

    private static final List<Integer> finalPath = new ArrayList<>();

    public static List<Integer> getFinalPath() {
        return new ArrayList<>(finalPath);
    }

    public static String getResult(Graphe g, int start) {
        StringBuilder sb = new StringBuilder();

        System.out.println("\n========== DEBUG PRIM START ==========");
        System.out.println("Graphe : " + g);
        System.out.println("Start : " + start + " (" + g.getVertexName(start) + ")");

        int[][] mat = g.getMatrix();
        int n = mat.length;

        System.out.println("\n--- Matrice d'adjacence ---");
        for (int i = 0; i < n; i++) {
            System.out.print(String.format("%10s : ", g.getVertexName(i)));
            for (int j = 0; j < n; j++) {
                System.out.print(String.format("%5d ", mat[i][j]));
            }
            System.out.println();
        }

        boolean[] visited = new boolean[n];
        int[] parent = new int[n];
        int[] key = new int[n];
        Arrays.fill(key, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        key[start] = 0;

        System.out.println("\n--- État initial ---");
        System.out.println("key[] = " + Arrays.toString(key));
        System.out.println("parent[] = " + Arrays.toString(parent));

        System.out.println("\n--- Exécution Prim ---");

        for (int iter = 0; iter < n - 1; iter++) {
            System.out.println("\n=== Itération " + iter + " ===");

            int u = minKey(key, visited, n);
            System.out.println("minKey retourné : u = " + u);

            if (u == -1) {
                System.out.println("ERROR: minKey = -1, graphe non connexe!");
                sb.append("Erreur : graphe non connexe ou invalide\n");
                break;
            }

            System.out.println("Nœud sélectionné : " + g.getVertexName(u) + " (key=" + key[u] + ")");
            visited[u] = true;
            System.out.println("visited[" + u + "] = true");

            System.out.println("\nVérification des voisins de " + g.getVertexName(u) + ":");
            for (int v = 0; v < n; v++) {
                System.out.println("  [" + u + "][" + v + "] : mat=" + mat[u][v] +
                        " | visited[" + v + "]=" + visited[v] +
                        " | key[" + v + "]=" + key[v]);

                if (mat[u][v] > 0 && !visited[v] && mat[u][v] < key[v]) {
                    System.out.println("    ✓ UPDATE: parent[" + v + "]=" + u + ", key[" + v + "]=" + mat[u][v]);
                    parent[v] = u;
                    key[v] = mat[u][v];
                } else if (mat[u][v] > 0) {
                    System.out.println("    ✗ Pas d'update: mat>0=" + (mat[u][v]>0) +
                            " !visited=" + (!visited[v]) + " mat<key=" + (mat[u][v]<key[v]));
                }
            }

            System.out.println("État après itération " + iter + ":");
            System.out.println("  key[] = " + Arrays.toString(key));
            System.out.println("  parent[] = " + Arrays.toString(parent));
        }

        System.out.println("\n--- Résultat final ---");
        sb.append("Résultat de Prim (départ : ").append(g.getVertexName(start)).append(")\n");
        int totalCost = 0;
        for (int i = 0; i < n; i++) {
            if (parent[i] != -1) {
                int cost = mat[parent[i]][i];  // ← BON SENS : parent[i] → i
                System.out.println("Arête : [" + parent[i] + "][" + i + "] = " +
                        g.getVertexName(parent[i]) + " — " + g.getVertexName(i) + " : " + cost);
                sb.append(String.format("%s — %s : %d\n",
                        g.getVertexName(parent[i]), g.getVertexName(i), cost));
                totalCost += cost;
            }
        }
        sb.append("Coût total = ").append(totalCost).append("\n");

        finalPath.clear();
        boolean[] addedToPath = new boolean[n];
        finalPath.add(start);
        addedToPath[start] = true;

        for (int i = 0; i < n; i++) {
            if (visited[i] && !addedToPath[i]) {
                finalPath.add(i);
                addedToPath[i] = true;
            }
        }

        System.out.println("\nfinalPath = " + finalPath);
        System.out.println("========== DEBUG PRIM END ==========\n");

        return sb.toString();
    }

    private static int minKey(int[] key, boolean[] visited, int n) {
        System.out.println("  [minKey] key=" + Arrays.toString(key) + " visited=" + Arrays.toString(visited));
        int min = Integer.MAX_VALUE, minIndex = -1;
        for (int v = 0; v < n; v++) {
            if (!visited[v] && key[v] < min) {
                min = key[v];
                minIndex = v;
                System.out.println("    v=" + v + " : !visited && key<min → minIndex=" + v + " min=" + min);
            }
        }
        System.out.println("  [minKey] retour : " + minIndex);
        return minIndex;
    }
}