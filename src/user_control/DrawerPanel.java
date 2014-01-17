package user_control;

import game_engine.Field;
import game_engine.Game;
import game_engine.figures.Figure;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class DrawerPanel extends JPanel {

	private static final long serialVersionUID = 2747906925617859260L;
	private final Component parent;
	private Game game;
	private Field field;
	private Figure figure;

	public static int MAX_WIDTH = 1200;
	public static int MAX_HEIGHT = 680;
	private int size = 25;

	public DrawerPanel(Component parent) {
		this.parent = parent;
		this.game = null;
		setBackground(Color.black);
	}

	public void gameToDraw(Game game) {
		this.game = game;
		int[][] grid = game.getFieldGrid();
		size = Math.min(Math.min(MAX_WIDTH/grid.length, 25), Math.min(MAX_HEIGHT/grid[0].length, 25));
		parent.setSize(size * grid.length + 112, size * grid[0].length + 64);
	}
	
	/**
	 * Rolls back game set by gameToDraw(Game)
	 * @param field
	 * @param figure
	 */
	public void fieldAndFigureToDraw(Field field, Figure figure)
	{
		this.field = field;
		this.figure = figure;
		this.game = null;
		int[][] grid = field.getGrid();
		size = Math.min(Math.min(MAX_WIDTH/grid.length, 25), Math.min(MAX_HEIGHT/grid[0].length, 25));
		parent.setSize(size * grid.length + 112, size * grid[0].length + 64);
	}
	
	void setSize(int size)
	{
		this.size = size;
		int[][] grid = game.getFieldGrid();
		parent.setSize(size * grid.length + 112, size * grid[0].length + 64);
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		if (game == null) {
			drawFieldAndFigure(g, field, figure);
			return;
		}
		int[][] grid = game.getFieldGrid();
		int w = grid.length;
		int h = grid[0].length;
		for (int i = 0; i < w; ++i) {
			for (int j = 0; j < h; ++j) {
				int cell = grid[i][j];
				switch (cell) {
				case Field.FREE:
					continue;
				default:
					g.setColor(Color.blue);
					g.fillRect(size * i, size * j, size - 1, size - 1);
				}
			}
		}

		int[] figure = game.getFigureCoordinates();
		g.setColor(Color.green);
		for (int i = 0; i < figure.length;) {
			g.fillRect(size * figure[i++], size * figure[i++], size - 1,
					size - 1);
		}

		g.setColor(Color.orange);
		g.drawRect(0, 0, size * w + 1, size * h + 1);

	}

	// FIXME bad method - copy of paintComponent
	private void drawFieldAndFigure(Graphics2D g, Field field, Figure figure) {
		if(field == null || figure == null)
			return;
		int[][] grid = field.getGrid();
		int w = grid.length;
		int h = grid[0].length;
		for (int i = 0; i < w; ++i) {
			for (int j = 0; j < h; ++j) {
				int cell = grid[i][j];
				switch (cell) {
				case Field.FREE:
					continue;
				default:
					g.setColor(Color.blue);
					g.fillRect(size * i, size * j, size - 1, size - 1);
				}
			}
		}

		int[] fig = figure.getCoordinates();
		g.setColor(Color.green);
		for (int i = 0; i < fig.length;) {
			g.fillRect(size * fig[i++], size * fig[i++], size - 1,
					size - 1);
		}

		g.setColor(Color.orange);
		g.drawRect(0, 0, size * w + 1, size * h + 1);
		
	}
}
