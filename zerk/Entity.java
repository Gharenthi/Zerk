package zerk;

import java.util.HashMap;
import java.util.Map;

import zerk.entity.Foe;
import zerk.lambda.Action;
import zerk.lambda.ActionRequest;
import zerk.lambda.Effect;
import zerk.lambda.Lambda;

public abstract class Entity {
	public Map<String, Action> actions;
	protected GameData data;
	protected Stats stats;
	protected String name;
	
	public Map<String, Effect> effects = new HashMap<String, Effect>();
	public Map<String, Map<String, Lambda>> triggers = initTriggers();
	public ActionRequest queued;
	
	public int actTime;
	public int mana;
	public int maxMana;
	public int hp;
	public int maxHp;
	
	public abstract String statusDisplay();
	protected abstract HashMap<String, Map<String, Lambda>> initTriggers();
	
	public void trigger(String event, String data){
		if (triggers.keySet().contains(event))
			for (Lambda trigger : triggers.get(event).values()) trigger.exec(data);
	}
	
	public void setAction(String action, String param){
		if (actions.keySet().contains(action))
			if (actions.get(action).isAcceptable(param))
				if (this.getClass() != Foe.class && mana < actions.get(action).getManaCost())
					data.echo("Not enough mana for that action!");
				else queued = new ActionRequest(actions.get(action), param);
	}
	
	public abstract void progressTime();
	

	public void addMana(int added){
		mana += added;
		trigger("regen_mana", added +"");
		int overcharge = mana - maxMana;
		if (overcharge > 0){
			mana -= overcharge;
			trigger("overcharge_mana", overcharge +"");
		}
	}

	public void addLife(int added){
		hp += added;
		trigger("regen_life", added +"");
		int overcharge = hp - maxHp;
		if (overcharge > 0){
			hp -= overcharge;
			trigger("overcharge_life", overcharge +"");
		}
	}
	
	public void damage(int damage){
		hp -= damage;
		trigger("damage", damage +"");
	}
}