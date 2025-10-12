package Modele;

import Vue.GraphDrawer;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextArea;

public class GraphManager {

    // Méthode statique pour initialiser le graphe par défaut
    public static Graphe initDefaultGraph(Canvas graphCanvas, TextArea stepsTextArea) {
        String[] villes = {
                "Paris", "Caen", "Rennes", "Nantes", "Bordeaux",
                "Lille", "Nancy", "Dijon", "Lyon", "Grenoble"
        };

        int n = villes.length;
        Graphe g = new Graphe(n, false, true, villes); // non orienté, pondéré

        // --- Arêtes pondérées ---
        g.addEdge(0, 1, 50);
        g.addEdge(0, 2, 110);
        g.addEdge(0, 3, 80);
        g.addEdge(0, 4, 150);
        g.addEdge(0, 5, 70);
        g.addEdge(0, 7, 60);
        g.addEdge(1, 2, 75);
        g.addEdge(1, 5, 65);
        g.addEdge(2, 3, 45);
        g.addEdge(2, 4, 130);
        g.addEdge(3, 4, 90);
        g.addEdge(4, 8, 100);
        g.addEdge(5, 6, 100);
        g.addEdge(5, 7, 120);
        g.addEdge(6, 7, 75);
        g.addEdge(6, 8, 90);
        g.addEdge(6, 9, 80);
        g.addEdge(7, 8, 70);
        g.addEdge(7, 9, 75);
        g.addEdge(8, 9, 40);

        // Dessine le graphe sur le Canvas si fourni
        if (graphCanvas != null) {
            GraphDrawer gd = new GraphDrawer(g);
            gd.drawGraph(graphCanvas);
        }

        // Affiche le graphe dans la TextArea si fourni
        if (stepsTextArea != null) {
            stepsTextArea.clear();
            stepsTextArea.setText(g.toString());
        }

        return g; // ✅ ON RETOURNE LE GRAPHE ICI
    }


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
