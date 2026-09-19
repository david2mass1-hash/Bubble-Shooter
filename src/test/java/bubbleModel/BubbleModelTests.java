package bubbleModel;


import model.BubbleModel;
import grid.Grid;
import grid.GridCell;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import bubble.Bubble;

/**
 * Tester klassen BubbleModel
 */
public class BubbleModelTests {
	
	@Test
	void clockTickDoesNotMoveBubbleTest() {
		//Ikke beveger seg hvis ikke i den ikke er skutt
		BubbleModel model = makeBubbleModel();
		Point2D.Double bubbleStart = model.getCurrentBubblePixelCenter();
		model.clockTick();
		Point2D.Double bubbleAfter = model.getCurrentBubblePixelCenter();
		assertEquals(bubbleStart.getX(), bubbleAfter.getX());
		assertEquals(bubbleStart.getY(), bubbleAfter.getY());
	}
	
	@Test
	void shootCurrentBubbleMovesBubbleTest() {
		BubbleModel model = makeBubbleModel();
		Point2D.Double bubbleStart = model.getCurrentBubblePixelCenter();
		model.shootCurrentBubble(-Math.PI/4);
		int i = 0;
		while (i<10) {
			model.clockTick();
			i += 1;
		}
		Point2D.Double bubbleAfter = model.getCurrentBubblePixelCenter();
		assertTrue(bubbleStart.getX() != bubbleAfter.getX());
		assertTrue(bubbleStart.getY() != bubbleAfter.getY());
	}
	
	@Test
	void wallBounceTest() {
		BubbleModel model = makeBubbleModel();
		double bubbleRadius = model.getCurrentBubbleRadius();
		double xPosBeforeWallBounce = 500 - bubbleRadius - 1;
		double yPosBeforeWallBounce = bubbleRadius * 2 * 14;
		model.setCurrentBubblePixelCenter(new Point2D.Double(xPosBeforeWallBounce, yPosBeforeWallBounce));
		//Skyter den skrått opp mot høyere ved startposisjon 1 pixel fra høyere vegg og tikker klokka 10 ganger.
		model.shootCurrentBubble(-Math.PI / 4);
		int i = 0;
		while ( i < 10) {
			model.clockTick();
			i += 1;
		}
		//sjekker om nævrende x punkt er til venstre for startpunkt.
		assertTrue(model.getCurrentBubblePixelCenter().getX() < xPosBeforeWallBounce);
	}
	
	@Test
	void bubbleStopsWhenItHitsAnotherAndGetsNewTest() {
		BubbleModel model = makeBubbleModel();
		int bubblesOnBoardBefore = 0;
		for (GridCell cell : model.getTilesOnBoard()) {
			if(cell.symbol() != '-') {
				bubblesOnBoardBefore += 1;
			}
		}
		model.shootCurrentBubble(-Math.PI / 2);
		int i = 0;
		while (i < 500) {
			model.clockTick();
			i += 1;
		}
		int bubblesOnBoardAfter = 0;
		for (GridCell cell : model.getTilesOnBoard()) {
			if(cell.symbol() != '-') {
				bubblesOnBoardAfter += 1;
			}
		}
		assertTrue(bubblesOnBoardAfter > bubblesOnBoardBefore);
	}
	
	@Test
	void bubbleMagTest() {
		BubbleModel model = makeBubbleModel();
		int bubblesBeforeShoot = 0;
		for (GridCell cell : model.getTilesOnBoard()) {
			if (cell.symbol() != '-') {
				bubblesBeforeShoot += 1;
			}
		}
		model.shootCurrentBubble(-Math.PI / 2);
		int magSizeBefore = model.getMagSize();
		int i = 0;
		while (i < 100) {
			model.clockTick();
			i += 1;
		}
		int magSizeAfter = model.getMagSize();
		int bubblesAfterShoot = 0;
		for (GridCell cell : model.getTilesOnBoard()) {
			if (cell.symbol() != '-') {
				bubblesAfterShoot += 1;
			}
		}
		if (bubblesBeforeShoot >= bubblesAfterShoot) {
			assertTrue(magSizeBefore == magSizeAfter);
		}
		else {
			assertTrue(magSizeBefore != magSizeAfter);
		}
	}
		
	private BubbleModel makeBubbleModel() {
		Grid grid = new Grid(18, 17, '-');
		TestBubbleFactory factory = new TestBubbleFactory(); 
		BubbleModel model = new BubbleModel(grid, factory);
		model.setTopLeftPixelCorner(new Point2D.Double(0, 0));
		model.setPixelWidth(500);
		model.setPixelHeight(500);
		model.setCurrentBubbleRadius(10);
		return model;
	}
}
