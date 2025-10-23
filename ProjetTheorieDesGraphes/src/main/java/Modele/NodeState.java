package Modele;

import javafx.scene.paint.Color;

/**
 * Énumération représentant les différents états possibles d’un nœud (sommet)
 * lors de l’exécution d’un algorithme sur le graphe.
 *
 * Chaque état est associé à une couleur distincte pour la visualisation.
 * Cette approche permet de suivre visuellement le déroulement d’un
 * algorithme (BFS, DFS, Dijkstra, etc.) dans l’interface graphique.
 *
 * Les états sont :
 * - NODE_TO_VISIT : nœud encore non exploré (rouge)
 * - NODE_VISITED : nœud visité pendant l’algorithme (jaune)
 * - NODE_KEPT : nœud inclus dans le résultat final ou un chemin (bleu clair)
 * - NODE_BACKTRACK : nœud revisité lors d’un retour arrière (bleu foncé)
 */
public enum NodeState {

    /** Nœud à visiter (non encore exploré) */
    NODE_TO_VISIT,

    /** Nœud actuellement visité pendant le parcours */
    NODE_VISITED,

    /** Nœud conservé dans le résultat final ou un chemin optimal */
    NODE_KEPT,

    /** Nœud revisité lors d’un retour arrière (backtracking) */
    NODE_BACKTRACK;

    /**
     * Retourne la couleur associée à chaque état du nœud.
     *
     * @return couleur JavaFX représentant l’état du nœud
     */
    public Color getColor() {
        return switch (this) {
            case NODE_TO_VISIT -> Color.RED;
            case NODE_VISITED -> Color.YELLOW;
            case NODE_KEPT -> Color.LIGHTBLUE;
            case NODE_BACKTRACK -> Color.BLUE;
        };
    }
}
