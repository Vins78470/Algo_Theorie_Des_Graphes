package Modele;

/**
 * Classe utilitaire pour exécuter les différents algorithmes de graphes.
 * Chaque fonction retourne une String affichable dans le terminal ou sur le front.
 */
public class AlgoFunctions {

    /** --- BFS --- */
    public static String runBFS(Graphe g, int start) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Parcours en largeur (BFS) ===\n");
        BFS bfs = new BFS();
        sb.append(bfs.getResult(g, start));
        return sb.toString();
    }

    /** --- DFS --- */
    public static String runDFS(Graphe g, int start) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Parcours en profondeur (DFS) ===\n");
        DFS dfs = new DFS();
        sb.append(dfs.getResult(g, start));
        return sb.toString();
    }

    /** --- Kruskal --- */
    public static String runKruskal(Graphe g) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Algorithme de Kruskal ===\n");
        sb.append(Kruskal.getResult(g));
        return sb.toString();
    }

    /** --- Prim --- */
    public static String runPrim(Graphe g, int start) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Algorithme de Prim ===\n");
        sb.append(Prim.getResult(g, start));
        return sb.toString();
    }

    /** --- Dijkstra --- */
    public static String runDijkstra(Graphe g, int start, int end) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Plus court chemin entre deux villes (Dijkstra) ===\n");
        sb.append(Dijkstra.getResult(g, start, end));
        return sb.toString();
    }
}
