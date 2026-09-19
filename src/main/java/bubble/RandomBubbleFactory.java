package bubble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Representerer en fabrikk som lager en bubble med tilfeldig farge. 
 */
public class RandomBubbleFactory implements BubbleFactory{

	@Override
	public Bubble getNext() {
		ArrayList<Character> bubbleTypes = new ArrayList<>(Arrays.asList('L','G','P','B','R','Y'));
		Random random = new Random();
		int index = random.nextInt(bubbleTypes.size());
		Character newBubble = bubbleTypes.get(index);
		return new Bubble(newBubble);
		}
}
