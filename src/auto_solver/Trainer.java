package auto_solver;

import io.SimpleIO;

import java.util.Arrays;

import user_control.GameMode;


public class Trainer {
	
	private final String trainingDirPath = "training/medium/";// TODO parameterize params!!!
//	private final String testDirPath = "test/large/";
	
	public static void main(String[] args)
	{
		GameMode params = GameMode.medium();
		
		Solver solver = new Solver(params);
//		solver.generateFigures(params, 1000);
//		solver.saveFigures("default_figures1000.fg");
		solver.loadFigures("medium_figures1000.fg");
		
		// TODO leave only fieldsd with min score as training sets
//		new Trainer().prepareTrainingSets(100, 500, params);
		
		Model model = Model.defaultModel();
		Model trainedModel = new Trainer().trainByMutations(params, model);
//		Model trainedModel = new Trainer().trainParametersSequently(params, new Model());
//		Model trainedModel = new Trainer().trainGradientStep(params, model);
//		Model trainedModel = new Trainer().trainByMonteCarlo(params, model);
		double score = solver.solve(trainedModel);
//		double score = solver.solve(new Model(1.9823714705500828, 56.569967701815045, 19.14941993497812, 7.842867791241197, 0.8990134561109907, 0.6704896633485788, 1.871878312294333));
//		double score = solver.solve(new Model(1.0, 56.02348217361618, 40.343159928416924, 15.87594984885185, 0.2794635131700676, 1.535287255698715, 1.793529194516746));
//		double score = solver.solve(new Model(2.885108217804605, 14.507160351360737, 37.19775248622448, 4.296797616085915, 1.6755190621652956, 1.9563546575155846, 0.48142405831659996));
		/*
		 * simple
		 * 3.2315759232190984, 45.716604258145146, 33.89344504009362, 19.403121053696115, 2.0860880493301694, 2.986381664239646, 0.5043876990292371
		 * medium
		 * 1.9823714705500828, 56.569967701815045, 19.14941993497812, 7.842867791241197, 0.8990134561109907, 0.6704896633485788, 1.871878312294333
		 * large
		 * 1.5245045228283027, 2.6650851808486697, 11.220494209261634, 4.956567111990763, 0.3603691377321131, 0.9822241938175513, 1.0045757342949906
		 * large with refl
		 * 2.885108217804605, 14.507160351360737, 37.19775248622448, 4.296797616085915, 1.6755190621652956, 1.9563546575155846, 0.48142405831659996
		 */
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
	void prepareTrainingSets(int count, int size, GameMode params) {
		Solver solver = new Solver(params);
		for (int i = 0; i < count; ++i) {
			solver.generateFigures(params, size);
			solver.saveFigures(trainingDirPath + "figures" + i + ".fg");
		}
	}
	
	// TODO search best on 1 step radius sphere
	
	private Model trainByMutations(GameMode params, Model model)
	{
		int count = 100;
		Solver[] solvers = new Solver[count];
		for(int i=0; i<count; ++i)
		{
			solvers[i] = new Solver(params);
			solvers[i].loadFigures(trainingDirPath+"figures"+i+".fg");
		}
		
		double bestScore = computeMinScore(solvers, model);
		for(int i=0; i<400; ++i)
		{
			Model mutatedModel = Model.mutate(model);
			double newScore = computeMinScore(solvers, mutatedModel);
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

	
	private Model trainParametersSequently(GameMode params, Model model) {
		
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


	private Model trainByMonteCarlo(GameMode params, Model model) {
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
