package agh.intob.jmetalintegration;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import agh.intob.jmetalintegration.utils.PumpLocation;
import agh.intob.jmetalintegration.utils.SolverRunner;
import agh.intob.jmetalintegration.utils.SuctionDetails;

public class Main {

	public static void main(String[] args){
		int nrOfIterations = 10;
		int nrOfPumps = 1;
		PumpLocation[] pumps = new PumpLocation[nrOfPumps];
		for(int i = 0 ; i < nrOfPumps ; i++){
			pumps[i] = new PumpLocation(0.5, 0.5, 0.5);
		}
		int nrOfSuctions = 1;
		SuctionDetails[] suctions = new SuctionDetails[nrOfSuctions];
		for(int i = 0 ; i < nrOfSuctions ; i++){
			suctions[i] = new SuctionDetails(0.1, 0.2, 0.3);
		}
		List<Double> energies = new LinkedList<>();
		double drain = 0;
		double pollution = 0;
		try {
			BufferedReader stdInput = SolverRunner.runSolver(nrOfIterations, pumps, suctions);
			String output = null;
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
		int i = 1;
		for(Double d : energies){
			System.out.println("Energy " + i + ": " + d);
			i++;
		}
		System.out.println("Drain: " + drain);
		System.out.println("Pollution: " + pollution);
	}

}
