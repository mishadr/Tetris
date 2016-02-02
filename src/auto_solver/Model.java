package auto_solver;

import auto_solver.genetic.Organism;
import game_engine.AbstractField;
import game_engine.FastField;
import game_engine.Field;
import game_engine.figures.FieldManager;

public class Model implements Organism {

	// parameters number
	private static final int size = 7;
	
//	List<Parameter> included;
	// weights:

	// less height - better
	double potentialCoeff;

	// full line - good
	double fullLineCoeff;

	// holes - bad
	double holeCoeff;

	// fitting to environment
	double fittingCoeff;

	// height above hole, big massive (2-3) above hole is worse (than 1)
	double heightAboveHoleCoeff;

	// hole in an almost full line is worse than that in row with many holes
	double holesInRowCoeff;

	// smooth relief
	double reliefCoeff;
	
	// when field is full lines deleting is more important
	

	/**
	 * Model with first <code>size</code> parameters set to 1.0.
	 * 
	 * @return
	 */
	public static Model defaultModel() {
		double[] params = new double[size];
		params[0] = 1.0;
		for(int i=0;i<size;++i)
			params[i] = 1.0;
		return new Model(params);
	}
	
	/**
	 * Model with random parameters uniformly in [0, <code>scope</code>).
	 * 
	 * @param scope
	 * @return
	 */
	public static Model randomModel(double scope) {
		double[] params = new double[size];
		params[0] = 1.0;
		for(int i=1;i<size;++i)
			params[i] = Math.random() * scope;
		return new Model(params);
	}

	public Model(double... params) {
//		included = new LinkedList<>();
		setParameters(params);
	}

	/**
	 * Get all parameters in order as they go to constructor.
	 * 
	 * @return
	 */
	public double[] getParameters() {
		
		return new double[] { potentialCoeff, fullLineCoeff, holeCoeff,
				fittingCoeff, heightAboveHoleCoeff, holesInRowCoeff, reliefCoeff };
	}

	public void setParameters(double... params) {
		try {
			this.potentialCoeff = params[0];
			this.fullLineCoeff = params[1];
			this.holeCoeff = params[2];
			this.fittingCoeff = params[3];
			this.heightAboveHoleCoeff = params[4];
			this.holesInRowCoeff = params[5];
			this.reliefCoeff = params[6];
		} catch (ArrayIndexOutOfBoundsException e) {}
	}

	/**
	 * Set parameter, specified by its index starting from 0. 
	 * 
	 * @param index
	 * @param value
	 */
	public void setParameter(int index, double value) {
		double[] params = getParameters();
		params[index] = value;
		setParameters(params);
	}

	public double evaluate(AbstractField field) {
		// XXX it doesn't help, why?
		return field.evaluate(this);
	}

	public double evaluate(FastField field) {
		// TODO evaluate all at the same time
		
		double result = 0;
		result += potential(field);
		result += fullLine(field);
		// full lines deleted now
		result += hole(field);
		result += fitting(field);
		result += heightAboveHole(field);
		result += holesInRow(field);
		result += relief(field);
//		System.out.println(result);
		return result;
	}

	private double potential(FastField field) {
		boolean[] grid = field.getGrid();
		int w = field.getWidth();
		int h = field.getHeight();

		double result = 0;
		for (int i = 0; i < w; ++i) {
			for (int j = 0; j < h; ++j) {
				if (grid[i * h + j]) {
					result += potentialCoeff * j;
				}
			}
		}
		return result;
	}

	private double fullLine(FastField field) {
//		boolean[] grid = field.getGrid();
//		int w = field.getWidth();
//		int h = field.getHeight();
//
//		double result = 0;
//		boolean[] notFull = new boolean[h];
//		for (int i = 0; i < w; ++i) {
//			for (int j = 0; j < h; ++j) {
//				notFull[j] |= !grid[i * h + j];
//			}
//		}
//		for (int j = 0; j < h; ++j) {
//			if(!notFull[j]) {
//				result += fullLineCoeff;
//				// delete full line
//				FieldManager.pullDown(grid, w, h, j);
//			}
//		}
		return fullLineCoeff * FieldManager.deleteFullLines(field);
	}

