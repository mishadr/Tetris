package game_engine;

import javax.swing.Timer;

import game_engine.figures.AbstractFigure;
import game_engine.figures.Figure;
import game_engine.figures.AbstractFiguresChooser;
import game_engine.figures.FiguresManager;
import game_engine.scoring.AbstractScoringStrategy;

/**
 * Game incapsulates the {@link Field} and set of {@link Figure}s. It delegates
 * {@link Action}s results calculations to {@link FiguresManager} and returns
 * their results without any postprocessing. Uses specified subclass of
 * {@link AbstractScoringStrategy} to compute score.
 * 
 * @author misha
 * 
 */
public class Game {

	public static enum Action {
		// TODO add figure change, field shift down
		LEFT, RIGHT, DOWN, ROTATE, FULL_DOWN, VERTICAL_REFLECT
	}

	private final AbstractFiguresChooser figuresChooser;
	private Field field;
	private Figure currentFigure;
	private final Timer timer;
	
	private static final int[] INTERVALS_PER_SPEED = new int[] {
		1500, 1200, 1000, 900, 800, 700, 600, 500, 400, 1000, 1000, 1000
	};
	private AbstractScoringStrategy scoringStrategy;
	private int speed;
	private int linesCount;
	private int figuresCount;
	private double prize;// Extra prize plus to constant score

	public Game(Timer timer, GameParameters params) {
		this.timer = timer;
		scoringStrategy = params.getScoringStrategy();
		figuresChooser = params.getFiguresChooser();
		field = new Field(params.getWidth(), params.getHeight());
		currentFigure = null;
		init();
	}

	private void init() {
		speed = 0;
		linesCount = 0;
		figuresCount = 0;
		prize = 0;
		timer.setInitialDelay(INTERVALS_PER_SPEED[speed]);
		timer.setDelay(INTERVALS_PER_SPEED[0]);
	}

	public boolean performStep() {
		boolean success = FiguresManager.move(currentFigure, field, 0, 1);
		return success;
	}

	public boolean performAction(Action action) {
		switch (action) {
		case LEFT:
			return FiguresManager.move(currentFigure, field, -1, 0);
		case RIGHT:
			return FiguresManager.move(currentFigure, field, 1, 0);
		case DOWN:
			if (FiguresManager.move(currentFigure, field, 0, 1)) {
				movedDown();
				return true;
			} else
				return false;
		case FULL_DOWN:
			while(FiguresManager.move(currentFigure, field, 0, 1)) {
				movedDown();
			}
			return true;
		case ROTATE:
			return FiguresManager.rotate(currentFigure, field);
		case VERTICAL_REFLECT:
			return FiguresManager.reflectVertical(currentFigure, field);
		default:
			break;
		}
		return false;
	}

	private void movedDown() {
		prize += scoringStrategy.figureMovedDown();
	}

	public boolean prepareNewFigure(AbstractFigure abstractFigure) {
		this.currentFigure = new Figure(abstractFigure);
		boolean success = FiguresManager.put(currentFigure, field);
		return success;
	}

	public boolean prepareNewFigure() {
		return prepareNewFigure(figuresChooser.next());
	}

	public void finishFigure() {
		FiguresManager.embed(currentFigure, field);
		figuresCount++;
//		score += strategy.figureFinished(field);
	}
	
	public void checkForNewSpeed() {
		int s = speed;
		speed = scoringStrategy.checkForNewSpeed(figuresCount, speed);
		if(s != speed)
		{
			timer.setInitialDelay(INTERVALS_PER_SPEED[speed]);
			timer.setDelay(INTERVALS_PER_SPEED[speed]);
			prize += scoringStrategy.newSpeed(speed);
		}
	}

	public void deleteFullLines() {
		int lines = FiguresManager.deleteFullLines(field);
		linesCount += lines;
		prize += scoringStrategy.linesDeleted(lines);
	}

	public int[][] getFieldGrid() {
		return field.getGrid();
	}

//	public int[] getFigureCoordinates() {
//		return currentFigure.getCoordinates();
//	}

	public void moveField(int dx) {
		FiguresManager.moveField(field, dx);
	}

	public int getSpeed() {
		return speed;
	}

	public int getLinesCount() {
		return linesCount;
	}

	public int getFiguresCount() {
		return figuresCount;
	}

	/**
	 * Prize + constant score in terms of concrete {@link AbstractScoringStrategy}.
	 * 
	 * @return
	 */
	public double getScore() {
		return prize + scoringStrategy.countScore(figuresCount, linesCount, speed, field);
	}

	public Figure getFigure() {
		return currentFigure;
	}
}
