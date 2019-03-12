package model.Room;

import java.util.Arrays;

public class Trailer extends Room {
	

	public Trailer() {
		super("trailer");
	}	
	
	@Override
	public void newDay() {
		return;
	}

	@Override
	public int[] getPlayerPos(){
		int[] pArea = Arrays.copyOf(area, area.length);
		pArea[1] += area[2]/5;
		return pArea;
	}

}
