package net.codingworks.sudoku;

import java.util.Random;

/**
 * Implementation of a backtracking algorithm which ranks empty cells by the
 * number of available values (possibilities). The empty cell with the least
 * number of possibilities gets selected in a random order. The selected cell is
 * filled with a value picked randomly from a list of possibilities.
 * 
 * @author Rongqin Sheng
 * @version 1.0
 */
public class Solver implements Stoppable {

	/**
	 * The input Board object
	 */
	private Board input;

	/**
	 * Flag for stopping the solver
	 */
	private boolean stopped = false;

	/**
	 * Constructor
	 * 
	 * @param input
	 *            an input Board object
	 */
	public Solver(Board input) {
		this.input = input;
	}

	/**
	 * Solve the puzzle
	 * 
	 * @return output Board object.
	 * @throws Exception
	 */
	public Board solve() throws Exception {
		input.updateCells();
		return solve(input, input.getNumberOfEmptyCells());
	}

	/**
	 * Solve the puzzle recursively
	 * 
	 * @param input
	 *            input Board object
	 * @param numberOfEmptyCells
	 *            number of empty cells
	 * @return output Board object
	 */
	private Board solve(Board input, int numberOfEmptyCells) {
		if (stopped) {
			return null;
		}
		Board ret = null;
		if (numberOfEmptyCells == 0) {
			return input;
		}
		int[] idx = selectCell(input);
		if (idx == null) {
			return input;
		}
		Cell cell = input.getCell(idx);
		byte rank = cell.getRank();
		byte[] availVal = new byte[rank];
		int cnt = 0;
		for (byte v = 1; v <= 9; v++) {
			if (cell.getAvailable(v)) {
				availVal[cnt] = v;
				cnt++;
			}
		}

		if (rank > 1) {
			shuffle(availVal);
		}

		for (byte i = 0; i < rank; i++) {
			byte v = availVal[i];
			Board board = input.copy(false);
			if (updateBoard(board, idx, v)) {
				Board result = solve(board, numberOfEmptyCells - 1);
				if (result != null)
					return result;
			}
		}
		return ret;
	}

	/**
	 * Shuffle a byte array.
	 */
	public static void shuffle(byte[] a) {
		Random random = new Random();
		for (int j = a.length - 1; j > 0; j--) {
			int r = random.nextInt(j + 1);
			byte b = a[j];
			a[j] = a[r];
			a[r] = b;
		}
	}

	/**
	 * Function called by a different thread to stop the solver.
	 */
	public synchronized void stop() {
		stopped = true;
	}

	/**
	 * Check if the solver is stopped.
	 * 
	 * @return true for stopped and false otherwise
	 */
	public boolean stopped() {
		return stopped;
	}

	/**
	 * Select the cell with the least number of possibilities in a random order
	 * 
	 * @param board
	 *            input Board object
	 * @return array index of selected Cell
	 */
	public static int[] selectCell(Board board) {
		byte rank = 10;
		int[] ret = new int[2];
		byte[] rankCount = new byte[10];
		for (byte m = 0; m < 9; m++) {
			for (byte n = 0; n < 9; n++) {
				Cell c = board.getCell(m, n);
				if (c.isEmpty()) {
					rankCount[c.getRank()]++;
				}
			}
		}

		for (byte i = 1; i <= 9; i++) {
			if (rankCount[i] > 0) {
				rank = i;
				break;
			}
		}

		if (rank == 10)
			return null;

		int randomCnt = 1;
		if (rankCount[rank] > 1) {
			randomCnt = new Random().nextInt(rankCount[rank]) + 1;
		}

		int cnt = 0;

		for (byte m = 0; m < 9; m++) {
			for (byte n = 0; n < 9; n++) {
				Cell c = board.getCell(m, n);
				if (c.isEmpty()) {
					byte r = c.getRank();
					if (r == rank) {
						cnt++;
						if (cnt == randomCnt) {
							ret[0] = m;
							ret[1] = n;
							break;
						}
					}
				}
			}
			if (cnt == randomCnt) {
				break;
			}
		}
		return ret;
	}

	/**
	 * Update the Board when an empty Cell is filled with a value. Empty cells
	 * in the row, column and box where the selected cell is are updated for
	 * their available values and ranks.
	 * 
	 * @param board
	 *            the Board to be updated
	 * @param idx
	 *            array index of the empty Cell to be filled with a value
	 * @param val
	 *            the value (1-9)
	 * @return true for success and false otherwise
	 */
	public static boolean updateBoard(Board board, int[] idx, byte val) {
		int i = idx[0];
		int j = idx[1];

		// update cell
		Cell cell = board.getCell(idx);
		cell.setValue(val);
		cell.setRank((byte) 0);
		for (byte v = 0; v <= 9; v++) {
			cell.setAvailable(v, false);
		}
		// update row
		for (byte n = 0; n < 9; n++) {
			if (n != j) {
				Cell c = board.getCell(i, n);
				if (c.getAvailable(val)) {
					c.setAvailable(val, false);
					c.setRank((byte) (c.getRank() - 1));
					if (c.getRank() == 0) {
						return false;
					}
				}
			}
		}

		// update column
		for (byte m = 0; m < 9; m++) {
			if (m != i) {
				Cell c = board.getCell(m, j);
				if (c.getAvailable(val)) {
					c.setAvailable(val, false);
					c.setRank((byte) (c.getRank() - 1));
					if (c.getRank() == 0) {
						return false;
					}
				}
			}
		}

		// update box
		int qRow = i / 3;
		int qCol = j / 3;

		for (int m = 3 * qRow; m < 3 * qRow + 3; m++) {
			for (int n = 3 * qCol; n < 3 * qCol + 3; n++) {
				if (m != i && n != j) {
					Cell c = board.getCell((byte) m, (byte) n);
					if (c.getAvailable(val)) {
						c.setAvailable(val, false);
						c.setRank((byte) (c.getRank() - 1));
						if (c.getRank() == 0) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Get a solution of a Sudoku puzzle. If there are multiple solutions, only
	 * one of them is returned.
	 * 
	 * @param inputStr
	 *            a input string listing cell values in row order
	 * @return a solution in the form of a string listing cell values in row
	 *         order. A null value is returned if there is no solution.
	 * @throws Exception
	 */
	public static String getSolution(String inputStr) throws Exception {
		String ret = null;
		Board board = new Board(inputStr);
		Solver solver = new Solver(board);
		Board result = solver.solve();
		if (result != null) {
			ret = result.toString();
		}
		return ret;
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err
					.println("Usage: java net.codingworks.sudoku.Solver <input string>");
			System.exit(1);
		}
		try {
			String result = getSolution(args[0]);
			if (result != null) {
				System.out.println(result);
				new Board(result).print();
			} else {
				System.out.println("No solution");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
}

