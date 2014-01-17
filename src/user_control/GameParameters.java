package user_control;

import java.io.Serializable;

import game_engine.figures.FigureType;

public class GameParameters implements Serializable {
	private static final long serialVersionUID = -6215777092801981319L;
	public static final int WIDTH_MIN = 8;
	public static final int WIDTH_MAX = 100;
	public static final int HEIGHT_MIN = 8;
	public static final int HEIGHT_MAX = 100;

	private final int width;
	private final int height;
	private final FigureType includedTypes[];

	public GameParameters(int width, int height, FigureType includedTypes[]) {
		this.width = width;
		this.height = height;
		this.includedTypes = includedTypes;
	}

	public FigureType[] getIncludedTypes() {
		return includedTypes;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public static GameParameters defaultParameters() {
		return new GameParameters(15, 20, new FigureType[] {
				FigureType.WHOLE_4, FigureType.WHOLE_5 });
	}

	public static GameParameters simple() {
		return new GameParameters(8, 12,
				new FigureType[] { FigureType.WHOLE_4 });
	}

	public static GameParameters medium() {
		return new GameParameters(12, 18, new FigureType[] {
				FigureType.WHOLE_4, FigureType.WHOLE_5 });
	}

	public static GameParameters large() {
		return new GameParameters(21, 27, new FigureType[] {
				FigureType.PENETRATING, FigureType.SEMIWHOLE_2,
				FigureType.SEMIWHOLE_3, FigureType.WHOLE_1, FigureType.WHOLE_2,
				FigureType.WHOLE_3, FigureType.WHOLE_4, FigureType.WHOLE_5,
				FigureType.WHOLE_6 });
	}

	public static GameParameters full() {
		return new GameParameters(18, 25, FigureType.values());
	}

}
