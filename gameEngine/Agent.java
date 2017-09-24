package gameEngine;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gui.Rect;
import gui.RectDraw;
import util.Distances;

public abstract class Agent {
	private boolean leader = false;
	private static int idBase = 0;
	protected AgentType type;
	public int id;
	protected int []pos;
	private char state;// i de idle, c de chase, l de seguindo lider
	protected int lifeCycle;
	protected int breedingCycle;
	protected int visionRadius;
	protected int visionAngle;
	
	public Agent(){
		this.id = idBase++;
	}
	protected abstract void move(Game game);

	public boolean isLeader(){
		return leader;
	}
	protected abstract Agent createCub(int[] pos);
	
	protected int[] randomMove(Game game){
		Random rand = new Random();
		int rowOrCol = rand.nextInt(2);
		int []movement = new int[]{0, 0};
		if(rowOrCol == 0){
				movement[0] = rand.nextInt(3)-1;
			}else{
				movement[1] = rand.nextInt(3)-1;
			}
		game.checkMovement(this,movement);
		return movement;
		


	}

	protected List<int[]> searchAgents(Game game,AgentType type){
		int row;
		int column;
		
		int mapSize = game.map.length;
		int []leaderPos = null;
		boolean hasEnemy = false, hasLeader = false;
		List<int[]> agentsPos = new ArrayList<int[]>();		

		
		for(int countX = -this.visionRadius; countX< this.visionRadius; countX++){
			row = this.getX() + countX;
			if(row < 0) row += mapSize;
			if(row >= mapSize) row -= mapSize;
			for(int countY = -this.visionRadius; countY < this.visionRadius;countY++){
				column = this.getY() + countY;
				if(column < 0) column += mapSize;
				if(column >= mapSize) column -= mapSize;
				if(row == this.getX() && column == this.getY()) continue;// can't search for itself
				if(!game.map[row][column].isEmpty()){//there's something inside the cell
					int id = game.map[row][column].getId();
					if(game.map[row][column].getType() == this.type){//Found a buddy, maybe the leader
						
						// chase leader mode triggered
						if(this.type == AgentType.SHARK){
							if(game.sharks.get(id).isLeader()){
								hasLeader = true;
								this.setState('l');
								 leaderPos = new int[]{row,column};	
							}

						}else if (this.type == AgentType.FISH){//Fishes always follow the leader
							if(game.fishes.get(id).isLeader()){
								this.setState('l');
								agentsPos.clear();
								agentsPos.add(new int[]{row,column});
								return agentsPos;// fish search ends here if have a leader on sight
							}

						}
					}else{// enemy spotted!!!!!
						hasEnemy = true;
						this.setState('c');// chase attack(shark) and run(fish) mode
						agentsPos.add(new int[]{row,column});
					}
				}
			}

			
		
		}
		if(!hasEnemy && !hasLeader) this.setState('i');// lost sight
		else if(hasLeader && !hasEnemy) agentsPos.add(leaderPos);// shark didn't found preys so follow the leader
		
		
		return agentsPos;
	}
	

	
	protected int[] nearestAgent(Game game,List<int[]>agents){
		int shorterDist = 10000;
		int[] shorterPos = new int[2];
		for(int[] agentPos : agents){
			int distance = Distances.manhattanDist(this.getPos(), agentPos,game.map.length);
			if (distance < shorterDist){
				shorterDist = distance;
				shorterPos = agentPos;
			}
		}
		return shorterPos;
	}
	
	protected int[] findEmptyCell(Game game,int []pos){
		int mapSize = game.map.length;
		for(int i = -1; i<2;i ++){
			for(int j = -1;j<2;j++){
				int row = pos[0] + i;
				int column = pos[1] + j;
				if(row < 0) row += mapSize;
				if(row==mapSize)row = 0;
				if(row >mapSize) row =-mapSize;
				if(column < 0) column += mapSize;
				if(column ==mapSize) column =0;
				if(column > mapSize) column -=mapSize;
				if(game.map[row][column].isEmpty()) return new int[]{row,column};
			}
		}
		return null;
		
	}
	
