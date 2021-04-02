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

        if (n > n_max) {
            System.out.println("Try again...");
            n = in.nextInt();
        }

        Graph g = new Graph(n);
        g.graphRandom(n, 0, min, max);
        g.nearest();

        String argument;
        System.out.println("[ NEAREST: ]");
        argument = g.printNearest();
        System.out.println(argument);

        g.toExchange(g.nearest, 0);

        System.out.println("Heurística?");
        System.out.println("1) Menor perímetro\n" +
                            "2) Primeiro Candidato\n" +
                            "3) Menos conflitos\n" +
                            "4) Candidato aleatório");

        int h = in.nextInt();
        switch (h) {
            case 1:
                g.toExchange(g.nearest, 1);
                if (g.candidates.size() > 0) {
                    System.out.println("[ CANDIDATES: ]");
                    g.printCandidates();
                }

                break;

            case 2:
                break;

            case 3:
                break;

            case 4:
                break;
        }

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

        System.out.println("[ LOWEST PERIMETER: ]");
        g.printLeastPerimeter();
    }
}

