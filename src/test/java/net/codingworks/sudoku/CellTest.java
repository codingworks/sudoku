package net.codingworks.sudoku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit tests for Cell
 * 
 * @author Rongqin Sheng
 * @version 1.0
 */
public class CellTest {

	@Test
	public void testConstructor() {
		assertEquals(0, new Cell((byte) 0).getValue());
		assertEquals(1, new Cell((byte) 1).getValue());
	}

	@Test
	public void testValue() {
		Cell c = new Cell((byte) 0);
		c.setValue((byte) 2);
		assertEquals(2, c.getValue());
	}

	@Test
	public void testAvailable() {
		Cell c = new Cell((byte) 0);
		c.setAvailable((byte) 3, true);
		assertTrue(c.getAvailable((byte) 3));
		c.setAvailable((byte) 6, false);
		assertTrue(!c.getAvailable((byte) 6));
	}

	@Test
	public void testRank() {
		Cell c = new Cell((byte) 0);
		c.setAvailable((byte) 3, true);
		c.setRank((byte) 1);
		assertEquals(1, c.getRank());
	}

	@Test
	public void testIsEmpty() {
		assertTrue(new Cell((byte) 0).isEmpty());
		assertTrue(!new Cell((byte) 1).isEmpty());
	}

	@Test
	public void testCopy() {
		Cell c0 = new Cell((byte) 0);
		c0.setAvailable((byte) 3, true);
		c0.setRank((byte) 1);
		Cell c = c0.copy();
		assertEquals(c0.getValue(), c.getValue());
		assertEquals(c0.getRank(), c.getRank());
		for (byte i = 0; i <= 9; i++) {
			assertEquals(c0.getAvailable(i), c.getAvailable(i));
		}
	}
}
