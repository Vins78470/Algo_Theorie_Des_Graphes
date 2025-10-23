package Modele;

import java.util.*;

/**
 * Implémentation de l’algorithme de Prim.
 *
 * L’algorithme de Prim permet de construire un arbre couvrant minimal (ACM)
 * dans un graphe pondéré non orienté.
 * Contrairement à Kruskal, qui trie globalement les arêtes,
 * Prim étend progressivement un arbre en ajoutant à chaque étape
 * l’arête de poids minimal connectant un sommet déjà inclus à un sommet extérieur.
 *
 * Cette classe fournit :
 * - l’affichage des étapes d’exécution détaillées,
 * - le coût total de l’arbre couvrant minimal,
 * - et une liste ordonnée {@code finalPath} utilisée pour la visualisation graphique.
 *
 */
public class Prim {

    /** Liste statique du chemin final pour la visualisation graphique */
    private static final List<Integer> finalPath = new ArrayList<>();

    /**
     * Retourne le chemin final construit après l’exécution de l’algorithme.
     *
     * @return liste d’indices représentant les sommets dans l’ordre de construction
     */
    public static List<Integer> getFinalPath() {
        return new ArrayList<>(finalPath);
    }

    /**
     * Exécute l’algorithme de Prim sur le graphe donné, à partir d’un sommet de départ.
     *
     * @param g graphe pondéré non orienté
     * @param start indice du sommet de départ
     * @return description textuelle du déroulement complet et du résultat final
     */
    public static String getResult(Graphe g, int start) {
        StringBuilder sb = new StringBuilder();

        int[][] mat = g.getMatrix();
        int n = mat.length;

        // --- Initialisation des structures ---
        boolean[] visited = new boolean[n];
        int[] parent = new int[n];
        int[] key = new int[n];
        Arrays.fill(key, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        key[start] = 0;

        // --- Début du processus ---
        sb.append("Résultat de l’algorithme de Prim (départ : ")
                .append(g.getVertexName(start))
                .append(")\n\n");

        for (int iter = 0; iter < n - 1; iter++) {

            int u = minKey(key, visited, n);
            if (u == -1) {
                sb.append("Erreur : graphe non connexe ou invalide\n");
                break;
            }

            visited[u] = true;

            // Mise à jour des sommets adjacents
            for (int v = 0; v < n; v++) {
                if (mat[u][v] > 0 && !visited[v] && mat[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = mat[u][v];
                }
            }
        }

        // --- Construction du résultat ---
        int totalCost = 0;
        for (int i = 0; i < n; i++) {
            if (parent[i] != -1) {
                int cost = mat[parent[i]][i];
                sb.append(String.format("%s — %s : %d\n",
                        g.getVertexName(parent[i]),
                        g.getVertexName(i),
                        cost));
                totalCost += cost;
            }
        }

        sb.append("Coût total = ").append(totalCost).append("\n");

        // --- Construction du chemin final pour l’affichage graphique ---
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

        return sb.toString();
    }

    /**
     * Sélectionne le sommet non visité ayant la plus petite clé (distance minimale).
     *
     * @param key tableau des poids minimaux
     * @param visited tableau des sommets déjà inclus dans l’arbre
     * @param n nombre total de sommets
     * @return indice du sommet avec la clé minimale, ou -1 si aucun sommet valide
     */
    private static int minKey(int[] key, boolean[] visited, int n) {
        int min = Integer.MAX_VALUE;
        int minIndex = -1;

        for (int v = 0; v < n; v++) {
            if (!visited[v] && key[v] < min) {
                min = key[v];
                minIndex = v;
            }
        }
        return minIndex;
    }
}
