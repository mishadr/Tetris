package game_engine.figures;

import java.io.Serializable;

/**
 * {@link FigureUnit} and 2 coordinates on a grid.
 * 
 * @author misha
 * 
 */
public class Figure implements Serializable {
	private static final long serialVersionUID = -1346582714893661925L;

	private final FigureUnit unit;

	/**
	 * 1 of possible moods (turn angle)
	 */
	private int mood;

	/**
	 * x & y position on the grid
	 */
	private int position[];

	public Figure(FigureUnit unit, int mood) {
		this.unit = unit;
		this.mood = mood;
		position = new int[2];
	}

	int[] getPosition() {
		return position;
	}

	int[] getCenter() {
		return unit.getCenter(mood);
	}

	int[] getBody() {
		return unit.getBody(mood);
	}

	void updatePosition(int x, int y) {
		position[0] = x;
		position[1] = y;
	}

	public int[] getCoordinates() {
		int dx = position[0] - getCenter()[0];
		int dy = position[1] - getCenter()[1];
		int[] body = getBody();
		int[] result = new int[body.length];
		for (int i = 0; i < body.length;) {
			result[i] = body[i++] + dx;
			result[i] = body[i++] + dy;
		}
		return result;
	}

	void nextMood() {
		mood = (mood + 1) % 4;
	}

	void prevMood() {
		mood = (mood + 3) % 4;
	}

	public Figure clone() {
		Figure clone = new Figure(unit, mood);
		clone.updatePosition(position[0], position[1]);
		return clone;
	}
}
