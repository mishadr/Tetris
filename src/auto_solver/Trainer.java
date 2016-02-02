package auto_solver;

import game_engine.GameParameters;
import io.SimpleIO;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import auto_solver.genetic.GeneticEvolution;
import auto_solver.genetic.Organism;

/**
 * Trainer implements several methods of training a tetris playing {@link Model}
 * . It runs {@link Solver} which uses a model for many times and updates
 * model's parameters.
 * 
 * @author misha
 * 
 */
public class Trainer {

	private String figuresPath;
	private final int trainingSetSize;
	private final int numberOfSteps;
	private Solver[] solvers;

	public Trainer(String figuresDirPath, GameParameters params, int trainingSetSize, int stepCount, int startPaddingHeight, double paddingDensity) {
		this.trainingSetSize = trainingSetSize;
		this.numberOfSteps = stepCount;
		this.figuresPath = figuresDirPath;
		
		System.out.println("Training sets count = " + trainingSetSize);
		solvers = new Solver[trainingSetSize];
		for (int i = 0; i < trainingSetSize; ++i) {
			solvers[i] = new Solver(params);
			solvers[i].loadFigures(figuresPath + "figures" + (i) + ".fs");
			solvers[i].fillField(startPaddingHeight, paddingDensity);
		}
	}

	// TODO train on random semifilled fields

	/**
	 * Performs mutation (random change of model's parameters) at each step and
	 * updates model when higher score achieved.
	 * 
	 * @param params
	 * @param model
	 * @return
	 */
	Model trainByMutations(GameParameters params, Model model) {
		System.out.println("Training by mutations");

		double bestScore = computeMinScore(solvers, model);
		for (int i = 0; i < numberOfSteps; ++i) {
			Model mutatedModel = Model.mutate(model);
			double newScore = computeAvgScore(solvers, mutatedModel);
			if (newScore > bestScore) {
				bestScore = newScore;
				model = mutatedModel;
				report(i, bestScore, model);
			} else {
				// System.out.println("Bad mutation: " + mutatedModel +
				// ", score=" + newScore);
			}
		}
		SimpleIO.out(model + "\n", "trainedModels.txt", true);
		return model;
	}

