package Modele;

import java.util.*;

/**
 * Implémentation complète de l’algorithme de Bellman-Ford,
 * utilisée pour calculer le plus court chemin entre deux sommets
 * d’un graphe pondéré pouvant contenir des arêtes à poids négatif.
 *
 * L’algorithme repose sur la relaxation successive de toutes les arêtes
 * pendant n-1 itérations (où n est le nombre de sommets).
 * Il est également capable de détecter la présence de cycles absorbants
 * (cycles dont la somme des poids est négative).
 *
 * Cette classe génère un texte explicatif contenant les étapes de calcul
 * (utile pour l’affichage dans l’interface graphique), ainsi que la
 * distance minimale et le chemin final trouvé.
 */
public class BellmanFord {

    /** Liste contenant les indices des sommets du chemin final. */
    private List<Integer> finalPath = new ArrayList<>();

    /** Constructeur vide pour compatibilité avec la partie graphique. */
    public BellmanFord() {}

    /**
     * Retourne le chemin final (sous forme d’une liste d’indices de sommets)
     * trouvé après exécution de l’algorithme.
     *
     * @return une liste d’indices représentant le plus court chemin
     */
    public List<Integer> getFinalPath() {
        return finalPath;
    }

    /**
     * Exécute l’algorithme de Bellman-Ford à partir des noms des sommets
     * source et destination.
     *
     * @param g graphe sur lequel exécuter l’algorithme
     * @param sourceName nom du sommet de départ
     * @param destName nom du sommet d’arrivée
     * @return une chaîne de caractères contenant le déroulement détaillé
     *         et le résultat de l’algorithme
     */
    public String run(Graphe g, String sourceName, String destName) {
        int n = g.getMatrix().length;
        String[] noms = new String[n];
        for (int i = 0; i < n; i++) noms[i] = g.getVertexName(i);

        int start = -1, end = -1;
        for (int i = 0; i < n; i++) {
            if (noms[i].equalsIgnoreCase(sourceName)) start = i;
            if (noms[i].equalsIgnoreCase(destName)) end = i;
        }

        if (start == -1 || end == -1) {
            return "Sommet invalide. Exemples valides : s1, s2, s3, ...";
        }

        return getResult(g, start, end);
    }

    /**
     * Calcule le plus court chemin entre deux sommets donnés (indices)
     * à l’aide de l’algorithme de Bellman-Ford.
     *
     * Cette méthode :
     * - Initialise les distances et les prédécesseurs
     * - Effectue n-1 itérations de relaxation
     * - Détecte la présence éventuelle de cycles absorbants
     * - Reconstruit le chemin final et renvoie les résultats formatés
     *
     * @param g graphe sur lequel exécuter l’algorithme
     * @param start indice du sommet source
     * @param end indice du sommet destination
     * @return une chaîne contenant le déroulement de l’algorithme et son résultat
     */
    public String getResult(Graphe g, int start, int end) {
        int n = g.getMatrix().length;
        int[][] mat = g.getMatrix();
        String[] noms = new String[n];
        for (int i = 0; i < n; i++) noms[i] = g.getVertexName(i);

        double[] dist = new double[n];
        int[] pere = new int[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(pere, -1);
        dist[start] = 0;

        StringBuilder sb = new StringBuilder();

        // --- Phase de relaxation (n - 1 itérations) ---
        for (int k = 1; k <= n - 1; k++) {
            boolean change = false;
            sb.append("=== Étape k = ").append(k).append(" ===\n");

            for (int u = 0; u < n; u++) {
                for (int v = 0; v < n; v++) {
                    // Si une arête (u, v) existe et qu’elle améliore la distance actuelle
                    if (mat[u][v] != 0 && dist[u] + mat[u][v] < dist[v]) {
                        dist[v] = dist[u] + mat[u][v];
                        pere[v] = u;
                        change = true;
                        sb.append("Mise à jour : dist[").append(noms[v]).append("] = ").append((int) dist[v])
                                .append(", père = ").append(noms[u]).append("\n");
                    }
                }
            }

            if (!change) sb.append("Aucune mise à jour à cette étape.\n");
            sb.append("\n");
        }

        // --- Détection de cycle absorbant (poids négatif) ---
        for (int u = 0; u < n; u++) {
            for (int v = 0; v < n; v++) {
                if (mat[u][v] != 0 && dist[u] + mat[u][v] < dist[v]) {
                    sb.append("Le graphe contient un cycle absorbant (poids négatif).\n");
                    return sb.toString();
                }
            }
        }

        // --- Vérification d’inaccessibilité ---
        if (dist[end] == Double.POSITIVE_INFINITY) {
            sb.append("Aucun chemin n’existe entre ")
                    .append(noms[start]).append(" et ").append(noms[end]).append(".\n");
            finalPath.clear();
            return sb.toString();
        }

        // --- Reconstruction du plus court chemin ---
        finalPath.clear();
        int v = end;
        while (v != -1) {
            finalPath.add(v);
            v = pere[v];
        }
        Collections.reverse(finalPath);

        // --- Résumé final ---
        sb.append("Chemin le plus court de ").append(noms[start])
                .append(" à ").append(noms[end]).append(" :\n");

        sb.append(String.join(" → ", finalPath.stream().map(i -> noms[i]).toArray(String[]::new)));
        sb.append("   →   Distance = ").append((int) dist[end]).append("\n");

        return sb.toString();
    }
}
