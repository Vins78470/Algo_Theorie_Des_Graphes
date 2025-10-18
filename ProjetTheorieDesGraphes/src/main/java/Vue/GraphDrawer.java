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

    // =========================
    // Dessiner le graphe complet
    // =========================
    public void drawGraph(Canvas canvas) {
        if (canvas == null) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        clearCanvas(gc, canvas);
        double[][] pos = computeNodePositions(canvas.getWidth(), canvas.getHeight());
        drawEdges(gc, g, pos, null);
        drawNodes(gc, g, pos, null);
    }




    // =========================
    // Dessiner le chemin final avec animation
    // =========================
    public void drawFinalPath(Canvas canvas, Graphe graphe, List<Integer> finalPath) {
        if (canvas == null || graphe == null || finalPath == null || finalPath.size() < 2) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        double[][] pos = computeNodePositions(canvas.getWidth(), canvas.getHeight());
        int[][] mat = graphe.getMatrix();
        boolean oriented = graphe.isOriented();

        new Thread(() -> {
            for (int i = 0; i < finalPath.size() - 1; i++) {
                int from = finalPath.get(i);
                int to = finalPath.get(i + 1);

                // Pour les graphes non orientés, normaliser l'ordre
                int normalFrom = from;
                int normalTo = to;
                if (!oriented && from > to) {
                    normalFrom = to;
                    normalTo = from;
                }

                int edgeCounter = findEdgeCounter(normalFrom, normalTo, mat, oriented);
                double[] curve = computeEdgeCurve(normalFrom, normalTo, pos, edgeCounter);

                javafx.application.Platform.runLater(() -> {
                    // Arête en rouge
                    gc.setStroke(Color.RED);
                    gc.setLineWidth(3);
                    gc.beginPath();

                    // Si l'ordre est inversé, inverser le tracé
                    if (!oriented && from > to) {
                        gc.moveTo(curve[4], curve[5]);
                        gc.quadraticCurveTo(curve[2], curve[3], curve[0], curve[1]);
                    } else {
                        gc.moveTo(curve[0], curve[1]);
                        gc.quadraticCurveTo(curve[2], curve[3], curve[4], curve[5]);
                    }
                    gc.stroke();

                    // Nœud d'arrivée en violet (toujours 'to')
                    double finalX = (!oriented && from > to) ? curve[0] : curve[4];
                    double finalY = (!oriented && from > to) ? curve[1] : curve[5];
                    drawNodeAt(gc, graphe, to, finalX, finalY, Color.VIOLET);
                });

                try { Thread.sleep(1000); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); break; }
            }
        }).start();
    }

    // =========================
    // Utils générales
    // =========================
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

                // Dessiner l'arête avec drawEdge
                drawEdge(gc, g, pos, i, j, edgeCounter, step, oriented, null);

                edgeCounter++;
            }
        }
    }

    // =========================
    // Dessiner toutes les arêtes
    // =========================
    private double[] drawEdge(GraphicsContext gc, Graphe g, double[][] pos,
                              int fromIndex, int toIndex, int edgeCounter,
                              Step step, boolean oriented, Color edgeColor) {
        // Calcul de la courbe
        double[] curve = computeEdgeCurve(fromIndex, toIndex, pos, edgeCounter);

        // Couleur : soit fournie, soit issue du Step
        Color color = (edgeColor != null) ? edgeColor : Color.GRAY;

        if (step != null && step.getFrom() != null && step.getTo() != null && edgeColor == null) {
            String fromName = step.getFrom().getName();
            String toName = step.getTo().getName();
            String nameI = g.getVertexName(fromIndex);
            String nameJ = g.getVertexName(toIndex);

            if ((nameI.equals(fromName) && nameJ.equals(toName)) ||
                    (!oriented && nameI.equals(toName) && nameJ.equals(fromName))) {
                color = step.getState().getColor();
            }
        }

        // Dessin de la ligne
        gc.setStroke(color);
        gc.beginPath();
        gc.moveTo(curve[0], curve[1]);
        gc.quadraticCurveTo(curve[2], curve[3], curve[4], curve[5]);
        gc.stroke();

        // Dessin du poids
        double midX = (curve[0] + curve[4]) / 2 + (curve[2] - (curve[0] + curve[4]) / 2) / 2;
        double midY = (curve[1] + curve[5]) / 2 + (curve[3] - (curve[1] + curve[5]) / 2) / 2;
        drawEdgeWeight(gc, midX, midY, g.getMatrix()[fromIndex][toIndex], edgeCounter);

        // Flèche si orienté
        if (oriented) drawArrowOnCurve(gc, curve[0], curve[1], curve[2], curve[3], curve[4], curve[5]);

        return curve;
    }


    // =========================
    // Dessiner tous les nœuds
    // =========================
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

            drawNodeAt(gc, g, i, x, y, state.getColor());
        }
    }

    private void drawNodeAt(GraphicsContext gc, Graphe g, int index, double x, double y, Color fill) {
        double nodeRadius = 10;
        gc.setFill(fill);
        gc.fillOval(x-nodeRadius, y-nodeRadius, nodeRadius*2, nodeRadius*2);
        gc.setStroke(Color.DARKBLUE);
        gc.strokeOval(x-nodeRadius, y-nodeRadius, nodeRadius*2, nodeRadius*2);
        gc.setFill(Color.BLACK);
        gc.fillText(g.getVertexName(index), x-nodeRadius-5, y-nodeRadius-10);
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

    // =========================
    // Factorisation du calcul d'une arête (pour offset/curve)
    // =========================
    private double[] computeEdgeCurve(int fromIndex, int toIndex, double[][] pos, int edgeCounter) {
        double x1 = pos[fromIndex][0], y1 = pos[fromIndex][1];
        double x2 = pos[toIndex][0], y2 = pos[toIndex][1];

        double dx = x2 - x1;
        double dy = y2 - y1;
        double length = Math.sqrt(dx * dx + dy * dy);

        double offset = 20 * ((edgeCounter % 2 == 0) ? 1 : -1); // Décalage pour éviter chevauchement
        double cx = (x1 + x2) / 2 - dy / length * offset;
        double cy = (y1 + y2) / 2 + dx / length * offset;

        return new double[]{x1, y1, cx, cy, x2, y2};
    }


    private int findEdgeCounter(int from, int to, int[][] mat, boolean oriented) {
        int n = mat.length;
        int edgeCounter = 0;
        outer:
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (!oriented && y <= x) continue;
                if (mat[x][y] != 0) {
                    if ((x == from && y == to) || (!oriented && x == to && y == from)) break outer;
                    edgeCounter++;
                }
            }
        }
        return edgeCounter;
    }
}
