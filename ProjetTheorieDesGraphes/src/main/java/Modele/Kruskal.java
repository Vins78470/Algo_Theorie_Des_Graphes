package Modele;

import java.util.*;

/**
 * Algorithme de Kruskal (version simplifiée et claire)
 * Étape 1 : tri des arêtes + affichage "ajoutée" ou "forme un cycle"
 * Étape 2 : affichage de l'arbre couvrant minimal final
 */
public class Kruskal {

    /** Classe interne pour représenter une arête */
    private static class Edge implements Comparable<Edge> {
        int u, v, weight;

        Edge(int u, int v, int weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }
    }

    /** Structure Union-Find (ensemble disjoint) */
    private static class UnionFind {
        int[] parent, rank;

        UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
        }

        int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]); // compression de chemin
            return parent[x];
        }

        boolean union(int x, int y) {
            int rx = find(x), ry = find(y);
            if (rx == ry) return false; // cycle détecté
            if (rank[rx] < rank[ry]) parent[rx] = ry;
            else if (rank[rx] > rank[ry]) parent[ry] = rx;
            else {
                parent[ry] = rx;
                rank[rx]++;
            }
            return true;
        }
    }

    /** Méthode principale : exécution de Kruskal */
    public static void run(Graphe g) {
        int[][] mat = g.getMatrix();
        int n = mat.length;
        List<Edge> edges = new ArrayList<>();

        // --- Récupération de toutes les arêtes ---
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (mat[i][j] != 0) {
                    edges.add(new Edge(i, j, mat[i][j]));
                }
            }
        }

        // --- Tri des arêtes dans l'ordre croissant ---
        Collections.sort(edges);

        // --- Étape 1 : Affichage avec "ajoutée" / "forme un cycle" ---
        System.out.println("\n=== Étape 1 : Arêtes triées par poids croissant ===");
        UnionFind uf = new UnionFind(n);
        List<Edge> mst = new ArrayList<>();
        int totalCost = 0;

        for (Edge e : edges) {
            boolean added = uf.union(e.u, e.v);
            if (added) {
                mst.add(e);
                totalCost += e.weight;
                System.out.printf("%s — %s : %d  (ajoutée)%n",
                        g.getVertexName(e.u), g.getVertexName(e.v), e.weight);
            } else {
                System.out.printf("%s — %s : %d  (forme un cycle)%n",
                        g.getVertexName(e.u), g.getVertexName(e.v), e.weight);
            }
        }

        // --- Étape 2 : Résultat final ---
        System.out.println("\n=== Étape 2 : Arbre couvrant minimal (résultat final) ===");
        for (Edge e : mst) {
            System.out.printf("%s — %s : %d%n",
                    g.getVertexName(e.u), g.getVertexName(e.v), e.weight);
        }
        System.out.println("Coût total de l'arbre couvrant = " + totalCost);
    }
}
