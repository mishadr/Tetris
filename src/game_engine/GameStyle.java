package game_engine;

import game_engine.scoring.AbstractScoringStrategy;
import game_engine.scoring.ClassicScoring;
import game_engine.scoring.PackingScoring;

/**
 * Game styles. Style determines some {@link GameParameters} and scoring
 * strategy
 * 
 * @author misha
 * 
 */
public enum GameStyle {
	CLASSIC(false, false, true, false, new ClassicScoring()), 
	CYCLED_FIELD(false, false, true, true, new ClassicScoring()), 
	BOX_PACKING(false, false, false, false, new PackingScoring()), 
	GARBAGE_DELETING(false, false, true, false, new ClassicScoring()), 
	;

	private final boolean downShiftAllowed;
	private final boolean figureSwapAllowed;
	private final boolean fullLinesDeleting;
	private final boolean fieldMoving;
	private final AbstractScoringStrategy scoringStrategy;

	private GameStyle(boolean downShiftAllowed, boolean figureSwapAllowed,
			boolean fullLinesDeleting, boolean fieldMoving, AbstractScoringStrategy scoringStrategy) {
		this.downShiftAllowed = downShiftAllowed;
		this.figureSwapAllowed = figureSwapAllowed;
		this.fullLinesDeleting = fullLinesDeleting;
		this.fieldMoving = fieldMoving;
		this.scoringStrategy = scoringStrategy;
	}

	public boolean isDownShiftAllowed() {
		return downShiftAllowed;
	}

	public boolean isFigureSwapAllowed() {
		return figureSwapAllowed;
	}

	public boolean isFullLinesDeleting() {
		return fullLinesDeleting;
	}

	public boolean isFieldMoving() {
		return fieldMoving;
	}

	public AbstractScoringStrategy getScoringStrategy() {
		return scoringStrategy;
	}
	
}
