package game_engine.figures;

import game_engine.AbstractField;
import game_engine.FastField;
import game_engine.Field;

/**
 * OneBodyFigure consisting of one skeleton. It is a {@link Skeleton},
 * isPenetrating flag and 2 coordinates on a grid.
 * 
 * @author misha
 * 
 */
public class OneBodyFigure extends AbstractFigure {
	private static final long serialVersionUID = -1346582714893661925L;

	protected final Skeleton unit;
	protected boolean isPenetrating;
	/**
	 * x & y coordinates of the left top
	 */
	protected int position[];

	public OneBodyFigure(Skeleton unit, boolean isPenetrating) {
		this.unit = unit;
		this.isPenetrating = isPenetrating;
		position = new int[2];
	}

	public int[] getCoordinates() {
		int[] body = unit.getBody();
		int[] result = new int[body.length];
		for (int i = 0; i < body.length;) {
			result[i] = body[i++] + position[0];
			result[i] = body[i++] + position[1];
		}
		return result;
	}

	@Override
	public int[] getPosition() {
		return position.clone();
	}

	@Override
	public void setPosition(int[] newPosition) {
		position = newPosition;
	}

	public OneBodyFigure clone() {
		OneBodyFigure clone = new OneBodyFigure(unit.clone(), isPenetrating);
		// XXX don't we need honest copying?
		clone.position = position.clone();
		return clone;
	}

	// ===============================================================================

	@Override
	public boolean put(AbstractField field) {
		int dx = field.getWidth() / 2 - 1;
		position[0] = dx;
		position[1] = 0;
		return field.checkFreeCells(this, 0, 0) > 0;
	}

	@Override
	public boolean rotate(AbstractField field) {
		unit.nextMood();
		boolean success = false;// = checkFreeCells(field.getGrid(),
								// figure.getBody(), x, y);
		// try to move down a bit
		for (int dy = 0; dy <= 2; ++dy) {
			success = field.checkFreeCells(this, 0, dy) > 0;
			if (success) {
				move(field, 0, dy);
				break;
			}
		}
		if (!success) {
			unit.prevMood();
		}
		return success;
	}

	@Override
	public boolean move(AbstractField field, int dx, int dy) {
		// XXX penetrating figure
		if (isPenetrating) {
			int iter = 0;
			int check;
			while (true) {
				++iter;
				check = field.checkFreeCells(this, iter * dx, iter * dy);
				if (check == 1) {// OK
					dx = iter * dx;
					dy = iter * dy;
					break;
				}
				if (check == -1) {// we reached border
					return false;
				}
			}
		}
		// common figure
		else if (field.checkFreeCells(this, dx, dy) <= 0) {
			return false;
		}
		position[0] += dx;
		position[1] += dy;
		return true;
	}

	@Override
	public int computeFullDownDistance(AbstractField field) {
		int dyMax = 0;
		if (isPenetrating) {
			int iter = 0;
			int check;
			while (true) {
				++iter;
				check = field.checkFreeCells(this, 0, iter);
				if (check == 1) {// OK
					dyMax = iter;
				}
				if (check == -1) {// we reached border
					break;
				}
			}
		}
		// common figure
		else {
			int[] body = unit.getBody();
			int max = field.getHeight() + 1;
			for (int i = 0; i < body.length; i += 2) {
				int cx = body[i] + position[0];
				int cy = body[i + 1] + position[1];
				int count = field.countFreeCellsDownFrom(cx, cy + 1);
				if (count < max) {
					max = count;
				}
			}
			dyMax = max;
		}
//		position[1] += dy;
		return dyMax;
	}

	@Override
	public void embed(AbstractField field) {
		int[] cells = getCoordinates();

		if (field instanceof FastField) {
			boolean[] grid = ((FastField) field).getGrid();
			int w = ((FastField) field).getWidth();
			int h = ((FastField) field).getHeight();
			for (int i = 0; i < cells.length;) {
				grid[cells[i++] * h + cells[i++]] = true;
			}
		} else {
			int[][] grid = ((Field) field).getGrid();
			for (int i = 0; i < cells.length;) {
				grid[cells[i++]][cells[i++]] = Field.EMBEDDED;
			}
		}
	}

	@Override
	public boolean reflectVertical(AbstractField field) {
		unit.reflectOY();
		boolean success = field.checkFreeCells(this, 0, 0) > 0;
		// TODO try to move left-right a bit
		if (!success) {
			unit.reflectOY();
		}
		return success;
	}

	// @Override
	// protected int checkFreeCells(AbstractField field, int dx, int dy) {
	// if (field instanceof FastField) {
	// return checkFreeCells((FastField) field, dx, dy);
	// }
	// return checkFreeCells((Field) field, dx, dy);
	// }

	@Override
	public int checkFreeCells(Field field, int dx, int dy) {
		int[][] grid = field.getGrid();
		int w = grid.length;
		int h = grid[0].length;
		int[] body = unit.getBody();
		dx += position[0];
		dy += position[1];
		for (int i = 0; i < body.length; i += 2) {
			int cx = body[i] + dx;
			int cy = body[i + 1] + dy;
			if (cx < 0 || cx >= w || cy < 0 || cy >= h) {
				return -1;
			}
			if (Field.isBuisy(grid[cx][cy])) {
				return 0;
			}
		}
		return 1;
	}

	@Override
	public int checkFreeCells(FastField field, int dx, int dy) {
		boolean[] grid = field.getGrid();
		int w = field.getWidth();
		int h = field.getHeight();
		int[] body = unit.getBody();
		dx += position[0];
		dy += position[1];
		for (int i = 0; i < body.length; i += 2) {
			int cx = body[i] + dx;
			int cy = body[i + 1] + dy;
			if (cx < 0 || cx >= w || cy < 0 || cy >= h) {
				return -1;
			}
			if (grid[cx * h + cy]) {
				return 0;
			}
		}
		return 1;
	}

	@Override
	public boolean isPenetrating() {
		return isPenetrating;
	}

	@Override
	public void nextMood() {
		unit.nextMood();
	}

}
