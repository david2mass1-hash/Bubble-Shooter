package model;

import grid.CellCoordinate;
import grid.Grid;
import grid.GridCell;
import grid.GridDimension;
import view.ViewableBubbleModel;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import bubble.Bubble;
import bubble.BubbleFactory;
import bubbleController.ControllableBubbleModel;

/**
 * Klassen representerer logikken i spillet "Bubble-Shooter", altså brettet, siktepilen, scoore
 * og skytemkenaikken. Samtidig brukes klassen til å oppdatere spillets tilstander. 
 */
public class BubbleModel implements ViewableBubbleModel, ControllableBubbleModel{
	
	private BubbleBoard board;
	private BubbleFactory bubbleFactory;
	private Bubble currentBubble;
	private BubbleTrajectory trajectory;
	private CellCoordinate targetCell;
	private AimArrow arrow;
	private boolean isCurrentBubbleMoving;
	private ArrayList<Bubble> bubbleMag;
	private int magsLeft;
	private int scoore;
	private GameState gameState;
	//Pixel variabler.
	private Point2D.Double shotDirectionVector;
	private Point2D.Double topLeftPixelCorner;
	private double pixelWidth;
	private double pixelHeight;
	private Point2D.Double pixelCenterCurrentBubble;
	private double pixelRadiusCurrentBubble;
	private Point2D.Double aimArrowStartPoint;
	
	/**
	 * Lager et nytt BubbleModel objekt. 
	 * 
	 * @param grid som brukes til å lage BubbleBoard.
	 * @param bubbleFactory som brukes til å lage nye bobler.
	 * @throws IllegalArgumentException hvis grid eller bubbleFactory er null. 
	 */
	public BubbleModel(Grid grid, BubbleFactory bubbleFactory) {
		if (bubbleFactory == null) {
			throw new IllegalArgumentException("bubbleFactory kan ikke være null");
		}
		if (grid == null) {
			throw new IllegalArgumentException("grid kan ikke være null");
		}
		this.gameState = GameState.HOME_SCREEN;
		this.board = new BubbleBoard(grid);
		this.bubbleFactory = bubbleFactory;
		this.magsLeft = 6;
		this.bubbleMag = reloadBubbleMag(this.magsLeft);
		this.currentBubble = this.bubbleMag.get(0);
		this.bubbleMag.remove(0);
		this.arrow = new AimArrow(-Math.PI / 2);
		this.isCurrentBubbleMoving = false;
		this.scoore = 0;
	}
	
	@Override
	public GridDimension getDimension() {
		return this.board;
	}

	@Override
	public Iterable<GridCell> getTilesOnBoard() {
		return this.board;
	}
	
	@Override
	public Bubble getCurrentBubble() {
		return this.currentBubble;
	}
	
	@Override
	public AimArrow getAimArrow() {
		return this.arrow;
	}
	
	@Override
	public Double getAimArrowStartPoint() {
		return this.aimArrowStartPoint;
	}
	
	@Override
	public Double getCurrentBubblePixelCenter() {
		return this.pixelCenterCurrentBubble;
	}

	@Override
	public double getCurrentBubbleRadius() {
		return this.pixelRadiusCurrentBubble;
	}
	
	@Override
	public GameState getGameState() {
		return this.gameState;
	}
	
	@Override
	public int getScoore() {
		return this.scoore;
	}
	
	@Override
	public int getMagSize() {
		return this.bubbleMag.size();
	}

	@Override
	public Bubble getNextBubbleInMag() {
		if (this.bubbleMag.size() == 0) {
			return null;
		}
		return this.bubbleMag.get(0);
	}

	@Override
	public void setTopLeftPixelCorner(Point2D.Double pixelPoint) {
		if (pixelPoint == null) {
			throw new IllegalArgumentException("pixelPoint can't be null");
		}
		this.topLeftPixelCorner = pixelPoint;
		if (!isCurrentBubbleMoving && isPixelDataReady()) {
			updateCurrentBubbleStartPosition();
		}
	}

	@Override
	public void setPixelWidth(double width) {
		if (width <= 0) {
			throw new IndexOutOfBoundsException("width must be greater than 0");
		}
		this.pixelWidth = width;
		if (!isCurrentBubbleMoving && isPixelDataReady()) {
			updateCurrentBubbleStartPosition();
		}
	}

