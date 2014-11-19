package game_engine.figures;

import game_engine.AbstractField;
import game_engine.FastField;
import game_engine.Field;

/**
 * Figure consisting of 2 {@link Skeleton}s.
 * 
 * XXX It cannot use {@link FastField} because of complicated figure managing
 * functions.
 * 
 * @author misha
 * 
 */
public class TwoBodiesFigure extends AbstractFigure {
	private static final long serialVersionUID = -2623516228557568638L;

	/**
	 * XXX items should be {@link Skeleton}?
	 */
	protected OneBodyFigure item1;
	protected OneBodyFigure item2;
	protected int dx, dy;

	protected TwoBodiesFigure(OneBodyFigure item1, OneBodyFigure item2, int dx,
			int dy) {
		this.item1 = item1;
		this.item2 = item2;
		this.dx = dx;
		this.dy = dy;
	}

	// /**
	// * Union of both items' bodies.
	// */
	// @Override
	// int[] getBody() {
	// int[] b1 = item1.getBody();
	// int[] b2 = item2.getBody();
	// int[] res = new int[b1.length + b2.length];
	// System.arraycopy(b1, 0, res, 0, b1.length);
	// for (int i = 0; i < b2.length; ++i) {
	// res[i + b1.length] = b2[i++] + dx;
	// res[i + b1.length] = b2[i] + dy;
	// }
	// // System.arraycopy(b2, 0, res, b1.length, b2.length);
	// return res;
	// }

	@Override
	void nextMood() {
		item1.nextMood();
		item2.nextMood();
	}

	// @Override
	// void reflectOY() {
	// // XXX ?
	// item1.reflectOY();
	// item2.reflectOY();
	// }

