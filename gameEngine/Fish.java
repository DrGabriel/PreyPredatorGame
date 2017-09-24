package gameEngine;

import java.util.ArrayList;
import java.util.List;
import  util.*;

public class Fish extends Agent{
	public char direction;
	public Fish(int[] pos){
		this.pos = new int[2];
		this.breedingCycle = 12;
		this.lifeCycle = 50;
		this.visionAngle = 360;
		this.visionRadius = 20;
		this.setState('i');
		this.setX(pos[0]);
		this.setY(pos[1]);
		this.setType(AgentType.FISH);
		this.direction = 'r';
	}
	
	protected Fish createCub(int[] pos){
		return new Fish(pos);
	}
	
	public int[] flee(Game game,List<int[]> predators){
		if(this.isLeader()) {
			int mapLenght = game.map.length;
			List<int[]> currentMovement = new ArrayList<int[]>(); 
			int[] nearestShark = nearestAgent(game, predators);
			List<int[]>movements = game.validMoves(this);
			
			currentMovement.add(ArrayArithmetics.sumIntArrays(this.getPos(),movements.get(0)));//moves right
			int rightDist = Distances.manhattanDist(currentMovement.get(0), nearestShark, mapLenght);// check if shark is closer
			
			currentMovement.add(ArrayArithmetics.sumIntArrays(this.getPos(),movements.get(1)));//moves down
			int downDist = Distances.manhattanDist(currentMovement.get(1), nearestShark, mapLenght);// check if shark is closer
			
			currentMovement.add(ArrayArithmetics.sumIntArrays(this.getPos(),movements.get(2)));//moves left
			int leftDist = Distances.manhattanDist(currentMovement.get(2), nearestShark, mapLenght);//check if shark is closer
			
			currentMovement.add(ArrayArithmetics.sumIntArrays(this.getPos(),movements.get(3)));//moves up
			int upDist = Distances.manhattanDist(currentMovement.get(3), nearestShark, mapLenght);//check if shark is closer
			
			if(this.direction == 'r') {
				// checks if running right is better than running down
				if(rightDist>downDist && !game.map[currentMovement.get(0)[0]][currentMovement.get(0)[1]].marked){
					game.map[currentMovement.get(0)[0]][currentMovement.get(0)[1]].marked = true;
					return movements.get(0);// since the fish is running clockwise, running right is better than running down
				}else if(downDist>leftDist && !game.map[currentMovement.get(1)[0]][currentMovement.get(1)[1]].marked){
					game.map[currentMovement.get(1)[0]][currentMovement.get(1)[1]].marked = true;
					return movements.get(1);
				}else if(leftDist>upDist && !game.map[currentMovement.get(2)[0]][currentMovement.get(2)[1]].marked){
					game.map[currentMovement.get(2)[0]][currentMovement.get(2)[1]].marked = true;
					return movements.get(2);
				}else if(!game.map[currentMovement.get(3)[0]][currentMovement.get(3)[1]].marked){
					game.map[currentMovement.get(3)[0]][currentMovement.get(3)[1]].marked = true;
					return movements.get(3);
				}else{
					game.map[currentMovement.get(3)[0]][currentMovement.get(3)[1]].marked = false;
					this.direction = 'l';//rotates left
					return movements.get(3);
				}
		
				
			}else if(this.direction == 'l') {// peixe fugindo rodando para a esquerda, caso o sentido seja 'e'
				// checks if running left is better than running down
				if(leftDist>downDist && !game.map[currentMovement.get(2)[0]][currentMovement.get(2)[1]].marked){
					game.map[currentMovement.get(2)[0]][currentMovement.get(2)[1]].marked = true;
					return movements.get(2);// since the fish is running clockwise, running right is better than running down
				}else if(downDist>rightDist && !game.map[currentMovement.get(1)[0]][currentMovement.get(1)[1]].marked){
					game.map[currentMovement.get(1)[0]][currentMovement.get(1)[1]].marked = true;
					return movements.get(1);
				}else if(rightDist>upDist && !game.map[currentMovement.get(0)[0]][currentMovement.get(0)[1]].marked){
					game.map[currentMovement.get(0)[0]][currentMovement.get(0)[1]].marked = true;
					return movements.get(0);
				}else if(!game.map[currentMovement.get(3)[0]][currentMovement.get(3)[1]].marked){
					game.map[currentMovement.get(3)[0]][currentMovement.get(3)[1]].marked = true;
					return movements.get(3);
				}else{
					game.map[currentMovement.get(3)[0]][currentMovement.get(3)[1]].marked = false;
					this.direction = 'r';//rotates right
					return movements.get(3);
				}
			}
		}
		return null;//something went bad O-o
	}

	@Override
	public void move(Game game) {
		if(!age(game)){
			List<int[]> agents = searchAgents(game,AgentType.FISH);
			int[] movement = null;
			switch(this.getState()){
				case 'i':
					movement = randomMove(game);
					breed(game,movement);
				break;
				case 'c':
					movement = flee(game,agents);
					if(movement == null) movement = randomMove(game);//isnt leader
					breed(game,movement);
				break;
				
				case 'l':
					movement = this.followAgent(game, agents.get(0));
					breed(game,movement);
				break;
				default:
					System.out.println("Invalid state!");
				break;
			}	

		}else{
			game.fishesCounter--;
			game.fishes.remove(this.id);
			if(this.isLeader()) game.setLeader(AgentType.FISH);
		}
	}
}