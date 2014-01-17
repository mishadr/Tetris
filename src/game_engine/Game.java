package game_engine;

import user_control.GameParameters;
import game_engine.figures.Figure;
import game_engine.figures.FiguresManager;

/**
 * Game performs defined {@link Action}s and returns their success without any
 * postprocessing. tells {@link Gameplay} about successful ones.
 * 
 * @author misha
 * 
 */
public class Game {

	public static enum Action {
		LEFT, RIGHT, DOWN, ROTATE, FULL_DOWN, VERTICAL_REFLECT
	}

	private final GameParameters params;
	private final Gameplay gameplay;
	private Field field;
	private Figure figure;

	public Game(GameParameters params, Gameplay gameplay) {
		this.params = params;
		this.gameplay = gameplay;
		field = new Field(params.getWidth(), params.getHeight());
		figure = null;
	}

	public boolean performStep() {
		boolean success = FiguresManager.move(figure, field, 0, 1);
		return success;
	}

	public boolean performAction(Action action) {
		switch (action) {
		case LEFT:
			return FiguresManager.move(figure, field, -1, 0);
		case RIGHT:
			return FiguresManager.move(figure, field, 1, 0);
		case DOWN:
			if (FiguresManager.move(figure, field, 0, 1)) {
				gameplay.movedDown();
				return true;
			} else
				return false;
		case FULL_DOWN:
			for (; FiguresManager.move(figure, field, 0, 1);) {
				gameplay.movedDown();
			}
			return true;
		case ROTATE:
			return FiguresManager.rotate(figure, field);
		case VERTICAL_REFLECT:
			return FiguresManager.reflectVertical(figure, field);
		default:
			break;
		}
		return false;
	}

	public boolean prepareNewFigure(Figure figure) {
		this.figure = figure;
		boolean success = FiguresManager.put(figure, field);
		return success;
	}

	public boolean prepareNewFigure() {
		return prepareNewFigure(FiguresManager.generateOne(params
				.getIncludedTypes()));
	}

	public void finishFigure() {
		FiguresManager.embed(figure, field);
		gameplay.finishedFigure();

		int lines = FiguresManager.deleteFullLines(field);
		gameplay.deletedLines(lines);
	}

	public int[][] getFieldGrid() {
		return field.getGrid();
	}

	public int[] getFigureCoordinates() {
		return figure.getCoordinates();
	}
}
