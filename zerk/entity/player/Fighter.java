package zerk.entity.player;

import zerk.GameData;
import zerk.Stats;
import zerk.entity.Player;
import zerk.lambda.Action;

public class Fighter extends Player{
	public Fighter(GameData data, String name){
		this.data = data;
		this.name = name;
		stats = new Stats(5,5, 5,5, 10,10);
		charClass = "fighter";
		initLevelup();
	}
	
	protected void initLevelup(){
		levelupEffects.put(1, str -> {
		});
	}
}
