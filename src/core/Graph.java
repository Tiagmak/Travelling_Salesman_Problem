import java.awt.*;
import java.util.LinkedList;

class Node {
  public boolean visited;
  Point point;

  Node(Point p) {
    this.point = p;
  }
}

public class Graph {
  public LinkedList<Node> nodes;
  public LinkedList<Point> nearest;
  int size;

  public Graph(int size) {
    this.size = size;
    nodes = new LinkedList<>();
    nearest = new LinkedList<>();
  }

  private int getSize() {
    return nodes.size();
  }

  public boolean contains(Point givenPoint) {
    if (nodes.isEmpty()) return false;

    for (Node n : nodes) {
        if (n.point.equals(givenPoint)) {
          return true;
        }
    }
    return false;
  }

  public void addPoint(Point p) {
    Node n = new Node(p);
    nodes.addLast(n);
  }

  public void graphRandom(int n, int i, int min, int max) {
    if (i >= n) return;

    int x = (int) (Math.random() * (max - min + 1) + min);
    int y = (int) (Math.random() * (max - min + 1) + min);

    Point p = new Point(x, y);

    // check if already in
    if (!contains(p)) {
      addPoint(p);
      ++i;
    }
    graphRandom(n, i, min, max);
  }

  String graphRandomToString() {
    StringBuilder s = new StringBuilder();
    for (Node node : nodes) {
      s.append("(").append(node.point.x).append(",").append(node.point.y).append(")");
    }
    s.append("(")
        .append(nodes.getFirst().point.x)
        .append(",")
        .append(nodes.getFirst().point.y)
        .append(")");
    s.append("\n");

    return s.toString();
  }

  public void findNearest(Node a) {
    nearest.addLast(a.point);
    a.visited = true;
    Node next = null;

    int i = 0;
    int nextCandidate = 0;
    int flag = 0;
    double distance = 0;

    while (i < nodes.size()) {
      Node T = nodes.get(i);
      if (!T.equals(a)) { // para nao pegar nele próprio
        if (!T.visited) { // ainda nao foi visitado
          if (flag == 0) {
            flag = 1;
            nextCandidate = i;
            distance = a.point.distance(T.point);
            next = nodes.get(i);
          } else {
            if (a.point.distance(T.point) < distance) {
              distance = a.point.distance(T.point);
              next = nodes.get(i);
              nextCandidate = i;
            }
          }
        }
      }
      ++i;
    }

    if (flag == 1) {
      findNearest(next);
    } else {
      nearest.addLast(nearest.get(0));
    }
  }

  public void nearest() {
    int randA = (int) (Math.random() * (size));
    Node a = nodes.get(randA);

    findNearest(a);
  }
}
