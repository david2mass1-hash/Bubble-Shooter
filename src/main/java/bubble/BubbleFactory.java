package bubble;

/**
 * Representerer en fabrikk som lager nye bubbles.
 */
public interface BubbleFactory {
	
	/**
	 * Metoden oppretter et nytt Bubble objekt utfra en mengde med gyldige symboler.
	 * 
	 * @return Bubble ny boble.
	 */
	public Bubble getNext();
}

