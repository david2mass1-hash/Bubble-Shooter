package grid;

import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * Iterator som går gjennom hver celle i et grid radvis. 
 */
public class RowWiseGridIterator implements Iterator<GridCell> {
	
	private CellCoordinate next;
	private Grid grid;
	
	/**
	 * Lager en ny iterator. 
	 * 
	 * @param grid gridet som skal itereres over. 
	 */
	public RowWiseGridIterator(Grid grid) {
		this.next = new CellCoordinate(0, 0);
		this.grid = grid;
	}

	@Override
	public boolean hasNext() {
		if (next.row() <= grid.getNumRows()-1 && next.col() <= grid.getNumCols()-1) {
			return true;
		}
		return false;
	}

	@Override
	public GridCell next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		int rowToGet = this.next.row();
		int colToGet = this.next.col();
		Character symbol = this.grid.getSymbolFromCellCoordinate(next);
		GridCell res = new GridCell(new CellCoordinate(rowToGet, colToGet), symbol);
		if (this.next.col() < this.grid.getNumCols()-1) {
			this.next = new CellCoordinate(next.row(), next.col() + 1);
		}
		else {
			this.next = new CellCoordinate(next.row() + 1, 0);
		}
		return res;
	}
}
