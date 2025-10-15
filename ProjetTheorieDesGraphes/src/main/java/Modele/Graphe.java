package Modele;

import java.util.Arrays;
import java.util.List;

/**
 * Classe pour construire et manipuler une matrice d'adjacence.
 * - Les sommets sont indexés de 0 à n-1.
 * - On peut leur attribuer des noms pour l'affichage.
 * - Pour un graphe non pondéré, la valeur utilisée pour une arête est 1.
 * - Pour un graphe pondéré, on stocke la valeur du poids (int).
 * - L'absence d'arête est représentée par 0.
 */
public class Graphe {

    private final int n;                 // nombre de sommets
    private final boolean directed;
    private final boolean weighted;
    private final int[][] mat;           // la matrice d'adjacence
    private final String[] vertexNames;  // noms des sommets

    /**
     * Constructeur sans noms (par défaut : "S0", "S1", ...)
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
     * @param vertexNames noms des sommets (peut être null ou de taille différente)
     */
    public Graphe(int n, boolean directed, boolean weighted, String[] vertexNames) {
        if (n <= 0) throw new IllegalArgumentException("n doit être > 0");
        this.n = n;
        this.directed = directed;
        this.weighted = weighted;
        this.mat = new int[n][n];
        this.vertexNames = new String[n];

        // initialise les noms
        for (int i = 0; i < n; i++) {
            if (vertexNames != null && i < vertexNames.length && vertexNames[i] != null) {
                this.vertexNames[i] = vertexNames[i];
            } else {
                this.vertexNames[i] = "S" + i; // nom par défaut
            }
        }
    }

    /** Ajoute une arête non pondérée u-v (ou u->v si orienté). */
    public void addEdge(int u, int v) {
        addEdge(u, v, 1);
    }

    /** Ajoute une arête pondérée u-v (ou u->v si orienté). */
    public void addEdge(int u, int v, int weight) {
        checkVertex(u);
        checkVertex(v);
        int w = weighted ? weight : 1;
        mat[u][v] = w;
        if (!directed) mat[v][u] = w;
    }

    /** Supprime une arête. */
    public void removeEdge(int u, int v) {
        checkVertex(u);
        checkVertex(v);
        mat[u][v] = 0;
        if (!directed) mat[v][u] = 0;
    }

    /** Retourne une copie de la matrice. */
    public int[][] getMatrix() {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) System.arraycopy(mat[i], 0, copy[i], 0, n);
        return copy;
    }

    /** Affiche la matrice d'adjacence avec les noms de sommets. */
    /** Affiche la matrice d'adjacence avec les noms de sommets (alignement dynamique). */
    public void printMatrix() {
        System.out.println("Matrice d'adjacence (" +
                (directed ? "orienté" : "non orienté") + ", " +
                (weighted ? "pondéré" : "non pondéré") + "):\n");

        // --- calcul de la largeur maximale pour l'affichage ---
        int maxNameLen = 0;
        for (String name : vertexNames) {
            if (name.length() > maxNameLen) maxNameLen = name.length();
        }
        int cellWidth = Math.max(maxNameLen + 2, 6); // largeur minimale

        // --- entête colonnes ---
        System.out.print(" ".repeat(maxNameLen + 3));
        for (String name : vertexNames) {
            System.out.printf("%" + cellWidth + "s", name);
        }
        System.out.println();

        // --- ligne de séparation ---
        System.out.print(" ".repeat(maxNameLen + 2) + "+");
        System.out.println("-".repeat(cellWidth * n));

        // --- lignes de la matrice ---
        for (int i = 0; i < n; i++) {
            System.out.printf("%" + maxNameLen + "s |", vertexNames[i]);
            for (int j = 0; j < n; j++) {
                System.out.printf("%" + cellWidth + "d", mat[i][j]);
            }
            System.out.println();
        }
    }

    private void checkVertex(int v) {
        if (v < 0 || v >= n) throw new IndexOutOfBoundsException("Sommet " + v + " invalide");
    }

    // --- Méthodes utilitaires ---
    public void addEdges(List<int[]> edges) {
        for (int[] e : edges) if (e.length >= 2) addEdge(e[0], e[1]);
    }

    public void addWeightedEdges(List<int[]> edges) {
        for (int[] e : edges) if (e.length >= 3) addEdge(e[0], e[1], e[2]);
    }

    public String getVertexName(int index) {
        return vertexNames[index];
    }
    /** Retourne le nombre total de sommets */
    public int getNbSommets() {
        return n;
    }

    public List<String> getAllVertexNames() {
        return Arrays.asList(vertexNames);
    }
    /** Retourne le poids entre deux sommets (0 si aucune arête) */
    public int getPoids(int u, int v) {
        checkVertex(u);
        checkVertex(v);
        return mat[u][v];
    }

    public boolean isOriented(){
        return directed;
    }


}
