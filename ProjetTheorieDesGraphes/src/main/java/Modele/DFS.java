package Modele;

import javafx.scene.paint.Color;
import java.util.*;

public class DFS {

    private final List<Integer> visitOrder = new ArrayList<>();

    // =========================
    // Partie texte classique
    // =========================
    public String getResult(Graphe graphe, int startNode) {
        StringBuilder sb = new StringBuilder();
        int[][] matrice = graphe.getMatrix();
        String[] noms = getVertexNames(graphe);

        Set<Integer> visited = new HashSet<>();
        Stack<Integer> stack = new Stack<>();
        visitOrder.clear();

        stack.push(startNode);
        sb.append("Départ depuis : ").append(noms[startNode]).append("\n\n");

        while (!stack.isEmpty()) {
            int node = stack.peek();

            if (!visited.contains(node)) {
                visited.add(node);
                visitOrder.add(node);
                sb.append("Visite du sommet : ").append(noms[node]).append("\n");
            }

            int nextNode = findNextNode(node, matrice, visited);
            if (nextNode != -1) {
                stack.push(nextNode);
                sb.append("  Descente vers : ").append(noms[nextNode])
                        .append(" (distance : ").append(matrice[node][nextNode]).append(")\n");
            } else {
                stack.pop();
            }
        }

        sb.append("\nParcours terminé !\n");
        sb.append("Ordre de visite : ").append(formatVisited(noms)).append("\n");
        return sb.toString();
    }

    // =========================
    // Partie Steps pour animation via StepManager
    // =========================
    public StepManager getStepManager(Graphe graphe, int startNodeIndex) {
        StepManager stepManager = new StepManager();
        int[][] matrice = graphe.getMatrix();
        Node[] nodes = graphe.getNodes();
        Set<Integer> visited = new HashSet<>();
        Stack<Integer> stack = new Stack<>();

        stack.push(startNodeIndex);
        stepManager.markNode(nodes[startNodeIndex], NodeState.NODE_TO_VISIT);

        while (!stack.isEmpty()) {
            int nodeIndex = stack.peek();
            Node currentNode = nodes[nodeIndex];

            if (!visited.contains(nodeIndex)) {
                visited.add(nodeIndex);
                currentNode.setState(NodeState.NODE_VISITED);
                stepManager.markNode(currentNode, NodeState.NODE_VISITED);
            }

            // On cherche tous les voisins non visités pour "essayer" les arêtes
            boolean hasNext = false;
            for (int nextIndex = 0; nextIndex < nodes.length; nextIndex++) {
                if (matrice[nodeIndex][nextIndex] != 0 && !visited.contains(nextIndex)) {
                    Node nextNode = nodes[nextIndex];

                    // 1️⃣ On marque l'arête comme parcourue pour tous les voisins
                    stepManager.markEdge(currentNode, nextNode, NodeState.NODE_VISITED);

                    // 2️⃣ On choisit la première arête pour continuer → NODE_KEPT
                    if (!hasNext) {
                        stepManager.markEdge(currentNode, nextNode, NodeState.NODE_KEPT);
                        stack.push(nextIndex);
                        stepManager.markNode(nextNode, NodeState.NODE_TO_VISIT);
                        hasNext = true;
                    }
                }
            }

            // 3️⃣ Si aucun voisin non visité, backtrack
            if (!hasNext) {
                currentNode.setState(NodeState.NODE_BACKTRACK);
                stepManager.markNode(currentNode, NodeState.NODE_BACKTRACK);
                stack.pop();
            }
        }
        System.out.println(stepManager);
        return stepManager;
    }




    // =========================
    // Fonctions utilitaires
    // =========================
    private int findNextNode(int node, int[][] matrice, Set<Integer> visited) {
        int minDistance = Integer.MAX_VALUE;
        int nextNode = -1;
        for (int j = 0; j < matrice[node].length; j++) {
            if (matrice[node][j] > 0 && !visited.contains(j) && matrice[node][j] < minDistance) {
                minDistance = matrice[node][j];
                nextNode = j;
            }
        }
        return nextNode;
    }



    private String[] getVertexNames(Graphe g) {
        try {
            var field = Graphe.class.getDeclaredField("vertexNames");
            field.setAccessible(true);
            return (String[]) field.get(g);
        } catch (Exception e) {
            throw new RuntimeException("Impossible de récupérer les noms des sommets du graphe.", e);
        }
    }

    private String formatVisited(String[] noms) {
        List<String> lst = new ArrayList<>();
        for (int v : visitOrder) lst.add(noms[v]);
        return String.join(" → ", lst);
    }
}