	private double hole(FastField field) {
		boolean[] grid = field.getGrid();
		int w = field.getWidth();
		int h = field.getHeight();

		double result = 0;
		for (int i = 0; i < w; ++i) {
			int holes = 0;
			int j = 0;
			for (; j < h; ++j) {
				if (grid[i * h + j]) {
					break;
				}
			}
			j++;
			for (; j < h; ++j) {
				if (!grid[i * h + j]) {
					holes++;
				}
			}
			result -= holeCoeff * holes;// holes are bad
		}
		return result;
	}

	private double fitting(FastField field) {
		boolean[] grid = field.getGrid();
		int w = field.getWidth();
		int h = field.getHeight();

		int diff = 0;
		for (int i = 0; i < w - 1; ++i) {
			for (int j = 0; j < h - 1; ++j) {
				if (grid[i * h + j] != grid[i * h + j + 1])
					diff++;
				if (grid[i * h + j] != grid[(i + 1) * h + j])
					diff++;
			}
			if (grid[i * h + h-1] != grid[(i + 1) * h + h-1])// bottom row
				diff++;
			if(!grid[i * h + h-1])// botom edge
				diff++;
			if(!grid[i * h + 0])// top edge
				diff++;
		}
		if(!grid[(w-1) * h + h-1])// right botom cell
			diff++;
		if(!grid[(w-1) * h + 0])// right top cell
			diff++;
		for (int j = 0; j < h - 1; ++j) {
			if (grid[(w-1) * h + j] != grid[(w-1) * h + j + 1])// right column
				diff++;
			if(!grid[(w-1) * h + j])// right edge
				diff++;
			if(!grid[0 * h + j])// left edge
				diff++;
		}
		if(!grid[(w-1) * h + h-1])// right botom cell - second time
			diff++;
		if(!grid[h-1])// left botom cell
			diff++;
		return -diff * fittingCoeff;
	}

	private double heightAboveHole(FastField field) {
		boolean[] grid = field.getGrid();
		int w = field.getWidth();
		int h = field.getHeight();

		double result = 0;
		for (int i = 0; i < w; ++i) {
			int height;
			int j = 0;
			for (; j < h; ++j) {
				if (grid[i * h + j]) {
					break;
				}
			}
			int upperHeight = j++;
			for (; j < h; ++j) {
				if (!grid[i * h + j]) {
					break;
				}
			}
			if (j < h) {
				height = j - upperHeight;// > 0
				double x = heightAboveHoleCoeff * (-height);// must be < 0
				result += x < 0 ? x : 0;
			}
		}
		return result;
	}

	private double holesInRow(FastField field) {
		boolean[] grid = field.getGrid();
		int w = field.getWidth();
		int h = field.getHeight();

		double result = 0;
		for (int i = 0; i < w; ++i) {
			int holesInRow = 0;
			int j = 0;
			for (; j < h; ++j) {
				if (grid[i * h + j]) {
					break;
				}
			}
			j++;
			for (; j < h; ++j) {
				if (!grid[i * h + j]) {
					holesInRow = 0;
					for (int k = 0; k < w; ++k) {
						if (!grid[k * h + j])
							holesInRow++;
					}
					double x = holesInRowCoeff * (holesInRow-2);// must be < 0
					result += x < 0 ? x : 0;
					break;// we condider only the highest hole
				}
			}
		}
		return result;
	}
	
	// invokation works slowly!
	public static final int abs(int x) {
		return x < 0 ? -x : x;
	}
	
	private double relief(FastField field) {
		boolean[] grid = field.getGrid();
		int w = field.getWidth();
		int h = field.getHeight();

		double result = 0;
		int leftHeight, j=0;
		for (; j < h; ++j) {
			if (grid[j]) {
				break;
			}
		}
		for (int i = 1; i < w; ++i) {
			leftHeight = j;
			j = 0;
			for (; j < h; ++j) {
				if (grid[i * h + j]) {
					break;
				}
			}

			int diff = (j - leftHeight);
			diff = diff < 0 ? -diff : diff;
//			int y = diff >>> 31;
//			diff = (diff^y) - y;
			// do not fire 1 brick differences
			if (diff > 1) {
				result -= reliefCoeff * diff;
			}
		}
		return result;
	}

