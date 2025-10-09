package Modele;

import java.util.*;

public class BFS {
    private final Queue<Integer> queue;
    private final Set<Integer> visited;
    private final List<Integer> visitOrder;

    public BFS() {
        this.queue = new LinkedList<>();
        this.visited = new HashSet<>();
        this.visitOrder = new ArrayList<>();
    }


    /**
     * Parcours BFS avec priorité aux distances minimales.
     * À chaque niveau, on ajoute TOUS les voisins triés par distance croissante.
     *
     * @param graphe le graphe à parcourir
     * @param startNode indice du sommet de départ
     */
    public void parcours(Graphe graphe, int startNode) {
        int[][] matrice = graphe.getMatrix();
        String[] noms = getVertexNames(graphe);

        queue.add(startNode);
        visited.add(startNode);
        visitOrder.add(startNode);

        System.out.println("Départ depuis : " + noms[startNode]);
        System.out.println();

        while (!queue.isEmpty()) {
            int node = queue.poll();
            System.out.println("Visite du sommet : " + noms[node]);

            // Collecter tous les voisins non visités
            List<Voisin> voisins = new ArrayList<>();
            for (int j = 0; j < matrice[node].length; j++) {
                int poids = matrice[node][j];
                if (poids > 0 && !visited.contains(j)) {
                    voisins.add(new Voisin(j, poids));
                }
            }

            // Trier les voisins par distance croissante
            voisins.sort(Comparator.comparingInt(v -> v.poids));

            // Ajouter TOUS les voisins triés à la queue
            if (!voisins.isEmpty()) {
                System.out.println("  Voisins ajoutés (ordre croissant) :");
                for (Voisin v : voisins) {
                    queue.add(v.id);
                    visited.add(v.id);
                    visitOrder.add(v.id);
                    System.out.println("    → " + noms[v.id] + " (distance : " + v.poids + ")");
                }
            } else {
                System.out.println("  Aucun nouveau voisin");
            }
            System.out.println();
        }

        System.out.println("Parcours terminé !");
        System.out.println("Ordre de visite : " + formatVisited(noms));
    }

    // --- Méthodes utilitaires ---

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
        for (int v : visitOrder) {
            lst.add(noms[v]);
        }
        return String.join(" → ", lst);
    }

    /**
     * Méthode pour obtenir la liste ordonnée des sommets visités
     */
    public List<Integer> getVisitedOrder() {
        return new ArrayList<>(visitOrder);
    }
}