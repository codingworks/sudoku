package net.codingworks.sudoku;

/**
 * Board is the class for Sudoku puzzles. A Board object contains a
 * two-dimensional array of Cell objects.
 * 
 * @author Rongqin Sheng
 * @version 1.0
 */
public class Board {

	/**
	 * A two-dimensional array of Cell objects
	 */
	private Cell[][] cells;

	/**
	 * Constructor
	 * 
	 * @param cells
	 *            a given two-dimensional array of Cell objects
	 */
	public Board(Cell[][] cells) {
		this.cells = cells;
	}

	/**
	 * Constructor
	 * 
	 * @param inp
	 *            a string listing cell values in row order. Any character other
	 *            than 1-9 can be used for an empty cell.
	 */
	public Board(String inp) {
		cells = new Cell[9][9];
		int idx = 0;
		for (byte i = 0; i < 9; i++) {
			for (byte j = 0; j < 9; j++) {
				byte val = 0;
				if (idx < inp.length()) {
					try {
						val = Byte.parseByte(inp.substring(idx, idx + 1));
					} catch (NumberFormatException nfe) {
						val = 0;
					}
				}
				cells[i][j] = new Cell(val);
				idx++;
			}
		}
	}

	/**
	 * Get a cell
	 * 
	 * @param idx
	 *            two-dimensional array index
	 * @return a Cell object
	 */
	public Cell getCell(int[] idx) {
		return cells[idx[0]][idx[1]];
	}

	/**
	 * Get a cell
	 * 
	 * @param i
	 *            the first array index
	 * @param j
	 *            the second array index
	 * @return a Cell object
	 */
	public Cell getCell(int i, int j) {
		return cells[i][j];
	}

	/**
	 * Get a copy of this Board.
	 * 
	 * @param independent
	 *            true to create a deep copy, false to create a partially deep
	 *            copy where filled Cell objects are not duplicated.
	 * @return a Board object
	 */
	public Board copy(boolean independent) {
		Cell[][] cs = new Cell[9][9];
		for (byte m = 0; m < 9; m++) {
			for (byte n = 0; n < 9; n++) {
				Cell c = cells[m][n];
				if (independent) {
					cs[m][n] = c.copy();
				} else {
					if (!c.isEmpty()) {

						cs[m][n] = c;
					}

					else {
						cs[m][n] = c.copy();
					}
				}
			}
		}
		return new Board(cs);
	}

