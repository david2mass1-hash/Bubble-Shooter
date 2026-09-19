package bubble;

import java.util.ArrayList;
import java.util.Arrays;

import grid.CellCoordinate;


/**
 * Representerer boblene i spillet "Bubble-Shooter". 
 */
public class Bubble {
	
	private Character symbol;
	private CellCoordinate pos;
	
	/**
	 * Oppretter et nytt Bubble objekt.
	 * 
	 * @param symbol ønsket symbol på Bobla
	 * @throws IllegalArgumentException hvis symbol er null eller ikke gyldig symbol.
	 */
	public Bubble(Character symbol) {
		ArrayList<Character> legalSymbols = new ArrayList<>(Arrays.asList('L','G','P','B','R','Y'));
		if(!legalSymbols.contains(symbol) || symbol == null) {
			throw new IllegalArgumentException("symbol can't be null and must be either L, G, P, B, R, Y" );
		}
		this.symbol = symbol;
		this.pos = new CellCoordinate(17, 9);
	}
	
	/**
	 * Metoden endrer posisjonen til bobla
	 * 
	 * @param pos poisjonen man øsnker å endre til.
	 * @throws IllegalArgumentException hvis pos er null.
	 */
	public void changePos(CellCoordinate pos) {
		if (pos == null) {
			throw new IllegalArgumentException("pos can't be null");
		}
		this.pos = pos;
	}
	
	/**
	 * Metoden finner posisjonen til en boble.
	 * 
	 * @return CellCoordinate som er posisjonen til bobla.
	 */
	public CellCoordinate getPos() {
		int rows = this.pos.row();
		int cols = this.pos.col();
		return new CellCoordinate(rows, cols);
	}
	
	/**
	 * Metoden finner symbolet til en boble.
	 * 
	 * @ Character som er verdien til bobla. 
	 */
	public Character getSymbol() {
		return this.symbol;
		
	}

}
	