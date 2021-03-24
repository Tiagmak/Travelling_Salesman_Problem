import java.awt.*;
import java.util.Arrays;
import java.util.Scanner;

public class Core {
    public static void nearest(Graph g) {
        int randA = (int) (Math.random() * (g.getSize()));
        System.out.println("começa em " + randA);
        Node a = g.nodes.get(randA);

        g.find_nearest(a);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n, min, max;

        // ask stuff
        System.out.println("Limite inferior e superior? ");
        min = in.nextInt();
        max = in.nextInt();

        int range = Math.abs(max) - Math.abs(min);
        int n_max = range * range;

        System.out.println("Quantos pontos? Inserir no máximo " + n_max + ".");
        n = in.nextInt();

        if (n > n_max) {
            System.out.println("Try again...");
            n = in.nextInt();
        }

        Graph g = new Graph();
        g.graphRandom(n, 0, min, max);
        nearest(g);

        for (int i = 0; i < g.nearest.size(); ++i) {
            System.out.println(g.nearest.get(i).toString());
        }


        System.out.println();

        g.toExchange();
        for (int i = 0; i < g.candidates.size(); ++i) {
            System.out.println(Arrays.toString(g.candidates.get(i).toArray()));
        }
    }
}

