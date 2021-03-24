import java.awt.*;
import java.awt.geom.Point2D;
import java.util.LinkedList;

// Classe que representa um no
class Node {
    //public LinkedList<Node> adj;
    public boolean visited;
    //public int distance;
    Point2D point;

    Node(Point p) {
        //this.adj = new LinkedList<Node>();
        this.point = p;
    }


}

public class Graph {
    public LinkedList<Node> nodes;
    public LinkedList<Point2D> nearest;
    public LinkedList<LinkedList<Point2D>> candidates;

    public Graph() {
        nodes = new LinkedList<>();
        nearest = new LinkedList<>();
        candidates = new LinkedList<>();
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

    /**
     * 0 --> colinear
     * 1 --> clockwise
     * 2 --> counter clockwise
     */
    public int checkOrientation(Point2D n1_start, Point2D n1_end, Point2D wildcard) {
        return (int) ((n1_end.getY() - n1_start.getY()) *
                (wildcard.getX() - n1_end.getX()) *
                (wildcard.getY() - n1_end.getY()));
    }

    static boolean onSegment(Point2D p, Point2D q, Point2D r) {
        return q.getX() <= Math.max(p.getX(), r.getX()) && q.getX() >= Math.min(p.getX(), r.getX()) &&
                q.getY() <= Math.max(p.getY(), r.getY()) && q.getY() >= Math.min(p.getY(), r.getY());
    }

    boolean checkIntersect(Point2D s1_start, Point2D s1_end, Point2D s2_start, Point2D s2_end) {
        int o1 = checkOrientation(s1_start, s1_end, s2_start);
        int o2 = checkOrientation(s1_start, s1_end, s2_end);
        int o3 = checkOrientation(s2_start, s2_end, s1_start);
        int o4 = checkOrientation(s2_start, s2_end, s1_end);

        if (o1 != o2 && o3 != o4)
            return true;

        if (o1 == 0 && onSegment(s1_start, s2_start, s1_end)) return true;

        // p1, q1 and q2 are colinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(s1_start, s2_end, s1_end)) return true;

        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(s2_start, s1_start, s2_end)) return true;

        // p2, q2 and q1 are colinear and q1 lies on segment p2q2
        return o4 == 0 && onSegment(s2_start, s1_end, s2_end);
    }

    // reverse bs need to work here
    LinkedList<Point2D> checkCase(Point2D s1_start, Point2D s1_end, Point2D s2_start, Point2D s2_end) {
        LinkedList<Point2D> candidate = new LinkedList<>();
        candidate = (LinkedList<Point2D>) nearest.clone();

        // if s1 -> e2 and s2 -> e1 already exist do second case
        if ((nearest.indexOf(s1_start) == (nearest.indexOf(s2_start) - 1)) && nearest.indexOf(s1_end) == (nearest.indexOf(s2_end) - 1)) {
            // switch indexes
            int to_advance_index = candidate.indexOf(s1_end);
            int to_previous_index = candidate.indexOf(s2_start);
            Point2D advance = candidate.get(to_advance_index);
            Point2D previous = candidate.get(to_previous_index);
            candidate.remove(to_advance_index);
            candidate.remove(to_previous_index);
            candidate.add(to_advance_index, advance);
            candidate.add(to_previous_index, previous);
        }

        return candidate;
    }

    int genCandidate(int i) {
        Point2D s1, s2, e1, e2;
        s1 = nearest.get(i);

        for (int j = i; j < nearest.size() - 3; ++j) {
            e1 = nearest.get(j + 1);
            s2 = nearest.get(j + 2);
            e2 = nearest.get(j + 3);

            if (checkIntersect(s1, e1, s2, e2)) {
                candidates.addLast(checkCase(s1, e1, s2, e2));
            }
        }

        return ++i;
    }

    public void toExchange() {
        int i = 0;

        while (i < nearest.size()) {
            i = genCandidate(i);
        }
    }

    public int getSize() {
        return nodes.size();
    }

    public void addPoint(Point p) {   //add a node with a point
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