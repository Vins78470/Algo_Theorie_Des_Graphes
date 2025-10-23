package Modele;

/**
 * Représente une arête d’un graphe pondéré.
 *
 * Une arête relie deux sommets (départ et arrivée) et possède un poids (ou coût)
 * associé. Cette classe est utilisée notamment par les algorithmes de Kruskal,
 * Prim et Bellman-Ford pour manipuler et trier les connexions du graphe.
 *
 * Les arêtes sont immuables : leurs valeurs sont définies au moment
 * de la création et ne peuvent plus être modifiées ensuite.
 */
public class Edge {

    /** Indice du sommet de départ de l’arête. */
    public final int from;

    /** Indice du sommet d’arrivée de l’arête. */
    public final int to;

    /** Poids (ou coût) associé à l’arête. */
    public final int weight;

    /**
     * Constructeur principal.
     *
     * @param from indice du sommet source
     * @param to indice du sommet destination
     * @param weight poids ou coût de l’arête
     */
    public Edge(int from, int to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    /**
     * Retourne une représentation textuelle lisible de l’arête,
     * indiquant les indices des sommets connectés et leur poids.
     *
     * Exemple : "0 — 2 : 15" signifie une arête entre les sommets 0 et 2
     * avec un poids de 15.
     *
     * @return chaîne formatée représentant l’arête
     */
    @Override
    public String toString() {
        return String.format("%d — %d : %d", from, to, weight);
    }
}
