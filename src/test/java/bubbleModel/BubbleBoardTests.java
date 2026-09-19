package bubbleModel;

import model.BubbleBoard;
import grid.CellCoordinate;
import grid.Grid;
import grid.GridCell;
import bubble.Bubble;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

/**
 * Tester klassen BubbleBoard
 */
public class BubbleBoardTests {
	
	@Test
	void boardConstructurTest() {
		Grid grid = new Grid(18, 17, '-');
		BubbleBoard board = new BubbleBoard(grid);
		int numRows = 0;
		int numCols = 0;
		ArrayList<Character> legalSymbols = new ArrayList<>(Arrays.asList('-','L','G','P','B','R','Y'));
		for (GridCell cell : board) {
			assertTrue(legalSymbols.contains(cell.symbol()));
			if (cell.pos().row() == 0) {
				numCols += 1;
			}
			if (cell.pos().col() == 0) {
				numRows += 1;
			}
			if (cell.pos().row() < 9) {
				ArrayList<Character> prePlacedBubbles = new ArrayList<>(Arrays.asList('L','G','P','B','R','Y'));
				assertTrue(prePlacedBubbles.contains(cell.symbol()));
			}
			if (cell.pos().row() >= 9) {
				assertTrue(cell.symbol() == '-');
			}
		}
		assertTrue(numRows == 18);
		assertTrue(numCols == 17);
		Grid grid1 = new Grid(18, 18, '-');
		assertThrows(IllegalArgumentException.class, ()-> new BubbleBoard(grid1));
	}
	
	@Test
	void getCellTest() {
		Grid grid = new Grid(18, 17, '-');
		BubbleBoard board = new BubbleBoard(grid);
		GridCell cell = new GridCell(new CellCoordinate(13, 10), 'L');
		board.setCellOnBoard(cell.pos(), cell.symbol());
		GridCell cell1 = board.getCell(new CellCoordinate(13, 10));
		assertEquals(cell1.pos().row(), cell.pos().row());
		assertEquals(cell1.pos().col(), cell.pos().col());
		assertEquals(cell1.symbol(), cell.symbol());
		assertThrows(IllegalArgumentException.class, ()-> board.getCell(null));
		assertThrows(IndexOutOfBoundsException.class, ()-> board.getCell(new CellCoordinate(-1, 2)));	
	}
	
	@Test
	void placeBubbleOnBoardTest() {
		Grid grid = new Grid(18, 17, '-');
		BubbleBoard board = new BubbleBoard(grid);
		GridCell cell = new GridCell(new CellCoordinate(2, 2), 'L');
		assertThrows(IllegalArgumentException.class, ()-> board.setCellOnBoard(cell.pos(), cell.symbol()));
		GridCell cell1 = new GridCell(new CellCoordinate(2, -1), 'L');
		assertThrows(IndexOutOfBoundsException.class, ()-> board.setCellOnBoard(cell1.pos(), cell1.symbol()));
		GridCell cell2 = new GridCell(new CellCoordinate(12, 4), 'l');
		assertThrows(IllegalArgumentException.class, ()-> board.setCellOnBoard(cell2.pos(), cell2.symbol()));
		GridCell cell3 = new GridCell(new CellCoordinate(11, 4), 'L');
		board.setCellOnBoard(cell3.pos(), cell3.symbol());
		assertEquals('L', grid.getSymbolFromCellCoordinate(cell3.pos()));
	}
	
	@Test
	void getCellsAroundCellTest() {
		Grid grid = new Grid(18, 17, '-');
		BubbleBoard board = new BubbleBoard(grid);
		//sjekk for partallsRad
		GridCell cellEven = new GridCell(new CellCoordinate(12, 10), 'B');
		board.setCellOnBoard(cellEven.pos(), cellEven.symbol());
		
		board.setCellOnBoard(new CellCoordinate(11, 10), 'R');
		board.setCellOnBoard(new CellCoordinate(11, 9), 'R');
		board.setCellOnBoard(new CellCoordinate(12, 9), 'R');
		board.setCellOnBoard(new CellCoordinate(12, 11), 'R');
		board.setCellOnBoard(new CellCoordinate(13, 10), '-');
		board.setCellOnBoard(new CellCoordinate(13, 9), 'R');
		ArrayList<GridCell> cellsAroundEven = board.getCellsAroundCell(cellEven);
		for (GridCell cellToCheck : cellsAroundEven) {
			if(cellToCheck.pos().row() == 13 & cellToCheck.pos().col() == 10) {
				assertEquals('-', cellToCheck.symbol());
			}
			else {
				assertEquals ('R', cellToCheck.symbol());
			}
		}
		assertTrue(cellsAroundEven.size() == 6);
		
		//sjekk for oddetallsRad helt til høyereVegg
		board = new BubbleBoard(new Grid(18, 17, '-'));
		GridCell cellOdd = new GridCell(new CellCoordinate(13, 16), 'B');
		board.setCellOnBoard(cellOdd.pos(), cellOdd.symbol());

		board.setCellOnBoard(new CellCoordinate(12, 16), 'R');
		board.setCellOnBoard(new CellCoordinate(14, 16), 'R');
		board.setCellOnBoard(new CellCoordinate(13, 15), 'P');
		ArrayList<GridCell> cellsAroundOdd = board.getCellsAroundCell(cellOdd);
		for (GridCell cellToCheck : cellsAroundOdd) {
			if(cellToCheck.pos().row() == 13 & cellToCheck.pos().col() == 15) {
				assertEquals('P', cellToCheck.symbol());
			}
			else {
				assertEquals ('R', cellToCheck.symbol());
			}
		}
		assertTrue(cellsAroundOdd.size() == 3);
	}
	
