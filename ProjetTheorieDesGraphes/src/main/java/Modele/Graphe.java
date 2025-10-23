package Modele;

import java.util.Arrays;
import java.util.List;

/**
 * Classe représentant un graphe sous forme de matrice d’adjacence.
 *
 * Cette classe permet de créer, modifier et afficher un graphe orienté ou non orienté,
 * pondéré ou non pondéré. Les sommets sont indexés de 0 à n-1 et peuvent être associés
 * à des noms personnalisés.
 *
 * - Une arête absente est représentée par la valeur 0.
 * - Une arête présente a une valeur correspondant à son poids (ou 1 si non pondéré).
 * - Pour un graphe non orienté, la matrice est symétrique.
 *
 * Cette structure est utilisée par les algorithmes : BFS, DFS, Dijkstra, Kruskal,
 * Prim, Bellman-Ford et Floyd-Warshall.
 */
public class Graphe {

    /** Nombre total de sommets dans le graphe. */
    private final int n;

    /** Indique si le graphe est orienté (true = orienté, false = non orienté). */
    private final boolean directed;

    /** Indique si le graphe est pondéré (true = pondéré, false = non pondéré). */
    private final boolean weighted;

    /** Matrice d’adjacence représentant les arêtes et leurs poids. */
    private final int[][] mat;

    /** Tableau contenant les noms des sommets pour un affichage lisible. */
    private final String[] vertexNames;

    /**
     * Constructeur de base : crée un graphe sans noms personnalisés.
     * Les sommets sont nommés automatiquement S0, S1, S2, ...
     *
     * @param n nombre de sommets
     * @param directed true si le graphe est orienté
     * @param weighted true si le graphe est pondéré
     */
    public Graphe(int n, boolean directed, boolean weighted) {
        this(n, directed, weighted, null);
    }

    /**
     * Constructeur avec noms de sommets personnalisés.
     *
     * @param n nombre de sommets
     * @param directed true si le graphe est orienté
     * @param weighted true si le graphe est pondéré
     * @param vertexNames tableau contenant les noms des sommets
     */
    public Graphe(int n, boolean directed, boolean weighted, String[] vertexNames) {
        if (n <= 0) throw new IllegalArgumentException("n doit être > 0");
        this.n = n;
        this.directed = directed;
        this.weighted = weighted;
        this.mat = new int[n][n];
        this.vertexNames = new String[n];

        // Initialisation des noms de sommets
        for (int i = 0; i < n; i++) {
            if (vertexNames != null && i < vertexNames.length && vertexNames[i] != null) {
                this.vertexNames[i] = vertexNames[i];
            } else {
                this.vertexNames[i] = "S" + i; // nom par défaut
            }
        }
    }

    /**
     * Ajoute une arête non pondérée entre deux sommets.
     * Si le graphe est non orienté, l’arête est ajoutée dans les deux sens.
     *
     * @param u indice du sommet de départ
     * @param v indice du sommet d’arrivée
     */
    public void addEdge(int u, int v) {
        addEdge(u, v, 1);
    }

    /**
     * Ajoute une arête pondérée entre deux sommets.
     * Si le graphe est non orienté, l’arête est ajoutée dans les deux sens.
     *
     * @param u indice du sommet source
     * @param v indice du sommet destination
     * @param weight poids de l’arête
     */
    public void addEdge(int u, int v, int weight) {
        checkVertex(u);
        checkVertex(v);
        int w = weighted ? weight : 1;
        mat[u][v] = w;
        if (!directed) mat[v][u] = w;
    }

    /**
     * Retourne la liste des nœuds sous forme d’un tableau d’objets Node.
     * Chaque Node contient un indice et un nom de sommet.
     *
     * @return tableau de nœuds représentant les sommets du graphe
     */
    public Node[] getNodes() {
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i, vertexNames[i]);
        }
        return nodes;
    }

    /**
     * Supprime une arête entre deux sommets.
     *
     * @param u indice du sommet source
     * @param v indice du sommet destination
     */
    public void removeEdge(int u, int v) {
        checkVertex(u);
        checkVertex(v);
        mat[u][v] = 0;
        if (!directed) mat[v][u] = 0;
    }

    /**
     * Retourne une copie complète de la matrice d’adjacence.
     *
     * @return copie indépendante de la matrice d’adjacence
     */
    public int[][] getMatrix() {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) System.arraycopy(mat[i], 0, copy[i], 0, n);
        return copy;
    }

    /**
     * Affiche la matrice d’adjacence dans la console
     * avec un formatage dynamique et lisible.
     */
    public void printMatrix() {
        System.out.println("Matrice d'adjacence (" +
                (directed ? "orienté" : "non orienté") + ", " +
                (weighted ? "pondéré" : "non pondéré") + "):\n");

        // Largeur dynamique selon les noms
        int maxNameLen = 0;
        for (String name : vertexNames) {
            if (name.length() > maxNameLen) maxNameLen = name.length();
        }
        int cellWidth = Math.max(maxNameLen + 2, 6);

        // En-tête des colonnes
        System.out.print(" ".repeat(maxNameLen + 3));
        for (String name : vertexNames) {
            System.out.printf("%" + cellWidth + "s", name);
        }
        System.out.println();

        // Ligne de séparation
        System.out.print(" ".repeat(maxNameLen + 2) + "+");
        System.out.println("-".repeat(cellWidth * n));

        // Contenu de la matrice
        for (int i = 0; i < n; i++) {
            System.out.printf("%" + maxNameLen + "s |", vertexNames[i]);
            for (int j = 0; j < n; j++) {
                System.out.printf("%" + cellWidth + "d", mat[i][j]);
            }
            System.out.println();
        }
    }

    /** Vérifie qu’un sommet existe dans le graphe. */
    private void checkVertex(int v) {
        if (v < 0 || v >= n) throw new IndexOutOfBoundsException("Sommet " + v + " invalide");
    }

    /**
     * Ajoute une liste d’arêtes non pondérées au graphe.
     *
     * @param edges liste contenant des couples [u, v]
     */
    public void addEdges(List<int[]> edges) {
        for (int[] e : edges) if (e.length >= 2) addEdge(e[0], e[1]);
    }

    /**
     * Ajoute une liste d’arêtes pondérées au graphe.
     *
     * @param edges liste contenant des triplets [u, v, poids]
     */
    public void addWeightedEdges(List<int[]> edges) {
        for (int[] e : edges) if (e.length >= 3) addEdge(e[0], e[1], e[2]);
    }

    /**
     * Retourne le nom du sommet à l’indice donné.
     *
     * @param index indice du sommet
     * @return nom du sommet
     */
    public String getVertexName(int index) {
        return vertexNames[index];
    }

    /** @return nombre total de sommets du graphe */
    public int getNbSommets() {
        return n;
    }

    /**
     * Retourne la liste des noms de tous les sommets du graphe.
     *
     * @return liste des noms
     */
    public List<String> getAllVertexNames() {
        return Arrays.asList(vertexNames);
    }

    /**
     * Retourne le poids d’une arête entre deux sommets.
     *
     * @param u sommet source
     * @param v sommet destination
     * @return poids de l’arête, ou 0 si elle n’existe pas
     */
    public int getPoids(int u, int v) {
        checkVertex(u);
        checkVertex(v);
        return mat[u][v];
    }

    /** @return true si le graphe est orienté, false sinon */
    public boolean isOriented() {
        return directed;
    }
}
