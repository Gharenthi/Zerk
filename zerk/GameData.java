package zerk;

import zerk.entity.Foe;
import zerk.entity.Player;
import zerk.entity.foe.RandomEnemy;
import zerk.entity.player.*;
import zerk.lambda.Lambda;
import zerk.map.World;

public class GameData {
	Main display;
	Lambda lambda;
	public Player player;
	public World world;
	public Foe enemy;
	boolean inCombat;

	public GameData(Main display) {
		this.display = display;
		getName(); // getname method progresses to getclass & constructs player
					// when done
	}

	public void attemptAction(String action) {
		action = action.toLowerCase();
		int spacePos = action.indexOf(' ');
		String param = (spacePos > -1) ? action.substring(spacePos) : "";
		action = (spacePos > -1) ? action.substring(0, spacePos) : action;
		if (player.actions.keySet().contains(action)) {
			player.setAction(action, param);
			startActionProgress();
			refreshState();
		}
	}

	public void send(String str) {
		lambda.exec(str);
	}

	public void echo(String str) {
		display.write(str + "\n\n");
	}

	private void getName() {
		echo("What is your name?");
		lambda = (str) -> {
			echo("Okay, so your name is '" + str + "'. And what sort of adventurer are you?");
			lambda = (charClass) -> {
				setCharClass(charClass, str);
			};
		};
	}

	private void setCharClass(String str, String name) {
		str = str.toLowerCase();
		switch (str) {
			case "mage":
				player = new Mage(this, name);
				break;
			case "fighter":
				player = new Fighter(this, name);
				break;
			case "reaper":
				player = new Reaper(this, name);
				break;
			default:
				echo("That is either made up or an incredibly shitty adventuring career. Choose again."
						+" (valid: mage, fighter)");
				break;
		}
		if (player != null) {
			echo("You are " + name + ", the " + str + ".");
			player.prepLevelOne();
			startGame();
		}
	}

	private void refreshDisplays(){
		if (player != null){
			display.itemDisplay.setText(player.inventoryDisplay());
			display.actionDisplay.setText(player.actionDisplay());
			display.playerDisplay.setText(player.statusDisplay());
		}
		if (enemy != null)
			display.itemDisplay.setText(player.inventoryDisplay());
		else display.enemyDisplay.setText("");
	}
	
	public void startBattle(Foe foe) {
		enemy = foe;
		player.actTime = 0;
		player.actions = player.combatActions;
	}

	public void endBattle() {
	}

	public void gameOver() {
		echo(player.name + " is most likely dead. Or you got bored. Either way, game over, press escape to exit.");
		lambda = str -> echo("Oi, the game's over. Leave.");
	}

	private void startGame() {
		startBattle(new RandomEnemy());
		lambda = (in) -> attemptAction(in);
		refreshState();
	}
	
	private void refreshState(){
		player.refreshStats();
		refreshDisplays();
	}

	
	private void progressCombat(){
		if (player.stats.spd > enemy.stats.spd){
			player.progressTime();
			enemy.progressTime();
		}
		else {
			player.progressTime();
			enemy.progressTime();
		}
	}
	
	private void startActionProgress(){
		if (inCombat)
			while (player.queued != null) progressCombat();
		else while (player.queued != null) player.progressTime();
	}
}
