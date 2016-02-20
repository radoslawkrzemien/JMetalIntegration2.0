package agh.intob.jmetalintegration;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import agh.intob.jmetalintegration.jmetal.SBXSolverCrossover;
import agh.intob.jmetalintegration.jmetal.SolverPolynomialMutation;
import agh.intob.jmetalintegration.jmetal.SolverProblem;
import agh.intob.jmetalintegration.utils.PumpLocation;
import agh.intob.jmetalintegration.utils.SolverRunner;
import agh.intob.jmetalintegration.utils.SuctionDetails;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.operators.crossover.Crossover;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.Selection;
import jmetal.operators.selection.SelectionFactory;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;

public class Main {
	public static Logger logger_;
	public static FileHandler fileHandler_;

	public static void main(String[] args) throws JMException, SecurityException, IOException, ClassNotFoundException{
		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("NSGAII_main.log");
		logger_.addHandler(fileHandler_);
		QualityIndicator indicators = null;
		Object problem;
		Object[] initTime;
		problem = new SolverProblem("Solver", 15);

		NSGAII algorithm = new NSGAII((Problem)problem);
		algorithm.setInputParameter("populationSize", Integer.valueOf(4));
		algorithm.setInputParameter("maxEvaluations", Integer.valueOf(10));
		HashMap parameters = new HashMap();
		parameters.put("probability", Double.valueOf(0.9D));
		parameters.put("distributionIndex", Double.valueOf(20.0D));
//		Crossover crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover", parameters);
		Crossover crossover = new SBXSolverCrossover(parameters);
		parameters = new HashMap();
		parameters.put("probability", Double.valueOf(1.0D / (double)((Problem)problem).getNumberOfVariables()));
		parameters.put("distributionIndex", Double.valueOf(20.0D));
//		Mutation mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters);
		Mutation mutation = new SolverPolynomialMutation(parameters);
		parameters = null;
		Selection selection = SelectionFactory.getSelectionOperator("BinaryTournament2", parameters);
		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.addOperator("selection", selection);
		algorithm.setInputParameter("indicators", indicators);
		long initTime1 = System.currentTimeMillis();
		SolutionSet population = algorithm.execute();
		long estimatedTime = System.currentTimeMillis() - initTime1;
		logger_.info("Total execution time: " + estimatedTime + "ms");
		logger_.info("Variables values have been writen to file VAR");
		population.printVariablesToFile("VAR");
		logger_.info("Objectives values have been writen to file FUN");
		population.printObjectivesToFile("FUN");
		if(indicators != null) {
			logger_.info("Quality indicators");
			logger_.info("Hypervolume: " + indicators.getHypervolume(population));
			logger_.info("GD         : " + indicators.getGD(population));
			logger_.info("IGD        : " + indicators.getIGD(population));
			logger_.info("Spread     : " + indicators.getSpread(population));
			logger_.info("Epsilon    : " + indicators.getEpsilon(population));
			int evaluations = ((Integer)algorithm.getOutputParameter("evaluations")).intValue();
			logger_.info("Speed      : " + evaluations + " evaluations");
		}
	}

}
