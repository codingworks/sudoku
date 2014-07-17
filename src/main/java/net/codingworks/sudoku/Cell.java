package net.codingworks.sudoku;

/**
 * Cell is the class for all units (cells) on a Sudoku board. A Cell object
 * encapsulates the state information needed to support exploration of Sudoku
 * puzzles.
 * 
 * @author Rongqin Sheng
 * @version 1.0
 */
public class Cell {

	/**
	 * Cell value. 1-9 for filled cells, 0 for empty cells.
	 */
	private byte value;

	/**
	 * This boolean array is indexed by cell values. A value i between 1 and 9
	 * can be filled in this cell iff available[i] = true. In addition,
	 * available[0] = true iff this cell is empty.
	 */
	private boolean[] available;

	/**
	 * Number of values (1-9) that can be filled in this cell.
	 */
	private byte rank;

	/**
	 * Constructor
	 * 
	 * @param value
	 *            cell value
	 */
	public Cell(byte value) {
		this.value = value;
		available = new boolean[10];
		if (value == 0) {
			available[0] = true;
		}
	}

	/**
	 * Set cell value
	 * 
	 * @param value
	 *            cell value
	 */
	public void setValue(byte value) {
		this.value = value;
	}

	/**
	 * Get cell value
	 * 
	 * @return cell value
	 */
	public byte getValue() {
		return value;
	}

	/**
	 * Set cell rank
	 * 
	 * @param rank
	 *            cell rank
	 */
	public void setRank(byte rank) {
		this.rank = rank;
	}

	/**
	 * Get cell rank
	 * 
	 * @return cell rank
	 */
	public byte getRank() {
		return rank;
	}

	/**
	 * Set availability of a cell value for filling this cell
	 * 
	 * @param val
	 *            cell value
	 * @param b
	 *            true for available and false otherwise
	 */
	public void setAvailable(byte val, boolean b) {
		available[val] = b;
	}

	/**
	 * Get availability of a cell value for filling this cell.
	 * 
	 * @param val
	 *            cell value
	 * @return available or not
	 */
	public boolean getAvailable(byte val) {
		return available[val];
	}

	/**
	 * Check if the cell is empty.
	 * 
	 * @return empty or not
	 */
	public boolean isEmpty() {
		return available[0];
	}

	/**
	 * Get a copy of the Cell object.
	 * 
	 * @return a copy of the Cell object
	 */
	public Cell copy() {
		Cell c = new Cell(value);
		for (byte val = 1; val <= 9; val++) {
			c.setAvailable(val, available[val]);
		}
		c.setRank(rank);
		return c;
	}
}
