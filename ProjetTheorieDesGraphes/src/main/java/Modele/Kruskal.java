package Modele;

import java.util.*;

/**
 * Implémentation de l’algorithme de Kruskal.
 *
 * Cet algorithme calcule l’arbre couvrant minimal (ACM) d’un graphe pondéré non orienté.
 * Il sélectionne progressivement les arêtes de plus faible poids sans créer de cycles,
 * jusqu’à connecter tous les sommets.
 *
 * Cette classe fournit :
 * - le détail du déroulement (étapes de tri et d’ajout d’arêtes),
 * - le coût total de l’arbre couvrant minimal,
 * - et une liste ordonnée {@code finalPath} pour la visualisation graphique.
 *
 */
public class Kruskal {

    /** Chemin final construit pour la visualisation graphique */
    private final List<Integer> finalPath = new ArrayList<>();

    /**
     * Retourne la liste des sommets du chemin final construit après exécution.
     *
     * @return liste des indices des sommets dans l’ordre d’apparition
     */
    public List<Integer> getFinalPath() {
        return new ArrayList<>(finalPath);
    }

    /**
     * Exécute l’algorithme de Kruskal sur un graphe donné.
     *
     * @param g graphe pondéré non orienté
     * @return description textuelle complète du déroulement et du résultat
     */
    public String getResult(Graphe g) {
        StringBuilder sb = new StringBuilder();
        int[][] mat = g.getMatrix();
        int n = mat.length;
        List<int[]> edges = new ArrayList<>();

        // --- Étape 1 : construction de la liste des arêtes ---
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (mat[i][j] != 0) {
                    edges.add(new int[]{i, j, mat[i][j]});
                }
            }
        }

        // --- Étape 2 : tri des arêtes par poids croissant ---
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

        // --- Étape 3 : affichage de l’arbre couvrant minimal ---
        sb.append("\nÉtape 2 : Arbre couvrant minimal (résultat final)\n");
        for (int[] e : mst) {
            sb.append(String.format("%s — %s : %d\n",
                    g.getVertexName(e[0]), g.getVertexName(e[1]), e[2]));
        }

        sb.append("Coût total de l'arbre couvrant = ").append(totalCost).append("\n");

        // --- Étape 4 : construction du chemin pour la visualisation ---
        finalPath.clear();

        if (!mst.isEmpty()) {
            Map<Integer, List<Integer>> tree = new HashMap<>();
            for (int[] e : mst) {
                tree.computeIfAbsent(e[0], k -> new ArrayList<>()).add(e[1]);
                tree.computeIfAbsent(e[1], k -> new ArrayList<>()).add(e[0]);
            }

            boolean[] visited = new boolean[n];
            generatePathWithBacktrack(0, tree, visited);
        }

        return sb.toString();
    }

    /**
     * Parcours récursif de l’arbre couvrant pour générer un chemin complet.
     * Ajoute également les retours arrière pour la visualisation séquentielle.
     *
     * @param node nœud courant
     * @param tree structure d’adjacence représentant l’arbre couvrant
     * @param visited tableau de sommets visités
     */
    private void generatePathWithBacktrack(int node, Map<Integer, List<Integer>> tree, boolean[] visited) {
        visited[node] = true;
        finalPath.add(node);

        List<Integer> neighbors = tree.getOrDefault(node, Collections.emptyList());
        for (int neighbor : neighbors) {
            if (!visited[neighbor]) {
                generatePathWithBacktrack(neighbor, tree, visited);
                // Retour arrière pour visualiser le retour au parent
                finalPath.add(node);
            }
        }
    }
}