	@Override
	public void setPixelHeight(double height) {
		if (height <= 0) {
			throw new IndexOutOfBoundsException("height must be greater than 0");
		}
		this.pixelHeight = height;
		if (!isCurrentBubbleMoving && isPixelDataReady()) {
			updateCurrentBubbleStartPosition();
		}
	}

	@Override
	public void setCurrentBubblePixelCenter(Point2D.Double pixelPoint) {
		if (pixelPoint == null) {
			throw new IllegalArgumentException("pixelPoint can't be null");
		}
		this.pixelCenterCurrentBubble = pixelPoint;
	}
	
	@Override
	public void setCurrentBubbleRadius(double radius) {
		this.pixelRadiusCurrentBubble = radius;
		if (!isCurrentBubbleMoving && isPixelDataReady()) {
			updateCurrentBubbleStartPosition();
		}
	}
	
	@Override
	public void setNewArrowAngel(double angle) {
		this.arrow.setAngle(angle);
	}
	
	@Override
	public void setGameStateToActiveGame() {
		this.gameState = GameState.ACTIVE_GAME;
	}

	@Override
	public void setGameStateToHomeScreen() {
		this.gameState = GameState.HOME_SCREEN;
	}

	@Override
	public void setGameStateToGameover() {
		this.gameState = GameState.GAME_OVER;
	}
	
	@Override
	public int milliSecoundsPerSec() {
		return 8;
	}

	@Override
	public void shootCurrentBubble(double angle) {
		if (!this.isCurrentBubbleMoving) {
			AimArrow direction = new AimArrow(angle);
			Rectangle2D.Double gameVindow = new Rectangle2D.Double(this.topLeftPixelCorner.getX(),
					this.topLeftPixelCorner.getY(), this.pixelWidth, this.pixelHeight);
			this.trajectory = new BubbleTrajectory(direction, this.board, gameVindow, this.pixelCenterCurrentBubble,
					this.pixelRadiusCurrentBubble);
			CellCoordinate landingPos = this.trajectory.calculateBubbleTrajectory(direction);
			this.targetCell = landingPos;
			this.shotDirectionVector = direction.getUnitVector(angle);
			this.isCurrentBubbleMoving = true;
		}
	}
	
	@Override
	public void clockTick() {
		if (!this.isCurrentBubbleMoving) {
			return;
		}
		Point2D.Double candidate = moveBubble(this.pixelCenterCurrentBubble, this.shotDirectionVector);
		if (this.trajectory.isNextMoveWallBounce(candidate)) {
			//oppdaterer retningsvektoren til å gå motsatt vei
			this.shotDirectionVector =  new Point2D.Double(- this.shotDirectionVector.getX(), 
					this.shotDirectionVector.getY());
			candidate = moveBubble(this.pixelCenterCurrentBubble, this.shotDirectionVector);
		}
		if (!this.trajectory.isNextMoveBubbleCollision(candidate) && !this.trajectory.isNextMoveCeelingHit(candidate)) {
			this.pixelCenterCurrentBubble = candidate;
		}
		if (this.trajectory.isNextMoveBubbleCollision(candidate) || this.trajectory.isNextMoveCeelingHit(candidate)) {
			finishShootingAnimasion();
		}
	}
	
	@Override
	public void resetGame() {
		Grid grid = new Grid(18, 17, '-');
		this.board = new BubbleBoard(grid);
		this.magsLeft = 6;
		this.bubbleMag = reloadBubbleMag(this.magsLeft);
		this.currentBubble = this.bubbleMag.get(0);
		this.bubbleMag.remove(0);
		this.arrow = new AimArrow(-Math.PI / 2);
		this.isCurrentBubbleMoving = false;
		this.trajectory = null;
		this.targetCell = null;
		this.shotDirectionVector = null;
		this.pixelCenterCurrentBubble = null;
		this.aimArrowStartPoint = null; 
		this.scoore = 0;
		setGameStateToActiveGame();
	}
	
