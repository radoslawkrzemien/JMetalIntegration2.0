package agh.intob.jmetalintegration;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import agh.intob.jmetalintegration.jmetal.SBXSolverCrossover;
import agh.intob.jmetalintegration.jmetal.SolverPolynomialMutation;
import agh.intob.jmetalintegration.jmetal.SolverProblem;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.operators.crossover.Crossover;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.selection.Selection;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.Configuration;
import jmetal.util.JMException;

/**
 * Main class for program execution and algorithm initialization with given parameters
 */

public class Main {

	public static Logger logger_;
	public static FileHandler fileHandler_;

	/**
	 * Main method
	 * @param args - arguments for algorithm initialization [populationSize] [maxEvaluations]
	 * @throws JMException
	 * @throws SecurityException
	 * @throws IOException
	 * @throws ClassNotFoundException
     */
	public static void main(String[] args) throws JMException, SecurityException, IOException, ClassNotFoundException{
		// Logger initialization
		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("NSGAII_main.log");
		logger_.addHandler(fileHandler_);

		//Problem object
		Object problem;
		problem = new SolverProblem("Solver", 15);

		//Algorith initialization
		NSGAII algorithm = new NSGAII((Problem)problem);
		if(args.length == 0) {
			algorithm.setInputParameter("populationSize", Integer.valueOf(4));
			algorithm.setInputParameter("maxEvaluations", Integer.valueOf(10));
		} else if (args.length == 1){
			algorithm.setInputParameter("populationSize", Integer.valueOf(args[0]));
			algorithm.setInputParameter("maxEvaluations", Integer.valueOf(10));
		} else {
			algorithm.setInputParameter("populationSize", Integer.valueOf(args[0]));
			algorithm.setInputParameter("maxEvaluations", Integer.valueOf(args[1]));
		}

		//Operators initialization
		HashMap parameters = new HashMap();
		parameters.put("probability", Double.valueOf(0.9D));
		parameters.put("distributionIndex", Double.valueOf(20.0D));
		Crossover crossover = new SBXSolverCrossover(parameters);
		parameters = new HashMap();
		parameters.put("probability", Double.valueOf(1.0D / (double)((Problem)problem).getNumberOfVariables()));
		parameters.put("distributionIndex", Double.valueOf(20.0D));
		Mutation mutation = new SolverPolynomialMutation(parameters);
		parameters = null;
		Selection selection = SelectionFactory.getSelectionOperator("BinaryTournament2", parameters);

		//Adding operators to algorithm
		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.addOperator("selection", selection);

		//Algorithm execution
		long initTime1 = System.currentTimeMillis();
		SolutionSet population = algorithm.execute();
		long estimatedTime = System.currentTimeMillis() - initTime1;

		//Logging information about execution
		logger_.info("Total execution time: " + estimatedTime + "ms");
		logger_.info("Variables values have been writen to file VAR");
		population.printVariablesToFile("VAR");
		logger_.info("Objectives values have been writen to file FUN");
		population.printObjectivesToFile("FUN");
	}

}
