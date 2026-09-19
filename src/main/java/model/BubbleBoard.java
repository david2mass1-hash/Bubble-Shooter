package model;

import grid.CellCoordinate;
import grid.Grid;
import grid.GridCell;
import grid.RowWiseGridIterator;
import bubble.Bubble;
import grid.GridDimension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Iterator;

/**
 * Representerer spillbrettet i spillet Bubble-Shooter.
 */
public class BubbleBoard implements Iterable<GridCell>, GridDimension{
	
	private Grid grid;
	private final Random random = new Random();
	
	/**
	 * lager et nytt Bubble-Shooter spillbrett.
	 * 
	 * @param grid rutenettet til spillet.
	 * @throws IllegalArgumentException hvis griddet ikke er riktig dimensjoner eller null.
	 */
	public BubbleBoard(Grid grid) {
		if (grid.getNumRows() != 18 || grid.getNumCols() != 17 || grid == null) {
			throw new IllegalArgumentException("grid must be 18x17 and can't be null");
		}
		for (GridCell cell: grid) {
			if (cell.pos().row() < 9) {
				grid.setCell(cell.pos(), randomBubbleSymbol());
			}
			else {
				grid.setCell(cell.pos(), '-');
			}
		}
		this.grid = grid;
	}
	
	/**
	 * Konstuktør som lager et nytt Bubble-Shooter spillbrett som brukes for å teste
	 * brettlogikken men ikke til å spille
	 * 
	 * @param grid rutenettet til spillet.
	 * @param symbol symbolet som skal være i hele brettet. 
	 * @throws IllegalArgumentException hvis griddet ikke er riktig dimensjoner eller null.
	 */
	public BubbleBoard(Grid grid, Character symbol) {
		if (grid.getNumRows() != 18 || grid.getNumCols() != 17 || grid == null || symbol == null || symbol != '-') {
			throw new IllegalArgumentException("grid must be 18x17 and can't be null. "
					+ "symbol can't either be null it must be '-'");
		}
		for (GridCell cell: grid) {
			if (cell.pos().row() < 9) {
				grid.setCell(cell.pos(), symbol);
			}
			else {
				grid.setCell(cell.pos(), symbol);
			}
		}
		this.grid = grid;
	}
	
	@Override
	public Iterator<GridCell> iterator() {
		return new RowWiseGridIterator(this.grid);
	}
	
	@Override
	public int getNumRows() {
		return grid.getNumRows();
	}

	@Override
	public int getNumCols() {
		return grid.getNumCols();
	}
	
	/**
	 * Meotden finner finner GridCell objektet i en gitt posisjon på brettet. 
	 * 
	 * @param pos posisjonen som skal hentes.
	 * @throws IllegalArgumentException hvis pos er null.
	 * @throws IndexOutOfBoundsException hvis pos ikke er på brettet. 
	 * @return GridCell som er cellen på gitt posisjon.
	 */
	public GridCell getCell(CellCoordinate pos) {
		if (pos == null) {
			throw new IllegalArgumentException("pos can't be null");
		}
		if (pos.row() < 0 || pos.col() < 0 || pos.row() >= getNumRows() || pos.col() >= getNumCols()) {
			throw new IndexOutOfBoundsException("pos not on board");
		}
		return this.grid.getCell(pos);
	}
	
	/**
	 * Sjekker om en gitt posisjon er ledig.
	 * 
	 * @param pos posisjonen som skal sjekkes.
	 * @return boolean-verdi som er true hvis cellen er ledig og false ellers. 
	 */
	public boolean isEmpty(CellCoordinate pos) {
		if (pos == null) {
			throw new IllegalArgumentException("pos can't be null");
		}
		isOnBoard(pos);
		return this.grid.getSymbolFromCellCoordinate(pos) == '-';
	}
	
