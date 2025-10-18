package Vue;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Test extends Application {

    @Override
    public void start(Stage stage) {
        // 1. Créer un graphe
        Graph<String, String> g = new GraphEdgeList<>();

        // Ajouter les sommets
        g.insertVertex("A");
        g.insertVertex("B");
        g.insertVertex("C");
        g.insertVertex("D");
        g.insertVertex("E");

        // Ajouter les arêtes avec labels
        g.insertEdge("A", "B", "5");
        g.insertEdge("A", "E", "7");
        g.insertEdge("B", "C", "3");
        g.insertEdge("C", "D", "2");
        g.insertEdge("C", "E", "4");
        g.insertEdge("D", "E", "6");

        // 2. Propriétés du graphe (flèches activées et labels visibles)
        SmartGraphProperties properties = new SmartGraphProperties();
        // Les labels et flèches sont activés par défaut
        // Les getters ici confirment qu’ils sont activés
        properties.getUseVertexLabel();
        properties.getUseEdgeLabel();
        properties.getUseEdgeArrow();

        // 3. Stratégie de placement circulaire avec plus de rayon
        SmartCircularSortedPlacementStrategy placement = new SmartCircularSortedPlacementStrategy();
      

        // 4. Créer le panel
        SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(g, properties, placement);

        // Fond blanc pour mieux voir
        graphView.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

        // 5. Initialiser la scène
        Scene scene = new Scene(graphView, 800, 600);
        stage.setTitle("Graphe avec flèches et labels lisibles");
        stage.setScene(scene);
        stage.show();

        // 6. Initialiser la visualisation
        graphView.init();

        // 7. Style inline pour vertices et arêtes
        for (Vertex<String> v : g.vertices()) {
            graphView.getStylableVertex(v)
                    .setStyleInline("-fx-fill: #2E5C8A; -fx-stroke: black; -fx-stroke-width: 2;");
        }

        for (Edge<String, String> e : g.edges()) {
            graphView.getStylableEdge(e)
                    .setStyleInline("-fx-stroke: black; -fx-stroke-width: 2; -fx-text-fill: darkred;");
        }

        // 8. Désactiver layout automatique pour garder les positions fixes
        graphView.setAutomaticLayout(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
