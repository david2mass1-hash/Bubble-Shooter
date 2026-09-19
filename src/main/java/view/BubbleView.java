package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import grid.GridDimension;
import model.AimArrow;
import model.GameState;
import grid.GridCell;
import grid.CellCoordinate;
import bubble.Bubble;

import javax.swing.JPanel;



public class BubbleView extends JPanel {
	
	/**Avastand mellom brettet og kanten av vinduet. */
    public static final int OUTERMARGIN = 40;
    
    /**Standard sidestørrelse for cellene */
    public static final int PREFERREDSIDESIZE = 30;

    private ColorTheme colorTheme;
    private ViewableBubbleModel bubbleModel;
    
    /**
     * Konstuktør som lager en ny visning for "Bubble-Shooter".
     * 
     * @param tetrisModel som er modellen som skal tegnes. 
     */
    public BubbleView(ViewableBubbleModel bubbleModel) {
    	this.bubbleModel = bubbleModel;
        this.colorTheme = new DefaultColorTheme();
        this.setBackground(colorTheme.getBackgroundColor());
        this.setFocusable(true);
        this.setPreferredSize(getDefaultSize(bubbleModel.getDimension()));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        drawGame(g2);
    }
    
    /**
     * Get-Metode for å hente nåværende bubbleModel, brukes til å regne ut vinkelen til AimArrow
     * @return ViewableBubbleModel spillmodellen.
     */
    public ViewableBubbleModel getBubbleModel() {
    	return this.bubbleModel;
    }
    
    /**
     * Metoden lager et Rectangle2D objekt som er spillvinduet i spillet, hvor oddetallsrader
     * er forskjøvet til høyere med en boble radius. 
     * 
     * @return Rectangle2D.Double rektangelet hvor spillet tegnes. 
     */
    public Rectangle2D getShiftedGameWindowSize() {
        int currentWidth = this.getWidth() - 2 * OUTERMARGIN;
        int currentHeight = this.getHeight() - 2 * OUTERMARGIN;
        int cols = this.bubbleModel.getDimension().getNumCols();
        int rows = this.bubbleModel.getDimension().getNumRows();
        // ekstra bredde siden annenhver rad er forskjøvet
        int cellWidth = currentWidth / (cols + 1);
        int cellHeight = currentHeight / rows;
        int cellSize = Math.min(cellWidth, cellHeight);
        // legger til (cellSize / 2), for å få plass til forskyvningen
        int boxWidth = (int) (cellSize * cols + cellSize / 2.0);
        int boxHeight = cellSize * rows;
        int x = (this.getWidth() - boxWidth) / 2;
        int y = (this.getHeight() - boxHeight) / 2;
        return new Rectangle2D.Double(x, y, boxWidth, boxHeight);
    }
    
