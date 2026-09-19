package grid;
import java.util.Iterator;


public interface GridDimension {

	/** Antall rader i griddet. */
	int getNumRows();

	/** Antall kolonner i griddet.  */
	int getNumCols();

		
	/**
	 * Avgjør om posisjonen er innenfor girddet.
	 * 
	 * @param pos posisjonen som skal sjekkes.
	 * @return true hvis innenfor griddet, false ellers.
	 */
	default boolean positionIsOnGrid(CellCoordinate pos) {
		if(pos==null)
			return false;
		boolean validRow = pos.row() >= 0 && pos.row() < getNumRows();
		boolean validCol = pos.col() >= 0 && pos.col() < getNumCols();
		return  validRow && validCol;
	}

}

