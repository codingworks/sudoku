package net.codingworks.sudoku;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 * GUI for exploring Sudoku puzzles
 * 
 * @author Rongqin Sheng
 * @version 1.0
 */
@SuppressWarnings("serial")
public class MainGUI extends JFrame implements ActionListener {

	JPanel board, progress, control;
	JPanel[][] boxes;
	JTextField[][] cells;
	JButton solve, step, clear, imp, analyze;
	Font font = new Font("SansSerif", Font.BOLD, 20);
	Timer timer = new Timer();

	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
			UIManager.put("swing.boldMetal", Boolean.FALSE);
			JFrame.setDefaultLookAndFeelDecorated(true);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		JFrame mainWindow = new MainGUI();
		mainWindow.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent wEvt) {
				System.exit(0);
			}
		});
		mainWindow.setVisible(true);
	}

	public MainGUI() {
		super("Sudoku");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(new Dimension(500, 550));

		board = new JPanel(new GridLayout(3, 3));
		boxes = new JPanel[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				boxes[i][j] = new JPanel(new GridLayout(3, 3));
				boxes[i][j].setBorder(BorderFactory
						.createLineBorder(Color.black));
				board.add(boxes[i][j]);
			}
		}

		cells = new JTextField[9][9];

		for (int hShift = 0; hShift < 3; hShift++) {
			for (int vShift = 0; vShift < 3; vShift++) {
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						int m = i + 3 * hShift;
						int n = j + 3 * vShift;
						cells[m][n] = new JFormattedTextField();
						cells[m][n].addKeyListener(new KeyListener() {
							public void keyReleased(KeyEvent ke) {
								JTextField jtf = (JTextField) ke.getComponent();
								String str = jtf.getText().trim();
								int i = 0;
								try {
									i = Integer.parseInt(str);
								} catch (NumberFormatException e) {
									jtf.setText("");
								}
								if (i < 1 || i > 9) {
									jtf.setText("");
								}
							}

							public void keyPressed(KeyEvent ke) {
							}

							public void keyTyped(KeyEvent ke) {
							}
						});
						cells[m][n].setFont(font);
						cells[m][n].setHorizontalAlignment(JTextField.CENTER);
						boxes[hShift][vShift].add(cells[m][n]);
					}
				}
			}
		}

		add(board, BorderLayout.CENTER);

		control = new JPanel();
		solve = new JButton("Solve");
		solve.addActionListener(this);
		control.add(solve);
		step = new JButton("Step");
		step.addActionListener(this);
		control.add(step);
		clear = new JButton("Clear");
		clear.addActionListener(this);
		control.add(clear);
		imp = new JButton("Import");
		imp.addActionListener(this);
		control.add(imp);
		analyze = new JButton("Analyze");
		analyze.addActionListener(this);
		control.add(analyze);
		add(control, BorderLayout.PAGE_END);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		if (cmd.equals("Solve") || cmd.equals("Step")) {
			Cell[][] cs = new Cell[9][9];
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					String str = cells[i][j].getText();
					if (str != null && str.trim().length() == 1) {

						cs[i][j] = new Cell(Byte.parseByte(str));
					} else {
						cs[i][j] = new Cell((byte) 0);
						cells[i][j].setForeground(Color.BLUE);
					}
				}
			}

			try {
				Board input = new Board(cs);
				if (input.getNumberOfEmptyCells() == 0) {
					input.validate();
					return;
				}
				Solver solver = new Solver(input);

				TimerTask stopper = new Stopper(solver);
				timer.schedule(stopper, 3000);
				Board result = solver.solve();

				if (!solver.stopped()) {
					stopper.cancel();
					timer.purge();
				}

				if (solver.stopped()) {
					JOptionPane.showMessageDialog(this,
							"Timed out. It's too difficult.");
				} else if (result == null) {
					JOptionPane.showMessageDialog(this, "No solution");
				} else {
					if (cmd.equals("Solve")) {
						for (byte i = 0; i < 9; i++) {
							for (byte j = 0; j < 9; j++) {
								if (input.getCell(i, j).isEmpty()) {
									cells[i][j].setText(""
											+ result.getCell(i, j).getValue());
								}
							}
						}
					} else {
						int[] idx = Solver.selectCell(input);
						cells[idx[0]][idx[1]].setText(""
								+ result.getCell(idx[0], idx[1]).getValue());
					}
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} else if (cmd.equals("Clear")) {
			for (byte i = 0; i < 9; i++) {
				for (byte j = 0; j < 9; j++) {
					cells[i][j].setForeground(Color.BLACK);
					cells[i][j].setText("");
				}
			}
		} else if (cmd.equals("Import")) {

			int messageType = JOptionPane.INFORMATION_MESSAGE;
			String answer = JOptionPane
					.showInputDialog(
							this,
							"Enter a string listing all cells in row order."
									+ "Use any character other than 1-9 for a blank cell.",
							"Import a Sudoku", messageType);

			if (answer != null) {
				int idx = 0;
				for (byte i = 0; i < 9; i++) {
					for (byte j = 0; j < 9; j++) {
						byte b = 0;
						if (idx < answer.length()) {
							try {
								b = Byte.parseByte(answer.substring(idx,
										idx + 1));
							} catch (NumberFormatException nfe) {
								b = 0;
							}
						}
						if (b > 0) {
							cells[i][j].setText("" + b);
						} else {
							cells[i][j].setText("");
						}
						cells[i][j].setForeground(Color.BLACK);
						idx++;
					}
				}
			}
		} else if (cmd.equals("Analyze")) {
			Cell[][] cs = new Cell[9][9];
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					String str = cells[i][j].getText();
					if (str != null && str.trim().length() == 1) {

						cs[i][j] = new Cell(Byte.parseByte(str));
					} else {
						cs[i][j] = new Cell((byte) 0);
						cells[i][j].setForeground(Color.BLUE);
					}
				}
			}

			try {
				Board input = new Board(cs);
				if (input.getNumberOfEmptyCells() == 0) {
					input.validate();
					return;
				}
				Analyzer analyzer = new Analyzer(input);

				TimerTask stopper = new Stopper(analyzer);
				timer.schedule(stopper, 5000);
				String msg = analyzer.getReport();

				if (!analyzer.stopped()) {
					JOptionPane.showMessageDialog(this, msg, "Report",
							JOptionPane.INFORMATION_MESSAGE);
					stopper.cancel();
					timer.purge();
				} else {
					JOptionPane.showMessageDialog(this,
							"Timed out. It's too difficult.");
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
