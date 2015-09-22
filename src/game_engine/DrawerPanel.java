package game_engine;

import game_engine.figures.AbstractFigure;
import game_engine.figures.TwoBodiesFigure;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class DrawerPanel extends JPanel {

	private static final long serialVersionUID = 2747906925617859260L;
	private final Component parent;
//	private Game game;
	private AbstractField field;
//	private int[][] fieldGrid;
	private AbstractFigure figure;
	private int figureProjectionDistance;
	private int fadingIterations = 8;

	public static int MAX_WIDTH = 1200;
	public static int MAX_HEIGHT = 680;
	private int size = 25;

	public DrawerPanel(Component parent) {
		this.parent = parent;
//		fieldGrid = null;
		field = null;
//		this.game = null;
		setBackground(Color.black);
	}

	public void setFieldToDraw(AbstractField newField) {
//		this.game = game;
//		int[][] grid = game.getFieldGrid();
		this.field = newField;
		int w = newField.getWidth();
		int h = newField.getHeight();
		size = Math.min(Math.min(MAX_WIDTH/w, 25), Math.min(MAX_HEIGHT/h, 25));
//		if(((Frame)parent).isResizable())
//			parent.setSize(size * grid.length + 112, size * grid[0].length + 64); // if resizable
//		else
			parent.setSize(size * w + 102, size * h + 51);
	}
	
//	/**
//	 * Rolls back game set by gameToDraw(Game).
//	 * Used in auto_solver
//	 * 
//	 * @param field
//	 * @param figure
//	 */
//	public void fieldAndFigureToDraw(Field field, OneBodyFigure figure) {
//		this.field = field;
//		this.figure = figure;
//		this.game = null;
//		int[][] grid = field.getGrid();
//		size = Math.min(Math.min(MAX_WIDTH/grid.length, 25), Math.min(MAX_HEIGHT/grid[0].length, 25));
//		parent.setSize(size * grid.length + 112, size * grid[0].length + 64);
//	}
	
	public void setFigureToDraw(AbstractFigure figure) {
		this.figure = figure;
	}

	public void setFigureProjectionDistance(int distance) {
		this.figureProjectionDistance = distance;
}

//	void setSize(int size) {
//		this.size = size;
////		int[][] grid = game.getFieldGrid();
//		parent.setSize(size * field.getWidth() + 112, size * field.getHeight() + 64);
//	}

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
		drawField(g);
		if(figure != null) {
			drawFigure(g, figure);
		}
		
	}

	// XXX bad idea with null game?
	private void drawField(Graphics2D g) {
		if(field == null) {
			System.err.println("Null field was given to Drawer");
			return;
		}
		int w = field.getWidth();
		int h = field.getHeight();
		if(field instanceof FastField) {
			// FastField drawing
			boolean[] grid = ((FastField) field).getGrid();
			g.setColor(Color.blue);
			for (int i = 0; i < w; ++i) {
				for (int j = 0; j < h; ++j) {
					boolean cell = grid[i * h + j];
					if (!cell) {
						continue;
					}
					g.fillRect(size * i, size * j, size - 1, size - 1);
				}
			}		
		}
		else {
			// common Field drawing
			int[][] grid = ((Field) field).getGrid();
			for (int i = 0; i < w; ++i) {
				for (int j = 0; j < h; ++j) {
					int cell = grid[i][j];
					if (Field.isFree(cell)) {
						continue;
					}
					g.setColor(getFadingColor(cell));
					g.fillRect(size * i, size * j, size - 1, size - 1);
				}
			}			
		}

		g.setColor(Color.orange);
		g.drawRect(0, 0, size * w + 1, size * h + 1);
	}

	private void drawFigure(Graphics2D g, AbstractFigure figure) {
		if (figure instanceof TwoBodiesFigure) {
			g.setColor(new Color(255, 200, 0));
			drawFigure(g, ((TwoBodiesFigure) figure).getLeft());
			g.setColor(new Color(200, 255, 0));
			drawFigure(g, ((TwoBodiesFigure) figure).getRight());
			return;
		}
		// draw projection if needed
		if (figureProjectionDistance >= 0) {
			g.setColor(Color.darkGray);
			for (int i = 0, cs[] = figure.getCoordinates(); i < cs.length;) {
				g.drawRect(size * cs[i++], size
						* (cs[i++] + figureProjectionDistance), size - 2,
						size - 2);
			}
		}
		// draw real figure
		if (figure.isPenetrating()) {
			g.setColor(Color.magenta);
		} else {
			g.setColor(Color.green);
		}
		for (int i = 0, cs[] = figure.getCoordinates(); i < cs.length;) {
			g.fillRect(size * cs[i++], size * cs[i++], size-1, size-1);
		}
	}

	/**
	 * value: (1 -> green) .. (fadingIterations -> blue)
	 * 
	 * @param value
	 * @return
	 */
	private Color getFadingColor(int value) {
		return new Color(0, (int) (255 - 255. * value / fadingIterations),
				(int) (255. * value / fadingIterations));
	}

	void tickTime(Field field) {
		int[][] grid = field.getGrid();
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
