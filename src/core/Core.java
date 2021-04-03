import javax.script.*;
import java.io.IOException;
import java.util.Scanner;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Core {

    public static int getRange(int min, int max) {
        int range = 0;

        if (min == 0) {
            range = Math.abs(max + 1);
        } else if (min < 0) {
            range = Math.abs(max) + Math.abs(min);
        } else {
            range = Math.abs(max) - Math.abs(min);
        }

        return range * range;
    }

    public static void main(String[] args) throws IOException, ScriptException {
        Scanner in = new Scanner(System.in);
        int n, min, max;

        System.out.println("Limite inferior e superior? ");
        min = in.nextInt();
        max = in.nextInt();

        int n_max = getRange(min, max);

        System.out.println("Quantos pontos? Inserir no máximo " + n_max + ".");
        n = in.nextInt();

        /**
         * NUMBER OF POINTS IS TOO BIG
         */
        if (n > n_max) {
            System.out.println("Try again...");
            n = in.nextInt();
        }

        /**
         * CREATE GRAPH AND APPLY NEAREST
         */
        Graph g = new Graph(n);
        g.graphRandom(n, 0, min, max);
        g.nearest();

        /**
         * CREATE A LIST OF CANDIDATES
         */
        g.toExchange(g.nearest, -1);

        /**
         * ENSURE THERE ARE INTERSECTIONS
         */
        while (g.candidates.peekFirst() == null) {
            g = new Graph(n);
            g.graphRandom(n, 0, min, max);
            g.nearest();
            g.toExchange(g.nearest, 0);
        }

        /**
         * PRINT NEAREST AND CANDIDATES
         */
        System.out.println(g.printNearest() + "\n");
        System.out.println("[ CANDIDATES: ]");
        g.printCandidates();

        /**
         * NEXT MENU
         */
        System.out.println("Heurística?");
        System.out.println("1) Menor perímetro\n" +
                            "2) Primeiro Candidato\n" +
                            "3) Menos conflitos\n" +
                            "4) Candidato aleatório");

        /**
         * APPLY ONE OF THE FOLLOWING HEURISTICS
         */
        int h = in.nextInt();
        switch (h) {
            case 1:
                g.toExchange(g.nearest, 1);
                if (g.candidates.peekFirst() != null)
                    g.toExchange(g.candidates.get(g.lowestPerimeterIndex), 0);

                if (g.candidates.peekFirst() != null) {
                    System.out.println("[ LOWEST PERIMETER: ]");
                    g.printCandidates();
                }
                break;

            case 2:
                if (g.candidates.peekFirst() != null)
                    g.toExchange(g.candidates.getFirst(), 0);

                if (g.candidates.peekFirst() != null) {
                    System.out.println("[ FIRST CANDIDATE: ]");
                    g.printCandidates();
                }
                break;

            case 3:
                break;

            case 4:
                int rand = (int)(Math.random() * ((g.candidates.size())));
                if (g.candidates.peekFirst() != null)
                    g.toExchange(g.candidates.get(rand), 0);

                if (g.candidates.peekFirst() != null) {
                    System.out.println("[ RANDOM CANDIDATE: ]");
                    g.printCandidates();
                }
                break;
        }
        /*
        int o = in.nextInt();
        System.out.println("Optimização?");
        System.out.println("1) Simulated Annealing");

        switch (o) {
            case 1:
                g.toExchange(g.bestUntilNowPerimeter, 1);
                System.out.println("[ CANDIDATES: ]");
                g.printCandidates();
                break;
        }
        */
    }
}

