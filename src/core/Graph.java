import java.awt.Point;
import java.util.Arrays;
import java.util.LinkedList;

// Classe que representa um no
class Node {
    //public LinkedList<Node> adj;
    public boolean visited;
    //public int distance;
    Point point;

    Node(Point p) {
        //this.adj = new LinkedList<Node>();
        this.point = p;
    }
}

public class Graph {
    public LinkedList<Node> nodes;
    public LinkedList<Point> nearest;
    public LinkedList<LinkedList<Point>> candidates;

    public Graph() {
        nodes = new LinkedList<>();
        nearest = new LinkedList<>();
    }

    public void graphRandom(int n, int i, int min, int max) {
        if (i == n) return;

        int x = (int) (Math.random() * (max - min + 1) + min);
        int y = (int) (Math.random() * (max - min + 1) + min);

        Point p = new Point(x, y);

        // check if already in
        if (!contains(p)) {
            System.out.println("[index: " + i + "] " + "x: " + p.getX() + "\t" + "y: " + p.getY());
            addPoint(p);
            ++i;
        } else {
            System.out.println("ALREADY IN");
        }
        graphRandom(n, i, min, max);
    }

    boolean checkIntersect(Point a, Point b, Point c, Point d) {
        double det = (b.getX() - a.getX()) * (d.getY() - c.getY()) - (d.getX() - c.getX()) * (b.getY() - a.getY());

        if (det == 0) return false;

        double lambda = ((d.getY() - c.getY()) * (d.getX() - a.getX()) + (c.getX() - d.getX()) * (d.getY() - a.getY())) / det;
        double gamma = ((a.getY() - b.getY()) * (d.getX() - a.getX()) + (b.getX() - a.getX()) * (d.getY() - a.getY())) / det;

        return (0 < lambda && lambda < 1) && (0 < gamma && gamma < 1);
    }

    // reverse bs need to work here
    void checkCase(Point s1_start, Point s1_end, Point s2_start, Point s2_end) {
        LinkedList<Point> candidate = new LinkedList<>();

        int to_advance_index = nearest.indexOf(s1_end);
        int to_previous_index = nearest.indexOf(s2_start);

        Point advance = new Point(nearest.get(to_advance_index));
        Point previous = new Point(nearest.get(to_previous_index));

        for (int i = 0; i < nearest.size(); ++i) {
            if (i == to_previous_index) {
                candidate.addLast(advance);
            } else if (i == to_advance_index) {
                candidate.addLast(previous);
            } else {
                candidate.addLast(nearest.get(i));
            }
        }

        candidates.addLast(candidate);
    }

    public void toExchange() {
        candidates = new LinkedList<>();
        Point s1, s2, e1, e2;

        for (int i = 0; i < nearest.size() - 1; i++) {
            s1 = nearest.get(i);
            e1 = nearest.get(i + 1);

            for (int j = 0; j < nearest.size() - 1; j++) {
                s2 = nearest.get(j);
                e2 = nearest.get(j + 1);

                if (s1.equals(s2) && e1.equals(e2)) continue;

                if (checkIntersect(s1, e1, s2, e2)) {
                    checkCase(s1, e1, s2, e2);
                }
            }
        }
    }

    public int getSize() {
        return nodes.size();
    }

    public void addPoint(Point p) {
        Node n = new Node(p);
        nodes.addLast(n);
    }

    public boolean contains(Point thisp) {
        int i = 0;

        if (nodes.size() == 0) return false;

        while (i < nodes.size()) {
            if (nodes.get(i).point.equals(thisp)) {
                return true;
            }
            ++i;
        }
        return false;
    }

    public void find_nearest(Node a) {
        nearest.addLast(a.point);
        a.visited = true;
        Node next = null;

        int i = 0;
        int nextCandidate = 0;
        int flag = 0;
        double distance = 0;

        while (i < nodes.size()) {
            Node T = nodes.get(i);
            if (!T.equals(a)) {                           //para nao pegar nele próprio
                if (!T.visited) {                           //ainda nao foi visitado
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
            System.out.println("vai para " + nextCandidate);
            find_nearest(next);
        } else {
            nearest.addLast(nearest.get(0));
        }
    }
}