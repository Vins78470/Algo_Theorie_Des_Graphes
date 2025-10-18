package Vue;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

public class Test extends Application {

    @Override
    public void start(Stage stage) {
        // 1. Créer le graphe
        Graph<String, String> g = new GraphEdgeList<>();

        // Ajouter les sommets
        g.insertVertex("A");
        g.insertVertex("B");
        g.insertVertex("C");
        g.insertVertex("D");
        g.insertVertex("E");

        // Ajouter les arêtes
        g.insertEdge("A", "B", "5");
        g.insertEdge("A", "E", "7");
        g.insertEdge("B", "C", "3");
        g.insertEdge("C", "D", "2");
        g.insertEdge("C", "E", "4");
        g.insertEdge("D", "E", "6");

        // 2. Propriétés du graphe (flèches activées et labels visibles)
        SmartGraphProperties properties = new SmartGraphProperties();

        // 3. Stratégie de placement circulaire
        SmartCircularSortedPlacementStrategy placement = new SmartCircularSortedPlacementStrategy();

        // 4. Créer le panel
        SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(g, properties, placement);
        graphView.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

        // 5. Initialiser la scène
        Scene scene = new Scene(graphView, 800, 600);
        stage.setTitle("Graphe avec étape par étape");
        stage.setScene(scene);
        stage.show();

        // 6. Initialiser la visualisation
        graphView.init();

        // 7. Style initial des sommets et arêtes
        for (Vertex<String> v : g.vertices()) {
            graphView.getStylableVertex(v)
                    .setStyleInline("-fx-fill: #2E5C8A; -fx-stroke: black; -fx-stroke-width: 2;");
        }
        for (Edge<String, String> e : g.edges()) {
            graphView.getStylableEdge(e)
                    .setStyleInline("-fx-stroke: black; -fx-stroke-width: 2;");
        }

        graphView.setAutomaticLayout(false);

        Vertex<String> start = g.vertices()
                .stream()
                .filter(v -> v.element().equals("A"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Vertex not found"));

        new Thread(() -> bfsStepByStep(g, graphView, start)).start();

    }

    private void bfsStepByStep(Graph<String, String> g, SmartGraphPanel<String, String> graphView, Vertex<String> start) {
        Queue<Vertex<String>> queue = new LinkedList<>();
        Set<Vertex<String>> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Vertex<String> current = queue.poll();

            // Colorier le sommet courant en jaune
            Platform.runLater(() -> graphView.getStylableVertex(current)
                    .setStyleInline("-fx-fill: yellow; -fx-stroke: black; -fx-stroke-width: 2;"));

            try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }

            for (Edge<String, String> e : g.edges()) {
                Vertex<String> src = e.vertices()[0];
                Vertex<String> tgt = e.vertices()[1];

                if (src.equals(current) && !visited.contains(tgt)) {
                    visited.add(tgt);
                    queue.add(tgt);

                    Vertex<String> finalTgt = tgt;
                    Edge<String, String> finalE = e;

                    // Colorier l'arête parcourue en rouge
                    Platform.runLater(() -> {
                        graphView.getStylableEdge(finalE)
                                .setStyleInline("-fx-stroke: red; -fx-stroke-width: 3;");
                        graphView.getStylableVertex(finalTgt)
                                .setStyleInline("-fx-fill: lightgreen; -fx-stroke: black; -fx-stroke-width: 2;");
                    });

                    try { Thread.sleep(1000); } catch (InterruptedException ex) { ex.printStackTrace(); }
                }
            }

            // Recolorier le sommet courant en vert foncé pour marquer qu'il est terminé
            Platform.runLater(() -> graphView.getStylableVertex(current)
                    .setStyleInline("-fx-fill: darkgreen; -fx-stroke: black; -fx-stroke-width: 2;"));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
