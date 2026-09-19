package grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Klasse som beskriver et objekt grid som er et 2D rutenett.
 */
public class Grid implements IGrid, GridDimension {
	
	private final ArrayList<ArrayList<GridCell>> grid;
	
	/**
	 * Metoden oppretter et nytt objekt av typen Grid, utfra angitte parametere.
	 * 
	 * @param rows antall rader i rutenettet.
	 * @param cols antall kolonner i rutenettet. 
	 * @param symbol start symbolet til alle cellene i rutenettet. 
	 */
	public Grid(int rows, int cols, Character symbol) {
		if (rows <= 0 || cols <= 0) {
			throw new IllegalArgumentException("Both number of rows and number og cols must be greater than 0");
		}
		if (symbol == null) {
			throw new IllegalArgumentException("Symbol can not be null it must be one of the following: ");
		}
		if (!isLegalSymbol(symbol)) {
			throw new IllegalArgumentException("Symbol must be uppercase and one of the following");
		}
		ArrayList<ArrayList<GridCell>> gridToMake = new ArrayList<>();
		for (int i = 0; i < rows; i++) {
			ArrayList<GridCell> row = new ArrayList<>();
			for (int j = 0; j < cols; j++) {
				CellCoordinate coordinates = new CellCoordinate(i, j);
				GridCell cell = new GridCell(coordinates, symbol);
				row.add(cell);
			}
			gridToMake.add(row);
		}
		this.grid=gridToMake;
	}
	
	@Override
	public GridCell getCell(CellCoordinate coordinate) {
		if (coordinate == null) {
			throw new IllegalArgumentException("Coordinate can't be null");
		}
		if (!isPositionOnGrid(coordinate)) {
			throw new IndexOutOfBoundsException("The given coordinate is not on the grid");
		}
		Character symbol = getSymbolFromCellCoordinate(coordinate);
		return new GridCell(coordinate, symbol);
	}
	
	@Override
	public void setCell(CellCoordinate pos, Character newSymbol) {
		if (pos == null || newSymbol == null) {
			throw new IllegalArgumentException("First ande secound param can't be null");
		}
		if (!isPositionOnGrid(pos)) {
			throw new IndexOutOfBoundsException("Position is not on Grid");
		}
		if (!isLegalSymbol(newSymbol)) {
			throw new IllegalArgumentException("Given symbol is not legal must be one of the following: ");
		}
		grid.get(pos.row()).set(pos.col(), new GridCell(pos, newSymbol));
	}
	
	@Override
	public Iterator<GridCell> iterator() {
		return new RowWiseGridIterator(this);
	}
	
	@Override
	public int getNumRows() {
		return this.grid.size();
	}
	
	@Override 
	public int getNumCols() {
		return  this.grid.get(0).size();
	}
	
	/**
	 * get-metode for å finne symbolet i en gitt celle.
	 * 
	 * @param cordinate posisjonen i griddet hvor symbolet skal hentes fra
	 * @return symbolet i den gitte posisjonen i griddet. 
	 * @throws IllegalArgumentException hvis coordinate er null.
	 * @throws IndexOutOfBoundsException hvis coordinate ikke er på griddet. 
	 */
	public Character getSymbolFromCellCoordinate(CellCoordinate coordinate) {
		if (coordinate == null) {
			throw new IllegalArgumentException("Coordinate can't be null");
		}
		if (!isPositionOnGrid(coordinate)) {
			throw new IndexOutOfBoundsException("The given coordinate is not on the grid");
		}
		int targetRow = coordinate.row();
		int targetCol = coordinate.col();
		ArrayList<GridCell> row = this.grid.get(targetRow);
		GridCell cell = row.get(targetCol);
		return cell.symbol();
	}
	
	/**
	 * get-metode for å hente ut en kopi av en rad fra rutenettet.
	 * 
	 * @param row raden som man ønsker å hente ut.
	 * @return returnerer en ny liste med cellene i raden.
	 * @throws IndexOutOfBoundsException hvis raden ikke er på brettet. 
	 */
	public ArrayList<GridCell> getRow(int row) {
		if (!(row >= 0 && row < this.getNumRows())) {
			throw new IndexOutOfBoundsException("Row not on board");
		}
		ArrayList<GridCell> rowToCopy = this.grid.get(row);
		ArrayList<GridCell> rowCopy = new ArrayList<>();
		for (GridCell cell : rowToCopy) {
			rowCopy.add(cell);
		}
		return rowCopy;
	}
	
	private boolean isPositionOnGrid(CellCoordinate pos) {
		return pos.col() >= 0 && pos.col() <this.getNumCols() && pos.row() >= 0 && pos.row() < this.getNumRows();
	}

	private boolean isLegalSymbol(Character symbol) {
		ArrayList<Character> legalValues = new ArrayList<>(Arrays.asList('-','L','G','P','B','R','Y'));
		if (!legalValues.contains(symbol)) {
			return false;
		}
		return true;
	}
}