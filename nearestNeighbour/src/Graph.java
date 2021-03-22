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

    public Graph() {
        nodes = new LinkedList<Node>();
    }

    public int getSize() {
        return nodes.size();
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

    public boolean containsLink(Node a, Node b) {
        if (a.equals(b)) return true;
        if (nodes.get(nodes.indexOf(a)).adj.contains(b)) return true;
        if (nodes.get(nodes.indexOf(b)).adj.contains(a)) return true;

        return false;
    }

}