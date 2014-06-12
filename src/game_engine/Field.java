package game_engine;

import java.util.Arrays;

/**
 * Representation of cells grid. 
 * 
 * @author misha
 *
 */

public class Field {
	private final int width;
	private final int height;
	private final int grid[][];
	public static final int FREE = 0;
	public static final int EMBEDDED = 1;
	
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
		this.width = width;
		this.height = height;
		grid = new int[width][height];
	}

	private Field(int width, int height, int[][] grid) {
		this.width = width;
		this.height = height;
		this.grid = new int[width][height];
		for (int i = 0; i < width; ++i) {
			this.grid[i] = Arrays.copyOf(grid[i], height);
		}
	}

	public int[][] getGrid() {
		return grid;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Field clone() {
		return new Field(width, height, grid);
	}
}
