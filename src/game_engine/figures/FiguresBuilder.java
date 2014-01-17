package game_engine.figures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Simple utility to draw figures.
 * 
 * @author misha
 *
 */
public class FiguresBuilder extends JPanel {
	private static final long serialVersionUID = 3627390607519465519L;
	private final int fieldSize = 10;
	private final int cellSize = 30;
	// private String path = "";
	private boolean field[][];

	public FiguresBuilder() {
		JFrame frame = new JFrame("Builder");
		frame.setSize(fieldSize * cellSize + 20, fieldSize * cellSize + 40);
		frame.setLocation(400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().add(this);
		frame.setVisible(true);

		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent ev) {
			}

			public void mousePressed(MouseEvent ev) {
				if (ev.getModifiersEx() == 1024)// left, 5120 - both
				{
					int i = Math.min(ev.getX() / cellSize, fieldSize - 1);
					int j = Math.min(ev.getY() / cellSize, fieldSize - 1);
					field[i][j] = !field[i][j];
				}
				repaint();
			}

			public void mouseReleased(MouseEvent ev) {
			}

			public void mouseEntered(MouseEvent ev) {
			}

			public void mouseExited(MouseEvent ev) {
			}
		});

		addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent ev) {
				int key = ev.getKeyCode();
				if (key == KeyEvent.VK_S) {
					for (int i = 0; i < fieldSize; ++i)
						for (int j = 0; j < fieldSize; ++j)
							if (field[i][j])
								System.out.print(i + ", " + j + ", ");
					System.out.println();
				}
				if (key == KeyEvent.VK_C) {
					for (int i = 0; i < fieldSize; ++i)
						for (int j = 0; j < fieldSize; ++j)
							field[i][j] = false;
				}
				repaint();
			}

			public void keyReleased(KeyEvent ev) {
			}

			public void keyTyped(KeyEvent ev) {
			}
		});
		setFocusable(true);

		field = new boolean[fieldSize][fieldSize];
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;

		g.setColor(Color.darkGray);
		for (int i = 0; i <= fieldSize; i++)
			g.drawLine(cellSize * i, 0, cellSize * i, cellSize * fieldSize);
		for (int j = 0; j <= fieldSize; j++)
			g.drawLine(0, cellSize * j, cellSize * fieldSize, cellSize * j);
		for (int i = 0; i < fieldSize; ++i)
			for (int j = 0; j < fieldSize; ++j) {
				g.setColor(field[i][j] ? Color.red : Color.darkGray.darker());
				g.fillRect(i * cellSize + 1, j * cellSize + 1, cellSize - 2,
						cellSize - 2);
			}
	}

	public static void main(String[] args) {

		new FiguresBuilder();
	}

	public static int[] rotate(int[] body) {
		int[] res = new int[body.length];
		int ymax = 0;
		for (int i = 1; i < body.length; i += 2) {
			if (body[i] > ymax) {
				ymax = body[i];
			}
		}
		for (int i = 0; i < body.length;) {
			int x = body[i];
			res[i] = ymax - body[++i];
			res[i++] = x;
		}
		return res;
	}
}