	Model trainParametersSequently(GameParameters params, Model model) {
		// System.out.println("Training parameters sequently");
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

	class ArrayIterator {
		private int length;

		// sizes of subarrays
		private int[] sizes;

		// current indices
		private int[] index;

		private boolean loop;

		public ArrayIterator(int[] sizesArray) {
			sizes = sizesArray;
			length = sizes.length;
			index = new int[length];
			loop = false;
		}

		public boolean loop() {
			return loop;
		}

		public int[] next() {
			loop = false;
			int[] result = index.clone();
			int j = length - 1;
			for (; j >= 0; --j) {
				int i = index[j] + 1;
				if (i < sizes[j]) {
					index[j] = i;
					break;
				}
				index[j] = 0;
			}
			if (j < 0) {
				loop = true;
			}
			return result;
		}

		public void startOver() {
			for (int i = 0; i < length; ++i) {
				index[i] = 0;
			}
			loop = false;
		}
	}

	Model trainGenetic(GameParameters params, Model model) {
		System.out.print("Training by genetic algorithm. ");

		int stepsNumber = 20;
		Function<Organism, Double> evaluator = m -> computeAvgScore(solvers, (Model) m);

		GeneticEvolution gen = new GeneticEvolution();
		gen.setInitialSize(64);
		gen.setMaxPopulationSize(64);
		gen.setSelectionRate(0.15);
		gen.setMutationProbability(0.7);
		gen.initPopulation(() -> Model.randomModel(2));

		for (int i = 0; i < stepsNumber; ++i) {
			System.out.printf("Simulating step %d...\n", i);
			gen.simulateCycle(evaluator);
		}

		return (Model) gen.getBest(evaluator);
	}

	Model trainByGradientDescent(GameParameters params, Model model) {
		System.out.print("Training by gradient descent. ");

		double[] bestValues = model.getParameters();
		double bestScore = 0;

		// parameters count to vary
		int n = 3;
		// radius of locality
		int radius = 2;
		int size = 2 * radius + 1;
		// parameters values
		double[][] values = new double[n][size];

		int[] sizes = new int[n];
		Arrays.fill(sizes, size);
		ArrayIterator iterator = new ArrayIterator(sizes);

		double prevScore = 0;
		int stepsNumber = numberOfSteps / (n*size);
		while (stepsNumber-- > 0) {
			updateValues(bestValues, n, radius, values);
			System.out.printf("Testing locality of values: [");
			for (int p = 0; p < n; ++p) {
				System.out.printf("%.3f ", values[p][radius]);
			}
			System.out.println("]");

			iterator.startOver();
			int counter = 0;
			while (!iterator.loop()) {
				int[] indices = iterator.next();
				for (int i = 0; i < n; ++i) {
					model.setParameter(i + 1, values[i][indices[i]]);
				}
				double newScore = computeAvgScore(solvers, model);
				if (newScore > bestScore) {
					bestScore = newScore;
					bestValues = model.getParameters();
				}
				// System.out.printf("%.1f (%.3f, %.3f)\t", newScore,
				// model.getParameters()[1], model.getParameters()[2]);
				System.out.printf("%.1f \t", newScore);
				if (++counter == size) {
					System.out.println();
					counter = 0;
				}
			}
			// if we could increase score
			if (bestScore > prevScore) {
				System.out.printf("New values: %s\n", Arrays.toString(bestValues));
				System.out.printf("Advanced from %.2f to %.2f\n", prevScore, bestScore);
				prevScore = bestScore;
			} else {
				System.out.printf("Can't advance at one step anymore\n");
				break;
			}
			System.out.println();
		}

		model.setParameters(bestValues);
		SimpleIO.out(bestScore + ": " + model + "\n", "trainedModels.txt", true);
		return model;
	}

	private void updateValues(double[] bestValues, int n, int radius, double[][] values) {
		double degree;
		for (int i = 0; i < n; ++i) {
			degree = -1; // -1 .. 1
			for (int j = 0; j < 2 * radius + 1; ++j) {
				values[i][j] = Math.pow(1.5, degree) * bestValues[i + 1];
				degree += 1.0 / radius;
			}
		}
	}

	Model trainByMonteCarlo(GameParameters params, Model model) {
		System.out.print("Training by monte carlo. ");

		// logarithmically uniform values
		int size = 24;
		double[] values = new double[2 * size + 1];
		double degree = -1; // -1 .. 1
		for (int i = 0; i <= 2 * size; ++i) {
			values[i] = Math.pow(5, degree) * 4;
			degree += 1.0 / size;
		}

		double[] bestValues = model.getParameters();
		double bestScore = 0;

		for (int v1 = 0; v1 < values.length; v1++) {
			model.setParameter(1, values[v1]);
			for (int v2 = 0; v2 < values.length; v2++) {
				model.setParameter(2, values[v2]);
				for (int v3 = 0; v3 < values.length; v3++) {
					model.setParameter(3, values[v3]);
					// for(int v4 = 0; v4 < values.length; v4 ++) {
					// model.setParameter(6, values[v4]);

					double newScore = computeAvgScore(solvers, model);
					if (newScore > bestScore) {
						bestScore = newScore;
						bestValues = model.getParameters();
					}
					System.out.printf("%.1f(%.3f,%.3f,%.3f)\t", newScore, values[v1], values[v2], values[v3]);
					// }
				}
				// System.out.printf("%.1f(%.3f,%.3f)\t", newScore, values[v1],
				// values[v2]);
				// System.out.printf("%.1f\t", newScore);
				// System.out.println();
			}
			// System.out.println();
			System.out.println();
		}

		// // one is fixed as 1.0
		// model.setParameter(0, 0.33);
		// int param = 1;
		// for (int v3 = 0; v3 < values.length; ++v3) {
		// model.setParameter(3, values[v3]);
		// for (int v2 = 0; v2 < values.length; ++v2) {
		// model.setParameter(2, values[v2]);
		// for (int v1 = 0; v1 < values.length; ++v1) {
		// model.setParameter(1, values[v1]);
		// double newScore = computeAvgScore(solvers, model);
		// if (newScore > bestScore) {
		// bestScore = newScore;
		// bestValues = model.getParameters();
		// System.out.println("Better values: "
		// + Arrays.toString(bestValues) + " , score= "
		// + bestScore);
		// }
		// }
		// }
		// }
		model.setParameters(bestValues);
		SimpleIO.out(model + "\n", "trainedModels.txt", true);
		return model;
	}

	Model trainBySimulatedAnnealing(GameParameters params, Model model) {
		System.out.println("Training by simulated annealing.");

		Model bestModel = model;
		double bestScore = computeAvgScore(solvers, model);

		double score = 0;
		for(int step=0; step < numberOfSteps; ++step) {
			Model newModel = Model.mutate(model);
			double newScore = computeAvgScore(solvers, newModel);
			if(newScore >= score) {
				score = newScore;
				model = newModel;
				report(step, score, model);
				// I would like to track the best model too
				if(score > bestScore) {
					bestScore = score;
					bestModel = model;
				}
			}
			else {
				double prob = Math.exp(0.3*(newScore - score)/(1 - 1.*step/numberOfSteps));
//				double prob = Math.exp(0.1*(newScore - score)*step);
				if(Math.random() < prob) {
					score = newScore;
					model = newModel;
					report(step, score, model);
				}
			}
		}

		SimpleIO.out(bestScore + ": " + model + "\n", "trainedModels.txt", true);
		return bestModel;
	}

	private void report(int step, double score, Model model) {
		System.out.printf("%d. Score=%.3f, model:%s\n", step, score, model);
	}

	private double computeAvgScore(Solver[] solvers, Model model) {
		double score = 0;
		int count = solvers.length;
		for (int i = 0; i < count; ++i) {
			score += solvers[i].solve(model, true);
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

	private double[] computeScores(Solver[] solvers, Model model) {
		double min = Double.MAX_VALUE;
		double total = 0;
		double max = 0.0;

		int count = solvers.length;
		for (int i = 0; i < count; ++i) {
			double score = solvers[i].solve(model, false);
			if (score < min) {
				min = score;
			}
			if (score > max) {
				max = score;
			}
			total += score;
		}
		return new double[] { min, total / count, max };
	}

	public void testModel(String dirPath, Model model, GameParameters params) {
		int count = new File(dirPath).list().length;
		System.out.printf("Testing... ");
		Solver[] solvers = new Solver[count];
		for (int i = 0; i < count; ++i) {
			solvers[i] = new Solver(params);
			solvers[i].loadFigures(dirPath + "figures" + (i) + ".fs");
		}
		double[] mam = computeScores(solvers, model);
		System.out.printf("performed %d tests.\nAverage score = %.3f\nMinimum score = %.3f", count, mam[1], mam[0]);
	}

}
