package Modele;

import javafx.scene.paint.Color;

public enum NodeState {
    NODE_TO_VISIT,
    NODE_VISITED,
    NODE_KEPT,
    NODE_BACKTRACK; // pas de ; avant !

    public Color getColor() {
        return switch (this) {
            case NODE_TO_VISIT -> Color.RED;
            case NODE_VISITED -> Color.YELLOW;
            case NODE_KEPT -> Color.LIGHTBLUE;
            case NODE_BACKTRACK -> Color.BLUE; // couleur pour le backtrack
        };
    }
}
