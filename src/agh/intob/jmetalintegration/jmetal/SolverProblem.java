package agh.intob.jmetalintegration.jmetal;

import agh.intob.jmetalintegration.utils.PumpLocation;
import agh.intob.jmetalintegration.utils.SolverRunner;
import agh.intob.jmetalintegration.utils.SuctionDetails;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.util.JMException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Extended standard JMetal Problem class, especially for solver problem
 */
public class SolverProblem extends Problem {

    /**
     * Constructor
     * @param solutionType - solver solution type
     * @param numberOfVariables - number of variables constructing solver solution
     */
    public SolverProblem(String solutionType, Integer numberOfVariables) {
        this.numberOfVariables_ = numberOfVariables.intValue();
        this.numberOfObjectives_ = 2;
        this.numberOfConstraints_ = 0;
        this.problemName_ = "Solverproblem";

        if(solutionType.compareTo("Solver") == 0){
            this.solutionType_ = new SolverSolutionType(this);
        }
        else {
            System.out.println("Error: solution type " + solutionType + " invalid");
            System.exit(-1);
        }
    }

    /**
     * Overriden method evaluating given solution, evaluation is better if drain is bigger and pollution is smaller
     * @param solution - solution to be evaluated
     * @throws JMException
     */
    @Override
    public void evaluate(Solution solution) throws JMException {
        //variables preparation
        Variable[] variables = solution.getDecisionVariables();
        double[] fx = new double[2];
        int nrOfIterations = (int)variables[0].getValue();
        int nrOfPumps = (int)variables[1].getValue();
        PumpLocation[] pumps = new PumpLocation[nrOfPumps];
        for(int i = 0 ; i < nrOfPumps ; i++){
            int indX = 2 + i*3;
            int indY = 3 + i*3;
            int indZ = 4 + i*3;
            pumps[i] = new PumpLocation(variables[indX].getValue(), variables[indY].getValue(), variables[indZ].getValue());
        }
        int indSuc = 2 + 3*nrOfPumps;
        int nrOfSuctions = (int) variables[indSuc].getValue();
        SuctionDetails[] suctions = new SuctionDetails[nrOfSuctions];
        int indSucDet = indSuc + 1;
        for(int i = 0 ; i < nrOfSuctions ; i++){
            int indX = indSucDet + i*3;
            int indY = indSucDet + i*3;
            int indZ = indSucDet + i*3;
            suctions[i] = new SuctionDetails(variables[indX].getValue(), variables[indY].getValue(), variables[indZ].getValue());
        }

        //solver execution and output reading
        List<Double> energies = new LinkedList<>();
        double drain = 0;
        double pollution = 0;
        try {
            BufferedReader stdInput = SolverRunner.runSolver(nrOfIterations, pumps, suctions);
            String output;
            while((output = stdInput.readLine()) != null) {
                if(output.startsWith("Iter")){
                    output = stdInput.readLine();
                    output = output.replaceFirst("Energy:[ ]*", "");
                    energies.add(Double.parseDouble(output));
                }
                else if (output.startsWith("Drained")){
                    output = output.replaceFirst("Drained:[ ]*", "");
                    drain = Double.parseDouble(output);
                }
                else if(output.startsWith("Pollution")){
                    output = output.replaceFirst("Pollution:[ ]*", "");
                    pollution = Double.parseDouble(output);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //objective preparation
        fx[0] = drain;
        fx[1] = 1.0 - pollution;

        //objective setting
        solution.setObjective(0, fx[0]);
        solution.setObjective(1, fx[1]);
    }
}
