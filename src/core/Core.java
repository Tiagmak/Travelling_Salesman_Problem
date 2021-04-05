import java.util.Scanner;

public class Core {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n, n_max, min, max;
        Graph g = null;

        /*
         * Getting user input and ensuring the number of given points
         * is within range. This way, we can guarantee further on that
         * all points are different.
         */
        System.out.println("Limite inferior e superior? ");
        min = in.nextInt();
        max = in.nextInt();

        int range = 0;

        if (min == 0) {
            range = Math.abs(max + 1);
        } else if (min < 0 && max > 0) {
            range = Math.abs(max) + Math.abs(min) + 1;
        } else if (min < 0) {
            range = Math.abs(max) + Math.abs(min);
        } else {
            range = Math.abs(max) - Math.abs(min);
        }
        n_max = range * range;

        System.out.println("Quantos pontos? Inserir no máximo " + n_max + ".");
        n = in.nextInt();
        while (n > n_max) {
            System.out.println("Try again...");
            n = in.nextInt();
        }

        /*
         * Create a new graph that contains the given number of points.
         * Ensures that a generated graph has at least one intersection.
         * This way the program may continue.
         *
         * graphRandom - a recursive function that adds points to it's
         * instance graph. Only adds those that aren't already part of it.
         * The recursion stops when the given number of points is met.
         *
         * nearest - applies the nearest neighbour algorithm to the whole
         * graph.
         *
         * toExchange - generates a first list of possible candidates.
         * One candidate is generated for each intersection, and each
         * intersection has two possible solutions. The one solution
         * with the smallest perimeter is the one which is ultimately
         * added to the candidates list.
         */
        boolean ok = false;
        while (!ok) {
            g = new Graph(n);
            g.graphRandom(n, 0, min, max);
            g.nearest();
            g.toExchange(g.nearest);
            if (g.candidates.peekFirst() != null) ok = true;
        }

        System.out.println("\n\n[ Original graph: ]" + "\n\n" + g.listToString(g.nearest) + "\n");
        System.out.println("[ Number of intersections: " + g.candidates.size() + " ]\n");
        System.out.println("[ Candidates: ]" + "\n");
        System.out.println(g.candidatesToString());

        System.out.println("\n[ Heurística escolhida ? ]");
        System.out.println("1) Menor perímetro\n" +
                "2) Primeiro Candidato\n" +
                "3) Menos conflitos\n" +
                "4) Candidato aleatório\n");

        int h = in.nextInt();
        switch (h) {
            case 1 -> {
                System.out.println("\n[ APPLIED LOWEST PERIMETER (best-improvement first) ]");
                System.out.println("[ RESULT: ]");
                while (g.candidates.peekFirst() != null) {
                    int index = g.getLowestPerimeter();
                    g.toExchange(g.candidates.get(index));
                }
                if (g.candidates.peekFirst() == null) {
                    System.out.println("[ NO INTERSECTIONS ]");
                }
            }
            case 2 -> {
                System.out.println("\n[ APPLIED FIRST CANDIDATE (first-improvement) ]");
                System.out.println("[ RESULT: ]");
                while (g.candidates.peekFirst() != null) {
                    g.toExchange(g.candidates.get(0));
                }
                if (g.candidates.peekFirst() == null) {
                    System.out.println("[ NO INTERSECTIONS ]");
                }
            }
            case 3 -> {
                System.out.println("\n[ APPLIED LEAST CONFLICTS ]");
                System.out.println("[ RESULT: ]");
                g.leastIntersections();
                if (g.candidates.peekFirst() == null) {
                    System.out.println("[ NO INTERSECTIONS ]");
                }
            }
            case 4 -> {
                System.out.println("\n[ APPLIED RANDOM CANDIDATE ]");
                System.out.println("[ RESULT: ]");
                while (g.candidates.peekFirst() != null) {
                    int rand = (int) (Math.random() * ((g.candidates.size())));
                    g.toExchange(g.candidates.get(rand));
                }
                if (g.candidates.peekFirst() == null) {
                    System.out.println("[ NO INTERSECTIONS ]");
                }
            }
        }
    }
}

