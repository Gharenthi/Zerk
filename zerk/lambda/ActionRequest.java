package zerk.lambda;

public class ActionRequest {
	private Action action;
	private String param;
	
	public ActionRequest(Action action, String param){
		this.action = action;
		this.param = param;
	}
	
	public int getTimeCost(){
		return action.getTimeCost();
	}
	
	public int getManaCost(){
		return action.getManaCost();
	}
	
	public void run(){
		action.run(param);
	}
}
