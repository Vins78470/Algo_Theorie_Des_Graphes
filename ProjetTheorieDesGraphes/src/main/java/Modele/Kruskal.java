package Modele;

import java.util.*;

/**
 * Algorithme de Kruskal sans classe externe Edge.
 * Utilise directement la matrice d'adjacence du graphe.
 */
public class Kruskal {

    public static void run(Graphe g) {
        int[][] mat = g.getMatrix();
        int n = mat.length;

        // --- Création d'une liste d'arêtes à partir du graphe ---
        List<int[]> edges = new ArrayList<>(); // chaque élément = [u, v, poids]

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (mat[i][j] != 0) {
                    edges.add(new int[]{i, j, mat[i][j]});
                }
            }
        }

        // --- Tri des arêtes par poids croissant ---
        edges.sort(Comparator.comparingInt(e -> e[2]));

        // --- Étape 1 : Affichage + construction ---
        System.out.println("\n=== Étape 1 : Arêtes triées par poids croissant ===");

        UnionFind uf = new UnionFind(n);
        List<int[]> mst = new ArrayList<>();
        int totalCost = 0;

        for (int[] e : edges) {
            int u = e[0], v = e[1], w = e[2];

            boolean added = uf.union(u, v);
            if (added) {
                mst.add(e);
                totalCost += w;
                System.out.printf("%s — %s : %d  (ajoutée)%n",
                        g.getVertexName(u), g.getVertexName(v), w);
            } else {
                System.out.printf("%s — %s : %d  (forme un cycle)%n",
                        g.getVertexName(u), g.getVertexName(v), w);
            }
        }

        // --- Étape 2 : Résultat final ---
        System.out.println("\n=== Étape 2 : Arbre couvrant minimal (résultat final) ===");
        for (int[] e : mst) {
            int u = e[0], v = e[1], w = e[2];
            System.out.printf("%s — %s : %d%n",
                    g.getVertexName(u), g.getVertexName(v), w);
        }
        System.out.println("Coût total de l'arbre couvrant = " + totalCost);
    }
}
