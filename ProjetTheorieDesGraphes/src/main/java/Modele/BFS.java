package Modele;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;

public class BFS
{
    Queue<Integer> queue;


    public BFS()
    {
        Queue<Integer> queue = new LinkedList<>();
    }

    public void parcoursMatrice(int[][] matrice)
    {
        for (int i = 0 ; i< matrice.length ; i++ )
        {
            for (int j = 0 ; j< matrice[i].length ; j++ )
            {
                queue.add(matrice[i][j]);
                System.out.print(queue.poll());
                Integer nextNodeToBeChoosen = chooseMinInQueue(queue);
                queue.add(matrice[i][j]);

            }
        }
    }


    public static Integer chooseMinInQueue(Queue<Integer> queue) {
        // retourne le minimum ou null si la queue est vide
        return queue.stream().min(Integer::compareTo).orElse(null);
    }
}
