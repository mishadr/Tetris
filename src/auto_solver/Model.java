package auto_solver;

import game_engine.Field;

public class Model {

	// parameters
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

	// hole in an almost full line is worse then that in row with many holes
	double holesInRowCoeff;

	// smooth relief
	double reliefCoeff;
	
	// when field is full lines deleting is more important
	

	/**
	 * Model with equal parameters.
	 * 
	 * @return
	 */
	public static Model defaultModel() {
		return new Model(1, 6, 1, 1, 1, 1.0, 1.0, 1.0);
		// return new Model(0.98, 2.93, 0.88, 1.0);
	}

	public Model(double... params) {
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

	public void setParameter(int index, double value) {
		double[] params = getParameters();
		params[index] = value;
		setParameters(params);
	}

	public double evaluate(int[][] grid) {
		double result = 0;
		result += potential(grid);
		result += fullLine(grid);
		result += hole(grid);
		result += fitting(grid);
//		result += heightAboveHole(grid);
//		result += holesInRow(grid);
		result += relief(grid);
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
		double result = 0;
		int w = grid.length;
		int h = grid[0].length;
		for (int j = 0; j < h; ++j) {
			boolean full = true;
			for (int i = 0; i < w; ++i) {
				if (grid[i][j] == Field.FREE) {
					full = false;
					break;
				}
			}
			if (full) {
				result += fullLineCoeff;
			}
		}
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
			result -= reliefCoeff*Math.abs(j - leftHeight);
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
		for (int i = 1; i < size; ++i) {// first param fixed
			params[i] *= (Math.random() * 1 + 0.6);
		}
		return new Model(params);
	}

	@Override
	public String toString() {
		return "Model [potential=" + potentialCoeff + ", fullLine="
				+ fullLineCoeff + ", hole=" + holeCoeff + ", fitting="
				+ fittingCoeff + ", aboveHole=" + heightAboveHoleCoeff
				+ ", holesInRow=" + holesInRowCoeff + ", relief=" + reliefCoeff
				+ "]";
	}

}