    /**
     * Get-metode for å hente nåværende radius til boblene basert på spillvinduets størrelse.
     * 
     * @return radius radiusen til boblene i spillet i antall pixler. 
     */
    public double getBubbleRadius() {
    	Rectangle2D box = getShiftedGameWindowSize();
    	CellPositionToPixelConverter converter = new CellPositionToPixelConverter(box, bubbleModel.getDimension());
    	return converter.getCurrentBubbleRadius();
    }
  
    
    private void drawGame(Graphics2D g2) {
    	if (this.bubbleModel.getGameState() == GameState.ACTIVE_GAME) {
    		// Tegner bakgrunnen til hele vinduet
    	    Rectangle2D inGameBackground = new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight());
    	    g2.setColor(Color.LIGHT_GRAY);
    	    g2.fill(inGameBackground);
    	    // Tegner boksen hvor selve spillet vises
    	    Rectangle2D box = getShiftedGameWindowSize();
    	    g2.setColor(new Color(15, 15, 25));
    	    g2.fill(box);

    	    // Tegner bobblene i brettet.
    	    CellPositionToPixelConverter converter = new  CellPositionToPixelConverter(box, 
    	    bubbleModel.getDimension());
    	    drawCells(g2, bubbleModel.getTilesOnBoard(), converter, colorTheme);
    	    
    	    // Tegner current bubble gameOver grense og pil
    	    Line2D.Double gameOverLine = new Line2D.Double(box.getX(), (box.getHeight() + getBubbleRadius()),
    	    		(box.getX() + box.getWidth()), (box.getHeight() + getBubbleRadius()));
    	    g2.setColor(Color.lightGray);
    	    g2.draw(gameOverLine);
    	    drawCurrentBubble(g2, bubbleModel, colorTheme);
    	    drawAimArrow(g2,bubbleModel.getAimArrow());
    	    
    	    //Tegner boksen under spillet hvor det står scoore, bobler igjen i magasinet og fargen på neste boble.
    	    Rectangle2D gameVariables = new Rectangle2D.Double(box.getX(), (box.getY() + box.getHeight()), box.getWidth(),
    	    		OUTERMARGIN);
    	    g2.setColor(Color.PINK);
    	    g2.fill(gameVariables);
    	    double bubbleDiameter = (OUTERMARGIN - 10);
    	    int bubblesLeftInMag = bubbleModel.getMagSize();
    	    double bubbleStartY = gameVariables.getCenterY() - (bubbleDiameter / 2);
    	    double bubbleStartX = gameVariables.getX() + bubbleDiameter;
    	    double spacing = bubbleDiameter / 2;
    	    int i = 0;
    	    int j = 0;
    	    while (i < bubblesLeftInMag) {
    	    	if (j == 0) {
    	    		Ellipse2D.Double nextBubble = new Ellipse2D.Double((bubbleStartX + spacing * 3 * i),
    	    				bubbleStartY, bubbleDiameter, bubbleDiameter);
    	    		Character symbol = bubbleModel.getNextBubbleInMag().getSymbol();
    	    		g2.setColor(colorTheme.getCellColor(symbol));
    	    		g2.fill(nextBubble);
    	    	}
    	    	else {
    	    		Ellipse2D.Double bubble = new Ellipse2D.Double((bubbleStartX + spacing * 3 * i),
    	    				bubbleStartY, bubbleDiameter, bubbleDiameter);
        	    	g2.setColor(Color.DARK_GRAY);
        	    	g2.fill(bubble);
    	    	}
    	    	i += 1;
    	    	j += 1;
    	    }
    	    Rectangle2D scooreBox = new Rectangle2D.Double((box.getX() + box.getWidth() * 0.7),
    	    		(box.getY() + box.getHeight()), (box.getWidth() * 0.25), gameVariables.getHeight());
        	g2.setColor(Color.DARK_GRAY);
        	g2.setFont(new Font("Arial", Font.BOLD, 25));
        	Inf101Graphics.drawCenteredString(g2,
        			"Scoore: " + bubbleModel.getScoore(),
        			scooreBox.getCenterX(),
        			scooreBox.getY() + scooreBox.getHeight() /1.5);
    	}
    	
