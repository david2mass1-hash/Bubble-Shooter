package view;

import grid.GridCell;
import grid.GridDimension;
import model.AimArrow;
import model.GameState;

import java.awt.geom.Point2D;

import bubble.Bubble;

public interface ViewableBubbleModel {
	
	/**
	 * Metode som returnere dimensjonene til BubbleShooter-brettet.
	 * 
	 * @return GridDimension som beskriver antall rader og kolonner.
	 */
	GridDimension getDimension();
	
	/**
	 * Metode som returnerer alle celler som er plassert på brettet.
	 * 
	 * @return Iterable med GridCell-objekter som representerer cellene på brettet. 
	 */
	Iterable<GridCell> getTilesOnBoard();
	
	/**
	 * Metoden returnerer nåværende spilltilstand.
	 * 
	 * @return GameState spilltilstanden. 
	 */
	GameState getGameState();
	
	/**
	 * Metode som returnere bobla som skal skytes.
	 * 
	 * @return Bubble som representere ballen som skal skytes. 
	 */
	Bubble getCurrentBubble();
	
	/**
	 * Metode som returnerere sikte pilen (AimArrow)
	 * 
	 * @return double vinkelen i grader. 
	 */
	AimArrow getAimArrow();
	
	/**
	 * Metode som returnerer pixelkoordinatet til midten av bobla som skal skytes.
	 * 
	 * @return Point2D.Double pixelkoordinatet til bobla. 
	 */
	Point2D.Double getCurrentBubblePixelCenter();
	
	/**
	 * Metode som returnere radiusen til boblene i forhold til størrelsen på skjermen.
	 * 
	 * @return double radiusen til boblene. 
	 */
	double getCurrentBubbleRadius();
	
	/**
	 * Metode som returnerer start pixelposisjonen til AimArrow altså siktet.
	 * 
	 * @return Point2D.Double start pixelkoordiantet til siktet. 
	 */
	Point2D.Double getAimArrowStartPoint();
	
	/**
	 * Metode som returnerer nåværende scoore.
	 * 
	 * @return int poengsummen. 
	 */
	int getScoore();
	
	/**
	 * Metode som finner antall bobler som er igjen i magasinet. 
	 *
	 * @return antall bobler igjen i magasinet. 
	 */
	int getMagSize();
	
	/**
	 * Metode som finner neste boble i magasinet.
	 * 
	 * @return Bubble neste boble som skal skytes og null hvis magasinet er tomt.
	 */
	Bubble getNextBubbleInMag();
}
