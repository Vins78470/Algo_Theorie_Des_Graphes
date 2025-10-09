package Modele;

import java.util.*;

public class Kruskal {

    public static String getResult(Graphe g) {
        StringBuilder sb = new StringBuilder();

        int[][] mat = g.getMatrix();
        int n = mat.length;
        List<int[]> edges = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (mat[i][j] != 0)
                    edges.add(new int[]{i, j, mat[i][j]});
            }
        }

        edges.sort(Comparator.comparingInt(e -> e[2]));
        UnionFind uf = new UnionFind(n);
        List<int[]> mst = new ArrayList<>();
        int totalCost = 0;

        sb.append("\nÉtape 1 : Arêtes triées par poids croissant\n");
        for (int[] e : edges) {
            int u = e[0], v = e[1], w = e[2];
            boolean added = uf.union(u, v);
            if (added) {
                mst.add(e);
                totalCost += w;
                sb.append(String.format("%s — %s : %d (ajoutée)\n",
                        g.getVertexName(u), g.getVertexName(v), w));
            } else {
                sb.append(String.format("%s — %s : %d (forme un cycle)\n",
                        g.getVertexName(u), g.getVertexName(v), w));
            }
        }

        sb.append("\nÉtape 2 : Arbre couvrant minimal (résultat final)\n");
        for (int[] e : mst) {
            sb.append(String.format("%s — %s : %d\n",
                    g.getVertexName(e[0]), g.getVertexName(e[1]), e[2]));
        }
        sb.append("Coût total de l'arbre couvrant = ").append(totalCost).append("\n");

        return sb.toString();
    }
}