	public void setState(char state){
		if(state == 'i' || state == 'c' || state == 'l') this.state = state;
		else throw new IllegalArgumentException("State can only be 'i' or 'c'");
	}
	protected  boolean age(Game game){
		this.lifeCycle--;
		if(this.breedingCycle>0) {
			this.breedingCycle--;
		}
		if(this.lifeCycle <= 0){
			game.map[this.getX()][this.getY()].clearCell();
			return true;//died =/
		}
		return false;
	}
	
	
	protected void breed(Game game,int[] movement){
		movement[0] += this.getX();//that's the new agent position after the movement
		movement[1] += this.getY();
		if(game.map[movement[0]][movement[1]].getType() == this.getType() && !game.mapIsFull() && this.breedingCycle == 0){
				int[] cubPos = null;
				cubPos = findEmptyCell(game,this.pos);
				if(cubPos != null){// search if there's empty space near the father
					Agent cub  = createCub(cubPos);
					if(this.getType() == AgentType.SHARK){
						game.sharks.put(cub.id, (Shark) cub);	
						game.sharksCounter++;
					}else if (this.getType() == AgentType.FISH){
						game.fishes.put(cub.id, (Fish) cub);
						game.fishesCounter++;
					}

					game.map[cub.getX()][cub.getY()].populateCell(cub);
				}else{//search if there's empty space near the mother
					cubPos = findEmptyCell(game,movement);
					if(cubPos != null){
						Agent cub  = createCub(cubPos);
						if(this.getType() == AgentType.SHARK){
							game.sharks.put(cub.id, (Shark) cub);	
							game.sharksCounter++;
						}else if (this.getType() == AgentType.FISH){
							game.fishes.put(cub.id, (Fish) cub);
							game.fishesCounter++;
						}

						game.map[cub.getX()][cub.getY()].populateCell(cub);
					}
				}		
		}else{
			game.map[this.getX()][this.getY()].clearCell();
			this.setX(movement[0]);
			this.setY(movement[1]);
			game.map[this.getX()][this.getY()].populateCell(this);
		}
	}

	protected int [] followAgent(Game game,int[] agentPos){
		List<int[]> movements = game.validMoves(this);
		int shortestDist = 10000;
		int[] shortestMove = new int[2];
		int[] currentMovement = new int[2];
		for(int[] movement : movements){
			currentMovement[0]=movement[0]+this.getX();
			currentMovement[1]=movement[1]+this.getY();
			int distance = Distances.manhattanDist(currentMovement, agentPos,game.map.length);
			if (distance < shortestDist){
					shortestDist = distance;
					shortestMove = movement;
				}
		}
		return shortestMove;
	}
	protected int [] escapeAgent(Game game,int[] agentPos){
		List<int[]> movements = game.validMoves(this);
		int longestDist = -10000;
		int[] longestMove = new int[2];
		int[] currentMovement = new int[2];
		for(int[] movement : movements){
			currentMovement[0]=movement[0]+this.getX();
			currentMovement[1]=movement[1]+this.getY();
			int distance = Distances.manhattanDist(currentMovement, agentPos,game.map.length);
			if (distance > longestDist){
					longestDist = distance;
					longestMove = movement;
				}
		}
		return longestMove;
	}
	
	protected void drawAgent(int[] cellSize,Color color, RectDraw des){
		Rect ret = new Rect(this.getX()*cellSize[0],this.getY()*cellSize[1],cellSize[0],cellSize[1],color);
		des.addRect(ret);
	}
	
	public void setX(int x){
		if(x >= 0) this.pos[0] = x;
		else throw new IllegalArgumentException("X position cannot be negative");
	}
	public void setY(int y){
		if(y >= 0) this.pos[1] = y;
		else throw new IllegalArgumentException("Y position cannot be negative");
	}
	public int getX(){
		return this.pos[0];
	}
	public int getY(){
		return this.pos[1];
	}
	public int[] getPos(){
		return this.pos;
	}
	public void setLeader(){
		this.leader = true;
	}
	public void setType(AgentType type){
		if(type == AgentType.SHARK || type == AgentType.FISH) this.type = type;
		else throw new IllegalArgumentException("Type can only be SHARK or FISH");
	}
	public AgentType getType(){
		return this.type;
	}
	public char getState(){
		return this.state;
	}

	 

}
