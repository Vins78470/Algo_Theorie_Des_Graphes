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

        // Dessine le graphe sur le Canvas
        if (graphCanvas != null) {
            GraphDrawer gd = new GraphDrawer(g);
            gd.drawGraph(graphCanvas);
        }

        // Affiche le graphe dans la TextArea
        if (stepsTextArea != null) {
            stepsTextArea.clear();
            stepsTextArea.setText(g.toString());
        }

        return g;
    }

    /** --- BFS --- */
    public static String[] runBFS(Graphe g, int start) {
        String full = "=== Parcours en largeur (BFS) ===\n" + new BFS().getResult(g, start);
        return split(full);
    }

    /** --- DFS --- */
    public static String[] runDFS(Graphe g, int start) {
        String full = "=== Parcours en profondeur (DFS) ===\n" + new DFS().getResult(g, start);
        return split(full);
    }

    /** --- Kruskal --- */
    public static String[] runKruskal(Graphe g) {
        String full = "=== Algorithme de Kruskal ===\n" + Kruskal.getResult(g);
        return split(full);
    }

    /** --- Prim --- */
    public static String[] runPrim(Graphe g, int start) {
        String full = "=== Algorithme de Prim ===\n" + Prim.getResult(g, start);
        return split(full);
    }

    /** --- Dijkstra --- */
    public static String[] runDijkstra(Graphe g, int start, int end) {
        String full = "=== Plus court chemin entre deux villes (Dijkstra) ===\n" + Dijkstra.getResult(g, start, end);
        return split(full);
    }


    private static String[] split(String fullText) {
        String[] lines = fullText.split("\n");
        if (lines.length == 0) return new String[] { "", "" };

        String result = lines[lines.length - 1]; // Dernière ligne = résultat
        StringBuilder steps = new StringBuilder();

        for (int i = 0; i < lines.length - 1; i++) {
            steps.append(lines[i]).append("\n");
        }

        return new String[] { steps.toString(), result };
    }

}
