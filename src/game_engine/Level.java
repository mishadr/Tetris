package game_engine;

import game_engine.figures.FigureType;

/**
 * Several standard levels enumeration.
 * 
 * @author misha
 * 
 */
public enum Level {

//	TEST(32, 40, new FigureType[] {FigureType.SEPARATE_5_5, 
//			FigureType.SEPARATE_4_4,FigureType.SEPARATE_4_5,FigureType.SEPARATE_5_4,
//			/*FigureType.PENETRATING_WHOLE_1,
//			FigureType.PENETRATING_WHOLE_2, FigureType.PENETRATING_WHOLE_3,
//			FigureType.PENETRATING_SEMIWHOLE_2,
//			FigureType.PENETRATING_SEMIWHOLE_3, FigureType.WHOLE_3,
//			FigureType.WHOLE_4, FigureType.WHOLE_5, FigureType.WHOLE_6,
//			FigureType.WHOLE_7, FigureType.WHOLE_8, */}, true, true, false),

	CLASSIC_4(10, 20, new FigureType[] { FigureType.WHOLE_4 }, false, false, false),
	CLASSIC_5(10, 20, new FigureType[] { FigureType.WHOLE_5 }, false, false, false),
//	CLASSIC_6(),
	
//	SMALL(8, 12, new FigureType[] { FigureType.WHOLE_4 }, false, true, false),

	MEDIUM(12, 18, new FigureType[] { FigureType.PENETRATING_WHOLE_1,
			FigureType.WHOLE_4, FigureType.WHOLE_5 }, false, true, false),

	HARD_6(21, 27, new FigureType[] { FigureType.PENETRATING_WHOLE_1,
			FigureType.SEMIWHOLE_2, FigureType.SEMIWHOLE_3, FigureType.WHOLE_1,
			FigureType.WHOLE_2, FigureType.WHOLE_3, FigureType.WHOLE_4,
			FigureType.WHOLE_5, FigureType.WHOLE_6 }, true, true, false),

	HARD_7(24, 30, new FigureType[] { FigureType.PENETRATING_WHOLE_1,
			FigureType.PENETRATING_WHOLE_2, FigureType.PENETRATING_WHOLE_3,
			FigureType.PENETRATING_SEMIWHOLE_2,
			FigureType.PENETRATING_SEMIWHOLE_3, FigureType.WHOLE_1,
			FigureType.WHOLE_2, FigureType.WHOLE_3, FigureType.WHOLE_4,
			FigureType.WHOLE_5, FigureType.WHOLE_6, FigureType.WHOLE_7 }, true,
			true, false),

	MASTER(24, 30, new FigureType[] { FigureType.PENETRATING_WHOLE_1,
			FigureType.PENETRATING_WHOLE_2, FigureType.PENETRATING_WHOLE_3,
			FigureType.PENETRATING_SEMIWHOLE_2,
			FigureType.PENETRATING_SEMIWHOLE_3, FigureType.SEMIWHOLE_2,
			FigureType.SEMIWHOLE_3, FigureType.WHOLE_1, FigureType.WHOLE_2,
			FigureType.WHOLE_3, FigureType.WHOLE_4, FigureType.WHOLE_5,
			FigureType.WHOLE_6, FigureType.WHOLE_7,
			FigureType.SEPARATE_2_2, FigureType.SEPARATE_2_3, FigureType.SEPARATE_3_2, 
			FigureType.SEPARATE_3_3, FigureType.SEPARATE_4_3, FigureType.SEPARATE_3_4, 
			}, true, true, false),

	FULL(28, 36, FigureType.values(), true, true, false), ;

	private final int width;
	private final int height;
	private final FigureType[] includedTypes;
	private final boolean reflectionsAllowed;
	private final boolean pauseAllowed;
	private final boolean nextFiguresShowed;

	Level(int width, int height, FigureType includedTypes[],
			boolean reflectionsAllowed, boolean pauseAllowed,
			boolean nextFiguresShowed) {
		this.width = width;
		this.height = height;
		this.includedTypes = includedTypes;
		this.reflectionsAllowed = reflectionsAllowed;
		this.pauseAllowed = pauseAllowed;
		this.nextFiguresShowed = nextFiguresShowed;
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

	public boolean isNextFiguresShowed() {
		return nextFiguresShowed;
	}

}
