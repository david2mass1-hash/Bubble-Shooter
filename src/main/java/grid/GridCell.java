package grid;
/**
 * Record som representerer en celle i gridet.
 * Består av pos som er et koordinat til cellen og
 * symbol som er den tilhørende verdien. 
 * 
 * @param pos koordinatene til cellen i gridet. 
 * @param symbol som er verdien til cellen. 
 */
public record GridCell(CellCoordinate pos, Character symbol) {

}
