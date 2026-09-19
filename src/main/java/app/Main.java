package app;
import javax.swing.JFrame;

import bubble.BubbleFactory;
import bubble.RandomBubbleFactory;
import bubbleController.BubbleController;
import view.BubbleView;
import grid.Grid;
import model.BubbleModel;

/**
 * Starter programmet ved å opprette modell, kontroller og visning. 
 */
public class Main {
	/**Tittelen som vises i spillvinduet*/
	public static final String WINDOW_TITLE = "Bubble Shooter";
	/**
	 * Starter programmet og viser vinduet.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		//Lager spillbrettet og oppretter objekt som lager nye tetrominoer.
		Grid grid = new Grid(18, 17, '-');
		BubbleFactory bubbleFactory = new RandomBubbleFactory();
		BubbleModel model = new BubbleModel(grid,bubbleFactory);
		BubbleView view = new BubbleView(model);
		BubbleController controller = new BubbleController(model, view); 
		
		JFrame frame = new  JFrame(WINDOW_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setContentPane(view);
		frame.pack();
		frame.setVisible(true);
		}
}


