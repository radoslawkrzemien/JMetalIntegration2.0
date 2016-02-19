package agh.intob.jmetalintegration.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SolverRunner {
	
	public static BufferedReader runSolver(int nrOfIterations, PumpLocation[] pumps, SuctionDetails[] suctions){
		int nrOfPumps = pumps.length;
		int nrOfSuctions = suctions.length;
		String[] cmd = new String[4 + 3*nrOfPumps  + 3*nrOfSuctions ];
		cmd[0] = ".\\ads";
		cmd[1] = String.valueOf(nrOfIterations);
		cmd[2] = String.valueOf(nrOfPumps);
		int i = 3;
		int j = 0;
		for(; i < 3 + 3*nrOfPumps ; i += 3){
			cmd[i] = String.valueOf(pumps[j].getX());
			cmd[i+1] = String.valueOf(pumps[j].getY());
			cmd[i+2] = String.valueOf(pumps[j].getZ());
			j++;
		}
		cmd[i] = String.valueOf(nrOfSuctions);
		i++;
		int level = i;
		j = 0;
		for(; i < level + 3*nrOfSuctions ; i +=3){
			cmd[i] = String.valueOf(suctions[j].getX());
			cmd[i+1] = String.valueOf(suctions[j].getY());
			cmd[i+2] = String.valueOf(suctions[j].getZ());
		}
		Process process;
		BufferedReader stdInput = null;
		try {
			process = Runtime.getRuntime().exec(cmd);
			stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return stdInput;
	}

}
