package agh.intob.jmetalintegration.jmetal;

import jmetal.core.Problem;
import jmetal.core.SolutionType;
import jmetal.core.Variable;
import jmetal.encodings.variable.Int;
import jmetal.encodings.variable.Real;

/**
 * Created by Doktor on 2016-02-20.
 */
public class SolverSolutionType extends SolutionType {

    public SolverSolutionType(Problem problem){
        super(problem);
    }

    @Override
    public Variable[] createVariables() throws ClassNotFoundException {
//        int nrOfVariables = this.problem_.getNumberOfVariables();
        int nrOfVariables = 15;
        Variable[] variables = new Variable[nrOfVariables];
        variables[0] = new Int(1,100);
        variables[1] = new Int(2,2,2);
        for(int i = 2 ; i < 8 ; i++){
            variables[i] = new Real(0.0,1.0);
        }
        variables[8] = new Int(2,2,2);
        for(int i = 9 ; i < 15 ; i++){
            variables[i] = new Real(0.0,1.0);
        }
        return variables;
    }
}
