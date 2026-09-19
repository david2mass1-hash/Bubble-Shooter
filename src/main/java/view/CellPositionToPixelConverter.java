package view;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;

import grid.CellCoordinate;
import grid.Grid;
import grid.GridDimension;

//Klassen representerer et objekt som konverterer celler i et grid til pixler. 
public class CellPositionToPixelConverter {

    private final Rectangle2D box;
    private final GridDimension gd;
    
    /**
     * Oppretter et CellPositionToPixelConverter objekt. 
     *  
     * @param box rektangelet som er ytterrammen til objektet.
     * @param gd gridets dimensjoner
     * @param margin ønsket mellomrom mellom cellene. 
     * @throws IllegalArgumentException hvis box eller gd er null.
     */
    public CellPositionToPixelConverter(Rectangle2D box, GridDimension gd) {
    	if (box == null || gd == null) {
    		throw new IllegalArgumentException("box and gd can't be null");
    	}
        this.box = box;
        this.gd = gd;
    }
    
    /**
     * Metoden konverterer en celle i griddet til en ellipse med pixel verider i forhold til 
     * spillvinduets strørrelse.
     * 
     * @param cellCoordinate kordinatet som skal konverteres
     * @throws IllegalArgumentException hvis kordinatet er lik null.
     * @throws IndexOutOfBoundsException hvis kordinatet ikke er på griddet.
     * @return Sirkel som skal tegnes.
     */
    public Ellipse2D.Double getBoundsForCell(CellCoordinate pos) {
    	checkPos(pos);
    	Point2D.Double topLeftPoint = getBubbleTopLeftPixelCorner(pos);
        double diameter = getCurrentBubbleDiameter();
        return new Ellipse2D.Double(topLeftPoint.getX(), topLeftPoint.getY(), diameter, diameter);
    }
    
    /**
     * Metoden finner diameteren av en boble i antall pixler utfra spillvinduets nårværende størrelse, 
     * tar også hensyn for at spillvinduets bredde har lagt til en ekstra margin på en bobleRadius. 
     * 
     * @return diameter i antall pixler. 
     */
    public double getCurrentBubbleDiameter() {
    	double windowWidth = this.box.getWidth();
    	double numBubblesPerRow = gd.getNumCols();
    	double diameter = windowWidth / (numBubblesPerRow + 0.5);
    	return diameter;	
     }
    
    /**
     * Metoden finner radiusen av en boble i antall pixler utfra spillvinduets nårværende størrelse,
     * tar også hensyn for at spillvinduets bredde har lagt til en ekstra margin på en bobleRadius. 
     * 
     * @return double radius i antall pixler.
     */
    public double getCurrentBubbleRadius() {
    	return getCurrentBubbleDiameter() / 2;
    }
    
    /**
     * Metoden finner venstre-topp pixelhjørne i en gitt celle.
     * 
     * @param pos cellen man skal finne hjørnet til.
     * @throws IllegalArgumentException hvis kordinatet er lik null.
     * @throws IndexOutOfBoundsException hvis kordinatet ikke er på griddet.
     * @return endPoint venstre-topp hjørne til gitt pos i pixel-koordinat. 
     */
    public Point2D.Double getBubbleTopLeftPixelCorner(CellCoordinate pos) {
    	checkPos(pos);
    	Point2D.Double startPoint = new Point2D.Double(this.box.getX(), this.box.getY());
    	double diameter = getCurrentBubbleDiameter();
    	if (isRowShifted(pos)) {
    		startPoint = new Point2D.Double(startPoint.getX() + getCurrentBubbleRadius(), startPoint.getY());
    	}
    	double endPointX = startPoint.getX() + (diameter * pos.col());
    	double endPointY = startPoint.getY() + (diameter * pos.row());
    	Point2D.Double endPoint = new Point2D.Double(endPointX, endPointY);
    	return endPoint; 
    }
    
    /**
     * Metoden finner midten av en boble i pixel kordinater ved hjelp av Point2D.Double 
     * fra java.awt.geom for å representere pixel-koordinatet.
     * 
     * 
     * @param pos koordinatet som skal konverteres.
     * @throws IllegalArgumentException hvis kordinatet er lik null.
     * @throws IndexOutOfBoundsException hvis kordinatet ikke er på griddet. 
     * @return punktet som er midten av bobla i pixel koordinater.
     */
    public Point2D.Double getBubblePixelCenter(CellCoordinate pos) {
    	checkPos(pos);
    	double radius = getCurrentBubbleRadius();
    	double diameter = getCurrentBubbleDiameter();
    	Point2D.Double startPoint = new Point2D.Double(this.box.getX() + radius, this.box.getY() + radius);
    	if (isRowShifted(pos)) {
    		startPoint = new Point2D.Double(startPoint.getX() + radius, startPoint.getY());
    	}
    	double endPointX = startPoint.getX() + (diameter * pos.col());
    	double endPointY = startPoint.getY() + (diameter * pos.row());
    	Point2D.Double endPoint = new Point2D.Double(endPointX, endPointY);
    	return endPoint; 
    }
    
    private void checkPos(CellCoordinate pos) {
    	if (pos == null) {
    		throw new IllegalArgumentException("cellCoordinate can't be null");
    	}
    	if (pos.row() < 0 || pos.col() < 0 || pos.row() >= gd.getNumRows() || pos.col() >= gd.getNumCols()) {
    		throw new IndexOutOfBoundsException("pos not on grid");
    	}
    }
    
    private boolean isRowShifted(CellCoordinate pos) {
    	if ((pos.row() % 2) == 0) {
    		return false;
    	}
    	return true; 
    }   
}
