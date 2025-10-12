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
    public StepManager getStepManager(Graphe graphe, int startNode) {
        StepManager stepManager = new StepManager();
        int[][] matrice = graphe.getMatrix();
        Set<Integer> visited = new HashSet<>();
        Stack<Integer> stack = new Stack<>();
        visitOrder.clear();

        stack.push(startNode);

        while (!stack.isEmpty()) {
            int node = stack.peek();

            if (!visited.contains(node)) visitNode(node, visited, stepManager);

            int nextNode = findNextNode(node, matrice, visited);
            if (nextNode != -1) {
                traverseEdge(node, nextNode, stepManager);
                stack.push(nextNode);
            } else {
                stack.pop();
            }
        }

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

    private void visitNode(int node, Set<Integer> visited, StepManager stepManager) {
        visited.add(node);
        visitOrder.add(node);
        stepManager.markNode(node, Color.RED);
    }

    private void traverseEdge(int from, int to, StepManager stepManager) {
        stepManager.markEdge(from, to, Color.PINK);
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
