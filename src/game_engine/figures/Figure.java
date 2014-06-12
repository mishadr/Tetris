package game_engine.figures;

import java.io.Serializable;

/**
 * {@link AbstractFigure}, isPenetrating flag and 2 coordinates on a grid.
 * 
 * @author misha
 * 
 */
public class Figure implements Serializable {
	private static final long serialVersionUID = -1346582714893661925L;

	private final AbstractFigure unit;
	/**
	 * x & y coordinates of the central point on the grid
	 */
	private int position[];

	public Figure(AbstractFigure unit) {
		this.unit = unit;
		position = new int[2];
	}

	/**
	 * Get position of left-top of this figure
	 * 
	 * @return
	 */
	int[] getPosition() {
		return new int[] { position[0] - unit.getCenter()[0],
				position[1] - unit.getCenter()[1] };
	}

	int[] getBody() {
		return unit.getBody();
	}

	void updatePosition(int dx, int dy) {
		position[0] += dx;
		position[1] += dy;
	}

	public int[] getCoordinates() {
		int[] pos = getPosition();
		int[] body = getBody();
		int[] result = new int[body.length];
		for (int i = 0; i < body.length;) {
			result[i] = body[i++] + pos[0];
			result[i] = body[i++] + pos[1];
		}
		return result;
	}
	
	void nextMood() {
		unit.nextMood();
	}

	void prevMood() {
		unit.prevMood();
	}

	public Figure clone() {
		Figure clone = new Figure(unit.clone());
		clone.position = position.clone();
		return clone;
	}

	void reflectOY() {
		unit.reflectOY();
	}

	public boolean isPenetrating() {
		return unit.isPenetrating();
	}

}
