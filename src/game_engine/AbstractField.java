package game_engine;

import auto_solver.Model;
import game_engine.figures.AbstractFigure;

/**
 * Representation of cells grid.
 * 
 * @author misha
 * 
 */
public abstract class AbstractField {
	protected final int width;
	protected final int height;

	protected AbstractField(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public abstract AbstractField clone();

	public abstract String toString();

	/**
	 * Special redirecting method to avoid type checking. It just redirects back
	 * to given figure according to field type.
	 * 
	 * But it doesn't help, why?
	 * 
	 * @param figure
	 * @param dx
	 * @param dy
	 * @return
	 */
	public abstract int checkFreeCells(AbstractFigure figure, int dx, int dy);

	/**
	 * Special redirecting method to avoid type checking. Redirects back to
	 * given model according to field type.
	 * 
	 * @param model
	 */
	public abstract double evaluate(Model model);

	/**
	 * Counts number of free cells downwards from the specified one.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public abstract int countFreeCellsDownFrom(int x, int y);
}
