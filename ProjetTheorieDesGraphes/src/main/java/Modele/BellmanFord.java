package Modele;

import java.util.*;

/**
 * Algorithme de Bellman-Ford (version EFREI, sans affichage console)
 * -------------------------------------------------------
 * Retourne le résultat sous forme de texte pour être affiché ailleurs (GUI, console, etc.)
 */
public class BellmanFord {

    public BellmanFord(StepManager stepManager) {
        // constructeur conservé pour compatibilité graphique
    }

    /**
     * L'utilisateur choisit le sommet de départ (nom, ex: "s1") et l'algo renvoie le résultat complet.
     * @param g Graphe sur lequel exécuter Bellman-Ford
     * @param sourceName Nom du sommet de départ (ex: "s1")
     * @return Résultat au format du cours EFREI
     */
    public String run(Graphe g, String sourceName) {
        int n = g.getMatrix().length;
        String[] noms = new String[n];
        for (int i = 0; i < n; i++) noms[i] = g.getVertexName(i);

        // Conversion du nom en index
        int start = -1;
        for (int i = 0; i < n; i++) {
            if (noms[i].equalsIgnoreCase(sourceName)) {
                start = i;
                break;
            }
        }

        if (start == -1) {
            return "Sommet invalide : " + sourceName + " (ex: s1, s2...).";
        }

        return getResult(g, start);
    }

    /**
     * Calcule les plus courts chemins depuis une source donnée (index du sommet).
     */
    public String getResult(Graphe g, int start) {
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

        // --- Détection cycle absorbant ---
        for (int u = 0; u < n; u++) {
            for (int v = 0; v < n; v++) {
                if (mat[u][v] != 0 && dist[u] + mat[u][v] < dist[v]) {
                    return "⚠️ Le graphe contient un cycle absorbant (poids négatif).";
                }
            }
        }

        // --- Construction du résultat texte ---
        StringBuilder sb = new StringBuilder();
        sb.append("Les PCCs (depuis ").append(noms[start]).append(") :\n");

        for (int i = 0; i < n; i++) {
            if (i == start || dist[i] == Double.POSITIVE_INFINITY) continue;

            // Reconstruction du chemin à rebours
            List<String> chemin = new ArrayList<>();
            int v = i;
            while (v != -1) {
                chemin.add(noms[v]);
                v = pere[v];
            }

            // Format : s4 ← s3 ← s5 ← s1 → PCC = ...
            for (int j = 0; j < chemin.size(); j++) {
                sb.append(chemin.get(j));
                if (j < chemin.size() - 1) sb.append(" ← ");
            }
            sb.append("   →   PCC = ").append((int) dist[i]).append("\n");
        }

        return sb.toString();
    }
}
