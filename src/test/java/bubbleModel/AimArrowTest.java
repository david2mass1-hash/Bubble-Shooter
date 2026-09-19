package bubbleModel;

import model.AimArrow;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.geom.Point2D;

import org.junit.jupiter.api.Test;

/**
 * Tester klassen AimArrow
 */
public class AimArrowTest {
	
	@Test
	void setAngleTest() {
		//Ugyldig input endrer ikke vinkele.
		AimArrow arrow = new AimArrow(-Math.PI / 2);
		arrow.setAngle(Math.PI / 2);
		assertTrue(arrow.getAngle() != Math.PI / 2);
		//Gyldig input endrer vinkelen.
		arrow.setAngle(-Math.PI / 4);
		assertTrue(arrow.getAngle() == (-Math.PI / 4));
	}
	
	@Test
	void getUnitVectorTest() {
		AimArrow arrow = new AimArrow(-Math.PI / 2);
		Point2D.Double expectedUnitVector = new Point2D.Double(0, -1);
		Point2D.Double actualUnitVector = arrow.getUnitVector(-Math.PI / 2);
		assertTrue((int) expectedUnitVector.getX() == (int) actualUnitVector.getX());
		assertTrue((int) expectedUnitVector.getY() == (int) actualUnitVector.getY());
	}
	
	@Test
	void calculateVectorEndPointTest() {
		AimArrow arrow = new AimArrow(-Math.PI / 2);
		Point2D.Double unitVector = arrow.getUnitVector(-Math.PI / 2);
		Point2D.Double bubbleToCalculateFrom = new Point2D.Double(0,4);
		Point2D.Double expectedEndPoint = new Point2D.Double(0,2);
		Point2D.Double actualEndPoint = arrow.calculateVectorEndPoint(bubbleToCalculateFrom,
				unitVector, 2);
		assertTrue((int) expectedEndPoint.getX() == (int) actualEndPoint.getX());
		assertTrue((int) expectedEndPoint.getY() == (int) actualEndPoint.getY());
		assertThrows(IllegalArgumentException.class, ()-> arrow.calculateVectorEndPoint(bubbleToCalculateFrom, 
				unitVector, 0));
		assertThrows(IllegalArgumentException.class, ()-> arrow.calculateVectorEndPoint(null, 
				unitVector, 2));
		assertThrows(IllegalArgumentException.class, ()-> arrow.calculateVectorEndPoint(bubbleToCalculateFrom, 
				null, 2));
	}

}
