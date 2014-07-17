package net.codingworks.sudoku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for Solver
 * 
 * @author Rongqin Sheng
 * @version 1.0
 */
public class SolverTest {
	
	@Test
	public void testUpdateBoard() {
		String input = "009003060" 
	                 + "000040100" 
				     + "500100000" 
	                 + "090000020"
				     + "800000400" 
	                 + "027006009" 
				     + "000000000" 
	                 + "003002070"
				     + "000850600";
		Board board = new Board(input);
		try {
			board.updateCells();

			Cell c = board.getCell(3, 3);

			// before updateBoard
			assertTrue(c.getAvailable((byte) 5));
			assertEquals(4, c.getRank());

			int[] idx = new int[2];
			idx[0] = 5;
			idx[1] = 3;
			assertTrue(Solver.updateBoard(board, idx, (byte) 5));

			// after updateBoard
			assertTrue(!c.getAvailable((byte) 5));
			assertEquals(3, c.getRank());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testHasSolution() {
		try {
			String input = "100030080" 
		                 + "067580000" 
					     + "000100200"
					     + "030060590" 
					     + "650070000" 
					     + "708000002" 
					     + "000094000"
					     + "000050000"
					     + "900000410";
			String expectedSol = "145236789" 
					           + "267589134" 
					           + "389147256"
					           + "431862597" 
					           + "652973841" 
					           + "798415362" 
					           + "513794628"
					           + "824651973" 
					           + "976328415";
			Board board = new Board(input);
			Solver solver = new Solver(board);
			Board result = solver.solve();
			assertNotNull(result);
			assertEquals(expectedSol, result.toString());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testNoSolution() {
		try {

			String input = "100030086" 
			             + "067580000" 
					     + "000100200"
					     + "030060590" 
					     + "650070000" 
					     + "708000002" 
					     + "000094000"
					     + "000050000" 
					     + "900000410";
			Board board = new Board(input);
			Solver solver = new Solver(board);
			Board result = solver.solve();
			assertNull(result);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
