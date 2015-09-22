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
			solver.generateFigures(params, setSize);
			solver.saveFiguresSequence(dirName + "figures" + i + ".fs");
		}
		System.out.printf("Generated %d sets of %d size in %s\n", setsCount,
				setSize, dirName);
	}

	public static void main(String[] args) {

		Level level = Level.MEDIUM;
		GameStyle style = GameStyle.CLASSIC;
		GameParameters params = new GameParameters(level, style);
		String paramsDirPath = level + ", " + style + "/";
		String testFileName = "test/" + paramsDirPath + "figures0.fs";

		Solver solver = new Solver(params);
		
//		generateFigureSequences(100, 500, params, trainingDirPath + paramsDirPath);
//		generateFigureSequences(100, 1000, params, testDirPath + paramsDirPath);
//		solver.generateFigures(params, 10000);
//		solver.saveFiguresSequence(testFileName);
		
		solver.loadFigures(testFileName);

		// TODO leave only fields with min score as training sets
		Trainer trainer = new Trainer();
		trainer.setFiguresDirPath(trainingDirPath + paramsDirPath);

		Model model = Model.defaultModel();
		
		double score;
//		score = solver.solve(model, true);
//		System.out.println("result without training: " + score);
//		solver.visualize(300);
		
		long t = System.nanoTime();
//		Model trainedModel = trainer.trainByMonteCarlo(params, model);
//		Model trainedModel = trainer.trainByGradientShift(params, model);
//		Model trainedModel = trainer.trainGenetic(params, model);
		System.out.printf("training time: %d mcs\n", (System.nanoTime() - t)/1000000);
//		System.out.printf("Trained model: %s\n", trainedModel);
		
//		Model lastModel = new Model(1.0, 22.360679774997898, 11.390625, 5.0625, 0.2962962962962963, 1.0, 1.0);
		// big test
//		trainer.testModel(testDirPath + paramsDirPath, trainedModel, params);
		
		solver.solve(new Model(1.0, 24.306758188201325, 25.15395047159094, 10.589260779166208, 0.575209673709097, 2.2109031792267406, 0.35060015333276384), true);

		// Model trainedModel = new Trainer().trainParametersSequently(params,
		// new Model());
		// Model trainedModel = new Trainer().trainGradientStep(params, model);
		// Model trainedModel = new Trainer().trainByMonteCarlo(params, model);
//		double score = solver.solve(trainedModel, false);
		// double score = solver.solve(new Model(1.9823714705500828,
		// 56.569967701815045, 19.14941993497812, 7.842867791241197,
		// 0.8990134561109907, 0.6704896633485788, 1.871878312294333));
		// double score = solver.solve(new Model(1.0, 56.02348217361618,
		// 40.343159928416924, 15.87594984885185, 0.2794635131700676,
		// 1.535287255698715, 1.793529194516746));
		// double score = solver.solve(new Model(2.885108217804605,
		// 14.507160351360737, 37.19775248622448, 4.296797616085915,
		// 1.6755190621652956, 1.9563546575155846, 0.48142405831659996));
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
		solver.visualize(900);

	}

}
