package Modele;

import javafx.scene.paint.Color;

public class Node {
    private final int id;
    private final String name;
    private NodeState state;

    public Node(int id, String name) {
        this.id = id;
        this.name = name;
        this.state = NodeState.NODE_TO_VISIT;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public NodeState getState() {
        return state;
    }

    public void setState(NodeState state) {
        this.state = state;
    }

    public Color getColor() {
        return state.getColor();
    }
}
