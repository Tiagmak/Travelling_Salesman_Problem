import java.awt.*;
import java.util.LinkedList;

public class Neighbour {
  private final Utils utils;
  public LinkedList<LinkedList<Point>> neighbours;
  public LinkedList<Point> candidate;
  public double min_perimeter;
  public Point p_intxn_prev;
  public Point p_intxn_cur;

  public Neighbour() {
    utils = new Utils();
    neighbours = new LinkedList<>();
    candidate = new LinkedList<>();
    min_perimeter = Double.MAX_VALUE;
    p_intxn_prev = new Point();
    p_intxn_cur = new Point();
  }

  public void add(LinkedList<Point> new_candidate) {
    this.candidate.clear();
    this.candidate = new LinkedList<>();
    this.candidate = new_candidate;
  }

  public void gen() {
    neighbours = new LinkedList<>();
    LinkedList<Point> possible_candidate;
    int a, c;
    boolean last = false;

    for (int i = 0; i <= candidate.size() - 4; ++i) {
      for (int j = i + 1; j <= candidate.size() - 2; ++j) {

        a = i;
        c = j;

        // check 2nd segment is last or not
        last = c == candidate.size() - 2;

        if (utils.isItxn(
            candidate.get(a), candidate.get(a + 1), candidate.get(c), candidate.get(c + 1))) {

          possible_candidate = toChange(candidate, a, c, last);

          if (!neighbours.contains(possible_candidate)) {
            neighbours.addLast(possible_candidate);
          }
        }
      }
    }
  }

  public LinkedList<Point> toChange(LinkedList<Point> list, int a, int c, boolean last) {
    LinkedList<Point> clone = new LinkedList<>();
    for (Point p : list) {
      clone.addLast(p);
    }

    int perimeterFirst;
    int perimeterSecond;

    int tmp_a = a;
    int tmp_c = c;
    a = Math.min(tmp_a, tmp_c);
    c = Math.max(tmp_a, tmp_c);

    int b = a + 1;
    int d = c + 1;
    Point point_a = clone.get(a);
    Point point_b = clone.get(b);
    Point point_c = clone.get(c);
    Point point_d = clone.get(d);

    /* FIRST OPTION */
    // A B C D => A C B D
    LinkedList<Point> candidate1 = new LinkedList<>();
    int i;
    for (i = 0; i <= a; ++i) {
      candidate1.addLast(clone.get(i));
    }
    for (i = c; i >= b; --i) {
      candidate1.addLast(clone.get(i));
    }
    for (i = d; i < list.size(); ++i) {
      candidate1.addLast(clone.get(i));
    }

    perimeterFirst = utils.checkPerimeter(true, point_a, point_b, point_c, point_d);

    /* SECOND OPTION */
    // A B C D => A C B D => A C D B => A D C B
    LinkedList<Point> candidate2 = new LinkedList<>();
    for (Point p : candidate1) {
      candidate2.addLast(p);
    }

    if (!last) {
      candidate2.set(a, (Point) point_a.clone());
      candidate2.set(a + 1, (Point) point_d.clone());
      candidate2.set(c, (Point) point_c.clone());
      candidate2.set(c + 1, (Point) point_b.clone());

    } else {
      candidate2.set(0, (Point) point_b.clone());
      candidate2.set(a, (Point) point_a.clone());
      candidate2.set(a + 1, (Point) point_d.clone());
      candidate2.set(c, (Point) point_c.clone());
      candidate2.set(c + 1, (Point) point_b.clone());
    }

    perimeterSecond = utils.checkPerimeter(false, point_a, point_b, point_c, point_d);

    if (perimeterFirst < perimeterSecond) {
      return candidate1;
    } else {
      return candidate2;
    }
  }

  public void randomCandidate() {
    int rand = (int) (Math.random() * ((neighbours.size())));
    this.candidate = neighbours.get(rand);
  }

  public LinkedList<Point> lowestConflictsCandidate() {
    if (neighbours.peekFirst() == null) {
      return null;
    }

    int itxns = utils.getItxns(neighbours.get(0));
    int index = 0;
    int intxnTemp;

    for (int i = 0; i < neighbours.size(); ++i) {
      intxnTemp = utils.getItxns(neighbours.get(i));
      if (intxnTemp < itxns) {
        itxns = intxnTemp;
        index = i;
      }
    }

    return neighbours.get(index);
  }

  public void lowestPerimeterCandidate() {
    int index = 0;
    int i = 0;
    double lp;
    for (LinkedList<Point> list : neighbours) {
      lp = utils.getPerimeter(list);
      if (lp < min_perimeter) {
        min_perimeter = lp;
        index = i;
      }
      ++i;
    }

    this.candidate = neighbours.get(index);
  }

  String listToString(LinkedList<Point> list) {
    StringBuilder s = new StringBuilder();
    for (Point point : list) {
      s.append("(").append(point.x).append(",").append(point.y).append(")");
    }
    s.append("\n\n");

    return s.toString();
  }

  String candidatesToString() {
    StringBuilder s = new StringBuilder();
    for (LinkedList<Point> list : neighbours) {
      s.append(listToString(list));
      s.append("\n\n");
    }

    return s.toString();
  }
}
