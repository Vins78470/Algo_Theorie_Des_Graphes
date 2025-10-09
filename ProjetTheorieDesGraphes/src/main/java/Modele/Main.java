package Modele;

import java.util.Scanner;

public class Main {

    // --- Exemple d'utilisation ---
    public static void main(String[] args) {
        DFS dfs = new DFS();
        BFS bfs = new BFS();
        String[] villes = {
                "Paris", "Caen", "Rennes", "Nantes", "Bordeaux",
                "Lille", "Nancy", "Dijon", "Lyon", "Grenoble"
        };

        int n = villes.length;
        Graphe g = new Graphe(n, false, true, villes); // non orienté, pondéré

        // --- Arêtes pondérées (selon l’image) ---
        g.addEdge(0, 1, 50);   // Paris - Caen
        g.addEdge(0, 2, 110);  // Paris - Rennes
        g.addEdge(0, 3, 80);   // Paris - Nantes
        g.addEdge(0, 4, 150);  // Paris - Bordeaux
        g.addEdge(0, 5, 70);   // Paris - Lille
        g.addEdge(0, 7, 60);   // Paris - Dijon

        g.addEdge(1, 2, 75);   // Caen - Rennes
        g.addEdge(1, 5, 65);   // Caen - Lille

        g.addEdge(2, 3, 45);   // Rennes - Nantes
        g.addEdge(2, 4, 130);  // Rennes - Bordeaux

        g.addEdge(3, 4, 90);   // Nantes - Bordeaux

        g.addEdge(4, 8, 100);  // Bordeaux - Lyon

        g.addEdge(5, 6, 100);  // Lille - Nancy
        g.addEdge(5, 7, 120);  // Lille - Dijon

        g.addEdge(6, 7, 75);   // Nancy - Dijon
        g.addEdge(6, 8, 90);   // Nancy - Lyon
        g.addEdge(6, 9, 80);   // Nancy - Grenoble

        g.addEdge(7, 8, 70);   // Dijon - Lyon
        g.addEdge(7, 9, 75);   // Dijon - Grenoble

        g.addEdge(8, 9, 40);   // Lyon - Grenoble




        System.out.println(AlgoFunctions.runBFS(g, 2));
        System.out.println(AlgoFunctions.runDFS(g, 2));
        System.out.println(AlgoFunctions.runKruskal(g));
        System.out.println(AlgoFunctions.runPrim(g, 0));
        System.out.println(AlgoFunctions.runDijkstra(g, 4, 5));


    }


}
