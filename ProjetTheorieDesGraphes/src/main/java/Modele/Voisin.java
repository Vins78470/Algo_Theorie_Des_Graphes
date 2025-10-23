package Modele;

/**
 * Représente un voisin d’un sommet dans un graphe pondéré.
 *
 * Cette classe est principalement utilisée dans les algorithmes de parcours
 * (comme BFS, DFS, Dijkstra, etc.) pour stocker les informations liées
 * à un voisin direct d’un sommet :
 * - son identifiant (indice dans la matrice d’adjacence),
 * - et le poids de l’arête reliant ce voisin au sommet courant.
 *
 * Elle sert de structure intermédiaire pour trier, filtrer
 * ou manipuler les sommets adjacents dans les algorithmes.
 *
 */
public class Voisin {

    /** Identifiant du voisin (indice du sommet dans la matrice du graphe) */
    int id;

    /** Poids de l’arête reliant ce voisin au sommet courant */
    int poids;

    /**
     * Constructeur principal de la classe Voisin.
     *
     * @param id identifiant du sommet voisin
     * @param poids poids de l’arête reliant le sommet courant à ce voisin
     */
    Voisin(int id, int poids) {
        this.id = id;
        this.poids = poids;
    }
}
