package util;

import java.util.ArrayList;
import java.util.List;

public class Resolution {
	private int[][] resolutionsList;
	public Resolution(){
		resolutionsList = new int[12][2];
		resolutionsList[0]= new int[]{800,600};
		resolutionsList[1] = new int[]{1024,768};
		resolutionsList[2] = new int[]{1280,720};
		resolutionsList[3] = new int[]{1280,768};
		resolutionsList[4] = new int[]{1280,1024};
		resolutionsList[5] = new int[]{1360,768};
		resolutionsList[6] = new int[]{1366,768};
		resolutionsList[7] = new int[]{1440,900};
		resolutionsList[8] = new int[]{1600,900};
		resolutionsList[9] = new int[]{1920,1080};
		resolutionsList[10] = new int[]{2560,1440};
		resolutionsList[11] = new int[]{3840,2160};
	}
	
	public List<int[]> supportedResolutions(int[] standardResolution){
		List<int[]> resolutions = new ArrayList<int[]>();
		for(int[] resolution : resolutionsList){
			if(resolution[0]<= standardResolution[0])
				resolutions.add(resolution);
		}
		return resolutions;
	}

}
