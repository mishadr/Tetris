package game_engine;

import game_engine.figures.AbstractFigure;

import java.util.Arrays;

import auto_solver.Model;

/**
 * Field for common playing with 4 possible bytes of information per cell.
 * Efficiency is not important.
 * 
 * @author misha
 * 
 */
public class Field extends AbstractField {
	public static final int FREE = 0;
	public static final int LABEL = -1;
	public static final int EMBEDDED = 1;

	private final int grid[][];

	/**
	 * Check if cell value says it is empty
	 */
	public static final boolean isFree(int cell) {
		return cell == FREE;
	}

	/**
	 * Check if cell value says it is not empty
	 */
	public static final boolean isBuisy(int cell) {
		return cell != FREE;
	}

	public Field(int width, int height) {
		super(width, height);
		grid = new int[width][height];
	}

	private Field(int width, int height, int[][] grid) {
		this(width, height);
		for (int i = 0; i < width; ++i) {
			this.grid[i] = Arrays.copyOf(grid[i], height);
		}
	}

	public int[][] getGrid() {
		return grid;
	}

	public Field clone() {
		return new Field(width, height, grid);
	}

	@Override
	public String toString() {
		return "Field [grid=" + Arrays.deepToString(grid) + "]";
	}

	@Override
	public int checkFreeCells(AbstractFigure figure, int dx, int dy) {
		return figure.checkFreeCells(this, dx, dy);
	}

	@Override
	public double evaluate(Model model) {
		return model.evaluate(this);
	}

	@Override
	public int countFreeCellsDownFrom(int x, int y) {
		int count = 0;
		while (y + count < height) {
			if (isBuisy(grid[x][y + count])) {
				break;
			}
			count++;
		}
		return count;
	}

}
