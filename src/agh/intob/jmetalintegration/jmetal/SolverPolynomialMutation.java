package agh.intob.jmetalintegration.jmetal;

import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.operators.mutation.PolynomialMutation;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Extended standard JMetal PolynomialMutation class, especially for solver problem
 */
public class SolverPolynomialMutation extends PolynomialMutation {
    private Double mutationProbability_ = null;
    private Double distributionIndex_ = Double.valueOf(20.0D);
    private static final List VALID_TYPES = Arrays.asList(new Class[]{SolverSolutionType.class});

    /**
     * Constructor
     * @param parameters - operator parameters
     */
    public SolverPolynomialMutation(HashMap<String, Object> parameters) {
        super(parameters);
        if(parameters.get("probability") != null) {
            this.mutationProbability_ = (Double)parameters.get("probability");
        }

        if(parameters.get("distributionIndex") != null) {
            this.distributionIndex_ = (Double)parameters.get("distributionIndex");
        }
    }

    /**
     * Overriden method from mutation operator, handles SolverSolutionType
     * @param object - solution to mutate
     * @return mutated solution
     * @throws JMException
     */
    @Override
    public Object execute(Object object) throws JMException {
        Solution solution = (Solution)object;
        if(!VALID_TYPES.contains(solution.getType().getClass())) {
            Configuration.logger_.severe("SolverPolynomialMutation.execute: the solution type " + solution.getType() + " is not allowed with this operator");
            Class cls = String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        } else {
            this.doMutation(this.mutationProbability_.doubleValue(), solution);
            return solution;
        }
    }

    /**
     * Overriden method from mutation operator, handles variablees from SolverSolutionType
     * @param probability - probability of mutation
     * @param solution - solution to be mutated
     * @throws JMException
     */
    @Override
    public void doMutation(double probability, Solution solution) throws JMException {
        Variable[] variables = solution.getDecisionVariables();
        for(int var = 0; var < solution.numberOfVariables(); ++var) {
            if(PseudoRandom.randDouble() <= probability) {
                double y = variables[var].getValue();
                double yl = variables[var].getLowerBound();
                double yu = variables[var].getUpperBound();
                double delta1 = (y - yl) / (yu - yl);
                double delta2 = (yu - y) / (yu - yl);
                double rnd = PseudoRandom.randDouble();
                double mut_pow = 0.047619047619047616D;
                double deltaq;
                double val;
                double xy;
                if(rnd <= 0.5D) {
                    xy = 1.0D - delta1;
                    val = 2.0D * rnd + (1.0D - 2.0D * rnd) * Math.pow(xy, this.distributionIndex_.doubleValue() + 1.0D);
                    deltaq = Math.pow(val, mut_pow) - 1.0D;
                } else {
                    xy = 1.0D - delta2;
                    val = 2.0D * (1.0D - rnd) + 2.0D * (rnd - 0.5D) * Math.pow(xy, this.distributionIndex_.doubleValue() + 1.0D);
                    deltaq = 1.0D - Math.pow(val, mut_pow);
                }

                y += deltaq * (yu - yl);
                if(y < yl) {
                    y = yl;
                }

                if(y > yu) {
                    y = yu;
                }

                variables[var].setValue(y);
                solution.setDecisionVariables(variables);
            }
        }

    }
}