	/**
	 * Metoden setter en git celle på brettet til en gitt verdi.
	 * 
	 * @param pos posisjonen som endres.
	 * @param symbol symbolet cellen endres til.
	 * @throws IllegalArgumentException hvis ugyldig symbol eller poisjon opptatt. 
	 * @throws IndexOutOfBoundsException hvis poisjonen ikke er på brettet. 
	 */
	public void setCellOnBoard(CellCoordinate pos, Character symbol) {
		if (!isLegalSymbol(symbol)) {
			throw new IllegalArgumentException("symbol not legal");
		}
		if (!isEmpty(pos)) {
			throw new IllegalArgumentException("Given pos is already taken");
		}
		this.grid.setCell(pos, symbol);
	}
	
	/**
	 * Metoden undersøker om den sist plasserte boblen danner en lenke bestående av tre eller flere bobler, 
	 * hvis ja fjernes lenken fra spillbrettet og metoden returnerer antallet bobler som ble fjærnet fra lenken. 
	 * 
	 * @param lastBubbleOnBoard sist plasserte boble.
	 * @throws IllegalArgumentException hvis lastBubbleOnBoard er null.
	 * @throws IndexOutOfBoundsException hvis poisjonen ikke er på brettet.
	 * @return chainLength antall bobbler i lenken som ble fjærnet. 
	 */
	public int clearBoard(Bubble lastBubbleOnBoard) {
		int chainLength = 0;
		if (lastBubbleOnBoard == null) {
			throw new IllegalArgumentException("lastBubbleOnBoard can't be null");
		}
		GridCell lastCellPlaced = new GridCell(new CellCoordinate(lastBubbleOnBoard.getPos().row(), lastBubbleOnBoard.getPos().col()),
				lastBubbleOnBoard.getSymbol());
		ArrayList<GridCell> cellChainToRemove = getCellChainWithSameSymbol(lastCellPlaced);
		if (cellChainToRemove.isEmpty()) {
			return 0;
		}
		if (cellChainToRemove.size() >= 3) {
			int chainLenth = cellChainToRemove.size();
			for (GridCell cell : cellChainToRemove) {
				this.grid.setCell(cell.pos(), '-');
			}
			removeAirBubbles();
			return chainLenth;
		}
		return chainLength;
	}
	
	/**
	 * Metoden finner alle cellene som er naboene til en git celle i "Bubble-Shooter",
	 * altså kan hver celle ha maks 6 naboer som ligger inntil bobla, antall naboer 
	 * varrierer utfra rad og kolonne. 
	 * 
	 * @param cell cellen man skal finne naboene til.
	 * @throws IllegalArgumentException hvis cell er null.
	 * @return cells som er naboene til den gitte cellen. 
	 */
	public ArrayList<GridCell> getCellsAroundCell(GridCell cell) {
		if (cell == null) {
			throw new IllegalArgumentException("cell can't be null");
		}
		if (cell.pos().row() < 0 || cell.pos().col() < 0 || cell.pos().row() >= getNumRows() || cell.pos().col() >= getNumCols()) {
			return new ArrayList<>();
		}
		ArrayList<GridCell> cells = new ArrayList <>();
		if (isRowShiftedToRight(cell.pos().row())) {
			cells = getCellsAroundOddRow(cell);
		}
		else {
			cells = getCellsAroundEvenRow(cell);
		}
		return cells;
	}
	
	/**
	 * Metoden forflytter alle eksisterende bobler på brettet et hakk ned og lager en ny full rad med
	 * tilfeldige boble verdier som plasseres i rad 0 på brettet. Deretter fjernes eventuelle luftbobler
	 * som oppstår på grunn av radskiftet. 
	 */
	public void addNewTopRow() {
		Grid newGrid = new Grid(getNumRows(), getNumCols(), '-');
		for (int i = 0; i < getNumCols(); i++) {
			Bubble bubbleToAdd = new Bubble(randomBubbleSymbol());
			bubbleToAdd.changePos(new CellCoordinate(0, i));
			newGrid.setCell(bubbleToAdd.getPos(), bubbleToAdd.getSymbol());
		}
		for(GridCell cell : this.grid) {
			if (cell.symbol() != '-' && cell.pos().row() < grid.getNumRows() - 1) {
				CellCoordinate newPos = new CellCoordinate(cell.pos().row() + 1, cell.pos().col());
				newGrid.setCell(newPos, cell.symbol());
			}
		}
		this.grid = newGrid;
		removeAirBubbles();
	}
	
