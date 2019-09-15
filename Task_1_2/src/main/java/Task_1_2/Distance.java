package Task_1_2;

public class Distance implements Comparable<Distance> {
  private boolean inf;
  private int weight;

  /** Construct infinite Distance. */
  public Distance() {
    inf = true;
    weight = 0;
  }

  /**
   * Counstruct finite Distance.
   *
   * @param wght distance value
   */
  public Distance(int wght) {
    weight = wght;
    inf = false;
  }

  /**
   * Copy constructor.
   *
   * @param d src Distance
   */
  public Distance(Distance d) {
    weight = d.weight;
    inf = d.inf;
  }

  public void setWeight(int wght) {
    weight = wght;
    inf = false;
  }

  /**
   * Get integer weight. Returns indefinite value if Distance is infinite.
   *
   * @return integer distance
   */
  public int getWeight() {
    return weight;
  }

  /**
   * Checks if Distance is infinite.
   *
   * @return true if infinite, false otherwise
   */
  public boolean isInf() {
    return inf;
  }

  /**
   * Make Distance finite or infinite.
   *
   * @param _inf true to set infinite, false to finite
   */
  public void setInf(boolean _inf) {
    inf = _inf;
  }

  /**
   * Add two Distance objects.
   *
   * @param other sum argument
   * @return sum result
   */
  public Distance add(Distance other) {
    Distance res = new Distance();
    if (!inf && !other.inf) res.setWeight(weight + other.weight);
    return res;
  }

  /**
   * Distance compare function.
   *
   * @param otherDist comparison argument
   * @return positive if this is more, zero if equal and negative if less
   */
  public int compareTo(Distance otherDist) {
    if (inf && otherDist.inf) return 0;
    if (inf) return 1;
    if (otherDist.inf) return (-1);
    return weight - otherDist.weight;
  }

  /**
   * Minimum of two Distances.
   *
   * @param a first Distance
   * @param b second Distance
   * @return minimum if a and b
   */
  public static Distance min(Distance a, Distance b) {
    if (a.compareTo(b) > 0) return b;
    return a;
  }

  /**
   * Maximum of two Distances.
   *
   * @param a first Distance
   * @param b second Distance
   * @return maximum if a and b
   */
  public static Distance max(Distance a, Distance b) {
    if (a.compareTo(b) > 0) return a;
    return b;
  }

  public String toString() {
    if (inf) return "inf";
    return Integer.toString(weight);
  }
}
