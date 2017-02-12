package org.usfirst.frc.team25.scouting.client.data;

import java.io.File;
import java.util.ArrayList;

import org.usfirst.frc.team25.scouting.client.models.ScoutEntry;

/** Object model containing individual reports of teams in events and methods to process data
 * 
 * @author sng
 *
 */
public class TeamReport {
	
	private transient int teamNum;
	private ArrayList<ScoutEntry> entries;
	String teamName;
	
	public TeamReport(int teamNum){
		this.teamNum = teamNum;
		entries = new ArrayList<ScoutEntry>(); //initialize ArrayList
	}
	
	/** Method to fetch the nickname of a team from a file 
	 * 
	 * @param dataLocation location of the TeamNameList file generated by <code>exportTeamList</code>
	 */
	public void autoGetTeamName(File dataLocation){
		String data = FileManager.getFileString(dataLocation);
		String[] values = data.split(",\n");
		for(int i = 0; i < values.length; i++)	
			if(values[i].split(",")[0].equals(Integer.toString(teamNum))){
				teamName = values[i].split(",")[1];
				return; //Terminates the method
			}	
	}
	

	public int getTeamNum(){
		return teamNum;
	}
	

	public void addEntry(ScoutEntry entry){
		entries.add(entry);
	}
	
	public void firstPickAbility(){
		//Calculate ability as alliance parter - change this every season
	}
	
	public void secondPickAbility(){
		//Calculate ability as alliance parter - change this every season
	}
	
	public void autoAbility(){
		
	}
	
	public void teleAbility(){
		
	}
	

}
