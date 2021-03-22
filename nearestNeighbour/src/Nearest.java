import java.awt.*;
import java.util.Scanner;

public class Nearest {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n, min, max;

        // ask stuff
        System.out.println("Quantos pontos? ");
        n = in.nextInt();
        System.out.println("Limite inferior e superior? ");
        min = in.nextInt();
        max = in.nextInt();

        Graph g = new Graph(n);

        for (int i = 0; i < n; ++i) {
            int x = (int) (Math.random() * (max - min + 1) + min);
            int y = (int) (Math.random() * (max - min + 1) + min);

            Point p = new Point(x, y);
            Node node = new Node(p);

            // check if already in
            if (!g.nodes.contains(node)){
                // add if not in
                g.addPoint(p);

                System.out.println("x: " + p.getX() + "\t" + "y: " + p.getY() + " " + g.nodes + "\t\n");
            } else {
                System.out.println(":((((((");
            }
        }
    }
}