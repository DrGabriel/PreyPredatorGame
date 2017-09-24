package gameEngine;

import java.util.List;


public class Shark extends Agent {
	public Shark(int []pos){
		this.pos = new int[2];
		this.breedingCycle = 30;
		this.lifeCycle = 50;
		this.visionAngle = 360;
		this.visionRadius = 30;
		this.setState('i');
		this.setX(pos[0]);
		this.setY(pos[1]);
		this.setType(AgentType.SHARK);
	}
	
	
	public int[] huntPreys(Game game,List<int[]> preys){
		int[] shortestPreyPos = nearestAgent(game,preys);
		int[] movement = followAgent(game,shortestPreyPos);
		game.checkMovement(this, movement);
		int posX = movement[0]+this.getX();
		int posY = movement[1]+this.getY();
		if(game.map[posX][posY].getType()== AgentType.FISH){// attack fish
			game.fishesCounter--;
			game.fishes.remove(game.map[posX][posY].getId());
			game.map[posX][posY].clearCell();	
		}
		return movement;
			
	}
	
	@Override
	protected Shark createCub(int[] pos){
		return new Shark(pos);
	}

	@Override
	public void move(Game game){
		if(!age(game)){
			List<int[]> preys = searchAgents(game,AgentType.FISH);
			int[] movement;
			switch(this.getState()){
				case 'i':
					 movement = randomMove(game);
					 breed(game,movement);
					break;
				case 'c':
				    movement = huntPreys(game,preys);
					breed(game,movement);
					break;
				case 'l':
					movement = followAgent(game, preys.get(0));
					breed(game,movement);
					break;
				default:
					System.out.println("Invalid state!");
					break;
				
			}
		}else{
			game.sharksCounter--;
			game.sharks.remove(this.id);
			if(this.isLeader()) game.setLeader(AgentType.SHARK);
		}
	}
}
