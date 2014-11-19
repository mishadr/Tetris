package game_engine.figures;

import java.util.List;
import java.util.Map;

/**
 * Utility that randomly chooses {@link AbstractSkeleton}s among allowed ones and
 * gives them one by one. Choosing method is specified in subclass.
 * 
 * @author misha
 * 
 */
public abstract class AbstractFiguresChooser {

	protected static final Map<FigureType, List<AbstractFigure>> allFigures = SkeletonsBuilder
			.getAllFigures();

//	protected AbstractFiguresChooser(FigureType[] figureTypes) {
//		// TODO load more figures if needed
//	}

	/**
	 * Generate one {@link AbstractSkeleton} in random mood according to the
	 * strategy.
	 * 
	 * @return ready to use AbstractFigure (cloned)
	 */
	public abstract AbstractFigure next();

}
