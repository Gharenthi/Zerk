package zerk.entity.player;

import zerk.GameData;
import zerk.Stats;
import zerk.entity.Player;
import zerk.lambda.Action;

public class Mage extends Player{
	public Mage(GameData data, String name){
		this.data = data;
		this.name = name;
		stats = new Stats(5,5, 5,5, 10,10);
		charClass = "mage";
		initLevelup();
	}
	
	protected void initLevelup(){
		levelupEffects.put(1, str -> {
			combatActions.replace("charge", new Action(5, 0, param->true, param->{
				addMana((int) (1.5 * Math.log(stats.att) / Math.log(2)) );
			}));
			combatActions.put("manablast", new Action(5, 1, param->true, param->{
				data.echo("Destructively releasing charged mana!");
				if (data.enemy.hasData()) data.enemy.damage(mana);
				else data.echo("If there were an enemy this would have dealt " + mana + " damage.");
				mana = 0;
			}));
			combatActions.put("heal", new Action(5, 5, param->true, param->{
				data.echo("Casting a healing spell...");
				addLife((int) (5 * Math.sqrt(stats.mag)));
				mana -= 5;
			}));
		});
	}
}