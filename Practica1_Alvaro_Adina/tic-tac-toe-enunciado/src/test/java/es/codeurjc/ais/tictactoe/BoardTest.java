package es.codeurjc.ais.tictactoe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import es.codeurjc.ais.tictactoe.Board;

public class BoardTest {

	Board tablero; 
	String x,o;
	
	@Before
	public void setUp() {
		x = "X";
		o = "O";
		tablero = new Board(); 
	}
	
	@Test
	public void testgetCellsIfWinner1() {
		
		tablero.getCell(0).value = x;
		tablero.getCell(1).value = o;
		tablero.getCell(3).value = x;
		tablero.getCell(7).value = o;
		tablero.getCell(6).value = x;
		
		int[] res = tablero.getCellsIfWinner(x);
		int[] res2 = tablero.getCellsIfWinner(o);
		assertNotNull(res);
		assertNull(res2);
		assertEquals(false,tablero.checkDraw());
		
	}
	
	@Test
	public void testgetCellsIfWinner2() {
		
		tablero.getCell(0).value = x;
		tablero.getCell(1).value = o;
		tablero.getCell(8).value = x;
		tablero.getCell(4).value = o;
		tablero.getCell(3).value = x;
		tablero.getCell(7).value = o;
		
		int[] res = tablero.getCellsIfWinner(o);
		int[] res2 = tablero.getCellsIfWinner(x);
		assertNotNull(res);
		assertNull(res2);
		assertEquals(false,tablero.checkDraw());
		
	}
	
	@Test
	public void testcheckDraw() {
		
		tablero.getCell(0).value = o;
		tablero.getCell(1).value = x;
		tablero.getCell(2).value = o;
		tablero.getCell(3).value = x;
		tablero.getCell(4).value = o;
		tablero.getCell(5).value = x;
		tablero.getCell(6).value = x;
		tablero.getCell(7).value = o;
		tablero.getCell(8).value = x;
		
		int[] res = tablero.getCellsIfWinner(x);
		int[] res2 = tablero.getCellsIfWinner(o);
		boolean empate = tablero.checkDraw(); 
		assertNull(res);
		assertNull(res2);
		assertTrue(empate);
	}
	
	
	
	

}
