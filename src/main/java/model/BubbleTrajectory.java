package model;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import grid.CellCoordinate;
import grid.GridCell;

/**
 * Klassen simulerer skytebanen til boblen i spillet "Bubble-Shooter". Klassen tar for seg 
 * beregninger for å finne ut hvor ballen som skytes kommer til å havne på brettet og kollisjoner med 
 * vegger, bobler og tak. Dette gjøres ved hjelp av et AimArrow
 * objekt, som forteller hva vinkelen var når skuddet ble avffyrt.   
 */
public class BubbleTrajectory {
	
	private AimArrow fieringAngle;
	private BubbleBoard board;
	private Point2D.Double topLeftCornerGameVindow;
	private double vindowWidth;
	private Point2D.Double bubblePixelCenter;
	private double bubblePixelRadius;
	
	/**
	 * Oppretter et nytt BubbleTrajectory objekt. 
	 * 
	 * @param fieringAngle vinkelen når skuddet ble avfyrt.
	 * @param board brettet til spillet nå skuddet ble avfyrt. 
	 * @param gameVindow spillvinduets dimensjoner. 
	 * @param bubblePixelCenter pixel sentrumet til boblen som skal skytes.
	 * @param bubblePixelRadius radiusen til boblene i frohold til spillvinduets størrelse.
	 */
	public BubbleTrajectory(AimArrow fieringAngle, BubbleBoard board, Rectangle2D.Double gameVindow,
			Point2D.Double bubblePixelCenter, double bubblePixelRadius) {
		if (fieringAngle == null || board == null || gameVindow == null || 
				bubblePixelCenter == null || bubblePixelRadius <= 0) {
			throw new IllegalArgumentException("param's can't be null and bubblePixelRadius must be greater than zero");
		}
		this.fieringAngle = fieringAngle;
		this.board = board;
		this.topLeftCornerGameVindow = new Point2D.Double(gameVindow.getX(), gameVindow.getY());
		this.vindowWidth = gameVindow.getWidth();
		this.bubblePixelCenter = bubblePixelCenter;
		this.bubblePixelRadius = bubblePixelRadius;
	}
	
	/**
	 * Metoden undersøker om en bobles possisjon koliderer med en vegg ved å finne
	 * distansen mellom veggene og boblens venstre ytterkant og høyre ytterkant. 
	 * 
	 * @param bubbleCenter posisjonen til boblen.
	 * @return boolean true hvis bobla treffer en vegg og false ellers.
	 * @throws IllegalArgumentException hvis bubblecenter er null. 
	 */
	public boolean isNextMoveWallBounce(Point2D.Double bubbleCenter) {
		if (bubbleCenter == null) {
			throw new IllegalArgumentException("bubbleCenter can't be null");
		}
		double rightBubbleEdge = bubbleCenter.getX() + this.bubblePixelRadius;
		double leftBubbleEdge = bubbleCenter.getX() - this.bubblePixelRadius;
		double leftXBound = this.topLeftCornerGameVindow.getX();
		double rightXBound = this.topLeftCornerGameVindow.getX() + this.vindowWidth;
		if (leftBubbleEdge < leftXBound || rightBubbleEdge > rightXBound) {
			return true;
		}
		return false;
	}
	
	/**
	 * Metoden undersøker om en bobles posisjon kolliderer med toppen av brettet ved å finne
	 * distansen mellom taket og boblens topp ytterkant. 
	 * 
	 * @param bubbleCenter posisjonen til boblen. 
	 * @return boolean true hvis bobla treffer taket og false eller.
	 * @throws IllegalArgumentException hvis bubblecenter er null.
	 */
	public boolean isNextMoveCeelingHit(Point2D.Double bubbleCenter) {
		if (bubbleCenter == null) {
			throw new IllegalArgumentException("bubbleCenter can't be null");
		}
		double toppBubbleEdge = bubbleCenter.getY() - this.bubblePixelRadius;
		double topYBound = this.topLeftCornerGameVindow.getY();
		if (toppBubbleEdge < topYBound) {
			return true; 
		}
		return false;
	}
	
	/**
	 * Metoden undersøker om en bobles posisjon kolliderer med en av de andre boblene på brette,
	 * ved å finne distansen mellom boblens sentrum og de andre bobelenes sentrum. Hvis distansen er mindre
	 * enn eller lik diameteren til en boble er det kollisjon mellom boblene. 
	 * 
	 * @param bubbleCenter posisjonen til boblen.
	 * @return boolean true hvis bobla treffer en annen boble og false ellers.
	 * @throws IllegalArgumentException hvis bubblecenter er null.
	 */
	public boolean isNextMoveBubbleCollision(Point2D.Double bubbleCenter) {
		if (bubbleCenter == null) {
			throw new IllegalArgumentException("bubbleCenter can't be null");
		}
		for (GridCell cell : this.board) {
			if (cell.symbol() != '-') {
				Point2D.Double centerdPixelPoint = convertCellToBubblePixelCenter(cell.pos());
				double xPixelBubble = centerdPixelPoint.getX();
				double yPixelBubble = centerdPixelPoint.getY();
				double xCurrentPixelBubble = bubbleCenter.getX();
				double yCurrentPixelBubble = bubbleCenter.getY();
				// Source - https://stackoverflow.com/a/74049671
				// Posted by parsecer
				// Retrieved 2026-04-11, License - CC BY-SA 4.0
				double centersDistance = Math.sqrt(Math.pow(xPixelBubble - xCurrentPixelBubble, 2) + Math.pow(yPixelBubble - yCurrentPixelBubble, 2));
				if (centersDistance <= (this.bubblePixelRadius * 2)) {
					return true;
				}
	 		}
		}
		return false;
	}
	
