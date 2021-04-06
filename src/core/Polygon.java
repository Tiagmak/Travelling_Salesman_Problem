public class Polygon {
  public Neighbour current;
  public Graph graph;

  public Polygon(Graph graph) {
    current = new Neighbour();
    this.graph = graph;
    this.graph.nearest();
    this.current.add(graph.nearest);
    this.current.gen();
  }

  public void simulatedAnnealing() {
    double temperature = 1000;
    double delta;

    this.current.add(this.current.lowestConflictsCandidate());
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
        this.current.add(next.candidate);
      } else if (Math.pow(Math.E, (delta / temperature)) > Math.random()) {
        this.current.add(next.candidate);
      }

      this.current.gen();
      intxnsCurrent = this.current.neighbours.size();
      temperature *= 0.95;
    }
  }
}
