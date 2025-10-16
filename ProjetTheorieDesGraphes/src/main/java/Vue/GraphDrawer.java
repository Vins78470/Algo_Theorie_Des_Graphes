package Vue;

import Modele.Graphe;
import Modele.NodeState;
import Modele.Step;
import Modele.StepManager;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.ArrayList;
import java.util.List;

public class GraphDrawer {

    private final Graphe g;
    private final Graph gsGraph;

    private final List<Step> keptSteps = new ArrayList<>();

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

    public void drawGraph(Canvas canvas) {
        if (canvas == null) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        clearCanvas(gc, canvas);
        double[][] pos = computeNodePositions(canvas.getWidth(), canvas.getHeight());
        drawEdges(gc, g, pos, null);
        drawNodes(gc, g, pos, null);
    }

    public void drawStep(Canvas canvas, Step step) {
        if (canvas == null || step == null) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        clearCanvas(gc, canvas);

        double[][] pos = computeNodePositions(canvas.getWidth(), canvas.getHeight());

        if (step.getState() == NodeState.NODE_KEPT) keptSteps.add(step);

        // Redessiner tous les KEPT précédents
        for (Step s : keptSteps) drawEdges(gc, g, pos, s);

        // Dessiner le step courant
        drawEdges(gc, g, pos, step);
        drawNodes(gc, g, pos, step);
    }

    public void drawStepManagerSequentially(Canvas canvas, StepManager stepManager, int delayMs) {
        if (canvas == null || stepManager == null) return;
        new Thread(() -> {
            for (Step step : stepManager.getSteps()) {
                javafx.application.Platform.runLater(() -> drawStep(canvas, step));
                try { Thread.sleep(delayMs); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); break; }
            }
        }).start();
    }

    private void clearCanvas(GraphicsContext gc, Canvas canvas) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private double[][] computeNodePositions(double width, double height) {
        int n = g.getMatrix().length;
        double[][] pos = new double[n][2];
        double centerX = width / 2, centerY = height / 2;
        double radius = Math.min(width, height) / 2.3;

        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n;
            pos[i][0] = centerX + radius * Math.cos(angle);
            pos[i][1] = centerY + radius * Math.sin(angle);

            Node node = gsGraph.getNode(String.valueOf(i));
            if (node != null) {
                node.setAttribute("x", pos[i][0]);
                node.setAttribute("y", pos[i][1]);
            }
        }
        return pos;
    }

    private void drawEdges(GraphicsContext gc, Graphe g, double[][] pos, Step step) {
        int[][] mat = g.getMatrix();
        int n = mat.length;
        boolean oriented = g.isOriented();
        gc.setLineWidth(2);
        int edgeCounter = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!oriented && j <= i) continue;
                if (mat[i][j] == 0) continue;

                double x1 = pos[i][0], y1 = pos[i][1];
                double x2 = pos[j][0], y2 = pos[j][1];
                double dx = x2 - x1, dy = y2 - y1;
                double length = Math.sqrt(dx*dx + dy*dy);
                double offset = 20 * ((edgeCounter % 2 == 0) ? 1 : -1);
                double cx = (x1+x2)/2 - dy/length*offset;
                double cy = (y1+y2)/2 + dx/length*offset;

                Color color = Color.GRAY;

                if (step != null && step.getFrom() != null && step.getTo() != null) {
                    String fromName = step.getFrom().getName();
                    String toName = step.getTo().getName();

                    String nameI = g.getVertexName(i);
                    String nameJ = g.getVertexName(j);

                    if ((nameI.equals(fromName) && nameJ.equals(toName)) ||
                            (!oriented && nameI.equals(toName) && nameJ.equals(fromName))) {
                        color = step.getState().getColor();
                    }
                }

                gc.setStroke(color);
                gc.beginPath();
                gc.moveTo(x1, y1);
                gc.quadraticCurveTo(cx, cy, x2, y2);
                gc.stroke();

                double midX = (x1+x2)/2 + (cx-(x1+x2)/2)/2;
                double midY = (y1+y2)/2 + (cy-(y1+y2)/2)/2;
                drawEdgeWeight(gc, midX, midY, mat[i][j], edgeCounter);

                if (oriented) drawArrowOnCurve(gc, x1, y1, cx, cy, x2, y2);

                edgeCounter++;
            }
        }
    }

    private void drawNodes(GraphicsContext gc, Graphe g, double[][] pos, Step step) {
        int n = g.getMatrix().length;
        gc.setFont(Font.font(20));
        double nodeRadius = 10;

        for (int i = 0; i < n; i++) {
            double x = pos[i][0], y = pos[i][1];
            NodeState state = NodeState.NODE_KEPT;

            if (step != null && step.getTo() != null &&
                    g.getVertexName(i).equals(step.getTo().getName())) {
                state = step.getState();
            }

            gc.setFill(state.getColor());
            gc.fillOval(x-nodeRadius, y-nodeRadius, nodeRadius*2, nodeRadius*2);

            gc.setStroke(Color.DARKBLUE);
            gc.strokeOval(x-nodeRadius, y-nodeRadius, nodeRadius*2, nodeRadius*2);

            gc.setFill(Color.BLACK);
            gc.fillText(g.getVertexName(i), x-nodeRadius-5, y-nodeRadius-10);
        }
    }

    private void drawEdgeWeight(GraphicsContext gc, double x, double y, int weight, int edgeIndex) {
        gc.setFill(Color.DARKBLUE);
        gc.setFont(Font.font(16));
        double offsetX = 5 + (edgeIndex % 2) * 3;
        double offsetY = -5 - (edgeIndex % 2) * 3;
        gc.fillText(String.valueOf(weight), x + offsetX, y + offsetY);
    }

    private void drawArrowOnCurve(GraphicsContext gc, double x1, double y1, double cx, double cy, double x2, double y2) {
        double nodeRadius = 10, arrowLength = 20;
        double totalLength = Math.hypot(x2-x1, y2-y1);
        double tOffset = Math.max(0, Math.min(1, (totalLength - nodeRadius - 5)/totalLength));
        double t = tOffset;
        double xt = Math.pow(1-t,2)*x1 + 2*(1-t)*t*cx + t*t*x2;
        double yt = Math.pow(1-t,2)*y1 + 2*(1-t)*t*cy + t*t*y2;
        double dx = 2*(1-t)*(cx-x1) + 2*t*(x2-cx);
        double dy = 2*(1-t)*(cy-y1) + 2*t*(y2-cy);
        double angle = Math.atan2(dy, dx);

        double xArrow1 = xt - arrowLength*Math.cos(angle-Math.PI/6);
        double yArrow1 = yt - arrowLength*Math.sin(angle-Math.PI/6);
        double xArrow2 = xt - arrowLength*Math.cos(angle+Math.PI/6);
        double yArrow2 = yt - arrowLength*Math.sin(angle+Math.PI/6);

        gc.setStroke(Color.DARKRED);
        gc.setLineWidth(2.5);
        gc.strokeLine(xt, yt, xArrow1, yArrow1);
        gc.strokeLine(xt, yt, xArrow2, yArrow2);
    }
}