	/**
	 * Metoden lader et nytt magasin med bobler, antallet bobler i magasinet er like mange 
	 * som magasiner spilleren har igjen. Hver gang man får et nytt magasin reduseres antallet bobler i
	 * magasinet med 1. Når man har null magasiner får man 6 nye magasiner og logikken gjentas. 
	 * 
	 * @param numMags antall magasiner spilleren har igjen.
	 * @return newMag nytt magasin hvor antallet bobler er likt numMags. 
	 */
	public ArrayList<Bubble> reloadBubbleMag(int numMags) {
		if (numMags == 1) {
			this.magsLeft = 6;
			numMags = 6;
		}
		ArrayList<Bubble> newMag = new ArrayList<>();
		int i = 0;
		while(i < numMags) {
			Bubble bubbleToAdd = getNextBubble();
			newMag.add(bubbleToAdd);
			i += 1;
		}
		this.magsLeft -= 1;
		return newMag;
	}
	
	//metoden oppretter en ny boble og regner ut startPoisjonen utfra brettet og flytter den dit. 
	private Bubble getNextBubble() {
		Bubble nextBubble = this.bubbleFactory.getNext();
		int numCols = board.getNumCols();
		int numRows = board.getNumRows();
		int middleCol = numCols / 2;
		nextBubble.changePos(new CellCoordinate(numRows - 1, middleCol));
		return nextBubble;
	}
		
	private void finishShootingAnimasion() {
		placeBubbleOnBoard(this.targetCell);
		addBonusBubbleIfChainHit();
		if (this.bubbleMag.size() == 0) {
			this.bubbleMag = reloadBubbleMag(this.magsLeft);
			this.board.addNewTopRow();
		}
		this.currentBubble = this.bubbleMag.get(0);
		this.bubbleMag.remove(0);
		updateCurrentBubbleStartPosition();
		if (isStateGameOver()) {
			setGameStateToGameover();
		}
		this.shotDirectionVector = null;
		this.targetCell = null;
		this.isCurrentBubbleMoving = false;
	}
	
	//sjekker om det ble fjærna bobler hvis ja får et ekstra skudd (tilknyttet magasin).
	private void addBonusBubbleIfChainHit() {
		int numBubblesRemoved = this.board.clearBoard(this.currentBubble);
		this.scoore += numBubblesRemoved * 10;
		if (numBubblesRemoved > 0) {
			this.bubbleMag.add(getNextBubble());
		}
	}
	
	//Flytter en boble oldPoint med dx og dy altså retingsvektor, med lengde speed=10.
	private Point2D.Double moveBubble(Point2D.Double oldPoint, Point2D.Double directionVector) {
		if (oldPoint == null || directionVector == null) {
			throw new IllegalArgumentException("oldPoint and direction can't be null");
		}
		Point2D.Double vectorEndPoint = new Point2D.Double(oldPoint.getX() + (directionVector.getX() * 10),
				oldPoint.getY() + (directionVector.getY() * 10));
		return vectorEndPoint;
	}
	
	private void placeBubbleOnBoard(CellCoordinate pos) {
		if (pos == null) {
			throw new IllegalArgumentException("pos can't be null");
		}
		this.currentBubble.changePos(pos);
		this.board.setCellOnBoard(pos, this.currentBubble.getSymbol());
	}
	
	private boolean isStateGameOver() {
		int gameOverRow = this.board.getNumRows() - 2;
		for (GridCell cell : getTilesOnBoard()) {
			if (cell.pos().row() == gameOverRow) {
				if (cell.symbol() != '-') {
					return true;
				}
			}
		}
		return false;
	}
	
	//sjekker om modellen er klar til å endre størrelsen (spillet har launcha).
	private boolean isPixelDataReady() {
		return this.topLeftPixelCorner != null && this.pixelWidth > 0 && this.pixelHeight > 0 && pixelRadiusCurrentBubble > 0;
	}
	
	private void updateCurrentBubbleStartPosition() {
		double toppLeftX = this.topLeftPixelCorner.getX();
		double toppLeftY = this.topLeftPixelCorner.getY();
		double width = this.pixelWidth;
		double height = this.pixelHeight;
		double centerX = toppLeftX + width / 2;
		double centerY = toppLeftY + height - this.pixelRadiusCurrentBubble;
		Point2D.Double bubbleCenter = new Point2D.Double(centerX, centerY);
		this.pixelCenterCurrentBubble = bubbleCenter;
		this.aimArrowStartPoint = new Point2D.Double(bubbleCenter.getX(), bubbleCenter.getY());
	}
}