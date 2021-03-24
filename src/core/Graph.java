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
    public LinkedList<Node> nearest;
    public LinkedList<LinkedList<Node>> candidates;

    public Graph() {
        nodes = new LinkedList<Node>();
        nearest = new LinkedList<Node>();
    }

    /**
     * 0 --> colinear
     * 1 --> clockwise
     * 2 --> counter clockwise
     */
    public int checkOrientation(Node n1_start, Node n1_end, Node wildcard) {
        return (int) ((n1_end.point.getY() - n1_start.point.getY()) *
                (wildcard.point.getX() - n1_end.point.getX()) *
                (wildcard.point.getY() - n1_end.point.getY()));
    }

    static boolean onSegment(Point2D p, Point2D q, Point2D r) {
        return q.getX() <= Math.max(p.getX(), r.getX()) && q.getX() >= Math.min(p.getX(), r.getX()) &&
                q.getY() <= Math.max(p.getY(), r.getY()) && q.getY() >= Math.min(p.getY(), r.getY());
    }

    boolean checkIntersect(Node s1_start, Node s1_end, Node s2_start, Node s2_end) {
        int o1 = checkOrientation(s1_start, s1_end, s2_start);
        int o2 = checkOrientation(s1_start, s1_end, s2_end);
        int o3 = checkOrientation(s2_start, s2_end, s1_start);
        int o4 = checkOrientation(s2_start, s2_end, s1_end);

        if (o1 != o2 && o3 != o4)
            return true;

        if (o1 == 0 && onSegment(s1_start.point, s2_start.point, s1_end.point)) return true;

        // p1, q1 and q2 are colinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(s1_start.point, s2_end.point, s1_end.point)) return true;

        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(s2_start.point, s1_start.point, s2_end.point)) return true;

        // p2, q2 and q1 are colinear and q1 lies on segment p2q2
        return o4 == 0 && onSegment(s2_start.point, s1_end.point, s2_end.point);
    }

    public void toExchange() {

    }

    public int getSize() {
        return nodes.size();
    }

    public void addPoint(Point p) {   //add a node with a point
        Node n = new Node(p);
        nodes.addLast(n);
    }


    public void addPointNearest(Point p) {   //add a node with a point
        Node n = new Node(p);
        nearest.addLast(n);
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

        a.visited = true;
        int flag = 0;
        double distance = 0;                               ///
        Node proximo = null;                               ///
        int i = 0;
        int elimina = 0;

        while (i < nodes.size()) {
            Node T = nodes.get(i);
            if (!T.equals(a)) {                           //para nao pegar nele próprio
                if (!T.visited) {                           //ainda nao foi visitado
                    if (flag == 0) {
                        flag = 1;
                        elimina = i;
                        distance = a.point.distance(T.point);
                        //System.out.println("yooo " +T);
                        proximo = nodes.get(i);
                    } else {
                        if (a.point.distance(T.point) < distance) {
                            distance = a.point.distance(T.point);
                            proximo = nodes.get(i);
                            elimina = i;
                            //System.out.println("é o indice " +i);
                        }
                    }
                }
                //System.out.println("yooo" +T);
            }
            ++i;
        }

        if (flag == 1) {
            //addPointNearest(proximo.p);
            System.out.println("vai para " + elimina);
            find_nearest(proximo);
        }/*
        else{
            addLink(a, inicial);
        }*/
    }


}