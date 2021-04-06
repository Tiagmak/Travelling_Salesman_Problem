import java.awt.*;
import java.util.LinkedList;

public class Utils {

  int getItxns(LinkedList<Point> list) {
    return list.size();
  }

  double getPerimeter(LinkedList<Point> list) {
    double distance = 0;
    for (int i = 0; i < list.size(); ++i) {
      distance += list.get(i).distance(list.get((i + 1) % list.size()));
    }

    return distance;
  }

  public boolean isItxn(Point a_og, Point b_og, Point c_og, Point d_og) {
    double distanceAB, cosine, sine, newX, ABpos;
    Point a = (Point) a_og.clone();
    Point b = (Point) b_og.clone();
    Point c = (Point) c_og.clone();
    Point d = (Point) d_og.clone();

    double point_ax = a.getX();
    double point_ay = a.getY();
    double point_bx = b.getX();
    double point_by = b.getY();
    double point_cx = c.getX();
    double point_cy = c.getY();
    double point_dx = d.getX();
    double point_dy = d.getY();

    //  Fail if either line segment is zero-length.
    if (point_ax == point_bx && point_ay == point_by
        || point_cx == point_dx && point_cy == point_dy) return false;

    //  Fail if the segments share an end-point.
    if (point_ax == point_cx && point_ay == point_cy
        || point_bx == point_cx && point_by == point_cy
        || point_ax == point_dx && point_ay == point_dy
        || point_bx == point_dx && point_by == point_dy) {
      return false;
    }

    //  (1) Translate the system so that point A is on the origin.
    point_bx -= point_ax;
    point_by -= point_ay;
    point_cx -= point_ax;
    point_cy -= point_ay;
    point_dx -= point_ax;
    point_dy -= point_ay;

    //  Discover the length of segment A-B.
    distanceAB = Math.sqrt(point_bx * point_bx + point_by * point_by);

    // Rotate the system so that point B is on the positive X axis.
    cosine = point_bx / distanceAB;
    sine = point_by / distanceAB;
    newX = point_cx * cosine + point_cy * sine;
    point_cy = point_cy * cosine - point_cx * sine;
    point_cx = newX;
    newX = point_dx * cosine + point_dy * sine;
    point_dy = point_dy * cosine - point_dx * sine;
    point_dx = newX;

    //  Fail if segment C-D doesn't cross line A-B.
    if (point_cy < 0. && point_dy < 0. || point_cy >= 0. && point_dy >= 0.) return false;

    // Discover the position of the intersection point along line A-B.
    ABpos = point_dx + (point_cx - point_dx) * point_dy / (point_dy - point_cy);

    //  Fail if segment C-D crosses line A-B outside of segment A-B.
    return !(ABpos < 0.) && !(ABpos > distanceAB);
  }

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
}
