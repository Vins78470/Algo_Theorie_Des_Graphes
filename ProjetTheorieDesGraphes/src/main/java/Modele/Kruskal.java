package Modele;
import java.util.*;

public class Kruskal {
    private List<Integer> finalPath = new ArrayList<>();

    public List<Integer> getFinalPath() {
        return new ArrayList<>(finalPath);
    }

    public String getResult(Graphe g) {
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

        // Construire le finalPath pour la visualisation
        finalPath.clear();
        if (!mst.isEmpty()) {
            // Construire l'arbre comme adjacence
            Map<Integer, List<Integer>> tree = new HashMap<>();
            for (int[] e : mst) {
                tree.computeIfAbsent(e[0], k -> new ArrayList<>()).add(e[1]);
                tree.computeIfAbsent(e[1], k -> new ArrayList<>()).add(e[0]);
            }

            // Parcours DFS avec backtrack pour garantir que chaque transition est une arête
            boolean[] visited = new boolean[n];
            generatePathWithBacktrack(0, tree, visited);
        }

        return sb.toString();
    }

    private void generatePathWithBacktrack(int node, Map<Integer, List<Integer>> tree, boolean[] visited) {
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
}