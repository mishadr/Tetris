package auto_solver;

import game_engine.AbstractField;
import game_engine.FastField;
import game_engine.Field;
//import game_engine.Field;
import game_engine.GameParameters;
import game_engine.figures.AbstractFigure;
import game_engine.figures.AbstractFiguresChooser;
import game_engine.figures.FieldManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Solver can run a solver model on a game with figures sequence using a solving
 * model.
 * 
 * @author misha
 * 
 */
public class Solver {

	private final AbstractField fieldInit;
	private List<AbstractFigure> figuresSequence;
	private boolean reflectionsAllowed;

	private List<AbstractField> fieldsList;

	/**
	 * Initialize a new solver for a game defined by {@link GameParameters}.
	 * 
	 * @param params
	 */
	public Solver(GameParameters params) {
		fieldInit = new Field(params.getWidth(), params.getHeight());
		// or fast variant
//		fieldInit = new FastField(params.getWidth(), params.getHeight());
		
		fieldsList = new ArrayList<>();
		figuresSequence = new ArrayList<>();
		// FieldManager.fillBottomLinesRandomly(fieldInit,
		// fieldInit.getHeight()-10, 0.9);
		reflectionsAllowed = params.isReflectionsAllowed();
	}

	@SuppressWarnings("unchecked")
	public static List<AbstractFigure> loadFiguresFromFile(String fileName) {
		try (ObjectInputStream os = new ObjectInputStream(new FileInputStream(
				fileName))) {
			return (List<AbstractFigure>) os.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Loads figures sequence from file with the specified name (inside solver's
	 * directory).
	 * 
	 * @param fileName
	 */
	public void loadFigures(String fileName) {
		figuresSequence = loadFiguresFromFile(fileName);
	}

	void generateFigures(GameParameters params, int count) {
		AbstractFiguresChooser figuresChooser = params.getFiguresChooser();
		figuresSequence = new ArrayList<>(count);
		for (int i = 0; i < count; ++i) {
			figuresSequence.add(figuresChooser.next());
		}
	}

	void saveFiguresSequence(String fileName) {
		new File(fileName).getParentFile().mkdirs();
		try (ObjectOutputStream os = new ObjectOutputStream(
				new FileOutputStream(fileName))) {
			os.writeObject(figuresSequence);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Performs playing iterations according to solving model until it's
	 * possible. Saves all intermediate fields if needed.
	 * 
	 * @param model
	 * @param save
	 *            true if save intermediate fields is needed
	 * @return final score
	 */
	public double solve(Model model, boolean save) {
		AbstractField field = fieldInit.clone();
		int iter = 0;
		for (AbstractFigure figure : figuresSequence) {
			if(save) {
				fieldsList.add(field.clone());				
			}
			field = playFigure(figure, field, model);
			if (field == null)
				break;
			iter++;
		}
		double score = iter;
		return score;
	}

	/**
	 * Puts figure in given field in the best way according to the solving
	 * model.
	 * 
	 * @param figure
	 * @param field
	 * @param model
	 * @return
	 */
	private AbstractField playFigure(AbstractFigure figure, AbstractField field, Model model) {
		List<AbstractField> variants = findVariants(field, figure);
		if (variants.isEmpty()) {
			return null;
		}
		return chooseBestField(variants, model);
	}
	// TODO use boolean fields (and of one line) for speeding up

	/**
	 * Find all possible results of playing the given figure in the given field.
	 * 
	 * @param field
	 * @param figure
	 * @return
	 */
	private List<AbstractField> findVariants(AbstractField field, AbstractFigure figure) {
		List<AbstractField> variants = new LinkedList<>();
		boolean ok;
		ok = figure.put(field);
		if (!ok)
			return variants;

		// simple case when we firstly rotate then move figure
		for (int orientation = 0; orientation < 2; ++orientation) {
			for (int mood = 0; mood < 4; ++mood) {
				// move left until possible
				int dx = 0;
				for (;; dx--) {
					ok = figure.move(field, -1, 0);
					if (!ok)
						break;
					AbstractFigure newFigure = figure.clone();
					for (; newFigure.move(field, 0, 1);) {// FIXME slow
						/* full down */
					}
					AbstractField newField = field.clone();
					newFigure.embed(newField);
					variants.add(newField);
				}
				// move back
				figure.move(field, -dx - 1, 0);
				// move right until possible
				dx = 0;
				for (;; dx++) {
					ok = figure.move(field, 1, 0);
					if (!ok)
						break;
					AbstractFigure newFigure = figure.clone();
					for (; newFigure.move(field, 0, 1);) {// FIXME slow -- 19% of time!
						/* full down */
					}
					AbstractField newField = field.clone();
					newFigure.embed(newField);
					variants.add(newField);
				}
				// move back
				figure.move(field, -dx, 0);
				// next mood
				ok = figure.rotate(field);
				if (!ok)
					break;
			}
			if (reflectionsAllowed) {
				ok = figure.reflectVertical(field);
				if (!ok)
					break;
			} else
				break;
		}

		return variants;
	}

	/**
	 * Choose the best variant among given fields depending on model parameters.
	 * 
	 * @param fields
	 * @param model
	 * @return best field or null
	 */
	private AbstractField chooseBestField(List<AbstractField> fields, Model model) {
		AbstractField best = null;
		double max = -Double.MAX_VALUE;
		for (AbstractField f : fields) {
			double goodness = model.evaluate(f);
			if (goodness > max) {
				best = f;
				max = goodness;
			}
		}
		return best;
	}

	public void visualize(int timerDelay) {
		new Visualizer(timerDelay).show(fieldsList, figuresSequence);
	}
}