	/**
	 * Metoden regner ut hvor boblen kommer til å plasseres på brettet utfra AimArrow sin tilstand når
	 * skuddet ble avfyrt. Utregningen håndterer også kollisjon med vegger, bobler og tak.
	 *   
	 * @param direction AimaArrow sin tilstand når skuddet ble avfyrt. 
	 * @return CellCoordinate posisonen hvor boblen ender opp. 
	 * @throws IllegalArgumentException hvis direction er null eller ugyldig vinkel.
	 */
	public CellCoordinate calculateBubbleTrajectory(AimArrow direction) {
		if (direction == null || direction.getAngle() <= (-Math.PI + 0.2) || direction.getAngle() >= (-0.2)) {
			throw new IllegalArgumentException("bubbleCenter can't be null and direction must be between (-pi + 0.2) and (-0.2)");
		}
		Point2D.Double directionVector = direction.getUnitVector(direction.getAngle());
		Point2D.Double currentPos = this.bubblePixelCenter;
		Point2D.Double nextPos = direction.calculateVectorEndPoint(currentPos, directionVector, 2);
		while(!isNextMoveBubbleCollision(nextPos) && !isNextMoveCeelingHit(nextPos)) {
			if (isNextMoveWallBounce(nextPos)) {
				//hvis vegg treff blir det samme vinkel bare motsatt fortegn, altså dx
				directionVector = new Point2D.Double(-directionVector.getX(), directionVector.getY());
				nextPos = direction.calculateVectorEndPoint(currentPos, directionVector, 2);
			}
			else {
				currentPos = new Point2D.Double(nextPos.getX(), nextPos.getY());
				nextPos = direction.calculateVectorEndPoint(currentPos, directionVector, 2);
			}
		}
		if (isNextMoveBubbleCollision(nextPos)) {
			//Finner bobla den krasjet med og finner alle dens tomme namboer 
			GridCell collisionCell = getBubbleFromCollision(nextPos);
			ArrayList<GridCell> neighbourCells = this.board.getCellsAroundCell(collisionCell);
			ArrayList<GridCell> emptyNeighbourCells = new ArrayList<>();
			for (GridCell cell : neighbourCells) {
				if (cell.symbol() == '-') {
					emptyNeighbourCells.add(cell);
				}
			}
			return findClosestLegalPos(emptyNeighbourCells, nextPos);
		}
		//hvis taktreff
		else {
			ArrayList<GridCell> emptyPlacesTopRow = new ArrayList<>();
			for (GridCell cell : this.board) {
				if (cell.pos().row() == 0 && cell.symbol() == '-') {
					emptyPlacesTopRow.add(cell);
				}
			}
			return findClosestLegalPos(emptyPlacesTopRow, nextPos);
		}
	}

	private CellCoordinate findClosestLegalPos(ArrayList<GridCell> neighbours, Point2D.Double bubbleToPlace) {
		double shortestDistance = 100000;
		CellCoordinate closestBubblePos = null;
		for (GridCell cell : neighbours) {
			Point2D.Double candidateBubble = convertCellToBubblePixelCenter(cell.pos());
			if (pixelDistanceBetweenBubbles(candidateBubble, bubbleToPlace) < shortestDistance) {
				shortestDistance = pixelDistanceBetweenBubbles(candidateBubble, bubbleToPlace);
				closestBubblePos = cell.pos();
			}
		}
		return closestBubblePos;
	}
	
	private GridCell getBubbleFromCollision(Point2D.Double bubbleToPlace) {
		ArrayList<GridCell> bubblesOnBoard = new ArrayList<>();
		for (GridCell cell : this.board) {
			if (cell.symbol() != '-') {
				bubblesOnBoard.add(cell);
			}
		}
		GridCell collisionCell = null;
		double shortestDistance = 1000000;
		for (GridCell cell : bubblesOnBoard) {
			Point2D.Double candidatePos = convertCellToBubblePixelCenter(cell.pos());
			if (pixelDistanceBetweenBubbles(candidatePos, bubbleToPlace) < shortestDistance) {
				shortestDistance = pixelDistanceBetweenBubbles(candidatePos, bubbleToPlace);
				collisionCell = cell;
			}
		}
		return collisionCell;
	}
	
	//X-representerer col og Y representerer row i pixler. 
	private Point2D.Double convertCellToBubblePixelCenter(CellCoordinate pos) {
		double bubbleDiameter = this.bubblePixelRadius * 2;
		double firstBubbleFirstRowX = this.topLeftCornerGameVindow.getX() + this.bubblePixelRadius;
		double firstBubbleFirstRowY = this.topLeftCornerGameVindow.getY() + this.bubblePixelRadius;
		double x = firstBubbleFirstRowX + bubbleDiameter * pos.col();
		double y = firstBubbleFirstRowY + bubbleDiameter * pos.row();
		//hvis oddetalls rad.
		if ((pos.row() % 2) != 0) {
			x += this.bubblePixelRadius;
		}
		Point2D.Double res = new Point2D.Double(x,y);
		return res;
	}
	
	private double pixelDistanceBetweenBubbles(Point2D.Double bubble1, Point2D.Double bubble2) {
		double xBubble1 = bubble1.getX();
		double yBubble1 = bubble1.getY();
		double xBubble2 = bubble2.getX();
		double yBubble2 = bubble2.getY();
		double centersDistance = Math.sqrt(Math.pow(xBubble1 - xBubble2, 2) + Math.pow(yBubble1 - yBubble2, 2));
		return centersDistance;
	}
}