	// Old version for compatibility with common Field
	
	public double evaluate(Field field) {
		int[][] grid = field.getGrid();
		// TODO evaluate all at the same time
		double result = 0;
		result += potential(grid);
		result += fullLine(grid);
		// full lines deleted now
		result += hole(grid);
		result += fitting(grid);
		result += heightAboveHole(grid);
		result += holesInRow(grid);
		result += relief(grid);
//		System.out.println(result);
		return result;
	}

	private double potential(int[][] grid) {
		double result = 0;
		int w = grid.length;
		int h = grid[0].length;
		for (int i = 0; i < w; ++i) {
			for (int j = 0; j < h; ++j) {
				if (grid[i][j] != Field.FREE) {
					result += potentialCoeff * j;
				}
			}
		}
		return result;
	}

	private double fullLine(int[][] grid) {
		// TODO We can run vertically checking h arrays concurrently
		double result = 0;
		int w = grid.length;
		int h = grid[0].length;
		boolean[] notFull = new boolean[h];
		for (int i = 0; i < w; ++i) {
			for (int j = 0; j < h; ++j) {
				notFull[j] |= grid[i][j] == Field.FREE;
			}			
		}
		for (int j = 0; j < h; ++j) {
			if(!notFull[j]) {
				result += fullLineCoeff;
				// delete full line
				FieldManager.pullDown(grid, j);
			}
		}
		
//		for (int j = 0; j < h; ++j) {
//			boolean full = true;
//			for (int i = 0; i < w; ++i) {
//				if (grid[i][j] == Field.FREE) {
//					full = false;
//					break;
//				}
//			}
//			if (full) {
//				result += fullLineCoeff;
//				FieldManager.pullDown(grid, j);// delete full line
//			}
//		}
		return result;
	}

	private double hole(int[][] grid) {
		double result = 0;
		int w = grid.length;
		int h = grid[0].length;
		for (int i = 0; i < w; ++i) {
			int holes = 0;
			int j = 0;
			for (; j < h; ++j) {
				if (grid[i][j] != Field.FREE) {
					break;
				}
			}
			j++;
			for (; j < h; ++j) {
				if (grid[i][j] == Field.FREE) {
					holes++;
				}
			}
			result -= holeCoeff * holes;// holes are bad
		}
		return result;
	}

	private double fitting(int[][] grid) {
		int diff = 0;
		int w = grid.length;
		int h = grid[0].length;
		for (int i = 0; i < w - 1; ++i) {
			for (int j = 0; j < h - 1; ++j) {
				if (grid[i][j] != grid[i][j + 1])
					diff++;
				if (grid[i][j] != grid[i + 1][j])
					diff++;
			}
			if (grid[i][h-1] != grid[i + 1][h-1])// bottom row
				diff++;
			if(grid[i][h-1] == Field.FREE)// botom edge
				diff++;
			if(grid[i][0] == Field.FREE)// top edge
				diff++;
		}
		if(grid[w-1][h-1] == Field.FREE)// right botom cell
			diff++;
		if(grid[w-1][0] == Field.FREE)// right top cell
			diff++;
		for (int j = 0; j < h - 1; ++j) {
			if (grid[w-1][j] != grid[w-1][j + 1])// right column
				diff++;
			if(grid[w-1][j] == Field.FREE)// right edge
				diff++;
			if(grid[0][j] == Field.FREE)// left edge
				diff++;
		}
		if(grid[w-1][h-1] == Field.FREE)// right botom cell - second time
			diff++;
		if(grid[0][h-1] == Field.FREE)// left botom cell
			diff++;
		return -diff * fittingCoeff;
	}

