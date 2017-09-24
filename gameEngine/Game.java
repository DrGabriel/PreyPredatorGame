package gameEngine;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;

import gui.RectDraw;
import util.Cell;

public class Game {
	public Cell [][]map;
	public boolean playing = false;
	public int fishesCounter = 0;
	public int sharksCounter = 0;
	public ConcurrentHashMap<Integer,Shark> sharks;
	public ConcurrentHashMap<Integer,Fish> fishes;
	public Game(int mapSize,int totalAgents){
		sharks = new ConcurrentHashMap <Integer, Shark>();
		fishes = new ConcurrentHashMap <Integer, Fish>();
		map = new Cell[mapSize][mapSize];
		for(int i =0; i< map.length; i++){
			for(int j = 0; j< map.length; j++)
				map[i][j] = new Cell();
		}
		this.createAgents(totalAgents);
		this.setLeader(AgentType.SHARK);
		this.setLeader(AgentType.FISH);
	}
	
	public int totalAgents(){
		return this.fishesCounter + this.sharksCounter;
	}
	public boolean mapIsFull(){
		return (this.totalAgents() == this.map.length * this.map.length);
	}
	
	protected  List<int[]> validMoves(Agent agent){
		List<int[]> finalMoves = new ArrayList<int[]>();
		int [][] moves = new int[4][2];
		moves[0] = new int[]{0, 1};
		moves[1] = new int[]{1, 0};
		moves[2] = new int[]{0, -1};
		moves[3] = new int[]{-1, 0};
		for (int[] movement : moves){
			checkMovement(agent,movement);
			finalMoves.add(movement);	
	
		}
		return finalMoves;
		
	}
	
	protected void checkMovement(Agent agent,int[] movement){
		int row = agent.getX() + movement[0]; 
		int column =agent.getY() + movement[1];
		int mapSize = map.length;
			if (row < 0) 
				movement[0] = mapSize -1 ;
			else if (row >= mapSize) 
				movement[0] = 0;
			else if (column >= mapSize) 
				movement[1] = 0;
			else if (column < 0 )
				movement[1] = mapSize -1;
	
	}
	
	
	private void createAgents(int totalAgents){
		Random rand = new Random();
		for(int i =0; i <totalAgents;i++){
			int[] newPos = null;
			do{
			 newPos = new int[]{rand.nextInt(map.length),rand.nextInt(map.length)};
			}while(!map[newPos[0]][newPos[1]].isEmpty());
			Shark jabber = new Shark(newPos);
			sharks.put(jabber.id, jabber);
			map[newPos[0]][newPos[1]].populateCell(jabber);
			do{
				 newPos = new int[]{rand.nextInt(map.length),rand.nextInt(map.length)};
				}while(!map[newPos[0]][newPos[1]].isEmpty());
				Fish nemo = new Fish(newPos);
				fishes.put(nemo.id, nemo);
				map[newPos[0]][newPos[1]].populateCell(nemo);
				fishesCounter++;
				sharksCounter++;

		}
	}
	
	public void update(JFrame frame, int[] cellSize,RectDraw rectangle){
		rectangle.cleanRects();
		Set<Integer> sharksKeys = sharks.keySet();
		Set<Integer> fishesKeys = fishes.keySet();
		if(!sharks.isEmpty()){
			for(int key : sharksKeys){
				Shark jabber = sharks.get(key);

				if(jabber!= null){
					jabber.move(this);
					if(jabber.isLeader())jabber.drawAgent(cellSize,Color.BLACK,rectangle);
					else jabber.drawAgent(cellSize,Color.BLUE,rectangle);
					frame.getContentPane().add(rectangle);
				}

			}
			
		}else{
			rectangle.cleanRects();
		}
		if(!fishes.isEmpty()){
			for(int key : fishesKeys){
				Fish nemo = fishes.get(key);

				if(nemo != null){
					nemo.move(this);
					if(nemo.isLeader())nemo.drawAgent(cellSize,Color.PINK,rectangle);
					else nemo.drawAgent(cellSize,Color.RED,rectangle);
					frame.getContentPane().add(rectangle);
				}

			}
			
		}else{
			rectangle.cleanRects();
		}


	}
	public void setLeader(AgentType type){
		if(type == AgentType.SHARK){
			Set<Integer> sharksKeys = sharks.keySet();
			for(int key : sharksKeys){
				Shark shark = this.sharks.get(key);
				if(shark!= null){
					shark.setLeader();
					return;
				}
			}

		}else if(type == AgentType.FISH ){
			Set<Integer> fishesKeys = fishes.keySet();

			for(int key : fishesKeys){
				Fish fish = this.fishes.get(key);
				if(fish!= null){
					fish.setLeader();
					return;
				}
			}
		}
	}
	
	

}
