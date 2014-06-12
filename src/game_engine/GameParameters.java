package game_engine;

import game_engine.figures.FigureType;
import game_engine.figures.AbstractFiguresChooser;
import game_engine.figures.UniformByItemFiguresChooser;
import game_engine.scoring.AbstractScoringStrategy;

/**
 * Container of all game parameters that can be built of {@link Level} and
 * {@link GameStyle} or fully custom.
 * 
 * @author misha
 * 
 */
public class GameParameters {

	public static final int WIDTH_MIN = 8;
	public static final int WIDTH_MAX = 100;
	public static final int HEIGHT_MIN = 8;
	public static final int HEIGHT_MAX = 100;

	private final int width;
	private final int height;
	private final FigureType includedTypes[];
	private final AbstractScoringStrategy scoringStrategy;

	private final boolean reflectionsAllowed;
	private final boolean pauseAllowed;
	private final boolean nextFiguresShowed;

	private final boolean downShiftAllowed;
	private final boolean figureSwapAllowed;
	private final boolean fullLinesDeleting;
	private final boolean fieldMoving;

	private final AbstractFiguresChooser figuresChooser;
	// XXX it should be in scoringStrategy
	private final boolean increaseSpeed;
	
	// XXX maybe not final or not here!
	private final boolean allTimeFading;
	private final boolean fadeWhenFigureFinished;
	private final int fadingIterationsCount;

	/**
	 * Fully custom parameters
	 */
	public GameParameters(int width, int height, FigureType[] includedTypes,
			AbstractScoringStrategy scoringStrategy, boolean allowReflections,
			boolean allowPause, boolean allowDownShift,
			boolean allowFigureSwap, boolean showNextFigures,
			boolean deleteFullLines, boolean moveField, boolean increaseSpeed,
			AbstractFiguresChooser figuresChooser) {
		this.width = width;
		this.height = height;
		this.includedTypes = includedTypes;
		this.reflectionsAllowed = allowReflections;
		this.pauseAllowed = allowPause;
		this.nextFiguresShowed = showNextFigures;

		this.downShiftAllowed = allowDownShift;
		this.figureSwapAllowed = allowFigureSwap;
		this.fullLinesDeleting = deleteFullLines;
		this.fieldMoving = moveField;
		this.scoringStrategy = scoringStrategy;

		this.figuresChooser = figuresChooser;
		this.increaseSpeed = increaseSpeed;
		
		allTimeFading = true;
		fadeWhenFigureFinished = false;
		fadingIterationsCount = 8;
	}

	/**
	 * Build parameters defined by {@link Level} and {@link GameStyle}
	 */
	public GameParameters(Level level, GameStyle gameStyle) {
		this.width = level.getWidth();
		this.height = level.getHeight();
		this.includedTypes = level.getIncludedTypes();
		this.reflectionsAllowed = level.isReflectionsAllowed();
		this.pauseAllowed = level.isPauseAllowed();
		this.nextFiguresShowed = level.isNextFiguresShowed();

		this.downShiftAllowed = gameStyle.isDownShiftAllowed();
		this.figureSwapAllowed = gameStyle.isFigureSwapAllowed();
		this.fullLinesDeleting = gameStyle.isFullLinesDeleting();
		this.fieldMoving = gameStyle.isFieldMoving();
		this.scoringStrategy = gameStyle.getScoringStrategy();

		this.figuresChooser = new UniformByItemFiguresChooser(includedTypes);
		this.increaseSpeed = true;
		
		allTimeFading = false;
		fadeWhenFigureFinished = true;
		fadingIterationsCount = 8;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public FigureType[] getIncludedTypes() {
		return includedTypes;
	}

	public boolean isReflectionsAllowed() {
		return reflectionsAllowed;
	}

	public boolean isPauseAllowed() {
		return pauseAllowed;
	}

	public boolean isDownShiftAllowed() {
		return downShiftAllowed;
	}

	public boolean isFigureSwapAllowed() {
		return figureSwapAllowed;
	}

	public boolean isNextFiguresShowed() {
		return nextFiguresShowed;
	}

	public boolean isIncreaseSpeed() {
		return increaseSpeed;
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

	public AbstractFiguresChooser getFiguresChooser() {
		return figuresChooser;
	}

	public boolean isAllTimeFading() {
		return allTimeFading;
	}

	public boolean isFadeWhenFigureFinished() {
		return fadeWhenFigureFinished;
	}

	public int getFadingIterationsCount() {
		return fadingIterationsCount;
	}
}
