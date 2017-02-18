package org.usfirst.frc.team25.scouting.client.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.usfirst.frc.team25.scouting.client.models.ScoutEntry;

/** Object model containing individual reports of teams in events and methods to process data
 * 
 * @author sng
 *
 */
public class TeamReport {
	
	transient private ArrayList<ScoutEntry> entries;
	
	int teamNum; //transient because it's the key of the HashMap in EventReport
	String teamName;

	double avgPointsPerCycle, avgCycles, sdCycles , reachBaselinePercentage, 
		avgHighGoals, avgLowGoals,sdHighGoals, sdLowGoals, sdPointsPerCycle;
	boolean autoShootsKey;
	double avgAutoScore, avgTeleOpScore, avgMatchScore, avgAutoKpa, avgTeleOpKpa, avgAutoGears, avgTeleOpGears, avgTotalFuel, avgHoppers;
	
	
	double sdAutoScore, sdTeleOpScore, sdMatchScore, sdAutoKpa, sdTeleOpKpa, sdAutoGears, sdTeleOpGears, sdTotalFuel;
	
	
	double takeoffAttemptPercentage, takeoffAttemptSuccessPercentage, takeoffPercentage;// attempt is out of all matches; success is for each attempt
	double pilotPlayPercentage;
	
	ArrayList<String> frequentRobotComment, frequentPilotComment;

	//TODO calculate these values and update EventReport
	double autoAbility, teleOpAbility, driveTeamAbility, robotQualities;
	double firstPickAbility, secondPickAbility;

	//Instance variables below should not be serialized but may be accessed by EventReports for analysis
	
	transient String frequentRobotCommentStr = "", frequentPilotCommentStr = "";
	int totalTakeoffAttempts, totalTakeoffSuccesses, totalPilotPlaying, 
		totalReachBaseline, totalAutoShootsKey;
	int[] totalHoppers, totalFuel, teleOpGears, autoKpas, autoScores, teleOpScores, matchScores,
		totalCycles, totalHighGoals, totalLowGoals, autoGears, teleOpKpa;
	double[] totalPointsPerCycle;
	
	public TeamReport(int teamNum){
		this.teamNum = teamNum;
		entries = new ArrayList<ScoutEntry>(); 
	}
	
	/** Method to fetch the nickname of a team from a file 
	 * 
	 * @param dataLocation location of the TeamNameList file generated by <code>exportTeamList</code>
	 */
	public void autoGetTeamName(File dataLocation){
		String data = FileManager.getFileString(dataLocation);
		String[] values = data.split(",\n");
		
		for(int i = 0; i < values.length; i++){	
			if(values[i].split(",")[0].equals(Integer.toString(teamNum))){
				
				teamName = values[i].split(",")[1];
				return; //Terminates the method
			}	
		}
	}
	
	void calculateTotals(){
		
		totalTakeoffAttempts = totalTakeoffSuccesses = 0;
		totalPilotPlaying = totalReachBaseline = totalAutoShootsKey = 0;
		totalHoppers = totalFuel = teleOpGears = autoKpas =  new int[entries.size()];
		matchScores = autoScores = teleOpScores = new int[entries.size()];
		totalCycles = totalHighGoals = totalLowGoals = new int[entries.size()];
		autoGears = teleOpKpa = new int[entries.size()];
		totalPointsPerCycle  = new double[entries.size()];
		
		
		for(int i = 0; i < entries.size(); i++){
			if(entries.get(i).getTeleOp().isAttemptTakeoff())
				totalTakeoffAttempts++;
			if(entries.get(i).getTeleOp().isReadyTakeoff())
				totalTakeoffSuccesses++;
			
			if(entries.get(i).getPreMatch().isPilotPlaying())
				totalPilotPlaying++;
			if(entries.get(i).getAuto().isBaselineCrossed())
				totalReachBaseline++;
			if(entries.get(i).getAuto().isShootsFromKey())
				totalAutoShootsKey++;
			
			totalHoppers[i] = (entries.get(i).getAuto().isUseHoppers() ? 1 : 0) 
					+ entries.get(i).getTeleOp().getHopppersUsed();
			totalFuel[i] = entries.get(i).getAuto().getHighGoals()+entries.get(i).getAuto().getLowGoals()
					+entries.get(i).getTeleOp().getHighGoals()+entries.get(i).getTeleOp().getLowGoals();
			autoGears[i] = entries.get(i).getAuto().getGearsDelivered();
			teleOpGears[i] = entries.get(i).getTeleOp().getGearsDelivered();
			teleOpKpa[i] = entries.get(i).teleOpKpa;
			autoKpas[i] = entries.get(i).autoKpa;
			autoScores[i]=entries.get(i).autoScore;
			teleOpScores[i] = entries.get(i).teleScore;
			matchScores[i] = entries.get(i).totalScore;
			totalPointsPerCycle[i] = entries.get(i).pointsPerCycle;
			totalCycles[i] = entries.get(i).getTeleOp().getNumCycles();
			totalHighGoals[i] = entries.get(i).getAuto().getHighGoals()+entries.get(i).getTeleOp().getHighGoals();
			totalLowGoals[i] = entries.get(i).getAuto().getLowGoals()+entries.get(i).getTeleOp().getLowGoals();
		}
			
		
	}
	
