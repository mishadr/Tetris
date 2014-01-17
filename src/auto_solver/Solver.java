package auto_solver;

import game_engine.Field;
import game_engine.figures.Figure;
import game_engine.figures.FiguresManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import user_control.GameParameters;

public class Solver {

	private final Field fieldInit;
	private List<Figure> figuresList;
	
	private List<Field> fieldsList;

	public Solver(GameParameters params)
	{
		fieldInit = new Field(params.getWidth(), params.getHeight());

	}
	
	@SuppressWarnings("unchecked")
	public static List<Figure> loadFiguresFromFile(String fileName) {
		try (ObjectInputStream os = new ObjectInputStream(new FileInputStream(
				fileName))) {
			return (List<Figure>) os.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void loadFigures(String fileName) {
		figuresList = loadFiguresFromFile(fileName);
	}
	
	void generateFigures(GameParameters params, int size) {
		figuresList = new ArrayList<>(size);
		for (int i = 0; i < size; ++i) {
			figuresList.add(FiguresManager.generateOne(params
					.getIncludedTypes()));
		}
	}

	void saveFigures(String fileName) {
		try(ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName)))
		{
			os.writeObject(figuresList);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public double solve(Model model)
	{
		int iter = 0;
		Field field = fieldInit.clone();
		fieldsList = new ArrayList<>();
		for(Figure figure: figuresList)
		{
			//System.out.println("iteration: " + iter++);
			FiguresManager.put(figure, field);
			fieldsList.add(field.clone());
			field = playFigure(figure, field, model);
			if(field == null)
				break;
			FiguresManager.deleteFullLines(field);
			iter++;
		}
		double score = iter;
		return score;
	}
	

	/**
	 * Puts figure in given field in the best way according to the model.
	 * 
	 * @param figure
	 * @param field
	 * @param model 
	 * @return
	 */
	private Field playFigure(Figure figure, Field field, Model model) {
		List<Field> variants = findVariants(field, figure);
		if (variants.isEmpty()) {
			return null;
		}
		return chooseBestField(variants, model);
	}

	/**
	 * Find all possible results of applying given figure to given field.
	 * 
	 * @param field
	 * @param figure
	 * @return
	 */
	private List<Field> findVariants(Field field, Figure figure) {
		List<Field> variants = new LinkedList<>();
		boolean ok;
		ok = FiguresManager.put(figure, field);
		if(!ok)
			return variants;
		
		// simple case when we firstly rotate then move figure
		for(int mood = 0; mood<4; ++mood)
		{
			// move left until possible
			int dx = 0;
			for(;;dx--)
			{
				ok = FiguresManager.move(figure, field, -1, 0);
				if(!ok)
					break;
				Figure newFigure = figure.clone();
				for (; FiguresManager.move(newFigure, field, 0, 1);) {
					/* full down */
				}
				Field newField = field.clone();
				FiguresManager.embed(newFigure, newField);
				variants.add(newField);
			}
			// move back
			FiguresManager.move(figure, field, -dx-1, 0);
			// move right until possible
			dx = 0;
			for(;;dx++)
			{
				ok = FiguresManager.move(figure, field, 1, 0);
				if(!ok)
					break;
				Figure newFigure = figure.clone();
				for (; FiguresManager.move(newFigure, field, 0, 1);) {
					/* full down */
				}
				Field newField = field.clone();
				FiguresManager.embed(newFigure, newField);
				variants.add(newField);
			}
			// move back
			FiguresManager.move(figure, field, -dx, 0);
			// next mood
			ok = FiguresManager.rotate(figure, field);
			if(!ok)
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
	private Field chooseBestField(List<Field> fields, Model model) {
		Field best = null;
		double max = -Double.MAX_VALUE;
		for(Field f: fields)
		{
			double goodness = model.evaluate(f.getGrid());
			if(goodness > max)
			{
				best = f;
				max = goodness;
			}
		}
		return best;
	}
	
	public void visualize() {
		new Visualizer().show(fieldsList, figuresList);
	}
}
