package Modele;

import java.util.*;

/**
 * Implémentation de l’algorithme de parcours en profondeur (DFS - Depth First Search)
 * appliqué à un graphe pondéré.
 *
 * Le DFS explore un graphe en suivant chaque branche jusqu’à son extrémité
 * avant de revenir en arrière. Cette approche permet une exploration exhaustive
 * des chemins possibles à partir d’un sommet donné.
 *
 * Dans cette version, lorsqu’un sommet possède plusieurs voisins non visités,
 * l’algorithme choisit celui ayant la distance (poids) la plus faible,
 * afin d’obtenir un parcours plus logique et lisible dans le contexte des graphes pondérés.
 *
 * L’algorithme conserve :
 * - l’ordre des sommets visités
 * - le chemin complet parcouru (finalPath)
 * - un texte descriptif détaillant chaque étape du parcours
 */
public class DFS {

    /** Liste représentant l’ordre de visite des sommets pendant le parcours. */
    private final List<Integer> visitOrder = new ArrayList<>();

    /** Liste représentant le chemin complet parcouru, utilisée pour l’affichage graphique. */
    private final List<Integer> finalPath = new ArrayList<>();

    /**
     * Exécute le parcours en profondeur à partir d’un sommet de départ donné.
     * Les voisins sont explorés selon l’ordre de leur distance croissante.
     *
     * @param graphe le graphe à parcourir
     * @param startNode indice du sommet de départ
     * @return une chaîne contenant le déroulement complet du parcours
     */
    public String getResult(Graphe graphe, int startNode) {
        StringBuilder sb = new StringBuilder();
        int[][] matrice = graphe.getMatrix();
        String[] noms = getVertexNames(graphe);

        Set<Integer> visited = new HashSet<>();
        Stack<Integer> stack = new Stack<>();
        visitOrder.clear();
        finalPath.clear();

        // Initialisation du parcours
        stack.push(startNode);
        finalPath.add(startNode);
        sb.append("Départ depuis : ").append(noms[startNode]).append("\n\n");

        // Parcours principal en profondeur
        while (!stack.isEmpty()) {
            int node = stack.peek();

            // Si le sommet n’a pas encore été visité, on le marque
            if (!visited.contains(node)) {
                visited.add(node);
                visitOrder.add(node);
                sb.append("Visite du sommet : ").append(noms[node]).append("\n");
            }

            // Recherche du prochain voisin à explorer
            int nextNode = findNextNode(node, matrice, visited);
            if (nextNode != -1) {
                // On descend dans la branche
                stack.push(nextNode);
                finalPath.add(nextNode);
                sb.append("  Descente vers : ").append(noms[nextNode])
                        .append(" (distance : ").append(matrice[node][nextNode]).append(")\n");
            } else {
                // Aucun voisin non visité → on remonte (backtrack)
                stack.pop();
            }
        }

        sb.append("\nParcours terminé !\n");
        sb.append("Ordre de visite : ").append(formatVisited(noms)).append("\n");
        return sb.toString();
    }

    /**
     * Retourne le chemin complet parcouru par le DFS.
     * Cette liste est utilisée pour mettre en surbrillance les arêtes
     * lors de la visualisation graphique du parcours.
     *
     * @return une nouvelle liste contenant les indices des sommets visités
     */
    public List<Integer> getFinalPath() {
        return new ArrayList<>(finalPath);
    }

    // ---------------------------------------------------------------------
    // Méthodes utilitaires internes
    // ---------------------------------------------------------------------

    /**
     * Recherche le prochain sommet à visiter à partir du sommet courant.
     * Parmi les voisins non encore visités, le sommet relié par l’arête
     * de poids minimal est choisi en priorité.
     *
     * @param node indice du sommet courant
     * @param matrice matrice d’adjacence du graphe
     * @param visited ensemble des sommets déjà visités
     * @return l’indice du prochain sommet à visiter, ou -1 s’il n’en existe pas
     */
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

    /**
     * Récupère les noms des sommets du graphe à l’aide de la réflexion.
     * Cette méthode permet d’accéder à l’attribut privé "vertexNames"
     * sans le modifier dans la classe Graphe.
     *
     * @param g le graphe dont on veut extraire les noms des sommets
     * @return un tableau de chaînes contenant les noms des sommets
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
     * Formate l’ordre de visite des sommets pour un affichage clair.
     *
     * @param noms tableau contenant les noms des sommets du graphe
     * @return chaîne formatée représentant la séquence des sommets visités
     */
    private String formatVisited(String[] noms) {
        List<String> lst = new ArrayList<>();
        for (int v : visitOrder) lst.add(noms[v]);
        return String.join(" → ", lst);
    }
}
