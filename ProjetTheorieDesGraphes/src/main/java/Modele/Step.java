package Modele;


import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class Step {

    // Couleur des nœuds : clé = indice du nœud, valeur = couleur
    public Map<Integer, Color> nodeStates;

    // Couleur des arêtes : clé = "i-j" (i < j), valeur = couleur
    public Map<String, Color> edgeStates;

    public Step() {
        nodeStates = new HashMap<>();
        edgeStates = new HashMap<>();
    }

    // Méthode pour marquer un nœud comme visité
    public void markNodeVisited(int nodeIndex, Color color) {
        nodeStates.put(nodeIndex, color);
    }

    // Méthode pour marquer une arête
    public void markEdge(int i, int j, Color color) {
        if (i > j) { // toujours utiliser i < j comme clé
            int tmp = i;
            i = j;
            j = tmp;
        }
        edgeStates.put(i + "-" + j, color);
    }

    // Méthode pour copier un Step (utile pour générer étapes successives)
    public Step copy() {
        Step newStep = new Step();
        newStep.nodeStates.putAll(this.nodeStates);
        newStep.edgeStates.putAll(this.edgeStates);
        return newStep;
    }
}

