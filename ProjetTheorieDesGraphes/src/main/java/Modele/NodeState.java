package Modele;

import javafx.scene.paint.Color;

public enum NodeState {
    NODE_TO_VISIT,
    NODE_VISITED,
    NODE_KEPT;

    public Color getColor() {
        return switch (this) {
            case NODE_TO_VISIT -> Color.YELLOW;
            case NODE_VISITED -> Color.RED;
            case NODE_KEPT -> Color.LIGHTBLUE;
        };
    }
}
