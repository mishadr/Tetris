package game_engine;

import game_engine.figures.Figure;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class DrawerPanel extends JPanel {

	private static final long serialVersionUID = 2747906925617859260L;
	private final Component parent;
//	private Game game;
//	private Field field;
	private int[][] fieldGrid;
	private Figure figure;
	private int fadingIterations = 8;

	public static int MAX_WIDTH = 1200;
	public static int MAX_HEIGHT = 680;
	private int size = 25;

	public DrawerPanel(Component parent) {
		this.parent = parent;
		fieldGrid = null;
//		this.game = null;
		setBackground(Color.black);
	}

	public void fieldToDraw(int[][] grid) {
//		this.game = game;
//		int[][] grid = game.getFieldGrid();
		this.fieldGrid = grid;
		size = Math.min(Math.min(MAX_WIDTH/grid.length, 25), Math.min(MAX_HEIGHT/grid[0].length, 25));
//		if(((Frame)parent).isResizable())
//			parent.setSize(size * grid.length + 112, size * grid[0].length + 64); // if resizable
//		else
			parent.setSize(size * grid.length + 102, size * grid[0].length + 54);
	}
	
//	/**
//	 * Rolls back game set by gameToDraw(Game).
//	 * Used in auto_solver
//	 * 
//	 * @param field
//	 * @param figure
//	 */
//	public void fieldAndFigureToDraw(Field field, Figure figure) {
//		this.field = field;
//		this.figure = figure;
//		this.game = null;
//		int[][] grid = field.getGrid();
//		size = Math.min(Math.min(MAX_WIDTH/grid.length, 25), Math.min(MAX_HEIGHT/grid[0].length, 25));
//		parent.setSize(size * grid.length + 112, size * grid[0].length + 64);
//	}
	
	public void figureToDraw(Figure figure) {
		this.figure = figure;
	}

	void setSize(int size) {
		this.size = size;
//		int[][] grid = game.getFieldGrid();
		parent.setSize(size * fieldGrid.length + 112, size * fieldGrid[0].length + 64);
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
//		if (game == null) {
//			if (field != null) {
//				drawFieldAndFigure(g, field.getGrid(), figure.getCoordinates());
//			}
//			return;
//		}
//		drawFieldAndFigure(g, game.getFieldGrid(), game.getFigureCoordinates());
		drawFieldAndFigure(g);
	}

	// XXX bad idea with null game?
	private void drawFieldAndFigure(Graphics2D g) {
		if(fieldGrid == null) {
			return;
		}
		int w = fieldGrid.length;
		int h = fieldGrid[0].length;
		for (int i = 0; i < w; ++i) {
			for (int j = 0; j < h; ++j) {
				int cell = fieldGrid[i][j];
				if (Field.isFree(cell)) {
					continue;
				}
				g.setColor(getFadingColor(cell));
				g.fillRect(size * i, size * j, size - 1, size - 1);
			}
		}

		if(figure == null) {
			return;
		}
		if(figure.isPenetrating()) {
			g.setColor(Color.magenta);
		}
		else {
			g.setColor(Color.green);
		}
		for (int i = 0, cs [] = figure.getCoordinates(); i < cs.length;) {
			g.fillRect(size * cs[i++], size * cs[i++], size - 1,
					size - 1);
		}

		g.setColor(Color.orange);
		g.drawRect(0, 0, size * w + 1, size * h + 1);
		
	}

	/**
	 * value: 1 -> green .. fadingIterations -> blue
	 * 
	 * @param value
	 * @return
	 */
	private Color getFadingColor(int value) {
		return new Color(0, (int) (255 - 255. * value / fadingIterations),
				(int) (255. * value / fadingIterations));
	}

	void timeStep(int[][] grid) {
		int w = grid.length;
		int h = grid[0].length;
		for (int i = 0; i < w; ++i) {
			for (int j = 0; j < h; ++j) {
				int cell = grid[i][j];
				if (Field.isBuisy(cell) && cell < fadingIterations) {
					grid[i][j]++;
				}
			}
		}
	}

	public int getFadingIterations() {
		return fadingIterations;
	}

	public void setFadingIterations(int fadingIterations) {
		this.fadingIterations = fadingIterations;
	}
}
