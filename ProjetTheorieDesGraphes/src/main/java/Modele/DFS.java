package Modele;

import java.util.*;

public class DFS {

    private final List<Integer> visitOrder;

    public DFS() {
        this.visitOrder = new ArrayList<>();
    }

    /**
     * DFS itératif avec pile, toujours descendre vers l'arête de plus petite distance.
     *
     * @param graphe Graphe à parcourir
     * @param startNode Indice du sommet de départ
     * @return String représentant le déroulement complet du DFS
     */
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

            // Visiter le sommet si pas encore fait
            if (!visited.contains(node)) {
                visited.add(node);
                visitOrder.add(node);
                sb.append("Visite du sommet : ").append(noms[node]).append("\n");
            }

            // Trouver le voisin non visité avec la plus petite distance
            int minDistance = Integer.MAX_VALUE;
            int nextNode = -1;

            for (int j = 0; j < matrice[node].length; j++) {
                if (matrice[node][j] > 0 && !visited.contains(j)
                        && matrice[node][j] < minDistance) {
                    minDistance = matrice[node][j];
                    nextNode = j;
                }
            }

            if (nextNode != -1) {
                stack.push(nextNode);
                sb.append("  Descente vers : ").append(noms[nextNode])
                        .append(" (distance : ").append(minDistance).append(")\n");
            } else {
                stack.pop();
            }
        }

        sb.append("\nParcours terminé !\n");
        sb.append("Ordre de visite : ").append(formatVisited(noms)).append("\n");
        return sb.toString();
    }

    // --- utilitaires ---
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
