package Modele;

import java.util.*;

/**
 * Implémentation de l’algorithme de parcours en largeur (BFS - Breadth First Search)
 * appliqué à un graphe pondéré.
 *
 * Le BFS explore le graphe couche par couche, en visitant d’abord
 * tous les voisins d’un sommet avant de passer au niveau suivant.
 *
 * Cette version de l’algorithme ajoute une particularité :
 * les voisins sont triés par distance croissante (poids minimal)
 * avant d’être ajoutés à la file d’attente, afin d’obtenir un parcours
 * plus cohérent visuellement sur les graphes pondérés.
 *
 * L’algorithme conserve :
 * - une trace du déroulement complet du parcours (pour affichage)
 * - l’ordre de visite des sommets
 * - le chemin final parcouru (pour la mise en surbrillance graphique)
 */
public class BFS {

    /** File utilisée pour gérer l’ordre de traitement des sommets (principe FIFO). */
    private final Queue<Integer> queue;

    /** Ensemble des sommets déjà visités pour éviter les doublons. */
    private final Set<Integer> visited;

    /** Liste de l’ordre exact dans lequel les sommets ont été visités. */
    private final List<Integer> visitOrder;

    /** Liste représentant le chemin final parcouru dans le graphe. */
    private final List<Integer> finalPath;

    /** Constructeur par défaut. Initialise les structures de données nécessaires. */
    public BFS() {
        this.queue = new LinkedList<>();
        this.visited = new HashSet<>();
        this.visitOrder = new ArrayList<>();
        this.finalPath = new ArrayList<>();
    }

    /**
     * Exécute le parcours en largeur (BFS) à partir d’un sommet de départ donné.
     * Les voisins de chaque sommet sont triés par distance croissante avant d’être visités.
     *
     * @param graphe le graphe sur lequel exécuter le BFS
     * @param startNode l’indice du sommet de départ
     * @return une chaîne de caractères décrivant les étapes du parcours
     */
    public String getResult(Graphe graphe, int startNode) {
        StringBuilder sb = new StringBuilder();

        int[][] matrice = graphe.getMatrix();
        String[] noms = getVertexNames(graphe);

        // Réinitialisation des structures avant chaque exécution
        queue.clear();
        visited.clear();
        visitOrder.clear();
        finalPath.clear();

        // Initialisation du parcours
        queue.add(startNode);
        visited.add(startNode);
        visitOrder.add(startNode);
        finalPath.add(startNode);

        sb.append("Départ depuis : ").append(noms[startNode]).append("\n\n");

        // Boucle principale du BFS
        while (!queue.isEmpty()) {
            int node = queue.poll();
            sb.append("Visite du sommet : ").append(noms[node]).append("\n");

            // Recherche des voisins non encore visités
            List<Voisin> voisins = new ArrayList<>();
            for (int j = 0; j < matrice[node].length; j++) {
                int poids = matrice[node][j];
                if (poids > 0 && !visited.contains(j)) {
                    voisins.add(new Voisin(j, poids));
                }
            }

            // Tri des voisins par poids croissant pour un affichage plus logique
            voisins.sort(Comparator.comparingInt(v -> v.poids));

            if (!voisins.isEmpty()) {
                sb.append("  Voisins ajoutés (ordre croissant) :\n");
                for (Voisin v : voisins) {
                    queue.add(v.id);
                    visited.add(v.id);
                    visitOrder.add(v.id);
                    finalPath.add(v.id);
                    sb.append("    → ").append(noms[v.id])
                            .append(" (distance : ").append(v.poids).append(")\n");
                }
            } else {
                sb.append("  Aucun nouveau voisin\n");
            }
            sb.append("\n");
        }

        // Fin du parcours
        sb.append("Parcours terminé !\n");
        sb.append("Ordre de visite : ").append(formatVisited(noms)).append("\n");

        return sb.toString();
    }

    /**
     * Retourne la liste des indices des sommets parcourus dans le chemin final.
     * Cette information est utilisée pour surligner les arêtes dans l’affichage graphique.
     *
     * @return une nouvelle liste contenant le chemin final parcouru
     */
    public List<Integer> getFinalPath() {
        return new ArrayList<>(finalPath);
    }

    // ---------------------------------------------------------------------
    // Méthodes utilitaires internes
    // ---------------------------------------------------------------------

    /**
     * Récupère les noms des sommets d’un graphe à l’aide de la réflexion.
     * Cette approche permet d’accéder directement à l’attribut privé "vertexNames"
     * sans modifier la classe Graphe.
     *
     * @param g graphe dont on souhaite obtenir les noms des sommets
     * @return un tableau de chaînes représentant les noms des sommets
     */
    private String[] getVertexNames(Graphe g) {
        try {
            var field = Graphe.class.getDeclaredField("vertexNames");
            field.setAccessible(true);
            return (String[]) field.get(g);
        } catch (Exception e) {
            throw new RuntimeException("Impossible de récupérer les noms des sommets du graphe.", e);
        }
    }

    /**
     * Formate l’ordre de visite des sommets pour un affichage clair et lisible.
     *
     * @param noms tableau des noms de sommets
     * @return chaîne formatée représentant l’ordre de visite (ex: "A → B → C → D")
     */
    private String formatVisited(String[] noms) {
        List<String> lst = new ArrayList<>();
        for (int v : visitOrder) lst.add(noms[v]);
        return String.join(" → ", lst);
    }
}
