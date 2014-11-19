package auto_solver;

import game_engine.GameParameters;
import io.SimpleIO;

import java.util.Arrays;

/**
 * TODO
 * 
 * @author misha
 * 
 */
public class Trainer {

	// TODO train on random semifilled fields
	
	private String figuresPath;

	// private final String testDirPath = "test/large/";

	public void setFiguresDirPath(String figuresDirPath) {
		this.figuresPath = figuresDirPath;
	}

	// TODO train on random semifilled fields

	// TODO search best on 1 step radius sphere

	Model trainByMutations(GameParameters params, Model model) {
		System.out.println("Training by mutations");
		int trainingSetsCount = 80;
		Solver[] solvers = new Solver[trainingSetsCount];
		for (int i = 0; i < trainingSetsCount; ++i) {
			solvers[i] = new Solver(params);
			solvers[i].loadFigures(figuresPath + "figures" + i + ".fs");
		}

		double bestScore = computeMinScore(solvers, model);
		for (int i = 0; i < 40; ++i) {
			Model mutatedModel = Model.mutate(model);
			double newScore = computeMinScore(solvers, mutatedModel);
			if (newScore > bestScore) {
				bestScore = newScore;
				model = mutatedModel;
				System.out.println(i + ". Good mutation: " + model + ", score="
						+ bestScore);
			} else {
				// System.out.println("Bad mutation: " + mutatedModel +
				// ", score=" + newScore);
			}
		}
		SimpleIO.out(model + "\n", "trainedModels.txt", true);
		return model;
	}

	Model trainParametersSequently(GameParameters params, Model model) {

//		System.out.println("Training parameters sequently");
		double[] parameters = model.getParameters();
		int size = parameters.length;

		Model newModel;
		for (int i = 2; i < size; ++i) {
			newModel = new Model(Arrays.copyOf(parameters, i));

			// TODO try to change only last parameter
			newModel = trainByMutations(params, newModel);
			System.arraycopy(newModel.getParameters(), 0, parameters, 0, i);
		}

		model.setParameters(parameters);
		SimpleIO.out(model + "\n", "trainedModels.txt", true);
		return model;
	}

	Model trainByGradientShift(GameParameters params, Model model) {
		System.out.print("Training by gradient shift. ");
		
		int trainingSetsCount = 1;
		System.out.println("Training sets count = " + trainingSetsCount);

		Solver[] solvers = new Solver[trainingSetsCount];
		for (int i = 0; i < trainingSetsCount; ++i) {
			solvers[i] = new Solver(params);
			solvers[i].loadFigures(figuresPath + "figures" + (i+9) + ".fs");
		}

		double step = 1.125;
		
		double[] bestValues = model.getParameters();
		double bestScore = 0;
		
		//parameters to vary
		int n = 2;
		//parameters values
		double[][] values = new double[n+1][3];
		for (int i = 0; i < n+1; ++i) {
			values[i][1] = bestValues[i];
			values[i][0] = values[i][1] / step;
			values[i][2] = values[i][1] * step;
		}
/*		// TODO find out, what the hell?
		model.setParameters(1.0, 1.125, 1.375, 1.0, 1.0, 1.0, 1.0);
		double newScore = computeAvgScore(solvers, model);
		System.out.printf("score = %.3f\t", newScore);
		newScore = computeAvgScore(solvers, model);
		System.out.printf("score = %.3f\t", newScore);
*/		
/*
		double prevScore = 0;
		int stepsNumber = 10;
		while(stepsNumber-- > 0) {
			System.out.printf("Testing locality of values: %.3f, %.3f\n",
					values[1][1], values[2][1]);
			for(int v1 = 0; v1 < values[1].length; v1 ++) {
				model.setParameter(1, values[1][v1]);
				for(int v2 = 0; v2 < values[2].length; v2 ++) {
					model.setParameter(2, values[2][v2]);
//					for(int v3 = 0; v3 < values.length; v3 ++) {
//						model.setParameter(3, values[v3]);
//							for(int v4 = 0; v4 < values.length; v4 ++) {
//								model.setParameter(6, values[v4]);
					
								double newScore = computeAvgScore(solvers, model);
								if (newScore > bestScore) {
									bestScore = newScore;
									bestValues = model.getParameters();
								}
//								System.out.printf("%.1f(%.3f,%.3f,%.3f)\t", newScore, values[v1], values[v2], values[v3]);
//							}
//						}
//						System.out.printf("%.1f(%.3f,%.3f)\t", newScore, values[v1], values[v2]);
						System.out.printf("%.1f (%.3f, %.3f)\t", newScore, model.getParameters()[1], model.getParameters()[2]);
//						System.out.println();
				}
//				System.out.println();
				System.out.println();
			}
			// if we could increase score
			if(bestScore > prevScore) {
				System.out.printf("Advanced from %.2f to %.2f\n", prevScore, bestScore);
				System.out.printf("New values: %s\n", Arrays.toString(bestValues));
				prevScore = bestScore;
				// update locality
				for (int i = 0; i < n+1; ++i) {
					values[i][1] = bestValues[i];
					values[i][0] = values[i][1] / step;
					values[i][2] = values[i][1] * step;
				}
			}
			else {
				System.out.printf("Can't advance at one step anymore\n");
				break;
			}
		}*/

		
		model.setParameters(bestValues);
		SimpleIO.out(model + "\n", "trainedModels.txt", true);
		return model;
	}
	
