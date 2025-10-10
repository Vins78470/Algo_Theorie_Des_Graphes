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




        // --- Affichage ---

        g.printMatrix();
        bfs.parcours(g,2);

        System.out.println("BFS");
        g.printMatrix();
        // --- Arbre couvrant minimal avec Kruskal ---
        System.out.println("\nKruskal");
        Kruskal.run(g);
        dfs.parcours(g,2);

        // --- Choix du sommet de départ pour Prim ---
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== Algorithme de Prim ===");
        System.out.println("Liste des sommets :");
        for (int i = 0; i < villes.length; i++) {
            System.out.println(i + " → " + villes[i]);
        }
        System.out.print("\nEntrez l'indice du sommet de départ : ");
        int start = scanner.nextInt();
        // --- Exécution de l'algorithme de Prim ---
        Prim.run(g, start);





// --- Plus court chemin entre deux villes (Dijkstra) ---
        System.out.println("\n=== Plus court chemin entre deux villes (Dijkstra) ===");

// Affiche la liste des villes avec leur index
        System.out.println("Liste des villes :");
        for (int i = 0; i < villes.length; i++) {
            System.out.println(i + " → " + villes[i]);
        }

// Demande du sommet de départ et d'arrivée
        Scanner scanner2 = new Scanner(System.in);
        System.out.print("\nEntrez le numéro de la ville de départ : ");
        int start2 = scanner2.nextInt();

        System.out.print("Entrez le numéro de la ville d'arrivée : ");
        int end2 = scanner2.nextInt();

// Exécution de l'algorithme de Dijkstra (plus court chemin entre deux villes)
        Dijkstra.run(g, start2, end2);


    }


}