	/**
	 * Validate this Board
	 * 
	 * @throws Exception
	 */
	public void validate() throws Exception {
		// check rows
		boolean[] inRow = new boolean[10];
		for (byte i = 0; i < 9; i++) {
			for (byte v = 1; v <= 9; v++) {
				inRow[v] = false;
			}
			for (byte j = 0; j < 9; j++) {
				Cell c = cells[i][j];
				byte val = c.getValue();
				if (val > 0) {
					if (val < 1 || val > 9) {
						throw new Exception("Invalid cell value " + val
								+ " at row " + (i + 1));
					}
					if (inRow[val]) {
						throw new Exception("Duplicates at row " + (i + 1));
					} else {
						inRow[val] = true;
					}
				}
			}
		}

		// check columns
		boolean[] inCol = new boolean[10];
		for (byte j = 0; j < 9; j++) {
			for (byte v = 1; v <= 9; v++) {
				inCol[v] = false;
			}
			for (byte i = 0; i < 9; i++) {
				Cell c = cells[i][j];
				byte val = c.getValue();
				if (val > 0) {
					if (inCol[val]) {
						throw new Exception("Duplicates at column " + (j + 1));
					} else {
						inCol[val] = true;
					}
				}
			}
		}

		// check boxes
		boolean[] inBox = new boolean[10];
		for (byte hShift = 0; hShift < 3; hShift++) {
			for (byte vShift = 0; vShift < 3; vShift++) {
				for (byte v = 1; v <= 9; v++) {
					inBox[v] = false;
				}
				for (byte i = 0; i < 3; i++) {
					for (byte j = 0; j < 3; j++) {
						int m = i + 3 * hShift;
						int n = j + 3 * vShift;
						Cell c = cells[m][n];
						byte val = c.getValue();
						if (val > 0) {
							if (inBox[val]) {
								throw new Exception(
										"Duplicates in box having row "
												+ (m + 1) + " and column "
												+ (n + 1));
							} else {
								inBox[val] = true;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Validate all cells and update empty cells.
	 * 
	 * @throws Exception
	 */
	public void updateCells() throws Exception {
		validate();
		for (byte i = 0; i < 9; i++) {
			for (byte j = 0; j < 9; j++) {
				if (cells[i][j].isEmpty()) {
					updateCell(i, j);
				}
			}
		}
	}

	/**
	 * Update an empty cell.
	 * 
	 * @param i
	 *            the first array index
	 * @param j
	 *            the second array index
	 * @throws Exception
	 */
	private void updateCell(byte i, byte j) throws Exception {
		Cell cell = cells[i][j];
		if (cell.isEmpty()) {
			// check row
			boolean[] inRow = new boolean[10];
			for (byte n = 0; n < 9; n++) {
				if (n != j) {
					Cell c = cells[i][n];
					if (!c.isEmpty()) {
						byte v = c.getValue();
						if (inRow[v]) {
							throw new Exception("Duplicates at row " + (i + 1));
						} else {
							inRow[v] = true;
						}
					}
				}
			}

			// check column
			boolean[] inCol = new boolean[10];
			for (byte m = 0; m < 9; m++) {
				if (m != i) {
					Cell c = cells[m][j];
					if (!c.isEmpty()) {
						byte v = c.getValue();
						if (inCol[v]) {
							throw new Exception("Duplicates at column "
									+ (j + 1));
						} else {
							inCol[v] = true;
						}
					}
				}
			}

			// check box
			boolean[] inBox = new boolean[10];
			int qRow = i / 3;
			int qCol = j / 3;

			for (int m = 3 * qRow; m < 3 * qRow + 3; m++) {
				for (int n = 3 * qCol; n < 3 * qCol + 3; n++) {
					if (m != i || n != j) {
						Cell c = cells[m][n];
						if (!c.isEmpty()) {
							byte v = c.getValue();
							if (inBox[v]) {
								throw new Exception(
										"Duplicates in box having row "
												+ (i + 1) + " and column "
												+ (j + 1));
							} else {
								inBox[v] = true;
							}
						}
					}
				}
			}

			byte rank = 0;
			for (byte v = 1; v <= 9; v++) {
				if (inRow[v] || inCol[v] || inBox[v]) {
					cell.setAvailable(v, false);
				} else {
					cell.setAvailable(v, true);
					rank++;
				}
			}

			if (rank > 0) {
				cell.setRank(rank);
			} else {
				throw new Exception("No value can be set at row " + (i + 1)
						+ " and column " + (j + 1));
			}
		}
	}

	/**
	 * Get the number of empty cells.
	 * 
	 * @return the number of empty cells
	 */
	public int getNumberOfEmptyCells() {
		int numberOfCellsToFill = 81;
		for (byte i = 0; i < 9; i++) {
			for (byte j = 0; j < 9; j++) {
				if (!cells[i][j].isEmpty()) {
					numberOfCellsToFill--;
				}
			}
		}
		return numberOfCellsToFill;
	}

	/**
	 * Print this Board.
	 */
	public void print() {
		for (byte m = 0; m < 9; m++) {
			for (byte n = 0; n < 9; n++) {
				Cell c = cells[m][n];
				byte val = c.getValue();
				if (val > 0) {
					System.out.print(c.getValue() + "  ");
				} else {
					System.out.print("*  ");
				}
				if (n == 2 || n == 5) {
					System.out.print("| ");
				}
				if (n == 8)
					System.out.println("");
			}
			if (m == 2 || m == 5) {
				System.out.println("-----------------------------");
			}
		}
	}

	/**
	 * Get the 81-character string listing cell values in row order. Use 0 for
	 * empty cells.
	 * 
	 * @return a string representation
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (byte m = 0; m < 9; m++) {
			for (byte n = 0; n < 9; n++) {
				sb.append(cells[m][n].getValue());
			}
		}
		return sb.toString();
	}
}
