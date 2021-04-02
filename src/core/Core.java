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
        g.toExchange();

        String argument;
        System.out.println("[ NEAREST: ]");
        argument = g.printNearest();
        System.out.println(argument);

        System.out.println("[ CANDIDATES: ]");
        g.printCandidates();

        System.out.println("[ LOWEST PERIMETER: ]");
        g.printLeastPerimeter();

        /*
        //Process p = Runtime.getRuntime().exec(new String[] {"bash", "-c", "./test.py", "lol"});
        Process pb = Runtime.getRuntime().exec("bash " + "-c " + "python3 ./file.py " + argument);
        //ProcessBuilder pb = new ProcessBuilder("bash", "-c", arguments[0], arguments[1]);
        //Process p = pb.start();
        BufferedReader input_py = new BufferedReader(new InputStreamReader(pb.getInputStream()));
        while ((s = input_py.readLine()) != null) {
            System.out.println(s);
        }

         */
    }
}