	private ArrayList<GridCell> getCellChainWithSameSymbol(GridCell cellToCheck) {
		ArrayList<GridCell> chain = new ArrayList<>(Arrays.asList(cellToCheck));
		ArrayList<GridCell> chainPiece = new ArrayList<>();
		int i = 0;
		while(i < chain.size()) {
			GridCell lastAddedChainBubble = chain.get(i);
			chainPiece = getCellNeighboursWithSameSymbol(chain, lastAddedChainBubble);
			if (chainPiece.size() == 0 && (i+1) == chain.size()) {
				i += 1;
				return chain;
			}
			else {
				for (GridCell cell : chainPiece) {
					chain.add(cell);
				}
				i += 1;
			}
		}
		return chain;
	}
	
	private ArrayList<GridCell> getCellNeighboursWithSameSymbol(ArrayList<GridCell> alreadyChecked, GridCell cellToCheck) {
		ArrayList<GridCell> neighbours = getCellsAroundCell(cellToCheck);
		ArrayList<GridCell> neighboursWithSameSymbol = new ArrayList<>();
		for (GridCell cell : neighbours) {
			if (cell.symbol() == cellToCheck.symbol() && !alreadyChecked.contains(cell)) {
				neighboursWithSameSymbol.add(cell);
			}
		}
		return neighboursWithSameSymbol;
	}
	
	//false er partall og true er oddetall.
	private boolean isRowShiftedToRight(int row) {
		if ((row % 2) == 0) {
			return false;
		}
		return true;
	}
	
	private ArrayList<GridCell> getCellsAroundOddRow(GridCell cell) {
		CellCoordinate cellAbove1 = new CellCoordinate(cell.pos().row() - 1, cell.pos().col());
		CellCoordinate cellAbove2 = new CellCoordinate(cell.pos().row() - 1, cell.pos().col() + 1 );
		CellCoordinate cellLeft = new CellCoordinate(cell.pos().row(), cell.pos().col() - 1);
		CellCoordinate cellRight = new CellCoordinate(cell.pos().row(), cell.pos().col() + 1);
		CellCoordinate cellBelow1 = new CellCoordinate(cell.pos().row() + 1, cell.pos().col());
		CellCoordinate cellBelow2 = new CellCoordinate(cell.pos().row() + 1, cell.pos().col() + 1 );
		ArrayList<CellCoordinate> candidates = new ArrayList<>(Arrays.asList(cellAbove1, cellAbove2, cellLeft,
				cellRight, cellBelow1, cellBelow2));
		ArrayList<GridCell> cells = removeIllegalCandidateNeighbours(candidates); 
		return cells;
	}
	
	private ArrayList<GridCell> getCellsAroundEvenRow(GridCell cell) {
		CellCoordinate cellAbove1 = new CellCoordinate(cell.pos().row() - 1, cell.pos().col());
		CellCoordinate cellAbove2 = new CellCoordinate(cell.pos().row() - 1, cell.pos().col() - 1);
		CellCoordinate cellLeft = new CellCoordinate(cell.pos().row(), cell.pos().col() - 1);
		CellCoordinate cellRight = new CellCoordinate(cell.pos().row(), cell.pos().col() + 1);
		CellCoordinate cellBelow1 = new CellCoordinate(cell.pos().row() + 1, cell.pos().col());
		CellCoordinate cellBelow2 = new CellCoordinate(cell.pos().row() + 1, cell.pos().col() - 1);
		ArrayList<CellCoordinate> candidates = new ArrayList<>(Arrays.asList(cellAbove1, cellAbove2, cellLeft,
				cellRight, cellBelow1, cellBelow2));
		ArrayList<GridCell> cells = removeIllegalCandidateNeighbours(candidates);
		return cells;
	}
	
