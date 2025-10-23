package Modele;

import javafx.scene.paint.Color;

/**
 * Représente un nœud (sommet) du graphe.
 *
 * Chaque nœud possède :
 * - un identifiant unique (id),
 * - un nom (nom affiché),
 * - un état courant ({@link NodeState}) déterminant sa couleur
 *   lors de la visualisation graphique.
 *
 * Cette classe est utilisée pour associer des propriétés logiques
 * et visuelles aux sommets du graphe.
 */
public class Node {

    /** Identifiant unique du nœud (correspond à son index dans la matrice) */
    private final int id;

    /** Nom du nœud (ex: "Paris", "S1", etc.) */
    private final String name;

    /** État courant du nœud (pour la couleur et le suivi d’algorithme) */
    private NodeState state;

    /**
     * Constructeur principal d’un nœud.
     *
     * @param id identifiant unique
     * @param name nom du nœud
     */
    public Node(int id, String name) {
        this.id = id;
        this.name = name;
        this.state = NodeState.NODE_TO_VISIT;
    }

    /**
     * Retourne l’identifiant du nœud.
     *
     * @return identifiant entier unique
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne le nom du nœud.
     *
     * @return nom du nœud (chaîne de caractères)
     */
    public String getName() {
        return name;
    }

    /**
     * Retourne l’état courant du nœud.
     *
     * @return instance de {@link NodeState}
     */
    public NodeState getState() {
        return state;
    }

    /**
     * Modifie l’état du nœud (utile pour les algorithmes de parcours).
     *
     * @param state nouvel état à appliquer
     */
    public void setState(NodeState state) {
        this.state = state;
    }

    /**
     * Retourne la couleur associée à l’état courant du nœud.
     *
     * @return couleur JavaFX correspondant à l’état du nœud
     */
    public Color getColor() {
        return state.getColor();
    }
}
