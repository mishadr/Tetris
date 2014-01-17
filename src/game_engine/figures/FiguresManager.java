package game_engine.figures;

import game_engine.Field;

/**
 * All math about field and figure should be here.
 * 
 * @author misha
 * 
 */
public class FiguresManager {

	/**
	 * Generate one {@link Figure} with one of given types.
	 * 
	 * @param types
	 * @return
	 */
	public static Figure generateOne(FigureType[] types) {
		FigureUnit[] values = FigureUnit.values(types);
		return new Figure(values[(int) (values.length * Math.random())],
				(int) (4 * Math.random()));
	}

	/**
	 * Tries to put given {@link Figure} on the {@link Field}.
	 * 
	 * @param figure
	 * @param field
	 * @return success
	 */
	public static boolean put(Figure figure, Field field) {
		int dx = field.getWidth() / 2 - 1;
		int[] center = figure.getCenter();
		figure.updatePosition(dx + center[0], center[1]);
		int x = figure.getPosition()[0] - figure.getCenter()[0];
		int y = figure.getPosition()[1] - figure.getCenter()[1];
		return checkFreeCells(field.getGrid(), figure.getBody(), x, y);
	}

	/**
	 * Check if specified cells are free in the grid, using {@link Field#FREE}.
	 * 
	 * @param grid
	 * @param cells
	 * @param x
	 *            x-displacement for cells
	 * @param y
	 *            y-displacement for cells
	 * @return
	 */
	private static boolean checkFreeCells(int[][] grid, int[] cells, int x,
			int y) {
		int w = grid.length;
		int h = grid[0].length;
		for (int i = 0; i < cells.length; i += 2) {
			int cx = cells[i] + x;
			int cy = cells[i + 1] + y;
			if (cx < 0 || cx >= w || cy < 0 || cy >= h) {
				return false;
			}
			if (grid[cx][cy] != Field.FREE) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Tries to rotate given {@link Figure} in the {@link Field}.
	 * 
	 * @param figure
	 * @return success
	 */
	public static boolean rotate(Figure figure, Field field) {
		figure.nextMood();
		int x = figure.getPosition()[0] - figure.getCenter()[0];
		int y = figure.getPosition()[1] - figure.getCenter()[1];
		boolean success = checkFreeCells(field.getGrid(), figure.getBody(), x, y);
		// try to move down a bit
		for (int dy = 0; dy < 2; ++dy) {
			success = checkFreeCells(field.getGrid(), figure.getBody(), x, y + dy);
			if (success) {
				move(figure, field, 0, dy);
				break;
			}
		}
		if (!success) {
			figure.prevMood();
		}
		return success;
	}

	/**
	 * Tries to move given {@link Figure} in the {@link Field} by (dx, dy).
	 * 
	 * @param figure
	 * @param field
	 * @param dx
	 * @param dy
	 * @return success
	 */
	public static boolean move(Figure figure, Field field, int dx, int dy) {

		int x = figure.getPosition()[0] - figure.getCenter()[0] + dx;
		int y = figure.getPosition()[1] - figure.getCenter()[1] + dy;
		if (!checkFreeCells(field.getGrid(), figure.getBody(), x, y)) {
			return false;
		}
		figure.updatePosition(figure.getPosition()[0] + dx,
				figure.getPosition()[1] + dy);
		return true;
	}

	public static void embed(Figure figure, Field field) {
		int[] cells = figure.getCoordinates();
		int[][] grid = field.getGrid();
		for (int i = 0; i < cells.length;) {
			grid[cells[i++]][cells[i++]] = Field.BUSY;
		}
	}

	public static int deleteFullLines(Field field) {
		int count = 0;
		int[][] grid = field.getGrid();
		int w = grid.length;
		int h = grid[0].length;
		for (int j = h - 1; j >= 0;) {
			boolean full = true;
			for (int i = 0; i < w; ++i) {
				if (grid[i][j] == Field.FREE) {
					full = false;
					break;
				}
			}
			if (full) {
				pullDown(grid, j);
				count++;
			} else {
				--j;
			}
		}
		return count;
	}

	private static void pullDown(int[][] grid, int h) {
		for (int i = 0; i < grid.length; ++i) {
			for (int j = h; j > 0;) {
				grid[i][j] = grid[i][--j];
			}
			grid[i][0] = Field.FREE;
		}
	}

	public static boolean reflectVertical(Figure figure, Field field) {
		// TODO Auto-generated method stub
		return false;
	}
}
