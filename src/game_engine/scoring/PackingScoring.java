package game_engine.scoring;

import java.text.NumberFormat;

import game_engine.Field;
import game_engine.figures.FiguresManager;

/**
 * Scoring in packing box game.
 * 
 * @author misha
 * 
 */
public class PackingScoring extends AbstractScoringStrategy {

	@Override
	public double figureMovedDown() {
		return 0;
	}

	@Override
	public double linesDeleted(int count) {
		return 0;
	}

	@Override
	public double newSpeed(int speed) {
		return 0;
	}

	@Override
	public double countScore(int figuresCount, int linesCount, int speed,
			Field field) {
		double score = FiguresManager.computeDensity(field);
		NumberFormat f = NumberFormat.getInstance();
		f.setMaximumFractionDigits(4);
		// FIXME bad solution
		return new Double(f.format(score).replace(',', '.'));
	}

}
