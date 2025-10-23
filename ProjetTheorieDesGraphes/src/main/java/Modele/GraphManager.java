package Modele;

import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextArea;
import javafx.util.Duration;

import java.util.List;

/**
 * Classe de gestion du graphe et de son affichage à l’aide de la librairie SmartGraph.
 *
 * Elle relie les classes du modèle (comme {@link Graphe}) à la visualisation JavaFX.
 * Elle gère :
 * - la création du SmartGraphPanel à partir du graphe logique,
 * - l’exécution des algorithmes (BFS, DFS, Dijkstra, etc.),
 * - l’animation graphique des chemins calculés.
 *
 * Librairie utilisée : {@code com.brunomnsilva:smartgraph}
 */
public class GraphManager {

    /**
     * Construit un panneau graphique SmartGraph à partir d’un graphe logique.
     *
     * @param g graphe logique à convertir
     * @return SmartGraphPanel affichable dans une interface JavaFX
     */
    public static SmartGraphPanel<String, String> buildSmartGraph(Graphe g) {
        Graph<String, String> sg;

        if (g.isOriented()) {
            sg = new DigraphEdgeList<>() {};
        } else {
            sg = new GraphEdgeList<>();
        }

        // Ajout des sommets
        for (int i = 0; i < g.getNbSommets(); i++) {
            sg.insertVertex(g.getVertexName(i));
        }

        int[][] matrix = g.getMatrix();
        int n = g.getNbSommets();

        // Ajout des arêtes dans le modèle SmartGraph
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!g.isOriented() && j <= i) continue; // éviter doublons

                if (matrix[i][j] != 0) {
                    String from = g.getVertexName(i);
                    String to = g.getVertexName(j);
                    int weight = matrix[i][j];
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

        // Afficher uniquement le poids sur les arêtes
        graphView.setEdgeLabelProvider(edgeElement -> {
            if (edgeElement.contains("#")) {
                return edgeElement.split("#")[1];
            }
            return edgeElement;
        });

        graphView.setAutomaticLayout(false);
        return graphView;
    }

    /**
     * Crée un graphe de démonstration représentant un réseau de villes françaises.
     *
     * @param canvas objet Canvas (facultatif, pour compatibilité)
     * @param textArea zone de texte pour affichage console (facultatif)
     * @return graphe pondéré non orienté
     */
    public static Graphe initDefaultGraph(Canvas canvas, TextArea textArea) {
        String[] villes = { "Paris", "Caen", "Rennes", "Nantes", "Bordeaux",
                "Lille", "Nancy", "Dijon", "Lyon", "Grenoble" };

        Graphe g = new Graphe(villes.length, false, true, villes);

        // Connexions entre villes (pondérations fictives)
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

        return g;
    }

    /**
     * Exécute un algorithme et formate le résultat sous forme de tableau.
     *
     * @param title titre de l’algorithme
     * @param algo instance fonctionnelle renvoyant le résultat texte
     * @return tableau [0] = étapes, [1] = résultat final
     */
    private static String[] runAlgorithm(String title, AlgorithmResult algo) {
        String full = "=== " + title + " ===\n" + algo.getResult();
        return split(full);
    }

    /** Exécution du parcours en largeur (BFS). */
    public static String[] runBFS(BFS bfs, Graphe g, int start) {
        return runAlgorithm("Parcours en largeur (BFS)", () -> bfs.getResult(g, start));
    }

    /** Exécution du parcours en profondeur (DFS). */
    public static String[] runDFS(DFS dfs, Graphe g, int start) {
        return runAlgorithm("Parcours en profondeur (DFS)", () -> dfs.getResult(g, start));
    }

    /** Exécution de l’algorithme de Kruskal. */
    public static String[] runKruskal(Kruskal kruskal, Graphe g) {
        return runAlgorithm("Algorithme de Kruskal", () -> kruskal.getResult(g));
    }

    /** Exécution de l’algorithme de Prim. */
    public static String[] runPrim(Prim prim, Graphe g, int start) {
        return runAlgorithm("Algorithme de Prim", () -> prim.getResult(g, start));
    }

    /** Exécution de l’algorithme de Dijkstra. */
    public static String[] runDijkstra(Dijkstra dijkstra, Graphe g, int start, int end) {
        return runAlgorithm("Algorithme de Dijkstra", () -> dijkstra.getResult(g, start, end));
    }

    /** Exécution de l’algorithme de Bellman-Ford. */
    public static String[] runBellmanFord(BellmanFord bf, Graphe g, int start, int end) {
        return runAlgorithm("Algorithme de Bellman-Ford", () -> bf.getResult(g, start, end));
    }

