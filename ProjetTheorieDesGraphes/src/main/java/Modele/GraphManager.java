package Modele;

import Vue.GraphDrawer;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;

public class GraphManager {


    public static SmartGraphPanel<String, String> buildSmartGraph(Graphe g) {
        Graph<String, String> sg = new GraphEdgeList<>();

        // Ajouter les sommets
        for (int i = 0; i < g.getNbSommets(); i++) {
            sg.insertVertex(g.getVertexName(i));
        }

        int[][] matrix = g.getMatrix();
        int n = g.getNbSommets();

        // Ajouter les arêtes
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (matrix[i][j] > 0) {
                    String from = g.getVertexName(i);
                    String to = g.getVertexName(j);
                    int weight = matrix[i][j];

                    // Élément unique interne (évite les collisions dans la structure)
                    String uniqueElement = from + "-" + to + "#" + weight;

                    try {
                        sg.insertEdge(from, to, uniqueElement);
                    } catch (Exception ex) {
                        System.err.println("Erreur ajout " + uniqueElement + " : " + ex.getMessage());
                    }
                }
            }
        }

        SmartGraphProperties props = new SmartGraphProperties();
        SmartCircularSortedPlacementStrategy placement = new SmartCircularSortedPlacementStrategy();
        SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(sg, props, placement);

        // ✅ Afficher uniquement le poids comme label
        graphView.setEdgeLabelProvider(edgeElement -> {
            if (edgeElement.contains("#")) {
                return edgeElement.split("#")[1]; // retourne juste le poids
            }
            return edgeElement; // fallback
        });

        graphView.setAutomaticLayout(false);
        return graphView;
    }





    // Initialisation d'un graphe par défaut
    public static Graphe initDefaultGraph(Canvas canvas,TextArea textArea) {
        String[] villes = { "Paris", "Caen", "Rennes", "Nantes", "Bordeaux",
                "Lille", "Nancy", "Dijon", "Lyon", "Grenoble" };

        // Créer un graphe non orienté et pondéré
        Graphe g = new Graphe(villes.length, false, true, villes);

        // Ajouter les 20 arêtes (la classe Graphe remplit symétrique automatiquement)
        g.addEdge(0, 1, 50);   // Paris - Caen
        g.addEdge(0, 2, 110);  // Paris - Rennes
        g.addEdge(0, 3, 80);   // Paris - Nantes
        g.addEdge(0, 4, 150);  // Paris - Bordeaux
        g.addEdge(0, 5, 70);   // Paris - Lille
        g.addEdge(0, 7, 60);   // Paris - Dijon
        g.addEdge(1, 2, 75);   // Caen - Rennes
        g.addEdge(1, 5, 65);   // Caen - Lille
        g.addEdge(2, 3, 45);   // Rennes - Nantes
        g.addEdge(2, 4, 130);  // Rennes - Bordeaux
        g.addEdge(3, 4, 90);   // Nantes - Bordeaux
        g.addEdge(4, 8, 100);  // Bordeaux - Lyon
        g.addEdge(5, 6, 100);  // Lille - Nancy
        g.addEdge(5, 7, 120);  // Lille - Dijon
        g.addEdge(6, 7, 75);   // Nancy - Dijon
        g.addEdge(6, 8, 90);   // Nancy - Lyon
        g.addEdge(6, 9, 80);   // Nancy - Grenoble
        g.addEdge(7, 8, 70);   // Dijon - Lyon
        g.addEdge(7, 9, 75);   // Dijon - Grenoble
        g.addEdge(8, 9, 40);   // Lyon - Grenoble

        return g;
    }

    // -----------------------------
    // Méthodes génériques pour exécuter un algorithme
    // -----------------------------
    private static String[] runAlgorithm(String title, AlgorithmResult algo) {
        String full = "=== " + title + " ===\n" + algo.getResult();
        return split(full);
    }

    public static String[] runBFS(BFS bfs, Graphe g, int start) { return runAlgorithm("Parcours en largeur (BFS)", () -> bfs.getResult(g, start)); }
    public static String[] runDFS(DFS dfs, Graphe g, int start) { return runAlgorithm("Parcours en profondeur (DFS)", () -> dfs.getResult(g, start)); }
    public static String[] runKruskal(Kruskal kruskal, Graphe g) { return runAlgorithm("Algorithme de Kruskal", () -> kruskal.getResult(g)); }
    public static String[] runPrim(Prim prim, Graphe g, int start) { return runAlgorithm("Algorithme de Prim", () -> prim.getResult(g, start)); }
    public static String[] runDijkstra(Dijkstra dijkstra, Graphe g, int start, int end) { return runAlgorithm("Plus court chemin entre deux villes (Dijkstra)", () -> dijkstra.getResult(g, start, end)); }
    public static String[] runBellmanFord(BellmanFord bf, Graphe g, int start, int end) { return runAlgorithm("Algorithme de Bellman-Ford", () -> bf.getResult(g, start, end)); }
    public static String[] runFloydWarshall(FloydWarshall fw, Graphe g, int start, int end) { return runAlgorithm("Algorithme de Floyd-Warshall", () -> fw.getResult(g, start, end)); }

    private static String[] split(String fullText) {
        String[] lines = fullText.split("\n");
        if (lines.length == 0) return new String[]{"",""};
        StringBuilder steps = new StringBuilder();
        for (int i=0; i<lines.length-1; i++) steps.append(lines[i]).append("\n");
        return new String[]{steps.toString(), lines[lines.length-1]};
    }

    public static boolean hasNegativeWeight(Graphe g) {
        int[][] mat = g.getMatrix();
        for (int i=0;i<mat.length;i++)
            for (int j=0;j<mat[i].length;j++)
                if (mat[i][j]<0) return true;
        return false;
    }

    // -----------------------------
    // Highlight path sur SmartGraphPanel
    // -----------------------------
    public static void highlightPath(SmartGraphPanel<String,String> panel, Graphe g, List<Integer> path) {
        if (panel == null || g == null || path == null || path.size() < 2) return;

        String vertexDefault = "-fx-fill: #2E5C8A; -fx-stroke: black;";
        String edgeDefault   = "-fx-stroke: black; -fx-stroke-width: 2;";

        // Reset
        panel.getModel().vertices().forEach(v -> panel.getStylableVertex(v).setStyleInline(vertexDefault));
        panel.getModel().edges().forEach(e -> panel.getStylableEdge(e).setStyleInline(edgeDefault));

        // Colorier le chemin
        for (int i=0; i<path.size()-1; i++) {
            String from = g.getVertexName(path.get(i));
            String to   = g.getVertexName(path.get(i+1));

            Vertex<String> vFrom = panel.getModel().vertices().stream().filter(v->v.element().equals(from)).findFirst().orElse(null);
            Vertex<String> vTo   = panel.getModel().vertices().stream().filter(v->v.element().equals(to)).findFirst().orElse(null);

            if (vFrom != null) panel.getStylableVertex(vFrom).setStyleInline("-fx-fill: violet; -fx-stroke: black;");
            if (vTo != null) panel.getStylableVertex(vTo).setStyleInline("-fx-fill: violet; -fx-stroke: black;");

            if (vFrom != null && vTo != null) {
                Edge<String,String> e = panel.getModel().edges().stream()
                        .filter(edge -> (edge.vertices()[0].element().equals(from) && edge.vertices()[1].element().equals(to)) ||
                                (edge.vertices()[0].element().equals(to) && edge.vertices()[1].element().equals(from)))
                        .findFirst().orElse(null);
                if (e != null) panel.getStylableEdge(e).setStyleInline("-fx-stroke: red; -fx-stroke-width: 3;");
            }
        }

        panel.update();
    }

    // -----------------------------
    // Interface pour factoriser runAlgorithm
    // -----------------------------
    private interface AlgorithmResult {
        String getResult();
    }
}
