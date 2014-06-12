package game_engine.scoring;

import game_engine.Field;

/**
 * Classic scoring: prizes for figure, line and level.
 * 
 * @author misha
 * 
 */
public class ClassicScoring extends AbstractScoringStrategy {

	private static final int SCORE_PER_LEVEL = 100;
	private static final int SCORE_PER_LINE = 10;
	private static final double SCORE_PER_FIGURE = 1;
	private static final double SCORE_PER_DOWNSTEP = 0;

	@Override
	public double figureMovedDown() {
		return SCORE_PER_DOWNSTEP;
	}

	@Override
	public double linesDeleted(int count) {
		return count * count * count * SCORE_PER_LINE;
	}

	@Override
	public double newSpeed(int speed) {
		if (speed == 0) {
			return MAX_LEVEL * SCORE_PER_LEVEL;
		}
		return speed * SCORE_PER_LEVEL;
	}

	@Override
	public double countScore(int figuresCount, int linesCount, int speed,
			Field field) {
		return figuresCount * SCORE_PER_FIGURE + linesCount * SCORE_PER_LINE
				+ speed * SCORE_PER_LEVEL;
	}

}
