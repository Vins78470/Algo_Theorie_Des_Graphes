package Modele;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // === Définition des sommets ===
        String[] sommets = {"s1", "s2", "s3", "s4", "s5", "s6"};
        Graphe g = new Graphe(sommets.length, true, true, sommets); // orienté, pondéré

        // === Arêtes (selon ta liste) ===
        g.addEdge(0, 1, 4);   // s1 -> s2
        g.addEdge(0, 4, 7);   // s1 -> s5

        g.addEdge(1, 2, 3);   // s2 -> s3
        g.addEdge(1, 5, 5);   // s2 -> s6

        g.addEdge(2, 3, 3);   // s3 -> s4
        g.addEdge(2, 4, 2);   // s3 -> s5
        g.addEdge(2, 5, 6);   // s3 -> s6

        g.addEdge(4, 1, -4);  // s5 -> s2
        g.addEdge(4, 2, -1);  // s5 -> s3
        g.addEdge(4, 5, 3);   // s5 -> s6

        g.addEdge(5, 2, -2);  // s6 -> s3
        g.addEdge(5, 3, 2);   // s6 -> s4

        // === Affichage de la matrice d’adjacence ===
        g.printMatrix();

        // === Choix du sommet de départ ===
        Scanner sc = new Scanner(System.in);
        System.out.print("\nEntrez le sommet de départ (ex: s1, s2, s3...) : ");
        String source = sc.nextLine().trim();

        // === Exécution de Bellman-Ford ===
        BellmanFord bf = new BellmanFord(new StepManager());
        String resultat = bf.run(g, source);

        // === Affichage du résultat final ===
        System.out.println("\n" + resultat);
        System.out.println("=== Fin du programme ===");
    }
}
