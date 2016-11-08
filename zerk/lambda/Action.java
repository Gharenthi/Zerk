package zerk.lambda;

public class Action {
	private int timeCost;
	private int manaCost;
	private Lambda script;
	private BoolLambda paramCheck;
	
	public Action(int timeCost, int manaCost, BoolLambda paramCheck, Lambda script){
		this.timeCost = timeCost;
		this.manaCost = manaCost;
		this.script = script;
		this.paramCheck = paramCheck;
	}
	
	public int getTimeCost(){
		return timeCost;
	}
	
	public int getManaCost(){
		return manaCost;
	}
	
	public void run(String scriptData){
		script.exec(scriptData);
	}
	
	public boolean isAcceptable(String param){
		return paramCheck.exec(param);
	}
}