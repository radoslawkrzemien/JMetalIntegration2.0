package agh.intob.jmetalintegration.jmetal;

import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.ArrayRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.operators.mutation.PolynomialMutation;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.wrapper.XReal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Doktor on 2016-02-20.
 */
public class SolverPolynomialMutation extends PolynomialMutation {
    private static final double ETA_M_DEFAULT_ = 20.0D;
    private final double eta_m_ = 20.0D;
    private Double mutationProbability_ = null;
    private Double distributionIndex_ = Double.valueOf(20.0D);
    private static final List VALID_TYPES = Arrays.asList(new Class[]{SolverSolutionType.class});

    public SolverPolynomialMutation(HashMap<String, Object> parameters) {
        super(parameters);
        if(parameters.get("probability") != null) {
            this.mutationProbability_ = (Double)parameters.get("probability");
        }

        if(parameters.get("distributionIndex") != null) {
            this.distributionIndex_ = (Double)parameters.get("distributionIndex");
        }
    }

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

    @Override
    public void doMutation(double probability, Solution solution) throws JMException {
//        XReal x = new XReal(solution);
        Variable[] variables = solution.getDecisionVariables();
        for(int var = 0; var < solution.numberOfVariables(); ++var) {
            if(PseudoRandom.randDouble() <= probability) {
//                double y = x.getValue(var);
                double y = variables[var].getValue();
//                double yl = x.getLowerBound(var);
                double yl = variables[var].getLowerBound();
//                double yu = x.getUpperBound(var);
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

//                x.setValue(var, y);
                variables[var].setValue(y);
                solution.setDecisionVariables(variables);
            }
        }

    }
}
