package Modele;

import java.util.*;

public class BFS {
    private final Queue<Integer> queue;
    private final Set<Integer> visited;
    private final List<Integer> visitOrder;
    private final List<Integer> finalPath; // <-- nouvelle liste pour le chemin final

    public BFS() {
        this.queue = new LinkedList<>();
        this.visited = new HashSet<>();
        this.visitOrder = new ArrayList<>();
        this.finalPath = new ArrayList<>();
    }

    /**
     * Parcours BFS avec priorité aux distances minimales.
     * À chaque niveau, on ajoute TOUS les voisins triés par distance croissante.
     *
     * @param graphe le graphe à parcourir
     * @param startNode indice du sommet de départ
     * @return String représentant le déroulement du parcours
     */
    public String getResult(Graphe graphe, int startNode) {
        StringBuilder sb = new StringBuilder();

        int[][] matrice = graphe.getMatrix();
        String[] noms = getVertexNames(graphe);

        queue.clear();
        visited.clear();
        visitOrder.clear();
        finalPath.clear(); // <-- réinitialisation

        queue.add(startNode);
        visited.add(startNode);
        visitOrder.add(startNode);
        finalPath.add(startNode); // <-- ajout au chemin final

        sb.append("Départ depuis : ").append(noms[startNode]).append("\n\n");

        while (!queue.isEmpty()) {
            int node = queue.poll();
            sb.append("Visite du sommet : ").append(noms[node]).append("\n");

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

            if (!voisins.isEmpty()) {
                sb.append("  Voisins ajoutés (ordre croissant) :\n");
                for (Voisin v : voisins) {
                    queue.add(v.id);
                    visited.add(v.id);
                    visitOrder.add(v.id);
                    finalPath.add(v.id); // <-- ajout au chemin final
                    sb.append("    → ").append(noms[v.id])
                            .append(" (distance : ").append(v.poids).append(")\n");
                }
            } else {
                sb.append("  Aucun nouveau voisin\n");
            }
            sb.append("\n");
        }

        sb.append("Parcours terminé !\n");
        sb.append("Ordre de visite : ").append(formatVisited(noms)).append("\n");

        return sb.toString();
    }

    /** Retourne le chemin final sous forme de liste d’indices */
    public List<Integer> getFinalPath() {
        return new ArrayList<>(finalPath);
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
        for (int v : visitOrder) lst.add(noms[v]);
        return String.join(" → ", lst);
    }


}
