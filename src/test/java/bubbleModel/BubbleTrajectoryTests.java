package bubbleModel;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import model.AimArrow;
import model.BubbleBoard;
import model.BubbleTrajectory;
import grid.CellCoordinate;
import grid.Grid;
import grid.GridCell;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

/**
 * Tester klassen BubbleTrajectory
 */
public class BubbleTrajectoryTests {
	
	@Test
	void isNextMoveWallBounceTest() {
		BubbleTrajectory  trajectory= makeBubbleTrajectory();
		Point2D.Double testBubble1 = new Point2D.Double(4, 150);
		Point2D.Double testBubble2 = new Point2D.Double(196, 150);
		Point2D.Double testBubble3 = new Point2D.Double(5, 150);
		assertThrows(IllegalArgumentException.class, ()-> trajectory.isNextMoveWallBounce(null));
		assertTrue(trajectory.isNextMoveWallBounce(testBubble1));
		assertTrue(trajectory.isNextMoveWallBounce(testBubble2));
		assertFalse(trajectory.isNextMoveWallBounce(testBubble3));
	}
	
	@Test 
	void isNextMoveBubbleCollisionTest() {
		//Lager tomt brett og setter in boble i celle 0,0.
		Grid grid = new Grid(18, 17, '-');
		BubbleBoard board = new BubbleBoard(grid, '-');
		board.setCellOnBoard(new CellCoordinate(0,0), 'R');
		AimArrow direction = new AimArrow(-Math.PI / 2);
		Rectangle2D.Double gameVindow = new Rectangle2D.Double(0, 0, 200, 200);
		Point2D.Double currentBubble=  new Point2D.Double(100, 180);
		double radius = 5;
		BubbleTrajectory  trajectory= new BubbleTrajectory(direction, board, gameVindow, currentBubble,
						radius);
		Point2D.Double testBubble1 = new Point2D.Double(14, 5);
		Point2D.Double testBubble2 = new Point2D.Double(5, 16);
		assertThrows(IllegalArgumentException.class, ()-> trajectory.isNextMoveBubbleCollision(null));
		assertTrue(trajectory.isNextMoveBubbleCollision(testBubble1));
		assertFalse(trajectory.isNextMoveBubbleCollision(testBubble2));
	}
	
	@Test
	void isNextMoveCellingHitTest() {
		BubbleTrajectory  trajectory= makeBubbleTrajectory();
		Point2D.Double testBubble1 = new Point2D.Double(50, 4);
		Point2D.Double testBubble2 = new Point2D.Double(50, 150);
		assertThrows(IllegalArgumentException.class, ()-> trajectory.isNextMoveCeelingHit(null));
		assertTrue(trajectory.isNextMoveCeelingHit(testBubble1));
		assertFalse(trajectory.isNextMoveCeelingHit(testBubble2));
	}
	
	@Test 
	void calculateBubbleTrajectoryTest() {
		BubbleTrajectory trajectory = makeBubbleTrajectory();
		CellCoordinate targetPos1 = trajectory.calculateBubbleTrajectory(new AimArrow(-Math.PI / 2));
		CellCoordinate targetPos2 = trajectory.calculateBubbleTrajectory(new AimArrow(-Math.PI / 4));
		assertTrue(targetPos1.col() == 9);
		assertTrue(targetPos1.row() == 0);
		assertTrue(targetPos2.col() != 8);
		assertTrue(targetPos2.row() == 0);
		assertThrows(IllegalArgumentException.class, ()-> trajectory.calculateBubbleTrajectory(null));
		assertThrows(IllegalArgumentException.class, ()-> trajectory.calculateBubbleTrajectory(new AimArrow(-Math.PI + 0.19)));
	}
	
	private BubbleTrajectory makeBubbleTrajectory() {
		//Lager tomt brett
		Grid grid = new Grid(18, 17, '-');
		BubbleBoard board = new BubbleBoard(grid, '-');
		AimArrow direction = new AimArrow(-Math.PI / 2);
		Rectangle2D.Double gameVindow = new Rectangle2D.Double(0, 0, 200, 200);
		Point2D.Double currentBubble=  new Point2D.Double(100, 180);
		double radius = 5;
		BubbleTrajectory  trajectory= new BubbleTrajectory(direction, board, gameVindow, currentBubble,
						radius);
		return trajectory;
	}
}
