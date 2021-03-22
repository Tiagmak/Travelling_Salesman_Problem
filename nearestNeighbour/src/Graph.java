import java.awt.*;
import java.awt.geom.Point2D;
import java.util.LinkedList;

// Classe que representa um no
class Node {
    public LinkedList<Integer> adj;
    public boolean visited;
    public int distance;
    Point2D xy;

    Node() {
        adj = new LinkedList<Integer>();
    }
}

public class Graph {
    public int n;
    public Node[] nodes;

    public Graph(int n) {
        this.n = n;
        nodes = new Node[n];
        for (int i = 0; i < n; i++)
            nodes[i] = new Node();
    }

    public void addLink(int a, int b) {
        nodes[a].adj.add(b);
        nodes[b].adj.add(a);
    }

    public void addPoint(int n, Point p) {
        nodes[n].xy = new Point(p);
    }

}