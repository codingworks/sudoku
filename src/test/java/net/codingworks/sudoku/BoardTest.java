package net.codingworks.sudoku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
/**
 * Unit tests for Board
 * 
 * @author Rongqin Sheng
 * @version 1.0
 */
public class BoardTest {
	private String inputStr;

	@Before
	public void setUp() throws Exception {
		inputStr = "100030080" 
	             + "067580000" 
				 + "000100200" 
	             + "030060590"
				 + "650070000" 
	             + "708000002" 
				 + "000094000" 
	             + "000050000"
				 + "900000410";
	}

	@Test
	public void testConstructor() {
		Board board = new Board(inputStr);
		assertEquals(8, board.getCell(0, 7).getValue());
		assertEquals(9, board.getCell(8, 0).getValue());
		assertEquals(0, board.getCell(8, 1).getValue());
	}

	@Test
	public void testUpdateCells() {
		try {
			Board board = new Board(inputStr);
			board.updateCells();
			Cell c = board.getCell(1, 0);
			assertTrue(!c.getAvailable((byte) 1));
			assertTrue(c.getAvailable((byte) 2));
			assertTrue(c.getAvailable((byte) 3));
			assertTrue(c.getAvailable((byte) 4));
			assertTrue(!c.getAvailable((byte) 5));
			assertTrue(!c.getAvailable((byte) 6));
			assertTrue(!c.getAvailable((byte) 7));
			assertTrue(!c.getAvailable((byte) 8));
			assertTrue(!c.getAvailable((byte) 9));

			assertEquals(3, c.getRank());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	private void testCopy(boolean independent) {
		try {
			Board board = new Board(inputStr);
			board.updateCells();
			Board copy = board.copy(independent);

			assertEquals(8, copy.getCell(0, 7).getValue());
			assertEquals(9, copy.getCell(8, 0).getValue());
			assertEquals(0, copy.getCell(8, 1).getValue());

			Cell c = board.getCell(1, 0);
			System.out.println(c.getRank());
			assertTrue(!c.getAvailable((byte) 1));
			assertTrue(c.getAvailable((byte) 2));
			assertTrue(c.getAvailable((byte) 3));
			assertTrue(c.getAvailable((byte) 4));
			assertTrue(!c.getAvailable((byte) 5));
			assertTrue(!c.getAvailable((byte) 6));
			assertTrue(!c.getAvailable((byte) 7));
			assertTrue(!c.getAvailable((byte) 8));
			assertTrue(!c.getAvailable((byte) 9));
			assertEquals(3, c.getRank());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testCopy() {
		testCopy(true);
		testCopy(false);
	}

	private void testValidate(String input, String starts) {
		try {
			new Board(input).validate();
			fail("Should have thrown Exception");
		} catch (Exception e) {
			String errMsg = e.getMessage();
			assertNotNull(errMsg);
			assertTrue(errMsg.startsWith(starts));
		}
	}

	@Test
	public void testValidate() {
		testValidate("108030080" 
	               + "067580000" 
				   + "000100200"
	               + "030060590"
				   + "650070000" 
	               + "708000002" 
				   + "000094000" 
	               + "000050000"
				   + "900000410", "Duplicates at row");
		testValidate("100030080" 
				   + "067580000" 
				   + "700100200" 
				   + "030060590"
				   + "650070000" 
				   + "708000002" 
				   + "000094000" 
				   + "000050000"
				   + "900000410", "Duplicates at column");
		testValidate("106030080" 
				   + "067580000" 
				   + "000100200" 
				   + "030060590"
				   + "650070000" 
				   + "708000002" 
				   + "000094000" 
				   + "000050000"
				   + "900000410", "Duplicates in box");
	}
}
