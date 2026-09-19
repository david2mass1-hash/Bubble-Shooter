package bubbleController;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.Timer;

import view.BubbleView;
import view.CellPositionToPixelConverter;
import grid.CellCoordinate;
import model.AimArrow;
import model.GameState;


/**
 * Representerer kontorlleren i "Bubble-Shooter".
 * Klassen håndterer tastetrykk fra brukeren og klokkeslag fra timeren,
 * og bruker dette til å oppdatere modellen og spillvinduet. 
 */
public class BubbleController extends MouseAdapter implements KeyListener {
	
	private ControllableBubbleModel controllableBubbleModel;
	private BubbleView bubbleView; 
	private Timer timer;

	/**
	 * Konstuktør som lager en ny kontroller i "Bubble-Shooter".
	 * 
	 * @param controllableBubbleModel som er modellen som skal styres.
	 * @param bubbleView som er spillvinduet som oppdateres.
	 * @throws IllegalArgumentException hvis controllableBubbleModel eller bubbleView er null.
	 */
	public BubbleController(ControllableBubbleModel controllableBubbleModel, BubbleView bubbleView) {
		if (controllableBubbleModel == null || bubbleView == null) {
			throw new IllegalArgumentException("controllableBubbleModel and bubbleView can't be nulll");
		}
		this.controllableBubbleModel = controllableBubbleModel;
		this.bubbleView = bubbleView;
		this.bubbleView.addKeyListener(this);
		this.bubbleView.addMouseListener(this);
		this.bubbleView.addMouseMotionListener(this);
		this.bubbleView.setFocusable(true);
		this.timer = new Timer(controllableBubbleModel.milliSecoundsPerSec(), this::clockTick);
		this.timer.start();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (this.controllableBubbleModel.getGameState() == GameState.HOME_SCREEN) {
				this.controllableBubbleModel.resetGame();
				updateModelsPixelDimensions();
				this.controllableBubbleModel.setGameStateToActiveGame();
				bubbleView.repaint();
				timer.restart();
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_R) {
			if (this.controllableBubbleModel.getGameState() == GameState.GAME_OVER) {
				this.controllableBubbleModel.resetGame();
				updateModelsPixelDimensions();
				this.timer.restart();
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_H) {
			if (this.controllableBubbleModel.getGameState() == GameState.GAME_OVER) {
				this.controllableBubbleModel.setGameStateToHomeScreen();
				this.timer.stop();
			}
		}
		bubbleView.repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if (this.controllableBubbleModel.getGameState() == GameState.ACTIVE_GAME) {
			double xPos = e.getX();
			double yPos = e.getY();
			Point2D.Double mouseInPixelPos = new Point2D.Double(xPos,yPos);
			double newAngle = controllableBubbleModel.getAimArrow().calculatAngle(mouseInPixelPos, bubbleView);
			this.controllableBubbleModel.getAimArrow().setAngle(newAngle);
		}
	}
	
	//inspirert fra clickeableGrid
	@Override
	public void mousePressed(MouseEvent e) {
		if (this.controllableBubbleModel.getGameState() == GameState.ACTIVE_GAME) {
			double angle = this.controllableBubbleModel.getAimArrow().getAngle();
			this.controllableBubbleModel.shootCurrentBubble(angle);
			this.bubbleView.repaint();
		}
	}
	
	/**
	 * Metode som kalles hver gang timeren økes,
	 * og oppdaterer modellen og tegner visnigen på nytt hvis spillets GameState er active.
	 * 
	 * @param actionEvent
	 */
	public void clockTick(ActionEvent actionEvent) {
		if (controllableBubbleModel.getGameState() == GameState.ACTIVE_GAME) {
			updateModelsPixelDimensions();
			controllableBubbleModel.clockTick();
			updateTimerDelay();
			this.bubbleView.repaint();
		}
	}
	
	//bruke get-metoder som tilhørere bubbleView for å få pixelkordinat start, bredde og høyde.
	/**
	 * Get-metode som finner venstre topphjørne til spillbrettet i pixelformat. 
	 * 
	 * @return Point2D.Double venstre topphjørne.
	 */
	public Point2D.Double getTopLeftPixelPoint() {
		Rectangle2D gameBoard = this.bubbleView.getShiftedGameWindowSize();
		double x = gameBoard.getX();
		double y = gameBoard.getY();
		Point2D.Double topLeftCorner = new Point2D.Double(x,y);
		return topLeftCorner;
	}
	
	/**
	 * Get-metode som finner bredden til spillbrettet i antall pixler. 
	 * 
	 * @return dx bredden til spillbrettet.
	 */
	public double getPixelWidth() {
		Rectangle2D gameBoard = this.bubbleView.getShiftedGameWindowSize();
		return gameBoard.getWidth();
	}
	
	/**
	 * Get-metode som finner høyden til spillbrettet i antall pixler. 
	 * 
	 * @return dy høyden til spillbrettet. 
	 */
	public double getPixelHeight() {
		Rectangle2D gameBoard = this.bubbleView.getShiftedGameWindowSize();
		return gameBoard.getHeight();
	}
	
	
	
	//bruker set-metoder som tilhører model, slik at jeg får inn pixeldimensjoner til brettet som brukes til skytemkanikk.
	private void updateModelsPixelDimensions() {
		Rectangle2D gameBoard = this.bubbleView.getShiftedGameWindowSize();
		if ( gameBoard.getWidth() <= 0 || gameBoard.getHeight() <= 0) {
			return;
		}
		this.controllableBubbleModel.setTopLeftPixelCorner(getTopLeftPixelPoint());
		this.controllableBubbleModel.setPixelWidth(getPixelWidth());
		this.controllableBubbleModel.setPixelHeight(getPixelHeight());
		this.controllableBubbleModel.setCurrentBubbleRadius(bubbleView.getBubbleRadius());
	}
	
	
	private void updateTimerDelay() {
		int delay = this.controllableBubbleModel.milliSecoundsPerSec();
		timer.setDelay(delay);
		timer.setInitialDelay(delay);
	}
}


