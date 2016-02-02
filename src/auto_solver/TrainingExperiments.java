package auto_solver;

import game_engine.GameParameters;
import game_engine.GameStyle;
import game_engine.Level;

import java.io.File;

/**
 * Class for performing experiments on different methods for model solving.
 * 
 * @author misha
 * 
 */
public class TrainingExperiments {

	public static final String trainingDirPath = "training sets/";
	public static final String testDirPath = "test sets/";

	/**
	 * Generates set of figure sequences of the same size, determined by the
	 * specified {@link GameParameters}.
	 * 
	 * @param setsCount
	 *            number of sets
	 * @param setSize
	 *            size of each set
	 * @param params
	 *            {@link GameParameters} for generating figures
	 * @param dirName
	 *            directory to save files with figures
	 */
	public static void generateFigureSequences(int setsCount, int setSize,
			GameParameters params, String dirName) {
		Solver solver = new Solver(params);
		for (int i = 0; i < setsCount; ++i) {
			solver.generateFiguresSequence(params, setSize);
			solver.saveFiguresSequence(dirName + "figures" + i + ".fs");
		}
		System.out.printf("Generated %d sets of %d size in %s\n", setsCount,
				setSize, dirName);
	}

	public static void main(String[] args) {

		Level level = Level.HARD_7;
		GameStyle style = GameStyle.CLASSIC;
		
		GameParameters params = new GameParameters(level, style);
		String paramsDirPath = level + ", " + style + "/";
		String trainingPath = trainingDirPath + paramsDirPath;
		if(!new File(trainingPath).exists()) {
			generateFigureSequences(100, 500, params, trainingPath);
		}
		String testPath = "test/" + paramsDirPath;
		if(!new File(testPath).exists()) {
			generateFigureSequences(1, 500, params, testPath);
		}

//		generateFigureSequences(100, 500, params, trainingDirPath + paramsDirPath);
//		generateFigureSequences(100, 1000, params, testDirPath + paramsDirPath);
//		solver.generateFigures(params, 10000);
//		solver.saveFiguresSequence(testFileName);

		// TODO leave only fields with min score as training sets
		Trainer trainer = new Trainer(trainingDirPath + paramsDirPath,
				params, 80, 100, 12, 0.85);

//		Model trainedModel = new Model(new double[] { 1.0, 350.4856111244215, 184.57811446588514,
//				151.5485635015622, 19.05786100373608, 95.30881354920116, 34.7184831714387 });

		long t = System.nanoTime();
		Model trainedModel = trainer.trainBySimulatedAnnealing(params, Model.defaultModel());
		System.out.printf("training time: %d mcs\n", (System.nanoTime() - t)/1000000);
		
		// Testing our trained model on the test set.
		Solver solver = new Solver(params);
		solver.loadFigures(testPath + "figures0.fs");
		solver.solve(trainedModel, true);

		/*
		 * simple 3.2315759232190984, 45.716604258145146, 33.89344504009362,
		 * 19.403121053696115, 2.0860880493301694, 2.986381664239646,
		 * 0.5043876990292371 medium 1.9823714705500828, 56.569967701815045,
		 * 19.14941993497812, 7.842867791241197, 0.8990134561109907,
		 * 0.6704896633485788, 1.871878312294333 large 1.5245045228283027,
		 * 2.6650851808486697, 11.220494209261634, 4.956567111990763,
		 * 0.3603691377321131, 0.9822241938175513, 1.0045757342949906 large with
		 * refl 2.885108217804605, 14.507160351360737, 37.19775248622448,
		 * 4.296797616085915, 1.6755190621652956, 1.9563546575155846,
		 * 0.48142405831659996
		 */
//		System.out.println("result: " + score);
		solver.visualize(100);

	}

}
