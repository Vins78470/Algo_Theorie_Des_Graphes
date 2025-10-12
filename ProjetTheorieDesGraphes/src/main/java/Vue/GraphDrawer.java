package Vue;

import Modele.Graphe;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.Node;

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
                if (mat[i][j] != 0) { // <- maintenant on prend tous les poids non nuls
                    gsGraph.addEdge(i + "-" + j, String.valueOf(i), String.valueOf(j));
                }
            }
        }
    }

    public void drawGraph(Canvas canvas) {
        if (canvas == null) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        clearCanvas(gc, canvas);
        double[][] positions = computeNodePositionsSafe(canvas.getWidth(), canvas.getHeight());

        drawEdges(gc, g, positions);
        drawNodes(gc, g, positions);
    }

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
        int[][] mat = g.getMatrix();
        int n = mat.length;

        gc.setLineWidth(2);
        int edgeCounter = 0;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int weight = mat[i][j];
                if (weight != 0) { // <- tous les poids non nuls
                    double x1 = pos[i][0];
                    double y1 = pos[i][1];
                    double x2 = pos[j][0];
                    double y2 = pos[j][1];

                    // Offset pour courbes
                    double dx = x2 - x1;
                    double dy = y2 - y1;
                    double length = Math.sqrt(dx*dx + dy*dy);
                    double offset = 20 * ((edgeCounter % 2 == 0) ? 1 : -1);

                    double controlX = (x1 + x2)/2 - dy/length * offset;
                    double controlY = (y1 + y2)/2 + dx/length * offset;

                    // couleur selon poids
                    gc.setStroke(Color.GRAY);

                    gc.beginPath();
                    gc.moveTo(x1, y1);
                    gc.quadraticCurveTo(controlX, controlY, x2, y2);
                    gc.stroke();

                    // Poids de l'arête
                    double midX = (x1 + x2)/2 + (controlX - (x1 + x2)/2)/2;
                    double midY = (y1 + y2)/2 + (controlY - (y1 + y2)/2)/2;
                    drawEdgeWeight(gc, midX, midY, weight, edgeCounter);

                    edgeCounter++;
                }
            }
        }
    }

    private void drawEdgeWeight(GraphicsContext gc, double x, double y, int weight, int edgeIndex) {
        gc.setFill(Color.DARKBLUE);
        gc.setFont(Font.font(20));
        double offset = 5 + (edgeIndex % 2) * 3;
        gc.fillText(String.valueOf(weight), x + offset, y - offset);
    }

    private void drawNodes(GraphicsContext gc, Graphe g, double[][] pos) {
        int n = g.getMatrix().length;
        gc.setFont(Font.font(20));
        gc.setLineWidth(2);
        double nodeRadius = 10;

        for (int i = 0; i < n; i++) {
            double x = pos[i][0];
            double y = pos[i][1];

            gc.setFill(Color.LIGHTBLUE);
            gc.fillOval(x - nodeRadius, y - nodeRadius, nodeRadius * 2, nodeRadius * 2);
            gc.setStroke(Color.DARKBLUE);
            gc.strokeOval(x - nodeRadius, y - nodeRadius, nodeRadius * 2, nodeRadius * 2);

            gc.setFill(Color.BLACK);
            gc.fillText(g.getVertexName(i), x - nodeRadius - 5, y - nodeRadius - 10);
        }
    }
}
