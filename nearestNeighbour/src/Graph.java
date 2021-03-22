import java.awt.*;
import java.awt.geom.Point2D;
import java.util.LinkedList;

// Classe que representa um no
class Node {
    public LinkedList<Node> adj;
    public boolean visited;
    public int distance;
    Point2D p;

    Node(Point p) {
        this.adj = new LinkedList<Node>();
        this.p = p;
    }
}

public class Graph {
    public LinkedList<Node> nodes;

    public Graph(int n) {
        nodes = new LinkedList<Node>();
    }

    public void addLink(Node a, Node b) {
        nodes.get(nodes.indexOf(a)).adj.add(b);
        nodes.get(nodes.indexOf(b)).adj.add(a);
    }

    public void addPoint(Point p) {
        Node n = new Node(p);
        nodes.addLast(n);
    }

    public boolean contains(Point thisp) {
        int i = 0;

        if (nodes.size() == 0) return false;

        while (i < nodes.size()) {
            if (nodes.get(i).p.equals(thisp)) {
                return true;
            }
            ++i;
        }
        return false;
    }

}