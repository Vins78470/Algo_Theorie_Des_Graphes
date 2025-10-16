package Modele;

public class Step {
    private final Node from;
    private final Node to;
    private final NodeState state;

    public Step(Node from, Node to, NodeState state) {
        this.from = from;
        this.to = to;
        this.state = state;
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    public NodeState getState() {
        return state;
    }

    public javafx.scene.paint.Color getColor() {
        return state.getColor();
    }
}