	@Test
	void clearBoardDontRemoveWrongChainsTest() {
		Grid grid = new Grid(18, 17, '-');
		BubbleBoard board = new BubbleBoard(grid, '-');
		assertThrows(IllegalArgumentException.class, ()-> board.clearBoard(null));
		GridCell cell1 = new GridCell(new CellCoordinate(0, 0), 'B');
		GridCell cell2 = new GridCell(new CellCoordinate(1, 0), 'R');
		Bubble lastPlacedBubble = new Bubble('R');
		lastPlacedBubble.changePos(new CellCoordinate(2, 0));
		board.setCellOnBoard(cell1.pos(), cell1.symbol());
		board.setCellOnBoard(cell2.pos(), cell2.symbol());
		board.setCellOnBoard(lastPlacedBubble.getPos(), lastPlacedBubble.getSymbol());
		board.clearBoard(lastPlacedBubble);
		assertTrue('B' == board.getCell(cell1.pos()).symbol());
		assertTrue('R' == board.getCell(cell2.pos()).symbol());
		assertTrue('R' == board.getCell(lastPlacedBubble.getPos()).symbol());
	}
	
	@Test
	void clearBoardRemovesChainsCorectTest() {
		Grid grid = new Grid(18, 17, '-');
		BubbleBoard board = new BubbleBoard(grid, '-');
		//Oppretter chains
		GridCell cell1 = new GridCell(new CellCoordinate(0, 0), 'R');
		GridCell cell2 = new GridCell(new CellCoordinate(1, 0), 'R');
		GridCell cell3 = new GridCell(new CellCoordinate(0, 1), 'R');
		GridCell cell4 = new GridCell(new CellCoordinate(0, 5), 'B');
		Bubble lastPlacedBubble = new Bubble('R');
		lastPlacedBubble.changePos(new CellCoordinate(2, 0));
		board.setCellOnBoard(cell1.pos(), cell1.symbol());
		board.setCellOnBoard(cell2.pos(), cell2.symbol());
		board.setCellOnBoard(cell3.pos(), cell3.symbol());
		board.setCellOnBoard(cell4.pos(), cell4.symbol());
		board.setCellOnBoard(lastPlacedBubble.getPos(), lastPlacedBubble.getSymbol());
		//Clearer board
		board.clearBoard(lastPlacedBubble);
		assertTrue('-' == board.getCell(cell1.pos()).symbol());
		assertTrue('-' == board.getCell(cell2.pos()).symbol());
		assertTrue('-' == board.getCell(cell3.pos()).symbol());
		assertTrue('B' == board.getCell(cell4.pos()).symbol());
		assertTrue('-' == board.getCell(lastPlacedBubble.getPos()).symbol());
		
	}
	
	@Test
	void clearBoardRemovesAirBubblesTest() {
		Grid grid = new Grid(18, 17, '-');
		BubbleBoard board = new BubbleBoard(grid, '-');
		
		GridCell cell1 = new GridCell(new CellCoordinate(0, 4), 'R');
		GridCell cell2 = new GridCell(new CellCoordinate(0, 5), 'R');
		GridCell cell3 = new GridCell(new CellCoordinate(1, 3), 'R');
		GridCell cell4 = new GridCell(new CellCoordinate(1, 4), 'B');
		Bubble lastPlacedBubble = new Bubble('R');
		lastPlacedBubble.changePos(new CellCoordinate(1, 5));
		
		board.setCellOnBoard(cell1.pos(), cell1.symbol());
		board.setCellOnBoard(cell2.pos(), cell2.symbol());
		board.setCellOnBoard(cell3.pos(), cell3.symbol());
		board.setCellOnBoard(cell4.pos(), cell4.symbol());
		board.setCellOnBoard(lastPlacedBubble.getPos(), lastPlacedBubble.getSymbol());
		board.clearBoard(lastPlacedBubble);
		assertTrue('-' == board.getCell(cell1.pos()).symbol());
		assertTrue('-' == board.getCell(cell2.pos()).symbol());
		assertTrue('-' == board.getCell(cell3.pos()).symbol());
		assertTrue('-' == board.getCell(cell4.pos()).symbol());
		assertTrue('-' == board.getCell(lastPlacedBubble.getPos()).symbol());
	}
	
	@Test 
	void addNewRowTest(){
		Grid grid = new Grid(18, 17, '-');
		BubbleBoard board = new BubbleBoard(grid, '-');
		GridCell cell1 = new GridCell(new CellCoordinate(0, 2), 'R');
		GridCell cell2 = new GridCell(new CellCoordinate(0, 3), 'R');
		GridCell cell3 = new GridCell(new CellCoordinate(3, 4), 'B');
		board.setCellOnBoard(cell1.pos(), cell1.symbol());
		board.setCellOnBoard(cell2.pos(), cell2.symbol());
		board.setCellOnBoard(cell3.pos(), cell3.symbol());
		board.addNewTopRow();
		for (GridCell cell : board) {
			if(cell.pos().row() == 0) {
				assertTrue (cell.symbol() != '-');
			}
		}
		assertTrue(board.getCell(new CellCoordinate(1,2)).symbol() == 'R');
		assertTrue(board.getCell(new CellCoordinate(1,3)).symbol() == 'R');
		assertTrue(board.getCell(new CellCoordinate(4,4)).symbol() == '-');
	}
}