	private double heightAboveHole(int[][] grid) {
		double result = 0;
		int w = grid.length;
		int h = grid[0].length;
		for (int i = 0; i < w; ++i) {
			int height;
			int j = 0;
			for (; j < h; ++j) {
				if (grid[i][j] != Field.FREE) {
					break;
				}
			}
			int upperHeight = j++;
			for (; j < h; ++j) {
				if (grid[i][j] == Field.FREE) {
					break;
				}
			}
			if (j < h) {
				height = j - upperHeight;// > 0
				result += Math.min(0, heightAboveHoleCoeff * (-height));// must be < 0
			}
		}
		return result;
	}

	private double holesInRow(int[][] grid) {
		double result = 0;
		int w = grid.length;
		int h = grid[0].length;
		for (int i = 0; i < w; ++i) {
			int holesInRow = 0;
			int j = 0;
			for (; j < h; ++j) {
				if (grid[i][j] != Field.FREE) {
					break;
				}
			}
			j++;
			for (; j < h; ++j) {
				if (grid[i][j] == Field.FREE) {
					holesInRow = 0;
					for (int k = 0; k < w; ++k) {
						if (grid[k][j] == Field.FREE)
							holesInRow++;
					}
					result += Math.min(0, holesInRowCoeff * (holesInRow-2));// mustn't be > 0
					break;// if only highest hole considered
				}
			}
		}
		return result;
	}

	private double relief(int[][] grid) {
		double result = 0;
		int w = grid.length;
		int h = grid[0].length;
		int j=0;
		for (; j < h; ++j) {
			if (grid[0][j] != Field.FREE) {
				break;
			}
		}
		int leftHeight = j;
		for (int i = 1; i < w; ++i) {
			j = 0;
			for (; j < h; ++j) {
				if (grid[i][j] != Field.FREE) {
					break;
				}
			}
			
			int diff = Math.abs(j - leftHeight);
			if(diff > 1)// do not fire 1 brick differences
			{
				result -= reliefCoeff*diff;
			}
			leftHeight = j;
		}
		return result;
	}

	

	// TODO optimize one parameter then another several times
	// the problem may be in local extremums
	
	public static Model mutate(Model model) {
		double[] params = model.getParameters();
		int size = params.length;
//		int index = 1+(int) (size * Math.random());
//		params[index] *= (Math.random() * 1 + 0.6);
		for (int i = 0; i < size; ++i) {// first param fixed
			params[i] *= (Math.random() * 1 + 0.6);
		}
		return new Model(params);
	}

	@Override
	public String toString() {
		return "Model [potential=" + potentialCoeff
				+ ", fullLine=" + fullLineCoeff
				+ ", hole=" + holeCoeff
				+ ", fitting=" + fittingCoeff
				+ ", aboveHole=" + heightAboveHoleCoeff
				+ ", holesInRow=" + holesInRowCoeff
				+ ", relief=" + reliefCoeff
				+ "]";
	}

	@Override
	public void mutate() {
		double[] params = getParameters();
		for (int i = 0; i < size; ++i) {// first param fixed
			params[i] *= (Math.random() * 1 + 0.6);
		}
		setParameters(params);
	}

	@Override
	public Organism crossingoverWith(Organism o) {
		Model model = (Model) o;
		double[] p0 = getParameters();
		double[] p1 = model.getParameters();
		double[] p = new double[size];
		p[0] = 1.0;
		for (int i = 1; i < size; ++i) {
			p[i] = Math.random() < 0.5 ? p0[i] : p1[i];
		}
		return new Model(p);
	}

}

//enum Parameter {
//	
////	public static final Parameter potentialCoeff = new Parameter("potential", 1.0);
////	public static final Parameter fullLineCoeff = new Parameter("full line", 1.0);
//	POTENTIAL_COEFF(1.0),
//	;
//	
////	private final String name;
//	private double value;
//	
//	Parameter(double value) {
////		this.name = name;
//		this.setValue(value);
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public double getValue() {
//		return value;
//	}
//
//	public void setValue(double value) {
//		this.value = value;
//	}
//}
