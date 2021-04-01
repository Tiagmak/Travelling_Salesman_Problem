import java.awt.*;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Scanner;

public class Core {


    public static void nearest(Graph g) {
        int randA = (int) (Math.random() * (g.getSize()));
        Node a = g.nodes.get(randA);

        g.find_nearest(a);
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        int n, min, max;

        System.out.println("Limite inferior e superior? ");
        min = in.nextInt();
        max = in.nextInt();
        int range = 0;

        if (min == 0) {
            range = Math.abs(max + 1);
        } else if (min < 0) {
            range = Math.abs(max) + Math.abs(min);
        } else {
            range = Math.abs(max) - Math.abs(min);
        }

        int n_max = range * range;

        System.out.println("Quantos pontos? Inserir no máximo " + n_max + ".");
        n = in.nextInt();

        if (n > n_max) {
            System.out.println("Try again...");
            n = in.nextInt();
        }

        Graph g = new Graph(n);
        g.graphRandom(n, 0, min, max);

        nearest(g);
        g.printNearest();

        g.toExchange();
        g.printCandidates();

        System.out.println();
        /*
        if (g.lowestPerimeterIndex < Math.pow(10, 9))
            System.out.println(g.candidates.get(g.lowestPerimeterIndex).toString());

         */
    }
}

