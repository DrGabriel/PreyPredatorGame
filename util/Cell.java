package util;

import gameEngine.Agent;
import gameEngine.AgentType;

public class Cell {
	public boolean empty = true;
	private AgentType type;
	private int id = 0; 
	private double trail = 0.0;
	public boolean marked = false;
	
	
	public Cell() {
		// TODO Auto-generated constructor stub
	}
	public boolean isEmpty(){
		return this.empty;
	}
	public void populateCell(Agent agent){
		this.id = agent.id;
		this.empty = false;
		this.type = agent.getType();
		if(agent.getType() == AgentType.FISH) this.trail = 10.0;
	}
	public void clearCell(){
		this.id = 0;
		this.empty = true;
		this.type = null;
		
		
	}
	public AgentType getType(){
		return this.type;
	}
	public int getId(){
		return this.id;
	}
	

}
