package game_engine.figures;

//import game_engine.FastField;
import game_engine.AbstractField;
import game_engine.FastField;
import game_engine.Field;

/**
 * Abstraction of figure on the {@link Field}.
 * 
 * @author misha
 * 
 */
public abstract class AbstractFigure implements java.io.Serializable {

	private static final long serialVersionUID = -772900941073049175L;
	

	public AbstractFigure() {
	}

	/**
	 * Get current coordinates of all blocks on the field.
	 * 
	 * @return [x1, y1, x2, y2, ...]
	 */
	public abstract int[] getCoordinates();

	public abstract AbstractFigure clone();

	//===============================================================================
	
	/**
	 * Tries to put figure on the top of {@link Field}.
	 * 
	 * @param field
	 * @return success
	 */
	public abstract boolean put(AbstractField field);

//	public abstract boolean put(FastField field);

	/**
	 * Tries to rotate figure in the {@link Field}.
	 * 
	 * @param field
	 * @return success
	 */
	public abstract boolean rotate(AbstractField field);

//	public abstract boolean rotate(FastField field);

	/**
	 * Tries to move figure in the {@link Field} by (dx, dy).
	 * 
	 * @param field
	 * @param dx
	 * @param dy
	 * @return success
	 */
	public abstract boolean move(AbstractField field, int dx, int dy);
	
//	public abstract boolean move(FastField field, int dx, int dy);

	public abstract void embed(AbstractField field);
	
//	public abstract void embed(FastField newField);

	public abstract boolean reflectVertical(AbstractField field);

//	public abstract boolean reflectVertical(FastField field);

	/**
	 * Check if specified cells are free in the grid, using
	 * {@link Field#isBuisy(int)}.
	 * 
	 * @param grid
	 * @param figure
	 * @param dx
	 *            x-displacement for cells
	 * @param dy
	 *            y-displacement for cells
	 * @return 1 if OK, 0 if figure intersects buisy area, -1 if figure comes
	 *         out of field
	 */
//	protected abstract int checkFreeCells(AbstractField field, int dx, int dy);
	public abstract int checkFreeCells(Field field, int dx, int dy);
	public abstract int checkFreeCells(FastField field, int dx, int dy);

	public abstract boolean isPenetrating();

	abstract void nextMood();
}
