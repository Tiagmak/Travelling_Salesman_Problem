import java.awt.*;
import java.util.LinkedList;

public class Polygon {
  public Neighbour current;
  public Graph graph;

  public Polygon(Graph graph) {
    this.graph = graph;
    this.graph.nearest();
  }

  public void simulatedAnnealing() {
    double temperature = 1000;
    double delta;

    this.current = new Neighbour();
    this.current.add(graph.nearest);
    this.current.gen();
    LinkedList<Point> current_candidate = this.current.neighbours.get(0);

    this.current.add(current_candidate);
    this.current.gen();

    int intxnsCurrent = this.current.neighbours.size();
    int intxnsNext;

    while (this.current.neighbours.peekFirst() != null) {
      Neighbour next = new Neighbour();
      next.add(this.current.lowestConflictsCandidate());
      next.gen();
      intxnsNext = next.neighbours.size();

      delta = intxnsNext - intxnsCurrent;
      if (delta < 0) {
        current_candidate = next.candidate;
      } else {
        double prob_accept_worse = Math.pow(Math.E, (delta / temperature));
        if (prob_accept_worse > Math.random()) {
          current_candidate = next.candidate;
        }
      }

      temperature *= 0.95;

      this.current = new Neighbour();
      this.current.add(current_candidate);
      this.current.gen();
      intxnsCurrent = this.current.neighbours.size();
    }
  }
}
