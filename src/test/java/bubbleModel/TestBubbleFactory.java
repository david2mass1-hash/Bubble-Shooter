package bubbleModel;

import bubble.Bubble;
import bubble.BubbleFactory;

public class TestBubbleFactory implements BubbleFactory {

	@Override
	public Bubble getNext() {
		return new Bubble('R');
	}

}
