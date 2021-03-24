import java.awt.*;
import java.util.Scanner;

public class Core {

    public static void graphRandom(Graph g, int n, int min, int max) {
        for (int i = 0; i < n; ++i) {
            int x = (int) (Math.random() * (max - min + 1) + min);
            int y = (int) (Math.random() * (max - min + 1) + min);

            Point p = new Point(x, y);

            // check if already in
            if (!g.contains(p)) {
                // add if not in
                g.addPoint(p);

                System.out.println("x: " + p.getX() + "\t" + "y: " + p.getY() + " " + g.nodes + "\t\n" + "indice " + i);
            } else {
                System.out.println("ALREADY IN");
            }
        }
    }
/*
    public static void linkRandom(Graph g) {
        for (int i = 0; i < g.getSize(); ++i) {
            int randA = (int) (Math.random() * (g.getSize()));
            int randB = (int) (Math.random() * (g.getSize()));

            Node a = g.nodes.get(randA);
            Node b = g.nodes.get(randB);

            if (!g.containsLink(a, b)) {
                g.addLink(a, b);
                System.out.println(g.nodes.get(randA) + " connected to " + g.nodes.get(randB));
            }
        }
    }
*/

    public static void nearest(Graph g) {
        int randA = (int) (Math.random() * (g.getSize()));
        System.out.println("começa em " + randA);
        Node a = g.nodes.get(randA);

        g.find_nearest(a);
        //System.out.println("vai para " +randA);

    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n, min, max;

        // ask stuff
        System.out.println("Quantos pontos? ");
        n = in.nextInt();
        System.out.println("Limite inferior e superior? ");
        min = in.nextInt();
        max = in.nextInt();

        Graph g = new Graph();
        graphRandom(g, n, min, max);

        //linkRandom(g);

        nearest(g);

    }
}

