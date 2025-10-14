package Modele;

import java.util.*;

/**
 * Algorithme de Bellman-Ford (cours EFREI)
 * Version ciblée : plus court chemin entre deux sommets choisis.
 */
public class BellmanFord {

    public BellmanFord(StepManager stepManager) {
        // Constructeur conservé pour compatibilité graphique
    }

    /**
     * Lance Bellman-Ford entre deux sommets donnés par leur nom (ex: "s1", "s6")
     */
    public String run(Graphe g, String sourceName, String destName) {
        int n = g.getMatrix().length;
        String[] noms = new String[n];
        for (int i = 0; i < n; i++) noms[i] = g.getVertexName(i);

        // Conversion noms → indices
        int start = -1, end = -1;
        for (int i = 0; i < n; i++) {
            if (noms[i].equalsIgnoreCase(sourceName)) start = i;
            if (noms[i].equalsIgnoreCase(destName)) end = i;
        }
//jsuis scillé mon reuf
        if (start == -1 || end == -1) {
            return "Sommet invalide. Exemples valides : s1, s2, s3, ...";
        }

        return getResult(g, start, end);
    }

    /**
     * Calcule le plus court chemin de start à end avec Bellman-Ford
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

        // --- Relaxation (n-1 fois) ---
        for (int k = 1; k <= n - 1; k++) {
            for (int u = 0; u < n; u++) {
                for (int v = 0; v < n; v++) {
                    if (mat[u][v] != 0 && dist[u] + mat[u][v] < dist[v]) {
                        dist[v] = dist[u] + mat[u][v];
                        pere[v] = u;
                    }
                }
            }
        }

        // --- Détection de cycle absorbant ---
        for (int u = 0; u < n; u++) {
            for (int v = 0; v < n; v++) {
                if (mat[u][v] != 0 && dist[u] + mat[u][v] < dist[v]) {
                    return "Le graphe contient un cycle absorbant (poids négatif).";
                }
            }
        }

        // --- Cas où aucun chemin n’existe ---
        if (dist[end] == Double.POSITIVE_INFINITY) {
            return "Aucun chemin n’existe entre " + noms[start] + " et " + noms[end] + ".";
        }

        // --- Reconstruction du chemin à rebours ---
        List<String> chemin = new ArrayList<>();
        int v = end;
        while (v != -1) {
            chemin.add(noms[v]);
            v = pere[v];
        }

        // --- Format du résultat ---
        StringBuilder sb = new StringBuilder();
        sb.append("Chemin le plus court de ").append(noms[start]).append(" à ").append(noms[end]).append(" :\n");

        for (int j = 0; j < chemin.size(); j++) {
            sb.append(chemin.get(j));
            if (j < chemin.size() - 1) sb.append(" ← ");
        }

        sb.append("   →   PCC = ").append((int) dist[end]).append("\n");

        return sb.toString();
    }
}
