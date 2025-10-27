package Modele;

import java.util.Scanner;

/**
 * Classe principale du projet de Théorie des Graphes.
 * <p>
 * Ce programme permet d'exécuter depuis le terminal différents algorithmes
 * de graphes (BFS, DFS, Dijkstra, Kruskal, Prim, Bellman-Ford et Floyd-Warshall)
 * sur un graphe d'exemple représentant des villes françaises.
 * <p>
 * L'utilisateur peut choisir un algorithme, puis entrer le nom ou l'indice des
 * sommets de départ et d'arrivée selon les besoins.
 */
public class Main {

    /**
     * Point d’entrée principal du programme.
     * Initialise le graphe par défaut, affiche la liste des villes
     * et propose à l’utilisateur de choisir un algorithme à exécuter.
     *
     * @param args arguments de ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        System.out.println("=== Projet Théorie des Graphes ===\n");

        // Initialisation du graphe de base contenant 10 villes
        Graphe g = GraphManager.initDefaultGraph(null, null);

        // Affiche la liste des sommets avec leur indice
        System.out.println("Villes disponibles :");
        for (int i = 0; i < g.getNbSommets(); i++) {
            System.out.println("  " + i + " → " + g.getVertexName(i));
        }

        // Menu principal
        System.out.println("\n=== Choisissez un algorithme ===");
        System.out.println("1 - BFS (Parcours en largeur)");
        System.out.println("2 - DFS (Parcours en profondeur)");
        System.out.println("3 - Dijkstra (Plus court chemin)");
        System.out.println("4 - Kruskal (Arbre couvrant minimal)");
        System.out.println("5 - Prim (Arbre couvrant minimal)");
        System.out.println("6 - Bellman-Ford (Plus court chemin)");
        System.out.println("7 - Floyd-Warshall (Tous les plus courts chemins)");
        System.out.print("\nVotre choix : ");

        Scanner sc = new Scanner(System.in);
        int choix = sc.nextInt();
        sc.nextLine(); // consomme le retour à la ligne pour éviter les erreurs de saisie

        // Sélection de l’algorithme choisi
        switch (choix) {
            case 1 -> runBFS(g, sc);
            case 2 -> runDFS(g, sc);
            case 3 -> runDijkstra(g, sc);
            case 4 -> runKruskal(g);
            case 5 -> runPrim(g, sc);
            case 6 -> runBellmanFord(g, sc);
            case 7 -> runFloydWarshall(g, sc);
            default -> System.out.println("Choix invalide !");
        }

        sc.close();
    }

    // -------------------- EXÉCUTION DES ALGORITHMES --------------------

    /** Exécute le parcours en largeur (BFS) à partir d’un sommet donné. */
    private static void runBFS(Graphe g, Scanner sc) {
        int start = askVertex(g, sc, "Nom ou indice du sommet de départ : ");
        if (start == -1) return;

        BFS bfs = new BFS();
        System.out.println("\n" + bfs.getResult(g, start));
    }

    /** Exécute le parcours en profondeur (DFS) à partir d’un sommet donné. */
    private static void runDFS(Graphe g, Scanner sc) {
        int start = askVertex(g, sc, "Nom ou indice du sommet de départ : ");
        if (start == -1) return;

        DFS dfs = new DFS();
        System.out.println("\n" + dfs.getResult(g, start));
    }

    /** Exécute l’algorithme de Dijkstra pour trouver le plus court chemin entre deux villes. */
    private static void runDijkstra(Graphe g, Scanner sc) {
        int start = askVertex(g, sc, "Nom ou indice du sommet de départ : ");
        if (start == -1) return;
        int end = askVertex(g, sc, "Nom ou indice du sommet d'arrivée : ");
        if (end == -1) return;

        Dijkstra dij = new Dijkstra();
        System.out.println("\n" + dij.getResult(g, start, end));
    }

    /** Exécute l’algorithme de Kruskal pour générer l’arbre couvrant minimal (MST). */
    private static void runKruskal(Graphe g) {
        Kruskal k = new Kruskal();
        System.out.println("\n" + k.getResult(g));
    }

    /** Exécute l’algorithme de Prim pour générer un arbre couvrant minimal à partir d’un sommet. */
    private static void runPrim(Graphe g, Scanner sc) {
        int start = askVertex(g, sc, "Nom ou indice du sommet de départ : ");
        if (start == -1) return;

        System.out.println("\n" + Prim.getResult(g, start));
    }

    /** Exécute l’algorithme de Bellman-Ford pour calculer le plus court chemin entre deux sommets. */
    private static void runBellmanFord(Graphe g, Scanner sc) {
        int start = askVertex(g, sc, "Nom ou indice du sommet de départ : ");
        if (start == -1) return;
        int end = askVertex(g, sc, "Nom ou indice du sommet d'arrivée : ");
        if (end == -1) return;

        BellmanFord bf = new BellmanFord();
        System.out.println("\n" + bf.getResult(g, start, end));
    }

    /** Exécute l’algorithme de Floyd-Warshall pour calculer tous les plus courts chemins. */
    private static void runFloydWarshall(Graphe g, Scanner sc) {
        int start = askVertex(g, sc, "Nom ou indice du sommet de départ : ");
        if (start == -1) return;
        int end = askVertex(g, sc, "Nom ou indice du sommet d'arrivée : ");
        if (end == -1) return;

        FloydWarshall fw = new FloydWarshall();
        System.out.println("\n" + fw.getResult(g, start, end));
    }

    // -------------------- MÉTHODES UTILITAIRES --------------------

    /**
     * Permet à l’utilisateur d’entrer un sommet en saisissant soit son nom, soit son indice.
     *
     * @param g       graphe sur lequel chercher le sommet
     * @param sc      scanner pour la saisie utilisateur
     * @param message texte affiché à l’écran
     * @return indice du sommet s’il existe, -1 sinon
     */
    private static int askVertex(Graphe g, Scanner sc, String message) {
        System.out.print(message);
        String input = sc.nextLine().trim();

        int index;
        try {
            index = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            index = getIndexByName(g, input);
        }

        if (index < 0 || index >= g.getNbSommets()) {
            System.out.println("Ville inconnue ou indice invalide : " + input);
            return -1;
        }
        return index;
    }

    /**
     * Recherche un sommet dans le graphe par son nom (insensible à la casse et aux espaces).
     *
     * @param g    graphe dans lequel chercher
     * @param name nom du sommet à trouver
     * @return indice du sommet si trouvé, -1 sinon
     */
    private static int getIndexByName(Graphe g, String name) {
        if (name == null || name.isBlank()) return -1;
        String cleaned = name.trim().toLowerCase();

        for (int i = 0; i < g.getNbSommets(); i++) {
            String vertexName = g.getVertexName(i).trim().toLowerCase();
            if (vertexName.equals(cleaned)) {
                return i;
            }
        }
        return -1;
    }
}
