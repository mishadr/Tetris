package game_engine.scoring;

import game_engine.Field;
import game_engine.Game.Action;

/**
 * Abstraction of scoring method. Subclass should specify prizes for a finished
 * figure, several lines deleted, step to next level.
 * 
 * @author misha
 * 
 */
public abstract class AbstractScoringStrategy {

	public static final int MAX_LEVEL = 12;
	private static final int[] FIGURES_PER_SPEED = new int[] { 90, 170, 240,
			300, 350, 390, 430, 420, 430, 1000, 10000, 100000 };

	public int checkForNewSpeed(int figuresCount, int speed) {
		if (figuresCount >= FIGURES_PER_SPEED[speed]) {
			speed++;
			if (speed == 9) {
				System.out.println("standard part is over!");
			}
			else if (speed > 9) {
				System.out.println(FIGURES_PER_SPEED[speed] + " figures passed!");
			}
			if (speed == MAX_LEVEL) {
				// TODO game is finished
			}
		}
		return speed;
	}

	/**
	 * Prize for succesful {@link Action#DOWN}
	 */
	public abstract double figureMovedDown();

	/**
	 * Prize for several lines deleting 
	 * 
	 * @param count
	 * @return
	 */
	public abstract double linesDeleted(int count);

	/**
	 * Prize for new speed (level)
	 * 
	 * @param speed
	 * @return
	 */
	public abstract double newSpeed(int speed);

	/**
	 * Count standard score depending on the parameters (maybe field condition)
	 * 
	 * @param figuresCount
	 * @param linesCount
	 * @param speed
	 * @param field
	 * @return
	 */
	public abstract double countScore(int figuresCount, int linesCount,
			int speed, Field field);
}
