package game_engine.figures;

import java.util.List;

/**
 * Chooses random type then random figure among the ones of that type.
 * 
 * @author misha
 *
 */
public class UniformByTypeFiguresChooser extends AbstractFiguresChooser {

	private final FigureType[] figureTypes;

	public UniformByTypeFiguresChooser(FigureType[] figureTypes) {
		this.figureTypes = figureTypes;
	}

	@Override
	public AbstractFigure next() {
		FigureType type = figureTypes[(int) (figureTypes.length * Math.random())];
		List<AbstractFigure> figures = allFigures.get(type);
		AbstractFigure figure = figures.get((int) (figures.size() * Math.random()));
		int mood = (int) (4 * Math.random());
		for (int i = 0; i < mood; ++i) {
			figure.nextMood();
		}
		return figure.clone();
	}

}
