package grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;

import org.junit.jupiter.api.Test;

/**
* Testing the class CellPosition
*/
public class CellCoordinateTest {
  
  @Test
  void sanityTest() {
    CellCoordinate cp = new  CellCoordinate(4, 3);
    assertEquals(4, cp.row());
    assertEquals(3, cp.col());
  }
  
  @Test
  void coordinateEqualityTest() {
	CellCoordinate a = new  CellCoordinate(2, 3);
	CellCoordinate b = new  CellCoordinate(2, 3);
    
    assertFalse(a == b);
    assertTrue(a.equals(b));
    assertTrue(b.equals(a));
    assertTrue(Objects.equals(a, b));
  }
  
  @Test
  void coordinateInequalityTest() {
	  CellCoordinate a = new  CellCoordinate(2, 3);
	  CellCoordinate b = new  CellCoordinate(3, 2);
    
    assertFalse(a == b);
    assertFalse(a.equals(b));
    assertFalse(b.equals(a));
    assertFalse(Objects.equals(a, b));
  }
  
  @Test
  void coordinateHashcodeTest() {
	  CellCoordinate a = new  CellCoordinate(2, 3);
	  CellCoordinate b = new CellCoordinate(2, 3);
    assertTrue(a.hashCode() == b.hashCode());
    
    CellCoordinate c = new  CellCoordinate(100, 100);
    CellCoordinate d = new  CellCoordinate(100, 100);
    assertTrue(c.hashCode() == d.hashCode());
  }
}
