package Tests;

import Modele.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class GraphManagerTest {

    @Test
    public void testInitDefaultGraph() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        assertNotNull(g);
        assertEquals(10, g.getNbSommets());

        assertEquals(50, g.getPoids(0, 1));  // Paris -> Caen
        assertEquals(40, g.getPoids(8, 9));  // Lyon -> Grenoble
    }



    @Test
    public void testRunBFSReal() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        BFS bfs = new BFS();

        String[] res = GraphManager.runBFS(bfs, g, 0);

        // Concatène les deux parties retournées par GraphManager (sécurité si res[1] est vide)
        String combined = (res[0] == null ? "" : res[0]) + "\n" + (res[1] == null ? "" : res[1]);

        String ordreLine = extractOrderLine(combined, "Ordre de visite :");
        assertNotNull("Impossible de trouver la ligne 'Ordre de visite' dans la sortie BFS", ordreLine);

        // extrait uniquement la partie après "Ordre de visite : "
        String actualOrder = ordreLine.replaceFirst("(?i)Ordre de visite\\s*:\\s*", "").trim();

        String expectedOrder = "Paris → Caen → Dijon → Lille → Nantes → Rennes → Bordeaux → Lyon → Nancy → Grenoble";
        assertEquals("Ordre BFS pondéré attendu", expectedOrder, actualOrder);
    }

    /** Utilitaire : retourne la première ligne contenant le motif, ou null si introuvable */
    private String extractOrderLine(String text, String motif) {
        if (text == null) return null;
        String[] lines = text.split("\\r?\\n");
        for (String line : lines) {
            if (line != null && line.contains(motif)) return line.trim();
        }
        return null;
    }




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



    @Test
    public void testRunDijkstraParisBordeaux() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        Dijkstra dij = new Dijkstra();

        String[] res = GraphManager.runDijkstra(dij, g, 0, 4); // Paris → Bordeaux
        String combined = (res[0] == null ? "" : res[0]) + "\n" + (res[1] == null ? "" : res[1]);

        // Vérifie distance
        assertTrue("Distance Dijkstra Paris→Bordeaux", combined.contains("Distance minimale : 150"));
        // Vérifie chemin exact
        assertTrue("Chemin Dijkstra Paris→Bordeaux", combined.contains("Paris → Bordeaux"));
    }

    @Test
    public void testRunDijkstraParisLyon() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        Dijkstra dij = new Dijkstra();

        String[] res = GraphManager.runDijkstra(dij, g, 0, 8); // Paris → Lyon
        String combined = (res[0] == null ? "" : res[0]) + "\n" + (res[1] == null ? "" : res[1]);

        // Vérifie distance
        assertTrue("Distance Dijkstra Paris→Lyon", combined.contains("Distance minimale : 130"));
        // Vérifie chemin exact
        assertTrue("Chemin Dijkstra Paris→Lyon", combined.contains("Paris → Dijon → Lyon"));
    }

    @Test
    public void testRunDijkstraRennesBordeaux() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        Dijkstra dij = new Dijkstra();

        String[] res = GraphManager.runDijkstra(dij, g, 2, 4); // Rennes → Bordeaux
        String combined = (res[0] == null ? "" : res[0]) + "\n" + (res[1] == null ? "" : res[1]);

        assertTrue("Distance Dijkstra Rennes→Bordeaux", combined.contains("Distance minimale : 130"));
        assertTrue("Chemin Dijkstra Rennes→Bordeaux", combined.contains("Rennes → Bordeaux"));
    }

    @Test
    public void testRunDijkstraRennesLyon() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        Dijkstra dij = new Dijkstra();

        String[] res = GraphManager.runDijkstra(dij, g, 2, 8); // Rennes → Lyon
        String combined = (res[0] == null ? "" : res[0]) + "\n" + (res[1] == null ? "" : res[1]);

        assertTrue("Distance Dijkstra Rennes→Lyon", combined.contains("Distance minimale : 230"));
        assertTrue("Chemin Dijkstra Rennes→Lyon", combined.contains("Rennes → Bordeaux → Lyon"));
    }



    @Test
    public void testRunDijkstraRennesGrenoble() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        Dijkstra dij = new Dijkstra();

        String[] res = GraphManager.runDijkstra(dij, g, 2, 9); // Rennes → Grenoble

        // Combine steps + résultat final
        String combined = res[0] + "\n" + res[1];

        // Vérifie que la distance minimale est bien 245
        assertTrue("Distance Dijkstra Rennes→Grenoble", combined.contains("Distance minimale : 245"));

        // Vérifie le chemin exact
        assertTrue("Chemin Dijkstra Rennes→Grenoble", combined.contains("Rennes → Paris → Dijon → Grenoble"));
    }




    @Test
    public void testRunKruskal() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        Kruskal k = new Kruskal();

        String[] res = GraphManager.runKruskal(k, g);

        // res[1] contient le résultat final (MST et coût total)
        // Pour ton graphe par défaut, le coût minimal total = 570
        assertTrue("Coût total MST Kruskal", res[1].contains("Coût total de l'arbre couvrant = 570"));


    }

    @Test
    public void testPrimFromRennes() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        int rennesIndex = 2; // Rennes

        String result = Prim.getResult(g, rennesIndex);

        // Le coût total doit être 570 (même que Kruskal, c'est le MST)
        assertTrue("Coût total MST Prim depuis Rennes", result.contains("Coût total = 570"));

        // Vérifier le point de départ
        assertTrue(result.contains("départ : Rennes"));
    }

    @Test
    public void testPrimFromParis() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        int parisIndex = 0; // Paris

        String result = Prim.getResult(g, parisIndex);

        // Le coût total doit toujours être 570 (MST unique)
        assertTrue("Coût total MST Prim depuis Paris", result.contains("Coût total = 570"));

        // Vérifier le point de départ
        assertTrue(result.contains("départ : Paris"));
    }

    @Test
    public void testPrimFromLille() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        int lilleIndex = 5; // Lille

        String result = Prim.getResult(g, lilleIndex);

        // Le coût total doit être 570
        assertTrue("Coût total MST Prim depuis Lille", result.contains("Coût total = 570"));

        // Vérifier le point de départ
        assertTrue(result.contains("départ : Lille"));
    }

    @Test
    public void testPrimFromGrenoble() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        int grenobleIndex = 9; // Grenoble

        String result = Prim.getResult(g, grenobleIndex);

        // Le coût total doit être 570
        assertTrue("Coût total MST Prim depuis Grenoble", result.contains("Coût total = 570"));

        // Vérifier le point de départ
        assertTrue(result.contains("départ : Grenoble"));
    }

    @Test
    public void testPrimMSTEdges() {
        Graphe g = GraphManager.initDefaultGraph(null, null);
        int startIndex = 0; // Peu importe le départ

        String result = Prim.getResult(g, startIndex);

        // Vérifier que les 9 arêtes du MST sont présentes (peu importe l'ordre d'affichage)
        // Total des poids : 40+45+50+60+65+70+75+75+90 = 570

        // Compter le nombre d'arêtes affichées (9 arêtes attendues)
        int edgeCount = result.split("—").length - 1;
        assertEquals("Nombre d'arêtes dans le MST", 9, edgeCount);

        // Le coût total doit être 570
        assertTrue(result.contains("Coût total = 570"));
    }

    @Test
    public void testPrimAllStartingPoints() {
        Graphe g = GraphManager.initDefaultGraph(null, null);

        // Test que Prim donne le même coût total quel que soit le point de départ
        for (int start = 0; start < 10; start++) {
            String result = Prim.getResult(g, start);
            assertTrue("Coût total MST depuis sommet " + start,
                    result.contains("Coût total = 570"));
        }
    }
}
