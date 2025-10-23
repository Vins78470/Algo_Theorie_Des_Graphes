package Modele;

/**
 * Structure de données Union-Find (aussi appelée Disjoint Set Union ou DSU).
 *
 * Cette structure permet de gérer efficacement des ensembles disjoints
 * et est utilisée principalement pour :
 * - détecter la présence de cycles dans un graphe,
 * - déterminer si deux sommets appartiennent au même ensemble.
 *
 * Elle est essentielle dans l’implémentation de l’algorithme de Kruskal
 * pour la construction d’un arbre couvrant minimal.
 *
 * L’implémentation ci-dessous inclut :
 * - la compression de chemin (optimisation du temps d’accès lors du find),
 * - la fusion par rang (minimise la hauteur des arbres de représentants).
 *
 */
public class UnionFind {

    /** Tableau des parents : parent[i] = représentant de l’ensemble du nœud i */
    private final int[] parent;

    /** Tableau des rangs : sert à équilibrer la hauteur lors des unions */
    private final int[] rank;

    /**
     * Initialise la structure Union-Find pour {@code n} éléments indépendants.
     *
     * @param n nombre total d’éléments (ou de sommets)
     */
    public UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i; // chaque élément est son propre représentant
        }
    }

    /**
     * Trouve le représentant (racine) de l’ensemble contenant {@code x}.
     * Applique la compression de chemin pour optimiser les appels suivants.
     *
     * @param x élément dont on cherche le représentant
     * @return l’indice du représentant (racine) de l’ensemble
     */
    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // compression de chemin
        }
        return parent[x];
    }

    /**
     * Fusionne les ensembles contenant {@code x} et {@code y}.
     * Utilise la fusion par rang pour minimiser la hauteur de l’arbre.
     *
     * @param x premier élément
     * @param y second élément
     * @return {@code true} si la fusion a eu lieu,
     *         {@code false} si {@code x} et {@code y} étaient déjà dans le même ensemble (cycle détecté)
     */
    public boolean union(int x, int y) {
        int rx = find(x);
        int ry = find(y);

        // Les deux éléments appartiennent déjà au même ensemble
        if (rx == ry) {
            return false;
        }

        // Fusion par rang
        if (rank[rx] < rank[ry]) {
            parent[rx] = ry;
        } else if (rank[rx] > rank[ry]) {
            parent[ry] = rx;
        } else {
            parent[ry] = rx;
            rank[rx]++;
        }

        return true;
    }
}
