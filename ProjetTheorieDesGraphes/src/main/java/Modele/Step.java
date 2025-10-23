package Modele;

import javafx.scene.paint.Color;

/**
 * Représente une étape (transition) dans l’exécution d’un algorithme sur le graphe.
 *
 * Chaque instance de Step définit :
 * - un nœud de départ ({@code from}),
 * - un nœud d’arrivée ({@code to}),
 * - un état ({@link NodeState}) appliqué à cette étape.
 *
 * Cette classe est principalement utilisée pour animer
 * les transitions ou changements d’état visuels dans la
 * représentation graphique du graphe (par exemple lors d’un BFS ou DFS).
 *
 * Exemple :
 * Un Step peut représenter la visite d’un sommet,
 * la découverte d’une arête, ou un retour arrière.
 *
 */
public class Step {

    /** Nœud de départ de l’étape (peut être null pour une étape isolée) */
    private final Node from;

    /** Nœud d’arrivée de l’étape (peut être le même que le départ pour un simple changement d’état) */
    private final Node to;

    /** État associé à cette étape (influence la couleur lors du rendu graphique) */
    private final NodeState state;

    /**
     * Constructeur principal d’une étape de l’algorithme.
     *
     * @param from nœud de départ
     * @param to nœud d’arrivée
     * @param state état appliqué à cette étape
     */
    public Step(Node from, Node to, NodeState state) {
        this.from = from;
        this.to = to;
        this.state = state;
    }

    /**
     * Retourne le nœud de départ.
     *
     * @return instance de {@link Node} correspondant au départ
     */
    public Node getFrom() {
        return from;
    }

    /**
     * Retourne le nœud d’arrivée.
     *
     * @return instance de {@link Node} correspondant à l’arrivée
     */
    public Node getTo() {
        return to;
    }

    /**
     * Retourne l’état associé à cette étape.
     *
     * @return instance de {@link NodeState}
     */
    public NodeState getState() {
        return state;
    }

    /**
     * Retourne la couleur correspondant à l’état de cette étape.
     *
     * @return couleur JavaFX issue de {@link NodeState#getColor()}
     */
    public Color getColor() {
        return state.getColor();
    }
}
