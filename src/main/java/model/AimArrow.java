package model;

import java.awt.geom.Point2D;

import view.BubbleView;
import view.ViewableBubbleModel;

/**
 * Klassen representerer pilen man bruker til å sikte i spille "Bubble-Shooter".
 */
public class AimArrow {
	
	private double angleRaidians;
	
	/**
	 * Lager et AimArrow objekt
	 * @param angleRaidians start vinkelen til pila.
	 * @throws IllegalArgumentException hvis angleRadians ikke er mellom eller lik (-pi + 0.2) og (-0.2) 
	 */
	public AimArrow(double angleRaidians) {
		if (angleRaidians < (-Math.PI + 0.2) || angleRaidians > (-0.2)) {
			throw new IllegalArgumentException("angleRadians must be between (-pi + 0.2) and (-0.2)");
		}
		this.angleRaidians = angleRaidians;
	}
	
	/**
	 * Metode som flytter på hvor pilen peker basert på musepekeren.
	 * Vinkelen er gitt i radianer (fra atan2), og blir begrenset slik at pilen alltid peker oppover og ikke horisontalt.
	 * - 0 peker mot høyere.
	 * - negative vinkler peker oppover mot de andre boblene.
	 * - positive vinkler peker nedover. 
	 * Gyldig område for pilen er øvre halvsirkel altså [-pi, 0]
	 * 
	 * @param angle vinkelen som musepeekeren peker på i radianer;
	 */
	public void setAngle(double angle) {
		if (angle > -0.2 && angle < Math.PI / 2) {
			this.angleRaidians = -0.2;
		}
		else if (angle > 0.2) {
			this.angleRaidians = -Math.PI + 0.2;
		}
		else if (angle < -Math.PI +0.2) {
			this.angleRaidians = -Math.PI + 0.2;
		}
		else {
			this.angleRaidians = angle;
		}
	}
	
	/**
	 * get-metode som henter nåværende vinkel.
	 * 
	 * @return double som er nåværende vinkel. 
	 */
	public double getAngle() {
		return this.angleRaidians;
	}
	
	/**
	 * Metode som regner ut nåværende vinkel mellom muse-pekeren og ballen som skal skytes.
	 * Inspirert av løsning fra StackOverflow:
	 * Bruker atan2 for å beregne vinkel mellom to punkter.
	 * 
	 * @param mouseInPixelPos pixel-koordiantet til musepekeren i forhold til JPanel.
	 * @param BubbelView vinduet til spillet. 
	 * @throws IllegalArgumentException hvis et av argumentet er lik null.
	 * @return double vinkelen mellom startbobla og musepekeren.
	 */
	public double calculatAngle(Point2D.Double mouseInPixelPos, BubbleView bubbleView) {
		if (mouseInPixelPos == null) {
			throw new IllegalArgumentException("mouseInPixelPos can't be null");
		}
		if (bubbleView == null) {
			throw new IllegalArgumentException("bubbleView can't be null");
		}
		ViewableBubbleModel bubbleModel = bubbleView.getBubbleModel();
		Point2D.Double bubbleInPixelPos = bubbleModel.getCurrentBubblePixelCenter();
		// Source - https://stackoverflow.com/q/9970281
		// Posted by Aich
		// Retrieved 2026-04-10, License - CC BY-SA 3.0
		return Math.atan2(mouseInPixelPos.getY() - bubbleInPixelPos.getY(), mouseInPixelPos.getX() - bubbleInPixelPos.getX());
	}
	
	/**
	 * Metoden regner ut en retningsvektor utfra vinkelen til AimArrow og returnerer endepunktet
	 * som et pixelpunkt.
	 * 
	 * @param bubbleToShoot currentBubble startpunktet til vektoren.
	 * @param unitVector ønsket bevegelses retining av en vektor i R^2.
	 * @param length ønsket lengde på ehetsvektoren. 
	 * @throws IllegalArgumentException hvis bubbleToShoot eller unit vector er null og nå length <= 0.
	 * @return vectorEndPoint endepunktet til retiningsvektoren.
	 */
	public Point2D.Double calculateVectorEndPoint(Point2D.Double bubbleToShoot, Point2D.Double unitVector,
			double length) {
		if (bubbleToShoot == null || unitVector == null || length <= 0) {
			throw new IllegalArgumentException("bubbleToShoot and unitVector can't be null, and length must be greater than 0");
		}
		double newXPoint = bubbleToShoot.getX() + (unitVector.getX() * length);
		double newYPoint = bubbleToShoot.getY() + (unitVector.getY() * length);
		Point2D.Double vectorEndPoint = new Point2D.Double(newXPoint,newYPoint);
		return vectorEndPoint;
	}
	
	/**
	 * Metoden regner ut enhetsvektoren til en gitt vinkel, og lagrer verdiene
	 * i Point2D.Double objekt hvor x-koordinat er dx og y-koordinat er dy;
	 * 
	 * @param angle vinkelen.
	 * @return unitVector en enhetsvektor.
	 */
	public Point2D.Double getUnitVector(double angle) {
		double dx = Math.cos(angle);
		double dy = Math.sin(angle);
		Point2D.Double unitVector = new Point2D.Double(dx, dy);
		return unitVector;
	}
}