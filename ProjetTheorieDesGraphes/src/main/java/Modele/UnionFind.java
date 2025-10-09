package Modele;

/**
 * Structure Union-Find (ou Disjoint Set Union)
 * utilisée pour détecter les cycles dans l'algorithme de Kruskal.
 */
public class UnionFind {
    private final int[] parent;
    private final int[] rank;

    public UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
    }

    public int find(int x) {
        if (parent[x] != x)
            parent[x] = find(parent[x]); // compression de chemin
        return parent[x];
    }

    public boolean union(int x, int y) {
        int rx = find(x);
        int ry = find(y);
        if (rx == ry) return false; // cycle détecté

        // fusion par rang
        if (rank[rx] < rank[ry]) parent[rx] = ry;
        else if (rank[rx] > rank[ry]) parent[ry] = rx;
        else {
            parent[ry] = rx;
            rank[rx]++;
        }
        return true;
    }
}