    	if (this.bubbleModel.getGameState() == GameState.GAME_OVER) {
    		//Tegner Game-Over teksten.
        	Rectangle2D gameOverBackground = new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight());
        	g2.setColor(Color.ORANGE);
        	g2.fill(gameOverBackground);
        	g2.setColor(Color.DARK_GRAY);
        	g2.setFont(new Font("Arial", Font.BOLD, 40));
        	Inf101Graphics.drawCenteredString(g2, "GAME OVER", gameOverBackground);
        	//skriver scoore og key info. 
        	Rectangle2D scooreBox = new Rectangle2D.Double(this.getWidth() * 0.25,
        			this.getHeight() *0.2, this.getWidth() *0.5, this.getHeight() * 0.1);
        	g2.setFont(new Font("Arial", Font.BOLD, 40));
        	Inf101Graphics.drawCenteredString(g2, "Your score was: " + this.bubbleModel.getScoore(),
        			scooreBox);
        	Rectangle2D infoBox = new Rectangle2D.Double((this.getWidth() - this.getWidth() * 0.4) / 2,
        		    this.getHeight() * 0.6, this.getWidth() * 0.4, this.getHeight() * 0.12);
        	g2.setColor(Color.DARK_GRAY);
        	g2.setFont(new Font("Arial", Font.BOLD, 20));
        	Inf101Graphics.drawCenteredString(g2, "Press (R) to restart game or (H) to return to homescreen", infoBox);
        	
    	}
    	
    	if (this.bubbleModel.getGameState() == GameState.HOME_SCREEN) {
    		//Bakgrunn
    		Rectangle2D homeScreenBackGround = new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight());
    		g2.setColor(Color.ORANGE);
    		g2.fill(homeScreenBackGround);
    		
        	//Tegner boksen med navnet Bubble-Shooter.
    		double boxWidth = this.getWidth() * 0.8;
    		double boxHeight = this.getHeight() *0.25;
    		double x = (this.getWidth() - boxWidth) / 2;
    		double y = this.getHeight() * 0.2;
        	Rectangle2D gameNameOnHomeScreen = new Rectangle2D.Double(x, y, boxWidth, boxHeight);
        	g2.setColor(Color.DARK_GRAY);
        	g2.setFont(new Font("Arial", Font.BOLD, 60));
        	Inf101Graphics.drawCenteredString(g2, "Bubble - Shooter", gameNameOnHomeScreen);
        	
        	//Tegner "Press "Space" to start".
        	g2.setColor(Color.DARK_GRAY);
        	g2.setFont(new Font("Arial", Font.BOLD, 30));
        	Inf101Graphics.drawCenteredString(g2,
        			"Press 'SPACE' to start game",
        			homeScreenBackGround.getCenterX(),
        			homeScreenBackGround.getY() + homeScreenBackGround.getHeight() /1.5);
        
        	//Tegner 3 bobler på startskjermen. 
        	double diameter = 70;
        	double centerX = this.getWidth() / 2;
        	double centerY = this.getHeight() / 2;
        	Ellipse2D.Double toppBubble = new Ellipse2D.Double(centerX - (diameter / 2), centerY - 90,
        			diameter, diameter);
        	g2.setColor(Color.MAGENTA);
        	g2.fill(toppBubble);
        	Ellipse2D.Double leftBubble = new Ellipse2D.Double(centerX - diameter - 10,
        			centerY, diameter, diameter);
        	g2.setColor(Color.RED);
        	g2.fill(leftBubble);
        	Ellipse2D.Double bubbleRight = new Ellipse2D.Double(centerX + 10, centerY, diameter, diameter);
        	g2.setColor(Color.BLUE);
        	g2.fill(bubbleRight);
    	}
    }
    
    private static void drawCells(Graphics2D g2, Iterable<GridCell> iterable,
            CellPositionToPixelConverter converter, ColorTheme colorTheme) {
    	for (GridCell cell : iterable) {
    		Character symbol = cell.symbol();
    		if (symbol != '-') {
    			CellCoordinate cellPos = cell.pos();
    			Ellipse2D cellSpace = converter.getBoundsForCell(cellPos);
    			Color color = colorTheme.getCellColor(symbol);
    			g2.setColor(color);
    			g2.fill(cellSpace);
    		}
		}
    }
    
    private static void drawCurrentBubble(Graphics2D g2, ViewableBubbleModel bubbleModel,
            ColorTheme colorTheme) {
        Bubble bubble = bubbleModel.getCurrentBubble();
        Point2D.Double center = bubbleModel.getCurrentBubblePixelCenter();
        if (bubble == null || center == null) {
        	return; 
        }
        double radius = bubbleModel.getCurrentBubbleRadius();
        double x = center.getX() - radius;
        double y = center.getY() - radius;
        double diameter = 2 * radius;
        Ellipse2D cellSpace = new Ellipse2D.Double(x, y, diameter, diameter);
        Character symbol = bubble.getSymbol();
        Color color = colorTheme.getCellColor(symbol);
        g2.setColor(color);
        g2.fill(cellSpace);
    }
    
    private void drawAimArrow(Graphics2D g2, AimArrow aimArrow) {
    	Point2D.Double bubbleCenter = bubbleModel.getAimArrowStartPoint();
    	if (aimArrow == null || bubbleCenter == null) {
        	return; 
        }
    	double currentAngle = aimArrow.getAngle();
    	// Regner ut parametrisk form for en linje (Fra MAT121 LineærAlgebra).
        double x1 = bubbleCenter.getX() + 100 * Math.cos(currentAngle);
        double y1 = bubbleCenter.getY() + 100 * Math.sin(currentAngle);
    	Line2D.Double line = new Line2D.Double(bubbleCenter.getX(), bubbleCenter.getY(), x1, y1);
    	g2.setColor(Color.WHITE);
    	g2.draw(line);
    }
    
    //Beregning av standardstørrelse på brettet basert på antall rader/kolonner, celle-størrelse og marginer. 
    private static Dimension getDefaultSize(GridDimension gd) {
        int width = (int) (PREFERREDSIDESIZE * gd.getNumCols() + (gd.getNumCols() + 1) + 2 * OUTERMARGIN);
        int height = (int) (PREFERREDSIDESIZE * gd.getNumRows() + (gd.getNumRows() + 1) + 2 * OUTERMARGIN);
        return new Dimension(width, height);
    }
}