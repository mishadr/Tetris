package game_engine;

import javax.swing.Timer;

import game_engine.figures.AbstractFigure;
import game_engine.figures.AbstractFiguresChooser;
import game_engine.figures.FieldManager;
import game_engine.scoring.AbstractScoringStrategy;

/**
 * Game incapsulates the {@link Field} and current active {@link AbstractFigure}
 * . It delegates {@link Action}s' results calculations to {@link FieldManager}
 * in order to update the {@link Field} and returns them without any
 * postprocessing. Uses specified subclass of {@link AbstractScoringStrategy} to
 * compute score.
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
	private AbstractFigure currentFigure;
	private final Timer timer;
	
	private static final int[] INTERVALS_PER_SPEED = new int[] {
		1500, 1200, 1000, 900, 800, 700, 600, 500, 400, 1000, 1000, 1000
	};
	private AbstractScoringStrategy scoringStrategy;
	private int speed;
	private int linesCount;
	private int figuresCount;
	private double prize;// Extra prize plus to constant score
	private int fullDownDist;

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

	public boolean performStepDown() {
		boolean success = currentFigure.move(field, 0, 1);
		fullDownDist = currentFigure.computeFullDownDistance(field);
		return success;
	}

	public boolean tryPerformAction(Action action) {
		boolean success;
		switch (action) {
		case LEFT:
			success = currentFigure.move(field, -1, 0);
			fullDownDist = currentFigure.computeFullDownDistance(field);
			return success;
		case RIGHT:
			success = currentFigure.move(field, 1, 0);
			fullDownDist = currentFigure.computeFullDownDistance(field);
			return success;
		case DOWN:
			if (currentFigure.move(field, 0, 1)) {
				fullDownDist = currentFigure.computeFullDownDistance(field);
				whenMovedDown();
				return true;
			}
			return false;
		case FULL_DOWN:
//			while(currentFigure.move(field, 0, 1)) {
//				movedDown();
//			}
			fullDownDist = currentFigure.computeFullDownDistance(field);
			currentFigure.move(field, 0, fullDownDist);
			fullDownDist = 0;
			return true;
		case ROTATE:
			success = currentFigure.rotate(field);
			fullDownDist = currentFigure.computeFullDownDistance(field);
			return success;
		case VERTICAL_REFLECT:
			success = currentFigure.reflectVertical(field);
			fullDownDist = currentFigure.computeFullDownDistance(field);
			return success;
		default:
			break;
		}
		return false;
	}

	private void whenMovedDown() {
		prize += scoringStrategy.figureMovedDown();
	}

	public boolean prepareNewFigure(AbstractFigure abstractSkeleton) {
		this.currentFigure = abstractSkeleton;
		boolean success = currentFigure.put(field);
		this.fullDownDist = currentFigure.computeFullDownDistance(field);
		return success;
	}

	public boolean prepareNewFigure() {
		return prepareNewFigure(figuresChooser.next());
	}

	public void finishFigure() {
		currentFigure.embed(field);
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
		int lines = FieldManager.deleteFullLines(field);
		linesCount += lines;
		prize += scoringStrategy.linesDeleted(lines);
	}

	public Field getField() {
		return field;
	}

//	public int[] getFigureCoordinates() {
//		return currentFigure.getCoordinates();
//	}

	public void moveField(int dx) {
		FieldManager.moveField(field, dx);
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

	public AbstractFigure getFigure() {
		return currentFigure;
	}

	public int getFigureProjectionDistance() {
		return fullDownDist;
	}
}