    /** Exécution de l’algorithme de Floyd-Warshall. */
    public static String[] runFloydWarshall(FloydWarshall fw, Graphe g, int start, int end) {
        return runAlgorithm("Algorithme de Floyd-Warshall", () -> fw.getResult(g, start, end));
    }

    /**
     * Sépare le texte en deux parties : étapes et résultat final.
     *
     * @param fullText texte complet de l’algorithme
     * @return tableau contenant les étapes et le résumé
     */
    private static String[] split(String fullText) {
        String[] lines = fullText.split("\n");
        if (lines.length == 0) return new String[]{"", ""};

        StringBuilder steps = new StringBuilder();
        for (int i = 0; i < lines.length - 1; i++) {
            steps.append(lines[i]).append("\n");
        }
        return new String[]{steps.toString(), lines[lines.length - 1]};
    }

    /**
     * Vérifie si le graphe contient au moins une arête de poids négatif.
     *
     * @param g graphe à vérifier
     * @return true si une arête a un poids négatif, false sinon
     */
    public static boolean hasNegativeWeight(Graphe g) {
        int[][] mat = g.getMatrix();
        for (int i = 0; i < mat.length; i++)
            for (int j = 0; j < mat[i].length; j++)
                if (mat[i][j] < 0)
                    return true;
        return false;
    }

    /**
     * Anime un chemin sur le graphe affiché en colorant progressivement les sommets et arêtes.
     *
     * @param panel panneau SmartGraph contenant le graphe
     * @param g graphe logique associé
     * @param path liste des indices du chemin à surligner
     * @param delayMs délai entre chaque étape d’animation (en millisecondes)
     */
    public static void highlightPathAnimated(SmartGraphPanel<String, String> panel, Graphe g, List<Integer> path, double delayMs) {
        if (panel == null || g == null || path == null || path.isEmpty()) {
            return;
        }

        String vertexDefault = "-fx-fill: #2E5C8A; -fx-stroke: black; -fx-stroke-width: 1;";
        String edgeDefault = "-fx-stroke: black; -fx-stroke-width: 1.5; -fx-fill: transparent; -fx-stroke-line-cap: butt;";

        // Réinitialisation du style
        panel.getModel().vertices().forEach(v ->
                panel.getStylableVertex(v).setStyleInline(vertexDefault)
        );
        panel.getModel().edges().forEach(e ->
                panel.getStylableEdge(e).setStyleInline(edgeDefault)
        );
        panel.update();

        // Cas d’un seul sommet
        if (path.size() == 1) {
            String vertexName = g.getVertexName(path.get(0));
            Vertex<String> v = panel.getModel().vertices().stream()
                    .filter(x -> x.element().equals(vertexName))
                    .findFirst().orElse(null);

            if (v != null) {
                panel.getStylableVertex(v).setStyleInline("-fx-fill: violet; -fx-stroke: black; -fx-stroke-width: 1;");
                panel.update();
            }
            return;
        }

        // Animation progressive
        Timeline timeline = new Timeline();
        for (int i = 0; i < path.size(); i++) {
            final int index = i;
            KeyFrame kf = new KeyFrame(Duration.millis(index * delayMs), event -> {
                String vertexName = g.getVertexName(path.get(index));
                Vertex<String> v = panel.getModel().vertices().stream()
                        .filter(x -> x.element().equals(vertexName))
                        .findFirst().orElse(null);

                if (v != null) {
                    panel.getStylableVertex(v).setStyleInline("-fx-fill: violet; -fx-stroke: black; -fx-stroke-width: 1;");
                }

                if (index < path.size() - 1) {
                    String from = g.getVertexName(path.get(index));
                    String to = g.getVertexName(path.get(index + 1));

                    Edge<String, String> e = panel.getModel().edges().stream()
                            .filter(edge -> {
                                String v1 = edge.vertices()[0].element();
                                String v2 = edge.vertices()[1].element();

                                // Si le graphe est orienté : on respecte la direction
                                if (g.isOriented()) {
                                    return v1.equals(from) && v2.equals(to);
                                }

                                // Si le graphe est non orienté : on autorise les deux sens
                                return (v1.equals(from) && v2.equals(to)) || (v1.equals(to) && v2.equals(from));
                            })
                            .findFirst()
                            .orElse(null);


                    if (e != null) {
                        panel.getStylableEdge(e).setStyleInline(
                                "-fx-stroke: red; -fx-stroke-width: 2.0; -fx-fill: transparent; -fx-stroke-line-cap: butt;"
                        );
                    }
                }
                panel.update();
            });
            timeline.getKeyFrames().add(kf);
        }
        timeline.play();
    }

    /** Interface fonctionnelle interne pour factoriser l’exécution des algorithmes. */
    private interface AlgorithmResult {
        String getResult();
    }
}
