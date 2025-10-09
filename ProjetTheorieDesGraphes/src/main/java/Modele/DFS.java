package Modele;

import java.util.*;

public class DFS {

    private final List<Integer> visitOrder;

    public DFS() {
        this.visitOrder = new ArrayList<>();
    }

    /**
     * DFS itératif avec pile, toujours descendre vers l'arête de plus petite distance
     *
     * @param graphe Graphe à parcourir
     * @param startNode Indice du sommet de départ
     */
    public void parcours(Graphe graphe, int startNode) {
        int[][] matrice = graphe.getMatrix();
        String[] noms = getVertexNames(graphe);

        Set<Integer> visited = new HashSet<>();
        Stack<Integer> stack = new Stack<>();
        stack.push(startNode);

        System.out.println("Départ depuis : " + noms[startNode]);
        System.out.println();

        while (!stack.isEmpty()) {
            int node = stack.peek();

            // Visiter le sommet si pas encore fait
            if (!visited.contains(node)) {
                visited.add(node);
                visitOrder.add(node);
                System.out.println("Visite du sommet : " + noms[node]);
            }

            // Trouver le voisin non visité avec la plus petite distance
            int minDistance = Integer.MAX_VALUE;
            int nextNode = -1;

            for (int j = 0; j < matrice[node].length; j++) {
                if (matrice[node][j] > 0 && !visited.contains(j) && matrice[node][j] < minDistance) {
                    minDistance = matrice[node][j];
                    nextNode = j;
                }
            }

            if (nextNode != -1) {
                stack.push(nextNode);
                System.out.println("  Descente vers : " + noms[nextNode] + " (distance : " + minDistance + ")");
            } else {
                // Aucun voisin non visité → remonter
                stack.pop();
            }
        }

        System.out.println();
        System.out.println("Parcours terminé !");
        System.out.println("Ordre de visite : " + formatVisited(noms));
    }

    // --- Récupérer les noms des sommets via reflection ---
    private String[] getVertexNames(Graphe g) {
        try {
            var field = Graphe.class.getDeclaredField("vertexNames");
            field.setAccessible(true);
            return (String[]) field.get(g);
        } catch (Exception e) {
            throw new RuntimeException("Impossible de récupérer les noms des sommets du graphe.", e);
        }
    }

    // --- Formater l'ordre de visite ---
    private String formatVisited(String[] noms) {
        List<String> lst = new ArrayList<>();
        for (int v : visitOrder) {
            lst.add(noms[v]);
        }
        return String.join(" → ", lst);
    }

    // --- Obtenir l'ordre de visite sous forme d'indices ---
    public List<Integer> getVisitedOrder() {
        return new ArrayList<>(visitOrder);
    }
}
