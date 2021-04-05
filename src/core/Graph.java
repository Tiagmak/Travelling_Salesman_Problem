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
    public LinkedList<LinkedList<Point>> candidates;
    int size = 0;

    public Graph(int size) {
        nodes = new LinkedList<>();
        nearest = new LinkedList<>();
        this.size = size;
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

    ///////////////////////////////////////////////////////////////////////
    // GENERATING NEAREST NEIGHBOUR
    ///////////////////////////////////////////////////////////////////////
    public void nearest() {
        int randA = (int) (Math.random() * (size));
        Node a = nodes.get(randA);

        findNearest(a);
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

    ///////////////////////////////////////////////////////////////////////
    // CHECKING INTERSECTIONS
    ///////////////////////////////////////////////////////////////////////
    public boolean checkIntersection(LinkedList<Point> list, int a, int c) {
        double Ax = list.get(a).x;
        double Bx = list.get(a + 1).x;
        double Cx = list.get(c).x;
        double Dx = list.get(c + 1).x;
        double Ay = list.get(a).y;
        double By = list.get(a + 1).y;
        double Cy = list.get(c).y;
        double Dy = list.get(c + 1).y;

        double distAB, theCos, theSin, newX, ABpos;

        //  Fail if either line segment is zero-length.
        if (Ax == Bx && Ay == By || Cx == Dx && Cy == Dy) return false;

        //  Fail if the segments share an end-point.
        if (Ax == Cx && Ay == Cy || Bx == Cx && By == Cy
                || Ax == Dx && Ay == Dy || Bx == Dx && By == Dy) {
            return false;
        }

        //  (1) Translate the system so that point A is on the origin.
        Bx -= Ax;
        By -= Ay;
        Cx -= Ax;
        Cy -= Ay;
        Dx -= Ax;
        Dy -= Ay;

        //  Discover the length of segment A-B.
        distAB = Math.sqrt(Bx * Bx + By * By);

        //  (2) Rotate the system so that point B is on the positive X axis.
        theCos = Bx / distAB;
        theSin = By / distAB;
        newX = Cx * theCos + Cy * theSin;
        Cy = Cy * theCos - Cx * theSin;
        Cx = newX;
        newX = Dx * theCos + Dy * theSin;
        Dy = Dy * theCos - Dx * theSin;
        Dx = newX;

        //  Fail if segment C-D doesn't cross line A-B.
        if (Cy < 0. && Dy < 0. || Cy >= 0. && Dy >= 0.) return false;

        //  (3) Discover the position of the intersection point along line A-B.
        ABpos = Dx + (Cx - Dx) * Dy / (Dy - Cy);

        //  Fail if segment C-D crosses line A-B outside of segment A-B.
        return !(ABpos < 0.) && !(ABpos > distAB);
    }

    public LinkedList<Point> checkCase(LinkedList<Point> list, int a, int c) {
        int perimeterFirst;
        int perimeterSecond;

        int b = a + 1;
        int d = c + 1;
        Point point_a = list.get(a);
        Point point_b = list.get(b);
        Point point_c = list.get(c);
        Point point_d = list.get(d);

        /* FIRST OPTION */
        // A B C D => A C B D
        LinkedList<Point> candidate = new LinkedList<>();
        int i;
        for (i = 0; i <= a; ++i) {
            candidate.addLast(list.get(i));
        }
        for (i = c; i >= b; --i) {
            candidate.addLast(list.get(i));
        }
        for (i = d; i < list.size(); ++i) {
            candidate.addLast(list.get(i));
        }

        perimeterFirst = checkPerimeter(true, point_a, point_b, point_c, point_d);

        /* SECOND OPTION */
        // A B C D => A C B D => A C D B => A D C B

        LinkedList<Point> candidate2 = new LinkedList<>();
        for (Point p : candidate) {
            candidate2.addLast(p);
        }

        Point tmp = candidate2.get(b);
        Point tmp2 = candidate2.get(c);

        candidate2.set(b, candidate2.get(d));
        candidate2.set(c, tmp);
        candidate2.set(d, tmp2);

        if (d == list.size() - 1) {
            candidate2.set(0, tmp2);
        }

        perimeterSecond = checkPerimeter(false, point_a, point_b, point_c, point_d);

        if (perimeterFirst < perimeterSecond) {
            if (!candidates.contains(candidate)) {
                return candidate;
            }
        } else {
            if (!candidates.contains(candidate2)) {
                return candidate2;
            }
        }
        return null;
    }

    public void leastIntersections() {
        if (candidates.peekFirst() == null) {
            return;
        }

        int intersections = checkIntersections(candidates.get(0));
        int index = 0, i = 1;
        int temp;

        for (; i < candidates.size(); ++i) {
            temp = checkIntersections(candidates.get(i));
            if (temp < intersections) {
                intersections = temp;
                index = i;
            }
        }

        toExchange(candidates.get(index));
        if (candidates.size() == 0) {
            toExchange(candidates.get(index));
            return;
        }
        leastIntersections();
    }

    public int checkIntersections(LinkedList<Point> list) {
        int a, c, i, j;
        int counter = 0;

        for (i = 0; i < list.size() - 1; ++i) {
            a = i;
            for (j = i + 2; j < list.size() - 1; ++j) {
                c = j;

                if (!(list.get(a).equals(list.get(c + 1)))) {
                    if (checkIntersection(list, a, c)) {
                        counter++;
                    }
                }
            }
        }

        return counter;
    }

    public void toExchange(LinkedList<Point> list) {
        // Tell garbage collector to free whatever memory is not being used
        System.gc();
        candidates = new LinkedList<>();
        int a, c, i, j;

        for (i = 0; i < size; ++i) {
            for (j = i + 2; j < size; ++j) {
                a = Math.min(i, j);
                c = Math.max(i, j);

                if (checkIntersection(list, a, c)) {
                    candidates.addLast(checkCase(list, a, c));
                }
            }
        }
        if (candidates.peekFirst() == null) {
            System.out.println(listToString(list));
        }
    }

    ///////////////////////////////////////////////////////////////////////
    // GENERATING GRAPH
    ///////////////////////////////////////////////////////////////////////
    public void addPoint(Point p) {
        Node n = new Node(p);
        nodes.addLast(n);
    }

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

    ///////////////////////////////////////////////////////////////////////
    // CHECKING PERIMETER
    ///////////////////////////////////////////////////////////////////////
    public int checkPerimeter(boolean first, Point a, Point b, Point c, Point d) {
        // get out
        double distanceA1 = a.distance(b);
        double distanceA2 = c.distance(d);
        double distanceA3, distanceA4;

        if (first) {
            // get in
            distanceA3 = a.distance(c);
            distanceA4 = b.distance(d);
        } else {
            // get in
            distanceA3 = a.distance(d);
            distanceA4 = c.distance(b);
        }

        return (int) (Math.pow(distanceA3 + distanceA4, 2) - Math.pow(distanceA1 + distanceA2, 2));
    }

    public double getPerimeter(LinkedList<Point> l) {
        double distance = 0;
        for (int i = 0; i <= size; ++i) {
            distance += l.get(i).distance(l.get((i + 1) % size));
        }
        return distance;
    }

    public int getLowestPerimeter() {
        double minPerimeter = getPerimeter(candidates.get(0));
        int index = 0;
        for (LinkedList<Point> l : candidates) {
            double lp = getPerimeter(l);
            if (lp < minPerimeter) {
                minPerimeter = lp;
                index = candidates.indexOf(l);
            }
        }
        return index;
    }

    ///////////////////////////////////////////////////////////////////////
    // STRINGS
    ///////////////////////////////////////////////////////////////////////
    public String listToString(LinkedList<Point> list) {
        StringBuilder s = new StringBuilder();
        Point p;
        int i = 0;
        for (; i <= size; ++i) {
            p = list.get(i);
            s.append("(").append((int) p.getX()).append(",").append((int) p.getY()).append(")");
        }
        s.append("\n\n");

        return s.toString();
    }

    public String candidatesToString() {
        assert(candidates != null);
        StringBuilder s = new StringBuilder();
        int i = 0;

        for (; i < candidates.size(); ++i) {
            s.append(listToString(candidates.get(i)));
        }
        s.append("\n\n");

        return s.toString();
    }
}