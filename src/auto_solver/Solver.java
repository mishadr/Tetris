package auto_solver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import game_engine.AbstractField;
import game_engine.FastField;
//import game_engine.Field;
import game_engine.GameParameters;
import game_engine.figures.AbstractFigure;
import game_engine.figures.AbstractFiguresChooser;

/**
 * Solver can run a solving model on a game with predefined sequence of figures
 * falling and field parameters. It also can generate and save random sequences
 * of figures from an allowed set determined by {@link GameParameters}.
 * 
 * @author misha
 * 
 */
public class Solver {

	private final FastField fieldInit;
	private List<AbstractFigure> figuresSequence;
	private boolean reflectionsAllowed;

	private List<AbstractField> fieldsList;

	/**
	 * Initialize a new solver for a game determined by {@link GameParameters}.
	 * 
	 * @param params
	 */
	public Solver(GameParameters params) {
		// fieldInit = new Field(params.getWidth(), params.getHeight());
		// or fast variant
		fieldInit = new FastField(params.getWidth(), params.getHeight());

		fieldsList = new ArrayList<>();
		figuresSequence = new ArrayList<>();
		// FieldManager.fillBottomLinesRandomly(fieldInit,
		// fieldInit.getHeight()-10, 0.9);
		reflectionsAllowed = params.isReflectionsAllowed();
	}

	@SuppressWarnings("unchecked")
	private static List<AbstractFigure> loadFiguresFromFile(String fileName) {
		try (ObjectInputStream os = new ObjectInputStream(new FileInputStream(fileName))) {
			return (List<AbstractFigure>) os.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Loads a sequence of figures from file with the specified name (inside
	 * solver's directory).
	 * 
	 * @param fileName
	 */
	public void loadFigures(String fileName) {
		figuresSequence = loadFiguresFromFile(fileName);
	}

	void generateFiguresSequence(GameParameters params, int count) {
		AbstractFiguresChooser figuresChooser = params.getFiguresChooser();
		figuresSequence = new ArrayList<>(count);
		for (int i = 0; i < count; ++i) {
			figuresSequence.add(figuresChooser.next());
		}
	}

	void saveFiguresSequence(String fileName) {
		new File(fileName).getParentFile().mkdirs();
		try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName))) {
			os.writeObject(figuresSequence);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void drop() {
		fieldsList = new ArrayList<>();
		fieldsList = new ArrayList<>();
	}

	/**
	 * Performs playing iterations using solving model until playing is
	 * possible. Saves all intermediate fields if specified.
	 * 
	 * @param model
	 * @param save
	 *            true if save intermediate fields is needed
	 * @return final score
	 */
	public double solve(Model model, boolean save) {
		drop();
		AbstractField field = fieldInit.clone();
		int iter = 0;
		for (AbstractFigure figure : figuresSequence) {
			if (save) {
				fieldsList.add(field.clone());
			}
			field = playFigure(figure.clone(), field, model);
			if (field == null)
				break;
			iter++;
		}
		double score = iter;
		return score;
	}

	/**
	 * Tries to put the figure in the given field in all possible ways and
	 * returns the resulting field which was max-scored by the given model.
	 * 
	 * @param figure
	 * @param field
	 * @param model
	 * @return
	 */
	private AbstractField playFigure(AbstractFigure figure, AbstractField field, Model model) {
		boolean ok;
		ok = figure.put(field);
		if (!ok)
			return null;

		AbstractField bestField = null;
		double maxScore = -Double.MAX_VALUE;

		// simple case when we firstly rotate then move figure
		for (int orientation = 0; orientation < 2; ++orientation) {
			for (int mood = 0; mood < 4; ++mood) {
				int[] originalPos = figure.getPosition();
				// move left while possible
				int dx = 0;
				for (;; dx--) {
					ok = figure.move(field, -1, 0);
					if (!ok)
						break;
					int[] pos = figure.getPosition();
					// AbstractFigure newFigure = figure.clone();
					AbstractField newField = field.clone();
					int dist = figure.computeFullDownDistance(field);
					figure.move(field, 0, dist);
					figure.embed(newField);
					figure.setPosition(pos);
					// check whether this field is better according to the model
					double score = model.evaluate(newField);
					if (score > maxScore) {
						bestField = newField;
						maxScore = score;
					}
				}
				// move back
				figure.setPosition(originalPos);
				// move right while possible
				dx = 0;
				for (;; dx++) {
					ok = figure.move(field, 1, 0);
					if (!ok)
						break;
					int[] pos = figure.getPosition();
					// AbstractFigure newFigure = figure.clone();
					AbstractField newField = field.clone();
					int dist = figure.computeFullDownDistance(field);
					figure.move(field, 0, dist);
					figure.embed(newField);
					figure.setPosition(pos);
					// check whether this field is better according to the model
					double score = model.evaluate(newField);
					if (score > maxScore) {
						bestField = newField;
						maxScore = score;
					}
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

		return bestField;
	}

	public void visualize(int timerDelay) {
		new Visualizer(timerDelay).show(fieldsList, figuresSequence);
	}

	public void fillField(int startHeight, double density) {
		fieldInit.fillRandomly(startHeight, density);
	}
}
