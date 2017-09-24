package util;

public class Distances {

	public static int manhattanDist(int[] pos1, int[] pos2,int mapLenght){
		int dx = Math.min(Math.abs(pos1[0]-pos2[0]),mapLenght-Math.abs(pos1[0]-pos2[0]));
		int dy = Math.min(Math.abs(pos1[1]-pos2[1]),mapLenght-Math.abs(pos1[1]-pos2[1]));
		return dx + dy;
	}
}
