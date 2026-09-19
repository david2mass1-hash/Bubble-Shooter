package view;


import java.awt.Color;

public class DefaultColorTheme implements ColorTheme {

    @Override
    public Color getCellColor(Character c) {
        Color color = switch(c) {
            case 'R' -> Color.RED;
            case 'G' -> Color.GREEN;
            case 'Y' -> Color.YELLOW;
            case 'B' -> Color.BLUE;
            case 'W' -> Color.WHITE;
            case 'L' -> Color.CYAN;
            case 'J' -> Color.BLUE;
            case 'S' -> Color.GREEN;
            case 'Z' -> Color.RED;
            case 'T' -> Color.PINK;
            case 'I' -> Color.MAGENTA;
            case 'O' -> Color.YELLOW;
            case 'P' -> Color.MAGENTA;
            case '-' -> Color.BLACK;
            default -> throw new IllegalArgumentException(
                    "No available color for '" + c + "'");
        };
        return color;
    }

    @Override
    public Color getFrameColor() {
        return new Color(0, 0, 0, 0);
    }

    @Override
    public Color getBackgroundColor() {
        return null;
    }

	@Override
	public Color getPopUpColor() {
		return new Color(0, 0, 0, 128);
	}

}