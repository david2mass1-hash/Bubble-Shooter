package bubbleController;

import java.awt.geom.Point2D;

import model.AimArrow;
import model.GameState;

public interface ControllableBubbleModel {
	
	
	/**
	 * Metoden starter et skudd med currentBubble i den gitte vinkelen. Deretter bergenes det hvor bobla
	 * skal lande ved hjelp av simulering, lagre retningen til skuddet og sette bobla i bevegelse slik 
	 * at den oppdatteres i clockTick().
	 * 
	 * @param angle vinkelen (i grader) som bestemmer retningen på skuddet.
	 */
	void shootCurrentBubble(double angle);
	
	/**
	 * Metoden returnerer nåværende spilltilstand.
	 * 
	 * @return GameState spilltilstanden. 
	 */
	GameState getGameState();
	
	/**
	 * Metoden endrer nåværende spilltilstand til ACTIVE_GAME.
	 */
	void setGameStateToActiveGame();
	
	/**
	 * Metodn endrer nåværende spilltilstand til HOME_SCREEN.
	 */
	void setGameStateToHomeScreen();
	
	/**
	 * Metoden endrer nåværende spilltilstand til GAME_OVER
	 */
	void setGameStateToGameover();
	
	/**
	 * Metode som returnerer hvor mange millisekunder det skal være mellom hvert
	 * klokkeslag, altså hastigheten som modellen oppdateres. 
	 * 
	 * @return int antall milliskeunder mellom hver oppdatering av modellen. 
	 */
	int milliSecoundsPerSec();
	
	/**
	 * Metoden oppdaterer posisjonen til boblen som er i bevegelse ved hvert klokkeslag.
	 * Boblen beveger seg i retningen gitt bestemt av vinkelen ved avfyring og tar håndterer
	 * kolisjoner med vegger, tak og andre bobler. Ved treff med tak eller boble, limes bobla på 
	 * brettet, og modellen gjøres klar for et nytt skudd. 
	 */
	void clockTick();
	
	/**
	 * Metoden restarter spillet til start tilstand ved å opprette et nytt model objekt. 
	 */
	void resetGame();
	
	/**
	 * Metode som returnere modellen sin sikte pil (AimArrow).
	 * 
	 * @return double vinkel melllom 0 og 180 grader.
	 */
	AimArrow getAimArrow();
	
	/**
	 * Metode som endrer vinkelen til sikte pilen i spillet.
	 * 
	 * @param angle ny vinkel i radianer. 
	 */
	void setNewArrowAngel(double angle);
	
	/**
	 * Metode som oppdaterer venstre topphjørne til spillbrettet i pixelformat. 
	 * 
	 * @param pixelPoint topphjørnet til brettet. 
	 * @throws IllegalArgrumentException hvis pixelPoint er null.
	 */
	void setTopLeftPixelCorner(Point2D.Double pixelPoint);
	
	/**
	 * Metode som oppdaterer bredden til brettet i antall pixler.
	 * 
	 * @param width bredden i antall pixler. 
	 * @throws IndexOutOfBoundsException hvis width er mindre enn eller lik 0.
	 */
	void setPixelWidth(double width);
	
	/**
	 * Metode som oppdaterer høyden til brettet i antall pixler.
	 * 
	 * @param width bredden i antall pixler. 
	 * @throws IndexOutOfBoundsException hvis height er mindre enn eller lik 0.
	 */
	void setPixelHeight(double height);
	
	/**
	 * Metode som oppdaterer senter av currentBubble i pixelformat utfra spillvinduets størrelse.
	 * 
	 * @param pixelPoint senteret til currentBubble.
	 * @throws IllegalArgrumentException hvis pixelPoint er null.
	 */
	void setCurrentBubblePixelCenter(Point2D.Double pixelPoint);
	
	/**
	 * Metode som oppdaterer radiusen til boblene i antall pixler utfra spilvinduets størrelse.
	 * 
	 * @param radius radiusen i antall pixler.
	 */
	void setCurrentBubbleRadius(double radius);
 
}

