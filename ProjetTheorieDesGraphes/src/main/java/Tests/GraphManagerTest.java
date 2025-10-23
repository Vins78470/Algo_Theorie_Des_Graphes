package Tests;

import Modele.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Classe de tests unitaires pour la classe {@link GraphManager}.
 *
 * Ces tests valident le bon fonctionnement des algorithmes de graphes :
 * - Parcours (BFS, DFS)
 * - Plus courts chemins (Dijkstra)
 * - Arbres couvrants minimum (Kruskal, Prim)
 *
 * Tous les tests s’appuient sur le graphe par défaut
 */
public class GraphManagerTest {

    /**
     * Vérifie la construction du graphe par défaut (10 sommets + arêtes connues).
     */
    @Test
    public void testInitDefaultGraph() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        assertNotNull(g);
        assertEquals(10, g.getNbSommets());

        assertEquals(50, g.getPoids(0, 1));  // Paris → Caen
        assertEquals(40, g.getPoids(8, 9));  // Lyon → Grenoble
    }

    /**
     * Vérifie le fonctionnement du BFS sur le graphe par défaut.
     */
    @Test
    public void testRunBFSReal() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        BFS bfs = new BFS();
        String[] res = GraphManager.runBFS(bfs, g, 0);

        String combined = (res[0] == null ? "" : res[0]) + "\n" + (res[1] == null ? "" : res[1]);
        String ordreLine = extractOrderLine(combined, "Ordre de visite :");
        assertNotNull("Impossible de trouver la ligne 'Ordre de visite' dans la sortie BFS", ordreLine);

        String actualOrder = ordreLine.replaceFirst("(?i)Ordre de visite\\s*:\\s*", "").trim();
        String expectedOrder = "Paris → Caen → Dijon → Lille → Nantes → Rennes → Bordeaux → Lyon → Nancy → Grenoble";
        assertEquals("Ordre BFS pondéré attendu", expectedOrder, actualOrder);
    }

    /**
     * Fonction utilitaire pour extraire une ligne spécifique d’un texte.
     */
    private String extractOrderLine(String text, String motif) {
        if (text == null) return null;
        String[] lines = text.split("\\r?\\n");
        for (String line : lines) {
            if (line != null && line.contains(motif)) return line.trim();
        }
        return null;
    }

    /**
     * Vérifie le fonctionnement du DFS sur le graphe par défaut.
     */
    @Test
    public void testRunDFSReal() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        DFS dfs = new DFS();
        String[] res = GraphManager.runDFS(dfs, g, 0);

        String combined = (res[0] == null ? "" : res[0]) + "\n" + (res[1] == null ? "" : res[1]);
        String ordreLine = extractOrderLine(combined, "Ordre de visite :");
        assertNotNull("Impossible de trouver la ligne 'Ordre de visite' dans la sortie DFS", ordreLine);

        String actualOrder = ordreLine.replaceFirst("(?i)Ordre de visite\\s*:\\s*", "").trim();
        String expectedOrder = "Paris → Caen → Lille → Nancy → Dijon → Lyon → Grenoble → Bordeaux → Nantes → Rennes";
        assertEquals("Ordre DFS pondéré attendu", expectedOrder, actualOrder);
    }

    /** Vérifie Dijkstra sur le trajet Paris → Bordeaux. */
    @Test
    public void testRunDijkstraParisBordeaux() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        Dijkstra dij = new Dijkstra();
        String[] res = GraphManager.runDijkstra(dij, g, 0, 4);
        String combined = (res[0] == null ? "" : res[0]) + "\n" + (res[1] == null ? "" : res[1]);

        assertTrue(combined.contains("Distance minimale : 150"));
        assertTrue(combined.contains("Paris → Bordeaux"));
    }

    /** Vérifie Dijkstra sur le trajet Paris → Lyon. */
    @Test
    public void testRunDijkstraParisLyon() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        Dijkstra dij = new Dijkstra();
        String[] res = GraphManager.runDijkstra(dij, g, 0, 8);
        String combined = (res[0] == null ? "" : res[0]) + "\n" + (res[1] == null ? "" : res[1]);

        assertTrue(combined.contains("Distance minimale : 130"));
        assertTrue(combined.contains("Paris → Dijon → Lyon"));
    }

    /** Vérifie Dijkstra sur le trajet Rennes → Bordeaux. */
    @Test
    public void testRunDijkstraRennesBordeaux() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        Dijkstra dij = new Dijkstra();
        String[] res = GraphManager.runDijkstra(dij, g, 2, 4);
        String combined = (res[0] == null ? "" : res[0]) + "\n" + (res[1] == null ? "" : res[1]);

        assertTrue(combined.contains("Distance minimale : 130"));
        assertTrue(combined.contains("Rennes → Bordeaux"));
    }

    /** Vérifie Dijkstra sur le trajet Rennes → Lyon. */
    @Test
    public void testRunDijkstraRennesLyon() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        Dijkstra dij = new Dijkstra();
        String[] res = GraphManager.runDijkstra(dij, g, 2, 8);
        String combined = (res[0] == null ? "" : res[0]) + "\n" + (res[1] == null ? "" : res[1]);

        assertTrue(combined.contains("Distance minimale : 230"));
        assertTrue(combined.contains("Rennes → Bordeaux → Lyon"));
    }

    /** Vérifie Dijkstra sur le trajet Rennes → Grenoble. */
    @Test
    public void testRunDijkstraRennesGrenoble() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        Dijkstra dij = new Dijkstra();
        String[] res = GraphManager.runDijkstra(dij, g, 2, 9);
        String combined = res[0] + "\n" + res[1];

        assertTrue(combined.contains("Distance minimale : 245"));
        assertTrue(combined.contains("Rennes → Paris → Dijon → Grenoble"));
    }

    /** Vérifie le coût total de l’arbre couvrant obtenu par Kruskal. */
    @Test
    public void testRunKruskal() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        Kruskal k = new Kruskal();
        String[] res = GraphManager.runKruskal(k, g);

        assertTrue(res[1].contains("Coût total de l'arbre couvrant = 570"));
    }

    /** Vérifie Prim depuis Rennes. */
    @Test
    public void testPrimFromRennes() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        String result = Prim.getResult(g, 2);
        assertTrue(result.contains("Coût total = 570"));
        assertTrue(result.contains("départ : Rennes"));
    }

    /** Vérifie Prim depuis Paris. */
    @Test
    public void testPrimFromParis() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        String result = Prim.getResult(g, 0);
        assertTrue(result.contains("Coût total = 570"));
        assertTrue(result.contains("départ : Paris"));
    }

    /** Vérifie Prim depuis Lille. */
    @Test
    public void testPrimFromLille() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        String result = Prim.getResult(g, 5);
        assertTrue(result.contains("Coût total = 570"));
        assertTrue(result.contains("départ : Lille"));
    }

    /** Vérifie Prim depuis Grenoble. */
    @Test
    public void testPrimFromGrenoble() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        String result = Prim.getResult(g, 9);
        assertTrue(result.contains("Coût total = 570"));
        assertTrue(result.contains("départ : Grenoble"));
    }

    /** Vérifie que Prim retourne 9 arêtes et le coût total attendu. */
    @Test
    public void testPrimMSTEdges() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        String result = Prim.getResult(g, 0);

        int edgeCount = result.split("—").length - 1;
        assertEquals("Nombre d'arêtes dans le MST", 9, edgeCount);
        assertTrue(result.contains("Coût total = 570"));
    }

    /** Vérifie que Prim retourne le même coût total quel que soit le point de départ. */
    @Test
    public void testPrimAllStartingPoints() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        for (int start = 0; start < 10; start++) {
            String result = Prim.getResult(g, start);
            assertTrue("Coût total MST depuis sommet " + start, result.contains("Coût total = 570"));
        }
    }
}
