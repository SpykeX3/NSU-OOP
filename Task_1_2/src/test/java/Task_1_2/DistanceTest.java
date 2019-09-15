package Task_1_2;

import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {
  @Test
  public void basicTest() {
    Distance d = new Distance();
    Distance dinf = new Distance();
    Distance d1 = new Distance(1);
    assertTrue(d.isInf());
    assertEquals("inf", d.toString());
    d.setWeight(10);
    assertFalse(d.isInf());
    assertEquals(10, d.getWeight());
    assertEquals("10", d.toString());
    Distance dsumm = d.add(dinf);
    assertTrue(dsumm.isInf());
    assertEquals("inf", dsumm.toString());
    Distance dmin = Distance.min(d1, dsumm);
    assertEquals(dmin.isInf(), d1.isInf());
    assertEquals(0, dmin.compareTo(d1));

    Distance d0 = new Distance(0);
    dmin = Distance.min(dinf, d0.add(dinf));
    assertEquals(dinf, dmin);
    dmin = Distance.min(dinf, dinf.add(d0));
    assertEquals(dinf, dmin);
  }
}
