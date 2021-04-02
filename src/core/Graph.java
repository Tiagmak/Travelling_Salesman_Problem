import java.awt.Point;
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
    int lowestPerimeterIndex, lowestPerimeter = (int) Math.pow(10, 9);

    public Graph(int n) {
        nodes = new LinkedList<>();
        nearest = new LinkedList<>();
        lowestPerimeterIndex = n;
    }

    public void addPoint(Point p) {
        Node n = new Node(p);
        nodes.addLast(n);
    }

    /**
     * @param n   - number of points
     * @param i   - index
     * @param min - minimum range
     * @param max - max range
     */
    public void graphRandom(int n, int i, int min, int max) {
        if (i == n) return;

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

    public void toExchange() {
        candidates = new LinkedList<>();
        Point s1, s2, e1, e2;

        for (int i = 0; i < nearest.size() - 3; i++) {
            s1 = nearest.get(i);
            ++i;
            e1 = nearest.get(i);

            for (int j = 0; j < nearest.size() - 1; j++) {
                s2 = nearest.get(j);
                ++j;
                e2 = nearest.get(j);

                if (s1.equals(s2) && e1.equals(e2)) continue;

                if (doIntersect(s1, e1, s2, e2)) {
                    checkCase(s1, e1, s2, e2);
                    checkPerimeter(s1, e1, s2, e2);
                }
            }
        }
    }

    public void nearest() {
        int randA = (int) (Math.random() * (getSize()));
        Node a = nodes.get(randA);

        findNearest(a);
    }

    private int getSize() {
        return nodes.size();
    }

    // To find orientation of ordered triplet (p, q, r).
    // The function returns following values
    // 0 --> p, q and r are colinear
    // 1 --> Clockwise
    // 2 --> Counterclockwise
    private int orientation(Point p, Point q, Point r) {
        // See https://www.geeksforgeeks.org/orientation-3-ordered-points/
        // for details of below formula.
        int val = (q.y - p.y) * (r.x - q.x) -
                (q.x - p.x) * (r.y - q.y);

        if (val == 0) return 0; // colinear

        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    // Given three colinear points p, q, r, the function checks if
    // point q lies on line segment 'pr'
    private boolean onSegment(Point p, Point q, Point r) {
        return q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
                q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y);
    }

    // The main function that returns true if line segment 'p1q1'
    // and 'p2q2' intersect.
    private boolean doIntersect(Point p1, Point q1, Point p2, Point q2) {
        // Find the four orientations needed for general and
        // special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        // General case
        if (o1 != o2 && o3 != o4)
            return true;

        // Special Cases
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;

        // p1, q1 and q2 are colinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;

        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;

        // p2, q2 and q1 are colinear and q1 lies on segment p2q2
        return o4 == 0 && onSegment(p2, q1, q2);// Doesn't fall in any of the above cases
    }

    private void checkCase(Point a, Point b, Point c, Point d) {
        LinkedList<Point> candidate = new LinkedList<>();
        for (Point p : nearest) {
            candidate.addLast(p);
        }

        Point b_copy = (Point) nearest.get(nearest.indexOf(b)).clone();

        boolean flip = false;
        for (int i = 0; i < nearest.size(); ++i) {
            Point cur = (Point) nearest.get(i).clone();

            if (flip) {
                if (cur.equals(c)) {
                    candidate.set(i, b_copy);
                    flip = false;
                } else {
                    candidate.set(nearest.size() - (i + 1), cur);
                    candidate.set(i, nearest.get(nearest.size() - (i + 1)));
                }
            } else if (cur.equals(b) && i != nearest.size() - 1) {
                flip = true;
                b_copy = (Point) nearest.get(i).clone();
                candidate.set(i, c);
            }
        }

        candidates.addLast(candidate);
    }

    private void checkPerimeter(Point a, Point b, Point c, Point d) {
        int index = candidates.size();

        // get out
        double distanceA1 = a.distance(b);
        double distanceA2 = c.distance(d);
        // get in
        double distanceA3 = a.distance(c);
        double distanceA4 = b.distance(d);

        int deltaPerimeter = (int) (Math.pow(distanceA3 + distanceA4, 2) - Math.pow(distanceA1 + distanceA2, 2));

        if (deltaPerimeter < lowestPerimeter) {
            lowestPerimeterIndex = index - 1;
        }
    }

    public boolean contains(Point givenPoint) {
        int i = 0;

        if (nodes.size() == 0) return false;

        while (i < nodes.size()) {
            if (nodes.get(i).point.equals(givenPoint)) {
                return true;
            }
            ++i;
        }
        return false;
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
            findNearest(next);
        } else {
            nearest.addLast(nearest.get(0));
        }
    }

    public String printNearest() {
        StringBuilder s = new StringBuilder();
        for (Point p : nearest) {
            s.append("(").append((int) p.getX()).append(",").append((int) p.getY()).append(")");
        }
        s.append("\n");

        return s.toString();
    }

    public void printCandidates() {
        for (LinkedList<Point> L : candidates) {
            for (Point p : L) {
                System.out.print("(" + (int) p.getX() + "," + (int) p.getY() + ")");
            }
            System.out.println("\n");
        }
    }

    public void printLeastPerimeter() {
        for (LinkedList<Point> L : candidates) {
            if (L.equals(candidates.get(lowestPerimeterIndex))) {
                for (Point p : L) {
                    System.out.print("(" + (int) p.getX() + "," + (int) p.getY() + ")");
                }
                System.out.println("\n");
            }
        }
    }
}