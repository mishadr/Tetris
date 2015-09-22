package game_engine.figures;

import game_engine.FastField;
import game_engine.Field;

/**
 * All math about field should be here.
 * 
 * @author misha
 * 
 */
public class FieldManager {

//	/**
//	 * Select subset of standard figures with specified {@link FigureType}s
//	 * 
//	 * @param allFigures all existing figures
//	 * @param includedTypes allowed types
//	 * @return
//	 */
//	public static AbstractSkeleton[] selectAllowedFigures(
//			Map<FigureType, List<AbstractSkeleton>> allFigures, FigureType[] includedTypes) {
//		List<AbstractSkeleton> list = new LinkedList<>();
//		for (FigureType type : includedTypes) {
//			list.addAll(allFigures.get(type));
//		}
//
//		return list.toArray(new AbstractSkeleton[list.size()]);
//	}

//	/**
//	 * Tries to put given {@link OneBodyFigure} on the {@link AbstractField}.
//	 * 
//	 * @param figure
//	 * @param field
//	 * @return success
//	 */
//	public static boolean put(OneBodyFigure figure, AbstractField field) {
//		int dx = field.getWidth() / 2 - 1;
//		figure.updatePosition(-figure.getPosition()[0] + dx, -figure.getPosition()[1]);
//		return checkFreeCells(field.getGrid(), figure, 0, 0) > 0;
//	}
//
//	/**
//	 * Check if specified cells are free in the grid, using {@link AbstractField#FREE}.
//	 * 
//	 * @param grid
//	 * @param figure
//	 * @param dx
//	 *            x-displacement for cells
//	 * @param dy
//	 *            y-displacement for cells
//	 * @return 1 if OK, 0 if figure intersects buisy area, -1 if figure comes
//	 *         out of field
//	 */
//	private static int checkFreeCells(int[][] grid, OneBodyFigure figure, int dx,
//			int dy) {
//		int w = grid.length;
//		int h = grid[0].length;
//		int[] body = figure.getBody();
//		dx += figure.getPosition()[0];
//		dy += figure.getPosition()[1];
//		for (int i = 0; i < body.length; i += 2) {
//			int cx = body[i] + dx;
//			int cy = body[i + 1] + dy;
//			if (cx < 0 || cx >= w || cy < 0 || cy >= h) {
//				return -1;
//			}
//			if (AbstractField.isBuisy(grid[cx][cy])) {
//				return 0;
//			}
//		}
//		return 1;
//	}
//
//	/**
//	 * Tries to rotate given {@link OneBodyFigure} in the {@link AbstractField}.
//	 * 
//	 * @param figure
//	 * @return success
//	 */
//	public static boolean rotate(OneBodyFigure figure, AbstractField field) {
//		figure.nextMood();
//		boolean success = false;// = checkFreeCells(field.getGrid(), figure.getBody(), x, y);
//		// try to move down a bit
//		for (int dy = 0; dy <= 2; ++dy) {
//			success = checkFreeCells(field.getGrid(), figure, 0, dy) > 0;
//			if (success) {
//				move(figure, field, 0, dy);
//				break;
//			}
//		}
//		if (!success) {
//			figure.prevMood();
//		}
//		return success;
//	}
//
//	/**
//	 * Tries to move given {@link OneBodyFigure} in the {@link AbstractField} by (dx, dy).
//	 * 
//	 * @param figure
//	 * @param field
//	 * @param dx
//	 * @param dy
//	 * @return success
//	 */
//	public static boolean move(OneBodyFigure figure, AbstractField field, int dx, int dy) {
//		// penetrating figure
//		if (figure.isPenetrating()) {
//			int iter = 0;
//			int check;
//			while (true) {
//				++iter;
//				check = checkFreeCells(field.getGrid(), figure, iter * dx, iter * dy);
//				if(check == 1) {// OK
//					dx = iter * dx;
//					dy = iter * dy;
//					break;
//				}
//				if (check == -1) {
//					return false;
//				}
//			}
//		}
//		// common figure
//		else if (checkFreeCells(field.getGrid(), figure, dx, dy) <= 0) {
//			return false;
//		}
//		figure.updatePosition(dx, dy);
//		return true;
//	}
//
//	public static void embed(OneBodyFigure figure, AbstractField field) {
//		int[] cells = figure.getCoordinates();
////		int[] pos = figure.getPosition();
////		int[] body = figure.getBody();
////		int[] cells = new int[body.length];
////		for (int i = 0; i < body.length;) {
////			cells[i] = body[i++] + pos[0];
////			cells[i] = body[i++] + pos[1];
////		}
//
//		int[][] grid = field.getGrid();
//		for (int i = 0; i < cells.length;) {
//			grid[cells[i++]][cells[i++]] = AbstractField.EMBEDDED;
//		}
//	}
//
	public static final int deleteFullLines(Field field) {
		int count = 0;
		int[][] grid = field.getGrid();
		int w = grid.length;
		int h = grid[0].length;
		for (int j = h - 1; j >= 0;) {
			boolean full = true;
			for (int i = 0; i < w; ++i) {
				if (Field.isFree(grid[i][j])) {
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

	public static final int deleteFullLines(FastField field) {
		int count = 0;
		boolean[] grid = field.getGrid();
		int w = field.getWidth();
		int h = field.getHeight();
		boolean[] notFull = new boolean[h];
		for (int i = 0; i < w; ++i) {
			for (int j = 0; j < h; ++j) {
				notFull[j] |= !grid[i * h + j];
			}
		}
		for (int j = 0; j < h; ++j) {
			if(!notFull[j]) {
				pullDown(grid, w, h, j);
				count++;
			}
		}
		return count;
	}

	public static final void pullDown(int[][] grid, int h) {
		for (int i = 0; i < grid.length; ++i) {
			for (int j = h; j > 0;) {
				grid[i][j] = grid[i][--j];
			}
			grid[i][0] = Field.FREE;
		}
	}

	public static final void pullDown(boolean[] grid, int w, int h, int level) {
		for (int i = 0; i < w; ++i) {
			for (int j = level; j > 0;) {
				grid[i * h + j] = grid[i * h + --j];
			}
			grid[i * h] = false;
		}
	}

//	public static boolean reflectVertical(OneBodyFigure figure, AbstractField field) {
//		figure.reflectOY();
//		boolean success = checkFreeCells(field.getGrid(), figure, 0, 0) > 0;
//		// TODO try to move left-right a bit
//		if (!success) {
//			figure.reflectOY();
//		}
//		return success;
//	}
//
	/**
	 * Fill randomly last <i>count</i> lines in given field.
	 * 
	 * @param field
	 * @param count
	 * @param density probability for each cell to be filled
	 */
	public static final void fillBottomLinesRandomly(Field field, int count,
			double density) {
		int[][] grid = field.getGrid();
		int w = grid.length;
		int h = grid[0].length;
		for (int i = 0; i < w; ++i) {
			// works if count < 0
			for (int j = 0; j < count; ++j) {
				if (Math.random() <= density) {
					grid[i][h - j - 1] = Field.EMBEDDED;
				}
			}
		}
	}

	/**
	 * Move field by <i>dx</i>
	 * 
	 * @param dx
	 */
	public static final void moveField(Field field, int dx) {
		// XXX generalize!
		dx = 1;
		int[][] grid = field.getGrid();
		int w = grid.length;
		int[] temp = grid[w-1];
		for(int i=w-1; i>0; --i) {
			grid[i] = grid[i-1];
		}
		grid[0] = temp;
	}

	/**
	 * Percent of completeness
	 * 
	 * @param field
	 * @return
	 */
	public static final double computeDensity(Field field) {
		int[][] grid = field.getGrid();
		int w = grid.length;
		int h = grid[0].length;
		int busy = 0;
		for (int i = 0; i < w; ++i) {
			for (int j = 0; j < h; ++j) {
				if (Field.isBuisy(grid[i][j])) {
					busy++;
				}
			}
		}
		return 1.0 * busy / w / h;
	}
}
