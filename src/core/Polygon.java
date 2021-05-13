import java.awt.*;
import java.util.LinkedList;

public class Polygon {
  public Graph graph;
  private final LinkedList<Point> nearest;

  public Polygon(LinkedList<Point> nearest) {
    this.nearest = nearest;
  }

  public void simulatedAnnealing() {
    double temperature = 1000;
    double delta;
    Neighbour current = new Neighbour();
    current.add(this.nearest);
    current.gen();
    LinkedList<Point> current_candidate = current.neighbours.get(0);

    current.add(current_candidate);
    current.gen();

    int intxnsCurrent = current.neighbours.size();
    int intxnsNext;

    long startTime = System.nanoTime();

    while (current.neighbours.peekFirst() != null && temperature > 0.000000001) {
      Neighbour next = new Neighbour();
      next.add(current.lowestConflictsCandidate());
      next.gen();
      intxnsNext = next.neighbours.size();

      delta = intxnsNext - intxnsCurrent;
      if (delta < 0) {
        current_candidate = next.candidate;
      } else {
        double prob_accept_worse = Math.pow(Math.E, (-delta / temperature));
        if (prob_accept_worse > Math.random()) {
          current_candidate = next.candidate;
        }
      }

      temperature *= 0.98;

      current = new Neighbour();
      current.add(current_candidate);
      current.gen();
      intxnsCurrent = current.neighbours.size();
    }

    long estimatedTime = System.nanoTime() - startTime;
    System.out.println(current.listToString(current.candidate) + "\n" + "ANNEALING" + "\n" + estimatedTime);
  }
}
