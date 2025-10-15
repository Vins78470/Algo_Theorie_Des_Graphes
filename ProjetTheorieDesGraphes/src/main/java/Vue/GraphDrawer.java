package Vue;

import Modele.Graphe;
import Modele.Step;
import Modele.StepManager;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.List;
import java.util.Map;

public class GraphDrawer {

    private final Graph gsGraph;
    private final Graphe g;

    public GraphDrawer(Graphe g) {
        this.g = g;
        this.gsGraph = new SingleGraph("layout");

        int n = g.getMatrix().length;
        for (int i = 0; i < n; i++) gsGraph.addNode(String.valueOf(i));

        int[][] mat = g.getMatrix();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (mat[i][j] != 0) {
                    gsGraph.addEdge(i + "-" + j, String.valueOf(i), String.valueOf(j));
                }
            }
        }
    }

    /** Dessine le graphe complet sans états particuliers */
    public void drawGraph(Canvas canvas) {
        if (canvas == null) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        clearCanvas(gc, canvas);
        double[][] positions = computeNodePositionsSafe(canvas.getWidth(), canvas.getHeight());

        drawEdges(gc, g, positions);
        drawNodes(gc, g, positions);
    }

    /** Dessine un step isolé */
    public void drawStep(Canvas canvas, Step step) {
        if (canvas == null || step == null) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        clearCanvas(gc, canvas);
        double[][] positions = computeNodePositionsSafe(canvas.getWidth(), canvas.getHeight());

        drawEdges(gc, g, positions, step.edgeStates);
        drawNodes(gc, g, positions, step.nodeStates);
    }

    /** Dessine tous les steps d’un StepManager avec animation */
    public void drawStepManagerSequentially(Canvas canvas, StepManager stepManager, int delayMs) {
        if (canvas == null || stepManager == null) return;

        new Thread(() -> {
            List<Step> steps = stepManager.getSteps();
            for (Step step : steps) {
                javafx.application.Platform.runLater(() -> drawStep(canvas, step));
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

    // --- Méthodes privées utilitaires ---

    private void clearCanvas(GraphicsContext gc, Canvas canvas) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private double[][] computeNodePositionsSafe(double width, double height) {
        int n = g.getMatrix().length;
        double[][] pos = new double[n][2];

        double centerX = width / 2;
        double centerY = height / 2;
        double radius = Math.min(width, height) / 2.3;

        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);

            pos[i][0] = x;
            pos[i][1] = y;

            Node node = gsGraph.getNode(String.valueOf(i));
            if (node != null) {
                node.setAttribute("x", x);
                node.setAttribute("y", y);
            }
        }
        return pos;
    }

    private void drawEdges(GraphicsContext gc, Graphe g, double[][] pos) {
        drawEdges(gc, g, pos, null);
    }

    private void drawEdges(GraphicsContext gc, Graphe g, double[][] pos, Map<String, Color> edgeStates) {
        int[][] mat = g.getMatrix();
        int n = mat.length;
        gc.setLineWidth(2);
        int edgeCounter = 0;

        boolean oriented = g.isOriented(); // Vérifie si le graphe est orienté

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!oriented && j <= i) continue; // éviter les doublons pour non-orienté

                int weight = mat[i][j];
                if (weight != 0) {
                    double x1 = pos[i][0];
                    double y1 = pos[i][1];
                    double x2 = pos[j][0];
                    double y2 = pos[j][1];

                    double dx = x2 - x1;
                    double dy = y2 - y1;
                    double length = Math.sqrt(dx * dx + dy * dy);

                    double offset = 20 * ((edgeCounter % 2 == 0) ? 1 : -1);
                    double controlX = (x1 + x2) / 2 - dy / length * offset;
                    double controlY = (y1 + y2) / 2 + dx / length * offset;

                    Color color = (edgeStates != null && edgeStates.containsKey(i + "-" + j)) ?
                            edgeStates.get(i + "-" + j) : Color.GRAY;
                    gc.setStroke(color);

                    // Dessin de la courbe
                    gc.beginPath();
                    gc.moveTo(x1, y1);
                    gc.quadraticCurveTo(controlX, controlY, x2, y2);
                    gc.stroke();

                    // Dessiner le poids
                    double midX = (x1 + x2) / 2 + (controlX - (x1 + x2) / 2) / 2;
                    double midY = (y1 + y2) / 2 + (controlY - (y1 + y2) / 2) / 2;
                    drawEdgeWeight(gc, midX, midY, weight, edgeCounter);

                    // Flèche si orienté
                    if (oriented) {
                        drawArrowOnCurve(gc, x1, y1, controlX, controlY, x2, y2);
                    }

                    edgeCounter++;
                }
            }
        }
    }

    private void drawEdgeWeight(GraphicsContext gc, double x, double y, int weight, int edgeIndex) {
        gc.setFill(Color.DARKBLUE);          // couleur du texte
        gc.setFont(Font.font(16));           // taille du texte
        double offsetX = 5 + (edgeIndex % 2) * 3; // léger décalage pour les arêtes multiples
        double offsetY = -5 - (edgeIndex % 2) * 3;
        gc.fillText(String.valueOf(weight), x + offsetX, y + offsetY);
    }


    private void drawArrowOnCurve(GraphicsContext gc, double x1, double y1, double cx, double cy, double x2, double y2) {
        double nodeRadius = 10; // rayon du cercle du nœud
        double arrowLength = 20;
        double arrowWidth = 12;

        // longueur totale de la ligne droite entre le départ et l'arrivée
        double totalLength = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));

        // proportion pour reculer un peu plus que juste le rayon
        double margin = nodeRadius + 5; // 5 pixels supplémentaires
        double tOffset = (totalLength - margin) / totalLength;
        if (tOffset > 1) tOffset = 1;
        if (tOffset < 0) tOffset = 0;

        double t = tOffset;
        // point sur la quadratique
        double xt = Math.pow(1 - t, 2) * x1 + 2 * (1 - t) * t * cx + t * t * x2;
        double yt = Math.pow(1 - t, 2) * y1 + 2 * (1 - t) * t * cy + t * t * y2;

        // tangente pour orientation de la flèche
        double dx = 2 * (1 - t) * (cx - x1) + 2 * t * (x2 - cx);
        double dy = 2 * (1 - t) * (cy - y1) + 2 * t * (y2 - cy);
        double angle = Math.atan2(dy, dx);

        // dessiner la flèche
        double xArrow1 = xt - arrowLength * Math.cos(angle - Math.PI / 6);
        double yArrow1 = yt - arrowLength * Math.sin(angle - Math.PI / 6);
        double xArrow2 = xt - arrowLength * Math.cos(angle + Math.PI / 6);
        double yArrow2 = yt - arrowLength * Math.sin(angle + Math.PI / 6);

        gc.setStroke(Color.DARKRED);
        gc.setLineWidth(2.5);
        gc.strokeLine(xt, yt, xArrow1, yArrow1);
        gc.strokeLine(xt, yt, xArrow2, yArrow2);
    }



    private void drawNodes(GraphicsContext gc, Graphe g, double[][] pos) {
        drawNodes(gc, g, pos, null);
    }

    private void drawNodes(GraphicsContext gc, Graphe g, double[][] pos, Map<Integer, Color> nodeStates) {
        int n = g.getMatrix().length;
        gc.setFont(Font.font(20));
        gc.setLineWidth(2);
        double nodeRadius = 10;

        for (int i = 0; i < n; i++) {
            double x = pos[i][0];
            double y = pos[i][1];

            Color fill = (nodeStates != null && nodeStates.containsKey(i)) ?
                    nodeStates.get(i) : Color.LIGHTBLUE;

            gc.setFill(fill);
            gc.fillOval(x - nodeRadius, y - nodeRadius, nodeRadius * 2, nodeRadius * 2);

            gc.setStroke(Color.DARKBLUE);
            gc.strokeOval(x - nodeRadius, y - nodeRadius, nodeRadius * 2, nodeRadius * 2);

            gc.setFill(Color.BLACK);
            gc.fillText(g.getVertexName(i), x - nodeRadius - 5, y - nodeRadius - 10);
        }
    }
}
