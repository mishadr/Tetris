package game_engine;

import game_engine.figures.AbstractFigure;

import java.util.Arrays;

import auto_solver.Model;

/**
 * Fast implementation of binary {@link AbstractField} for speed calculations.
 * 
 * @author misha
 * 
 */
public class FastField extends AbstractField {

	/**
	 * Binary grid. <code>true</code> denotes busy cell and <code>false</code>
	 * denotes free one.
	 * <p>
	 * Intended access: <code>[i][j] == [i*height + j]</code>
	 */
	private final boolean[] grid;

	public FastField(int width, int height) {
		super(width, height);
		grid = new boolean[width * height];
	}

	public FastField(int width, int height, boolean[] newGrid) {
		super(width, height);
		this.grid = Arrays.copyOf(newGrid, width * height);

	}

	/**
	 * @return {@link #grid}
	 */
	public boolean[] getGrid() {
		return grid;
	}

	@Override
	public FastField clone() {
		return new FastField(width, height, grid);
	}

	@Override
	public String toString() {
		return "FastField [grid=" + Arrays.toString(grid) + "]";
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
			if (grid[x * height + y + count]) {
				break;
			}
			count++;
		}
		return count;
	}

}
