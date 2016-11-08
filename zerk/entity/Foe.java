package zerk.entity;

import java.util.HashMap;
import java.util.Map;

import zerk.Entity;
import zerk.Stats;
import zerk.lambda.Lambda;

public class Foe extends Entity{
	private boolean hasData;
	
	
	public Foe(){
		stats = new Stats();
		hasData = false;
	}
	
	protected HashMap<String, Map<String, Lambda>> initTriggers(){
		HashMap<String, Map<String, Lambda>> out = new HashMap<String, Map<String, Lambda>>();
		return out;
	}
	
	public String statusDisplay(){
		String out = new String();
		
		out += name
				+"\n"
				+"\n" + "Hp: " + hp + " / " + maxHp;
		
		return out;
	}

	public void progressTime(){
		actTime += stats.spd;
		if (actTime >= queued.getTimeCost()){
			if (mana >= queued.getManaCost()) queued.run();
			actTime -= queued.getTimeCost();
			queued = null;
		}
	}
	
	public boolean hasData(){
		return hasData;
	}
}