	Model trainByMonteCarlo(GameParameters params, Model model) {
		System.out.print("Training by monte carlo. ");

		int trainingSetsCount = 20;
		System.out.println("Training sets count = " + trainingSetsCount);

		Solver[] solvers = new Solver[trainingSetsCount];
		for (int i = 0; i < trainingSetsCount; ++i) {
			solvers[i] = new Solver(params);
			solvers[i].loadFigures(figuresPath + "figures" + i + ".fs");
		}

		// logarithmically uniform values 
		int size = 4;
		double[] values = new double[2*size+1];
		double degree = -1; // -1 .. 1
		for(int i=0; i<=2*size; ++i) { 
			values[i] = Math.pow(5, degree)*4;
			degree += 1.0/size;
		}
		
		double[] bestValues = model.getParameters();
		double bestScore = 0;

		for(int v1 = 0; v1 < values.length; v1 ++) {
			model.setParameter(1, values[v1]);
			for(int v2 = 0; v2 < values.length; v2 ++) {
				model.setParameter(2, values[v2]);
//				for(int v3 = 0; v3 < values.length; v3 ++) {
//					model.setParameter(3, values[v3]);
//						for(int v4 = 0; v4 < values.length; v4 ++) {
//							model.setParameter(6, values[v4]);
				
							double newScore = computeAvgScore(solvers, model);
							if (newScore > bestScore) {
								bestScore = newScore;
								bestValues = model.getParameters();
							}
//							System.out.printf("%.1f(%.3f,%.3f,%.3f)\t", newScore, values[v1], values[v2], values[v3]);
//						}
//					}
//					System.out.printf("%.1f(%.3f,%.3f)\t", newScore, values[v1], values[v2]);
					System.out.printf("%.1f\t", newScore);
//					System.out.println();
			}
//			System.out.println();
			System.out.println();
		}
		
//		// one is fixed as 1.0
//		model.setParameter(0, 0.33);
//		int param = 1;
//		for (int v3 = 0; v3 < values.length; ++v3) {
//			model.setParameter(3, values[v3]);
//			for (int v2 = 0; v2 < values.length; ++v2) {
//				model.setParameter(2, values[v2]);
//				for (int v1 = 0; v1 < values.length; ++v1) {
//					model.setParameter(1, values[v1]);
//					double newScore = computeAvgScore(solvers, model);
//					if (newScore > bestScore) {
//						bestScore = newScore;
//						bestValues = model.getParameters();
//						System.out.println("Better values: "
//								+ Arrays.toString(bestValues) + " , score= "
//								+ bestScore);
//					}
//				}
//			}
//		}
		model.setParameters(bestValues);
		SimpleIO.out(model + "\n", "trainedModels.txt", true);
		return model;
	}

	private double computeAvgScore(Solver[] solvers, Model model) {
		double score = 0;
		int count = solvers.length;
		for (int i = 0; i < count; ++i) {
			score += solvers[i].solve(model, false);
		}
		return score / count;
	}

	private double computeMinScore(Solver[] solvers, Model model) {
		double min = Double.MAX_VALUE;
		// int worst = 0;
		int count = solvers.length;
		for (int i = 0; i < count; ++i) {
			double score = solvers[i].solve(model, false);
			if (score < min) {
				min = score;
				// worst = i;
			}
		}
		// System.out.println("worst index="+worst+", score = "+min);
		return min;
	}

}
