package Modele;

import java.util.*;

/**
 * Algorithme de Bellman-Ford avec affichage des étapes
 */
public class BellmanFord {

    private List<Integer> finalPath = new ArrayList<>(); // ✅ Chemin final stocké ici

    public BellmanFord() {
        // Constructeur pour compatibilité graphique
    }

    public List<Integer> getFinalPath() {  // ✅ Getter public
        return finalPath;
    }

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

        // --- Relaxation (n-1 fois) ---
        for (int k = 1; k <= n - 1; k++) {
            boolean change = false;
            sb.append("=== Étape k = ").append(k).append(" ===\n");

            for (int u = 0; u < n; u++) {
                for (int v = 0; v < n; v++) {
                    if (mat[u][v] != 0 && dist[u] + mat[u][v] < dist[v]) {
                        dist[v] = dist[u] + mat[u][v];
                        pere[v] = u;
                        change = true;
                        sb.append(String.format("Mise à jour : dist[%s] = %.0f, père = %s\n", noms[v], dist[v], noms[u]));
                    }
                }
            }

            if (!change) sb.append("Aucune mise à jour cette étape.\n");
            sb.append("\n");
        }

        // --- Détection de cycle absorbant ---
        for (int u = 0; u < n; u++) {
            for (int v = 0; v < n; v++) {
                if (mat[u][v] != 0 && dist[u] + mat[u][v] < dist[v]) {
                    sb.append("⚠️ Le graphe contient un cycle absorbant (poids négatif).\n");
                    return sb.toString();
                }
            }
        }

        // --- Cas où aucun chemin n’existe ---
        if (dist[end] == Double.POSITIVE_INFINITY) {
            sb.append("Aucun chemin n’existe entre " + noms[start] + " et " + noms[end] + ".\n");
            finalPath.clear(); // ✅ Aucune solution
            return sb.toString();
        }

        // --- Reconstruction du chemin à rebours ---
        finalPath.clear(); // ✅ Vide avant d'ajouter
        int v = end;
        while (v != -1) {
            finalPath.add(v);
            v = pere[v];
        }
        Collections.reverse(finalPath);

        // --- Affichage ---
        sb.append("Chemin le plus court de ").append(noms[start]).append(" à ").append(noms[end]).append(" :\n");
        sb.append(String.join(" → ", finalPath.stream().map(i -> noms[i]).toArray(String[]::new)));
        sb.append("   →   Distance = ").append((int) dist[end]).append("\n");

        return sb.toString();
    }
}