	public void calculateStats(){
		
		calculateTotals();
		
		takeoffAttemptPercentage = ((double) totalTakeoffAttempts)/entries.size(); //how often they attempt
		takeoffAttemptSuccessPercentage = ((double) totalTakeoffSuccesses)/totalTakeoffAttempts; //percentage for all attempts, "consistency"
		
		if(totalTakeoffAttempts ==0)
			takeoffAttemptSuccessPercentage = 0;
		
		takeoffPercentage = ((double) totalTakeoffSuccesses)/entries.size(); //percentage for all matches
				
		
		avgHoppers = Statistics.average(totalHoppers);
		
		
		avgTotalFuel = Statistics.average(totalFuel);
		sdTotalFuel = Statistics.popStandardDeviation(totalFuel);
		
		
		avgTeleOpGears = Statistics.average(teleOpGears);
		sdTeleOpGears = Statistics.popStandardDeviation(teleOpGears);
		
		
		avgAutoKpa = Statistics.average(autoKpas);
		sdAutoKpa = Statistics.popStandardDeviation(autoKpas);
		
		avgTeleOpKpa = Statistics.average(teleOpKpa);
		sdTeleOpKpa = Statistics.popStandardDeviation(teleOpKpa);
		
		avgAutoGears = Statistics.average(autoGears);
		sdAutoGears = Statistics.popStandardDeviation(autoGears);
		
		
		avgAutoScore = Statistics.average(autoScores);
		sdAutoScore = Statistics.popStandardDeviation(autoScores);
		
		
		avgTeleOpScore = Statistics.average(teleOpScores);
		sdTeleOpScore = Statistics.popStandardDeviation(teleOpScores);
		
		
		avgMatchScore = Statistics.average(matchScores);
		sdTeleOpScore = Statistics.popStandardDeviation(matchScores);
		
		pilotPlayPercentage = ((double) totalPilotPlaying)/entries.size();
		
		avgPointsPerCycle = Statistics.average(totalPointsPerCycle);
		sdPointsPerCycle = Statistics.popStandardDeviation(totalPointsPerCycle);
		
		avgCycles = Statistics.average(totalPointsPerCycle);
		sdCycles = Statistics.popStandardDeviation(totalPointsPerCycle);
				
		reachBaselinePercentage = totalReachBaseline/((double) entries.size());
		
		avgHighGoals = Statistics.average(totalHighGoals);
		sdHighGoals = Statistics.popStandardDeviation(totalHighGoals);
		
		avgLowGoals = Statistics.average(totalLowGoals);
		sdLowGoals = Statistics.popStandardDeviation(totalLowGoals);
		
		if(totalAutoShootsKey/((double)entries.size())>=0.50)
			autoShootsKey = true;
		else autoShootsKey = false;
		
		HashMap<String, Integer> commentFrequencies = new HashMap<>();
		
		
		for(String key : entries.get(0).getPostMatch().getRobotQuickCommentSelections().keySet()){
			commentFrequencies.put(key, 0);
			for(ScoutEntry entry : entries)
				if(entry.getPostMatch().getRobotQuickCommentSelections().get(key))
					commentFrequencies.put(key, 1+commentFrequencies.get(key));
		}
		
		frequentRobotComment = new ArrayList<>();
		
		for(String key : commentFrequencies.keySet())
			if(commentFrequencies.get(key)>=entries.size()/4.0)
				frequentRobotComment.add(key);
		
		commentFrequencies = new HashMap<>();
		
		for(String key : entries.get(0).getPostMatch().getPilotQuickCommentSelections().keySet()){
			commentFrequencies.put(key, 0);
			for(ScoutEntry entry : entries)
				if(entry.getPreMatch().isPilotPlaying())
					if(entry.getPostMatch().getPilotQuickCommentSelections().get(key))
						commentFrequencies.put(key, 1+commentFrequencies.get(key));
		}
		
		frequentPilotComment = new ArrayList<>();
		
		for(String key : commentFrequencies.keySet())
			if(commentFrequencies.get(key)>=totalPilotPlaying/4.0)
				frequentPilotComment.add(key);
		
		for(String comment : frequentRobotComment)
			frequentRobotCommentStr+=comment+';';
		for(String comment : frequentPilotComment)
			frequentPilotCommentStr+=comment+"; ";
		
		computeRankingMetrics();
				
	}	
	
	void computeRankingMetrics(){
		autoAbility = 0;
		teleOpAbility = 0;
		driveTeamAbility = 0;
		robotQualities = 0;
		firstPickAbility = 0;
		secondPickAbility = 0;
	}

	public int getTeamNum(){
		return teamNum;
	}
	

	public void addEntry(ScoutEntry entry){
		entries.add(entry);
	}
	

}