	@Override
	public TwoBodiesFigure clone() {
		TwoBodiesFigure clone = new TwoBodiesFigure(item1.clone(),
				item2.clone(), dx, dy);
		return clone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dx;
		result = prime * result + dy;
		result = prime * result + ((item1 == null) ? 0 : item1.hashCode());
		result = prime * result + ((item2 == null) ? 0 : item2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TwoBodiesFigure other = (TwoBodiesFigure) obj;
		if (dx != other.dx)
			return false;
		if (dy != other.dy)
			return false;
		if (item1 == null) {
			if (other.item1 != null)
				return false;
		} else if (!item1.equals(other.item1))
			return false;
		if (item2 == null) {
			if (other.item2 != null)
				return false;
		} else if (!item2.equals(other.item2))
			return false;
		return true;
	}

	@Override
	public int[] getCoordinates() {
		int[] c1 = item1.getCoordinates();
		int[] c2 = item2.getCoordinates();
		int[] res = new int[c1.length + c2.length];
		System.arraycopy(c1, 0, res, 0, c1.length);
		System.arraycopy(c2, 0, res, c1.length, c2.length);
		return res;
	}

	@Override
	public boolean put(AbstractField field) {
		// TODO check
		boolean b = item1.put(field);
		b &= item2.put(field);
		b &= item2.move(field, dx, dy);
		return b;
	}

	@Override
	public boolean move(AbstractField aField, int dx, int dy) {
		Field field = (Field) aField;
		// TODO check
		int[][] grid = field.getGrid();

		int c1 = item1.checkFreeCells(field, dx, dy);
		if (c1 != 1) {// 1st surely won't move, 2nd may overlap it
			// check cointersection
			int[] tempFill = item1.getCoordinates();
			for (int i = 0; i < tempFill.length;) {
				grid[tempFill[i++]][tempFill[i++]] = Field.LABEL;
			}
			boolean b2 = item2.move(field, dx, dy);
			for (int i = 0; i < tempFill.length;) {
				grid[tempFill[i++]][tempFill[i++]] = Field.FREE;
			}
			return b2;
		}
		// 1st may move
		boolean b2 = item2.move(field, dx, dy);
		if (b2) {
			// if 2nd can move then 1st can't overlap it
			item1.position[0] += dx;
			item1.position[1] += dy;
			return true;
		}
		// 1st may overlap 2nd when 2nd don't move
		// check cointersection
		int[] tempFill = item2.getCoordinates();
		for (int i = 0; i < tempFill.length;) {
			grid[tempFill[i++]][tempFill[i++]] = Field.LABEL;
		}
		boolean b1 = item1.move(field, dx, dy);
		for (int i = 0; i < tempFill.length;) {
			grid[tempFill[i++]][tempFill[i++]] = Field.FREE;
		}
		return b1;

	}

	@Override
	public boolean rotate(AbstractField aField) {
		Field field = (Field) aField;
		// TODO consider other variants, too complicated
		// if only 1 can be rotated, 1st is superior
		int[][] grid = field.getGrid();
		item1.nextMood();
		int c1 = item1.checkFreeCells(field, 0, 0);
		if (c1 != 1) {// 1st surely won't rotate, 2nd may overlap it
			item1.unit.prevMood();// XXX abstract prevMood()
			item2.nextMood();
			// check cointersection
			int[] tempFill = item1.getCoordinates();
			for (int i = 0; i < tempFill.length;) {
				grid[tempFill[i++]][tempFill[i++]] = Field.LABEL;
			}
			int c2 = item2.checkFreeCells(field, 0, 0);
			for (int i = 0; i < tempFill.length;) {
				grid[tempFill[i++]][tempFill[i++]] = Field.FREE;
			}
			if (c2 != 1) {
				item2.unit.prevMood();
				return false;
			}
			return true;
		}
		// 1st may rotate
		item2.nextMood();
		int[] tempFill = item1.getCoordinates();
		for (int i = 0; i < tempFill.length;) {
			grid[tempFill[i++]][tempFill[i++]] = Field.LABEL;
		}
		int c2 = item2.checkFreeCells(field, 0, 0);
		for (int i = 0; i < tempFill.length;) {
			grid[tempFill[i++]][tempFill[i++]] = Field.FREE;
		}
		if (c2 != 1) {// 2nd can't rotate
			item2.unit.prevMood();
			tempFill = item2.getCoordinates();
			for (int i = 0; i < tempFill.length;) {
				grid[tempFill[i++]][tempFill[i++]] = Field.LABEL;
			}
			c1 = item1.checkFreeCells(field, 0, 0);
			for (int i = 0; i < tempFill.length;) {
				grid[tempFill[i++]][tempFill[i++]] = Field.FREE;
			}
			if (c1 != 1) {// 1st won't rotate
				item1.unit.prevMood();
				// check if 2nd may rotate
				item2.nextMood();
				// check cointersection
				tempFill = item1.getCoordinates();
				for (int i = 0; i < tempFill.length;) {
					grid[tempFill[i++]][tempFill[i++]] = Field.LABEL;
				}
				c2 = item2.checkFreeCells(field, 0, 0);
				for (int i = 0; i < tempFill.length;) {
					grid[tempFill[i++]][tempFill[i++]] = Field.FREE;
				}
				if (c2 != 1) {
					item2.unit.prevMood();
					return false;
				}
				return true;
			}
		}
		return true;

		// boolean b = item1.rotate(field);
		// b &= item2.rotate(field);
		// return b;
	}

	@Override
	public boolean reflectVertical(AbstractField aField) {
		Field field = (Field) aField;
		// TODO consider other variants
		// if only 1 can be rotated, 1st is superior
		int[][] grid = field.getGrid();
		item1.reflectVertical(field);
		int c1 = item1.checkFreeCells(field, 0, 0);
		if (c1 != 1) {// 1st surely won't rotate, 2nd may overlap it
			item1.reflectVertical(field);// XXX abstract prevMood()
			item2.reflectVertical(field);
			// check cointersection
			int[] tempFill = item1.getCoordinates();
			for (int i = 0; i < tempFill.length;) {
				grid[tempFill[i++]][tempFill[i++]] = Field.LABEL;
			}
			int c2 = item2.checkFreeCells(field, 0, 0);
			for (int i = 0; i < tempFill.length;) {
				grid[tempFill[i++]][tempFill[i++]] = Field.FREE;
			}
			if (c2 != 1) {
				item2.reflectVertical(field);
				return false;
			}
			return true;
		}
		// 1st may rotate
		item2.reflectVertical(field);
		int[] tempFill = item1.getCoordinates();
		for (int i = 0; i < tempFill.length;) {
			grid[tempFill[i++]][tempFill[i++]] = Field.LABEL;
		}
		int c2 = item2.checkFreeCells(field, 0, 0);
		for (int i = 0; i < tempFill.length;) {
			grid[tempFill[i++]][tempFill[i++]] = Field.FREE;
		}
		if (c2 != 1) {// 2nd can't rotate
			item2.reflectVertical(field);
			tempFill = item2.getCoordinates();
			for (int i = 0; i < tempFill.length;) {
				grid[tempFill[i++]][tempFill[i++]] = Field.LABEL;
			}
			c1 = item1.checkFreeCells(field, 0, 0);
			for (int i = 0; i < tempFill.length;) {
				grid[tempFill[i++]][tempFill[i++]] = Field.FREE;
			}
			if (c1 != 1) {// 1st won't rotate
				item1.reflectVertical(field);
				// check if 2nd may rotate
				item2.reflectVertical(field);
				// check cointersection
				tempFill = item1.getCoordinates();
				for (int i = 0; i < tempFill.length;) {
					grid[tempFill[i++]][tempFill[i++]] = Field.LABEL;
				}
				c2 = item2.checkFreeCells(field, 0, 0);
				for (int i = 0; i < tempFill.length;) {
					grid[tempFill[i++]][tempFill[i++]] = Field.FREE;
				}
				if (c2 != 1) {
					item2.reflectVertical(field);
					return false;
				}
				return true;
			}
		}
		return true;
	}

	@Override
	public void embed(AbstractField field) {
		item1.embed(field);
		item2.embed(field);
	}

	@Override
	public int checkFreeCells(Field field, int dx, int dy) {
		int[][] grid = field.getGrid();
		// TODO check
		// check cointersection
		int[] tempFill = item2.getCoordinates();
		for (int i = 0; i < tempFill.length;) {
			grid[tempFill[i++]][tempFill[i++]] = Field.LABEL;
		}
		int c = item1.checkFreeCells(field, dx, dy);
		for (int i = 0; i < tempFill.length;) {
			grid[tempFill[i++]][tempFill[i++]] = Field.FREE;
		}
		if (c <= 0) {
			return c;
		}
		// check for each item
		return Math.min(item1.checkFreeCells(field, dx, dy),
				item2.checkFreeCells(field, dx, dy));
	}

	@Override
	public int checkFreeCells(FastField field, int dx, int dy) {
		// XXX Does not work, cause binary field is not sufficient!
		return 0;
	}

	@Override
	public boolean isPenetrating() {
		// TODO decide later
		return item1.isPenetrating && item2.isPenetrating;
	}

	public OneBodyFigure getLeft() {
		return item1;
	}

	public OneBodyFigure getRight() {
		return item2;
	}

}
