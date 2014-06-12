package game_engine.figures;

import java.util.LinkedList;
import java.util.List;

/**
 * Chooses random figure among the whole set.
 * 
 * @author misha
 * 
 */
public class UniformByItemFiguresChooser extends AbstractFiguresChooser {

	private final AbstractFigure[] allowed;

	public UniformByItemFiguresChooser(FigureType[] figureTypes) {
		List<AbstractFigure> list = new LinkedList<>();
		for (FigureType type : figureTypes) {
			list.addAll(allFigures.get(type));
		}
		this.allowed = list.toArray(new AbstractFigure[list.size()]);
	}

	@Override
	public AbstractFigure next() {
		AbstractFigure figure = allowed[(int) (allowed.length * Math.random())]
				.clone();
		int mood = (int) (4 * Math.random());
		for (int i = 0; i < mood; ++i) {
			figure.nextMood();
		}
		return figure;
	}
}
