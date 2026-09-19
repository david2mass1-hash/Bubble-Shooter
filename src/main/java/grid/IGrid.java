package grid;

public interface IGrid extends Iterable<GridCell>{

	/**
	 * Metode som setter en gitt celle i koordinatSystemet til en ny verdi. Et etterfølgende
	 * kall til {@link #getCell(CellCoordintes)} med en lik posisjonen som argument vil returnere verdien som ble satt.
	 * Metoden vil overstyre enhver tidligere verdi som var lagret på den posisjonen. 
	 * 
	 * @param coordinate posisjonen hvor verdien skal lagres
	 * @param symbol den nye veriden. 
	 * @throws IndexOutOfBoundsException hvis posisjonen ikke er i gridet. 
	 */
	void setCell(CellCoordinate coordinate, Character symbol);
	
	/**
	 * get-metode for å hente en kopi av en celle på rutenettet. 
	 * 
	 * @param coordinate posisjon til ønsket celle
	 * @return kopi av ønsket celle av typen GridCell.
	 * @throws IndexOutOfBoundsException hvis posisjonen ikke er i gridet.
	 * 
	 */
	GridCell getCell(CellCoordinate coordinate);

}
	