	private ArrayList<GridCell> removeIllegalCandidateNeighbours(ArrayList<CellCoordinate> candidates) {
		ArrayList<GridCell> res = new ArrayList<>();
		for(CellCoordinate pos : candidates) {
			if (pos.row() >= 0 && pos.col() >= 0 && pos.row() < getNumRows() && pos.col() < getNumCols()) {
				GridCell resCell = new GridCell(pos, this.grid.getSymbolFromCellCoordinate(pos));
				res.add(resCell);
			}
		}
		return res;
	}
	
	private Character randomBubbleSymbol() {
		ArrayList<Character> bubbleTypes = new ArrayList<>(Arrays.asList('L','G','P','B','R','Y'));
		int index = this.random.nextInt(bubbleTypes.size());
		return bubbleTypes.get(index);
	}
	
	private boolean isOnBoard(CellCoordinate pos) {
		if (pos == null) {
			throw new IllegalArgumentException("Given position can't be null");
		}
		if (this.grid.getNumCols() <= pos.col() || this.grid.getNumRows() <= pos.row() 
				|| pos.row() < 0 || pos.col() < 0) {
			throw new IndexOutOfBoundsException("Given position is not on board");
		}
		return true;
	}
	
	private boolean isLegalSymbol(Character symbol) {
		if (symbol == null) {
			throw new IllegalArgumentException("Given symbol can't be null");
		}
		ArrayList<Character> legalSymbols = new ArrayList<>(Arrays.asList('L','G','P','B','R','Y','-'));
		return legalSymbols.contains(symbol);
	}
	
	//Fjerner bobler som ikke henger i et chain fra takboblene
	private void removeAirBubbles() {
		ArrayList<GridCell> ceelingConnectedCells = new ArrayList<>();
		for (GridCell cell : this.grid) {
			if (cell.symbol() != '-' && cell.pos().row() == 0) {
				ceelingConnectedCells.add(cell);
			}
		}
		int i = 0;
		while (i < ceelingConnectedCells.size()) {
			GridCell startCell = ceelingConnectedCells.get(i);
			ArrayList<GridCell> chainPiece = getCellChain(startCell);
			for (GridCell cell : chainPiece) {
				if (!ceelingConnectedCells.contains(cell)) {
					ceelingConnectedCells.add(cell);
				}
			}
			i += 1;
		}
		for (GridCell cell : this) {
			if (cell.symbol() != '-' && !ceelingConnectedCells.contains(cell)) {
				this.grid.setCell(cell.pos(), '-');
			}
		}
	}
	
	private ArrayList<GridCell> getCellChain(GridCell cellToCheck) {
		ArrayList<GridCell> chain = new ArrayList<>(Arrays.asList(cellToCheck));
		ArrayList<GridCell> chainPiece = new ArrayList<>();
		int i = 0;
		while(i < chain.size()) {
			GridCell lastAddedChainBubble = chain.get(i);
			chainPiece = getAllNeighbours(chain, lastAddedChainBubble);
			if (chainPiece.size() == 0 && (i+1) == chain.size()) {
				i += 1;
				return chain;
			}
			else {
				for (GridCell cell : chainPiece) {
					chain.add(cell);
				}
				i += 1;
			}
		}
		return chain;
	}
	
	private ArrayList<GridCell> getAllNeighbours(ArrayList<GridCell> alreadyChecked, GridCell cellToCheck) {
		ArrayList<GridCell> neighbours = getCellsAroundCell(cellToCheck);
		ArrayList<GridCell> res = new ArrayList<>();
		for (GridCell cell : neighbours) {
			if (!alreadyChecked.contains(cell) && cell.symbol() != '-') {
				res.add(cell);
			}
		}
		return res;
	}
}
