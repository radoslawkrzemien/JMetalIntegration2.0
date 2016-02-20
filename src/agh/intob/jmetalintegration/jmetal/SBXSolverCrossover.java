package agh.intob.jmetalintegration.jmetal;

import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.ArrayRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.operators.crossover.SBXCrossover;
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
public class SBXSolverCrossover extends SBXCrossover {
    private static final double EPS = 1.0E-14D;
    private static final double ETA_C_DEFAULT_ = 20.0D;
    private Double crossoverProbability_ = Double.valueOf(0.9D);
    private double distributionIndex_ = 20.0D;
    private static final List VALID_TYPES = Arrays.asList(new Class[]{SolverSolutionType.class});

    public SBXSolverCrossover(HashMap<String, Object> parameters) {
        super(parameters);
        if(parameters.get("probability") != null) {
            this.crossoverProbability_ = (Double)parameters.get("probability");
        }

        if(parameters.get("distributionIndex") != null) {
            this.distributionIndex_ = ((Double)parameters.get("distributionIndex")).doubleValue();
        }
    }

    @Override
    public Object execute(Object object) throws JMException {
        Solution[] parents = (Solution[])((Solution[])object);
        Class offSpring;
        String name;
        if(parents.length != 2) {
            Configuration.logger_.severe("SBXSolverCrossover.execute: operator needs two parents");
            offSpring = String.class;
            name = offSpring.getName();
            throw new JMException("Exception in " + name + ".execute()");
        } else if(VALID_TYPES.contains(parents[0].getType().getClass()) && VALID_TYPES.contains(parents[1].getType().getClass())) {
            Solution[] offSpring1 = this.doCrossover(this.crossoverProbability_.doubleValue(), parents[0], parents[1]);
            return offSpring1;
        } else {
            Configuration.logger_.severe("SBXSolverCrossover.execute: the solutions type " + parents[0].getType() + " is not allowed with this operator");
            offSpring = String.class;
            name = offSpring.getName();
            throw new JMException("Exception in " + name + ".execute()");
        }
    }

    @Override
    public Solution[] doCrossover(double probability, Solution parent1, Solution parent2) throws JMException {
        Solution[] offSpring = new Solution[]{new Solution(parent1), new Solution(parent2)};
//        XReal x1 = new XReal(parent1);
//        XReal x2 = new XReal(parent2);
//        XReal offs1 = new XReal(offSpring[0]);
//        XReal offs2 = new XReal(offSpring[1]);
        Variable[] vars1 = parent1.getDecisionVariables();
        Variable[] vars2 = parent2.getDecisionVariables();
        Variable[] offVars1 = offSpring[0].getDecisionVariables();
        Variable[] offVars2 = offSpring[1].getDecisionVariables();
//        int numberOfVariables = x1.getNumberOfDecisionVariables();
        int numberOfVariables = vars1.length;
        if(PseudoRandom.randDouble() <= probability) {
            for(int i = 0; i < numberOfVariables; ++i) {
//                double valueX1 = x1.getValue(i);
//                double valueX2 = x2.getValue(i);
                double valueX1 = vars1[i].getValue();
                double valueX2 = vars2[i].getValue();
                if(PseudoRandom.randDouble() <= 0.5D) {
                    if(Math.abs(valueX1 - valueX2) > 1.0E-14D) {
                        double y1;
                        double y2;
                        if(valueX1 < valueX2) {
                            y1 = valueX1;
                            y2 = valueX2;
                        } else {
                            y1 = valueX2;
                            y2 = valueX1;
                        }
//
//                        double yL = x1.getLowerBound(i);
//                        double yu = x1.getUpperBound(i);
                        double yL = vars1[i].getLowerBound();
                        double yu = vars1[i].getUpperBound();
                        double rand = PseudoRandom.randDouble();
                        double beta = 1.0D + 2.0D * (y1 - yL) / (y2 - y1);
                        double alpha = 2.0D - Math.pow(beta, -(this.distributionIndex_ + 1.0D));
                        double betaq;
                        if(rand <= 1.0D / alpha) {
                            betaq = Math.pow(rand * alpha, 1.0D / (this.distributionIndex_ + 1.0D));
                        } else {
                            betaq = Math.pow(1.0D / (2.0D - rand * alpha), 1.0D / (this.distributionIndex_ + 1.0D));
                        }

                        double c1 = 0.5D * (y1 + y2 - betaq * (y2 - y1));
                        beta = 1.0D + 2.0D * (yu - y2) / (y2 - y1);
                        alpha = 2.0D - Math.pow(beta, -(this.distributionIndex_ + 1.0D));
                        if(rand <= 1.0D / alpha) {
                            betaq = Math.pow(rand * alpha, 1.0D / (this.distributionIndex_ + 1.0D));
                        } else {
                            betaq = Math.pow(1.0D / (2.0D - rand * alpha), 1.0D / (this.distributionIndex_ + 1.0D));
                        }

                        double c2 = 0.5D * (y1 + y2 + betaq * (y2 - y1));
                        if(c1 < yL) {
                            c1 = yL;
                        }

                        if(c2 < yL) {
                            c2 = yL;
                        }

                        if(c1 > yu) {
                            c1 = yu;
                        }

                        if(c2 > yu) {
                            c2 = yu;
                        }

                        if(PseudoRandom.randDouble() <= 0.5D) {
//                            offs1.setValue(i, c2);
//                            offs2.setValue(i, c1);
                            offVars1[i].setValue(c2);
                            offSpring[0].setDecisionVariables(offVars1);
                            offVars2[i].setValue(c1);
                            offSpring[1].setDecisionVariables(offVars2);
                        } else {
//                            offs1.setValue(i, c1);
//                            offs2.setValue(i, c2);
                            offVars1[i].setValue(c1);
                            offSpring[0].setDecisionVariables(offVars1);
                            offVars2[i].setValue(c2);
                            offSpring[1].setDecisionVariables(offVars2);
                        }
                    } else {
//                        offs1.setValue(i, valueX1);
//                        offs2.setValue(i, valueX2);
                        offVars1[i].setValue(valueX1);
                        offSpring[0].setDecisionVariables(offVars1);
                        offVars2[i].setValue(valueX2);
                        offSpring[1].setDecisionVariables(offVars2);
                    }
                } else {
//                    offs1.setValue(i, valueX2);
//                    offs2.setValue(i, valueX1);
                    offVars1[i].setValue(valueX2);
                    offSpring[0].setDecisionVariables(offVars1);
                    offVars2[i].setValue(valueX1);
                    offSpring[1].setDecisionVariables(offVars2);
                }
            }
        }

        return offSpring;
    }
}
