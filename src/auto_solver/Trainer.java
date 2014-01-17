package auto_solver;

import io.SimpleIO;

import java.util.Arrays;

import user_control.GameParameters;


public class Trainer {
	
	private final String trainingDirPath = "training/large/";
	
	public static void main(String[] args)
	{
		GameParameters params = GameParameters.large();
		
		Solver solver = new Solver(params);
//		solver.generateFigures(params, 1000);
//		solver.saveFigures("large_figures1000.fg");
		solver.loadFigures("large_figures1000.fg");
		
		
//		new Trainer().prepareTrainingSets(100, 500, params);
		
		Model model = Model.defaultModel();
//		Model trainedModel = new Trainer().trainByMutations(params, model);
//		Model trainedModel = new Trainer().trainParametersSequently(params, new Model());
//		Model trainedModel = new Trainer().trainGradientStep(params, model);
//		Model trainedModel = new Trainer().trainByMonteCarlo(params, model);
//		double score = solver.solve(trainedModel);
//		double score = solver.solve(new Model(1.0, 33.695360448986236, 12.98661681273085, 5.638791255657701, 1.578122552461168, 1.781601263373758, 0.5112894823771317));
		double score = solver.solve(new Model(1.0, 24.375249833402645, 15.638896104617158, 6.64950636292486, 1.2011615482452267, 1.967466752949057, 0.7));
		System.out.println("result: " + score);
		solver.visualize();
		
	
	}
	// TODO train on random semifilled fields
	
	
	/**
	 * Generates several training sets with the same size
	 * 
	 * @param count number of sets
	 * @param size size of each set
	 * @param params
	 */
	void prepareTrainingSets(int count, int size, GameParameters params) {
		Solver solver = new Solver(params);
		for (int i = 0; i < count; ++i) {
			solver.generateFigures(params, size);
			solver.saveFigures(trainingDirPath + "figures" + i + ".fg");
		}
	}
	
	private Model trainByMutations(GameParameters params, Model model)
	{
		int count = 20;
		Solver[] solvers = new Solver[count];
		for(int i=0; i<count; ++i)
		{
			solvers[i] = new Solver(params);
			solvers[i].loadFigures(trainingDirPath+"figures"+i+".fg");
		}
		
		double bestScore = computeAvgScore(solvers, model);
		for(int i=0; i<100; ++i)
		{
			Model mutatedModel = Model.mutate(model);
			double newScore = computeAvgScore(solvers, mutatedModel);
			if(newScore > bestScore)
			{
				bestScore = newScore;
				model = mutatedModel;
				System.out.println(i + ". Good mutation: " + model + ", score=" + bestScore);
			}
			else
			{
//				System.out.println("Bad mutation: " + mutatedModel + ", score=" + newScore);
			}
		}
		SimpleIO.out(model+"\n", "trainedModels.txt", true);
		return model;
	}

	
	private Model trainParametersSequently(GameParameters params, Model model) {
		
		double[] parameters = model.getParameters();
		int size = parameters.length;
		
		Model newModel;
		for(int i=2; i<size; ++i)
		{
			newModel = new Model(Arrays.copyOf(parameters, i));
			
			// TODO try to change only last parameter
			newModel = trainByMutations(params, newModel);
			System.arraycopy(newModel.getParameters(), 0, parameters, 0, i);
		}
		
		model.setParameters(parameters);
		SimpleIO.out(model+"\n", "trainedModels.txt", true);
		return model;
	}


	
//	private Model trainGradientStep(GameParameters simple, Model model) {
//		// TODO Auto-generated method stub
//		return null;
//	}


	private Model trainByMonteCarlo(GameParameters params, Model model) {
		int count = 10;
		Solver[] solvers = new Solver[count];
		for(int i=0; i<count; ++i)
		{
			solvers[i] = new Solver(params);
			solvers[i].loadFigures(trainingDirPath+"figures"+i+".fg");
		}
		
		double[] values = new double[] {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 
				0.05, 0.15, 0.25, 0.35, 0.45, 0.55, 0.65, 0.75, 0.85, 0.95 };
		double[] bestValues = model.getParameters();
		double bestScore = -Double.MAX_VALUE;
		
//		one is fixed as 1.0
		model.setParameter(0, 0.33);
		int param = 1;
		for(int v3=0; v3<values.length; ++v3)
		{
			model.setParameter(3, values[v3]);
			for(int v2=0; v2<values.length; ++v2)
			{
				model.setParameter(2, values[v2]);
				for(int v1=0; v1<values.length; ++v1)
				{
					model.setParameter(1, values[v1]);
					double newScore = computeAvgScore(solvers, model);
					if(newScore > bestScore)
					{
						bestScore = newScore;
						bestValues = model.getParameters();
						System.out.println("Better values: " + Arrays.toString(bestValues) + " , score= " + bestScore);
					}
				}
			}
		}
		model.setParameters(bestValues);
		SimpleIO.out(model+"\n", "trainedModels.txt", true);
		return model;
	}


	private double computeAvgScore(Solver[] solvers, Model model) {
		double score = 0;
		int count = solvers.length;
		for (int i = 0; i < count; ++i) {
			score += solvers[i].solve(model);
		}
		return score / count;
	}

	private double computeMinScore(Solver[] solvers, Model model) {
		double min = Double.MAX_VALUE;
//		int worst = 0;
		int count = solvers.length;
		for (int i = 0; i < count; ++i) {
			double score = solvers[i].solve(model);
			if (score < min) {
				min = score;
//				worst = i;
			}
		}
//		System.out.println("worst index="+worst+", score = "+min);
		return min;
	}
}
