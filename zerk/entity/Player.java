package zerk.entity;

import java.util.HashMap;
import java.util.Map;

import zerk.Entity;
import zerk.lambda.Action;
import zerk.lambda.Effect;
import zerk.lambda.Lambda;

public abstract class Player extends Entity {
	public Map<String, Action> combatActions = initCombatActions();
	public Map<String, Action> nonCombatActions = initNonCombatActions();
	protected Map<Integer, Lambda> levelupEffects = new HashMap<Integer, Lambda>();
	
	protected String charClass;
	public int level = 0;
	public int xp = 0;
	
	public int hpMods;
	public int mpMods;
	
	protected abstract void initLevelup();
	
	protected HashMap<String, Map<String, Lambda>> initTriggers(){
		HashMap<String, Map<String, Lambda>> out = new HashMap<String, Map<String, Lambda>>();
			out.put("overcharge_mana", new HashMap<String, Lambda>());
			out.put("overcharge_life", new HashMap<String, Lambda>());
			out.put("death", new HashMap<String, Lambda>());
			out.put("damage", new HashMap<String, Lambda>());
			out.put("regen_life", new HashMap<String, Lambda>());
			out.put("regen_mana", new HashMap<String, Lambda>());
			
			out.get("death").put("die", param -> effects.put("dead", new Effect()));

			out.get("overcharge_mana").put("message", param -> data.echo("Overcharged " + param + " mana!"));
			out.get("overcharge_mana").put("damage", param -> hp -= Integer.parseInt(param));
			
			out.get("overcharge_life").put("message", param -> data.echo("Overhealed " + param + " life!"));
			
			out.get("damage").put("message", param -> data.echo("Took " + param + " damage!"));
			
			out.get("regen_mana").put("message", param -> data.echo("Gained " + param + " mana"));
			
			out.get("regen_life").put("message", param -> data.echo("Regained " + param + " life"));
		return out;
	}

	public String actionDisplay(){
		String out = new String();
		out += "\t\t\t\tActions\n";
		
		for (String action : actions.keySet()) out += action + "\t";
		
		return out;
	}
	
	public String inventoryDisplay(){
		String out = new String();
		
		out += "\t\t\t\tInventory";
		
		return out;
	}

	public String statusDisplay(){
		String out = new String();
		
		out += name + ", " + charClass + " (" + level + ")"
				+"\n"
				+"\n" + "Hp: " + hp + " / " + maxHp	+"\t\t" + "Mp: " + mana + " / " + maxMana
				+"\n\t" + "Str: " + stats.str + "\t\t" + "Con: " + stats.con
				+"\n\t" + "Dex: " + stats.dex + "\t\t" + "Spd: " + stats.spd
				+"\n\t" + "Mag: " + stats.mag + "\t\t" + "Att: " + stats.att;
		out += "\n\n";
		
		for (String effect : effects.keySet()){
			out += effect + "\t";
		}
		
		return out;
	}

	
	public void progressTime(){
		actTime += stats.spd;
		if (actTime >= queued.getTimeCost()){
			if (mana >= queued.getManaCost()) queued.run();
			else data.echo("Not enough mana to perform that action!");
			actTime -= queued.getTimeCost();
			queued = null;
		}
	}
	
	public void addXp(int added){
		xp += added;
		while (xp >= levelupXp()){
			levelUp();
		}
	}

	public void refreshStats(){
		if (hp < 0)	trigger("death", hp +"");
		if (effects.keySet().contains("dead")) data.gameOver();
		maxHp = baseHp() + hpMods;
		maxMana = baseMp() + mpMods;
	}
	
	protected void levelUp(){
		xp -= levelupXp();
		level ++;
		data.echo("Level up! " + name + " is now a level " + level + " " + charClass);
		if (levelupEffects.keySet().contains(level))
			levelupEffects.get(level).exec("");
		refreshStats();
		hp = maxHp;
	}

	protected int baseHp(){
		return (level * stats.con) + 20;
	}
	
	protected int baseMp(){
		return (int) (Math.sqrt(level) * stats.att);
	}
	
	private int levelupXp(){
		return level;
	}
	
	public void prepLevelOne(){
		if (level == 0){
			level ++;
			if (levelupEffects.keySet().contains(level))
				levelupEffects.get(level).exec("");
			refreshStats();
			hp = maxHp;
		}
	}

	private Map<String, Action> initCombatActions(){
		Map<String, Action> out = new HashMap<String, Action>();
		
		out.put("nearly_die", new Action(0, 0, param->true, param->{
			data.echo("Okay, fine. You are nearly dead now.");
			hp = 1;
		}));
		
		combatActions.put("charge", new Action(5, 0, param->true, param->{
				addMana((int) (Math.log(stats.att) / Math.log(2)) );
		}));
		
		return out;
	}
	
	private Map<String, Action> initNonCombatActions(){
		Map<String, Action> out = new HashMap<String, Action>();		
		return out;
	}
}






