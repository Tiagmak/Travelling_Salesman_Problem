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

    g = new Graph(n);
    g.graphRandom(n, 0, min, max);

    System.out.println("\n[ Original graph: ]" + "\n" + g.graphRandomToString() + "\n");

    Neighbour neigh = new Neighbour();
    g.nearest();
    neigh.add(g.nearest);
    neigh.gen();
    System.out.println("[ Nearest-neighbour first graph: ]" + "\n" + neigh.listToString(g.nearest));
    System.out.println("[ Number of intersections: " + neigh.neighbours.size() + " ]\n");

    int h = 0;
    if (neigh.neighbours.peekFirst() != null) {
      System.out.println("[ Candidates: ]" + "\n");
      System.out.println(neigh.candidatesToString());

      System.out.println("\n[ Heurística escolhida ? ]");
      System.out.println(
          "1) Menor perímetro\n"
              + "2) Primeiro Candidato\n"
              + "3) Menos conflitos\n"
              + "4) Candidato aleatório\n");

      h = in.nextInt();

    } else {
      System.out.println("[ No candidates... ]" + "\n");
    }

    switch (h) {
      case 1:
        {
          long startTime = System.nanoTime();
          System.out.println("\n[ APPLIED LOWEST PERIMETER (best-improvement first) ]");
          System.out.println("[ RESULT: ]");

          while (!neigh.neighbours.isEmpty()) {
            neigh.lowestPerimeterCandidate();
            neigh.gen();
          }

          long estimatedTime = System.nanoTime() - startTime;
          System.out.println(neigh.listToString(neigh.candidate) + "\nLOWEST PERIMETER\n" + estimatedTime);
          break;
        }

      case 2:
        {
          long startTime = System.nanoTime();
          System.out.println("\n[ APPLIED FIRST CANDIDATE (first-improvement) ]");
          System.out.println("[ RESULT: ]");

          while (!neigh.neighbours.isEmpty()) {
            neigh.add(neigh.neighbours.get(0));
            neigh.gen();
          }

          long estimatedTime = System.nanoTime() - startTime;
          System.out.println(neigh.listToString(neigh.candidate) + "\nFIRST\n" + estimatedTime);
          break;
        }

      case 3:
        {
          long startTime = System.nanoTime();
          System.out.println("\n[ APPLIED LEAST CONFLICTS ]");
          System.out.println("[ RESULT: ]");

          while (!neigh.neighbours.isEmpty()) {
            neigh.add(neigh.lowestConflictsCandidate());
            neigh.gen();
          }

          long estimatedTime = System.nanoTime() - startTime;
          System.out.println(neigh.listToString(neigh.candidate) + "\nLEAST\n" + estimatedTime);

          break;
        }

      case 4:
        {
          long startTime = System.nanoTime();
          System.out.println("\n[ APPLIED RANDOM CANDIDATE ]");
          System.out.println("[ RESULT: ]");

          while (!neigh.neighbours.isEmpty()) {
            neigh.randomCandidate();
            neigh.gen();
          }

          long estimatedTime = System.nanoTime() - startTime;
          System.out.println(neigh.listToString(neigh.candidate) + "\nRANDOM\n" + estimatedTime);
          break;
        }

      default:
        return;
    }

    System.out.println("\n[ Simulated Annealing ? ]");
    System.out.println("1) Sim\n" + "2) Não\n");

    int s = in.nextInt();
    switch (s) {
      case 1:
        {
          Polygon poly = new Polygon(g.nearest);
          System.out.println("\n[ APPLIED SIMULATED ANNEALING ]");
          System.out.println("[ RESULT: ]");

          poly.simulatedAnnealing();
          break;
        }

      default:
        break;
    }
  }
}
