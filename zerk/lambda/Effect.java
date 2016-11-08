package zerk.lambda;

public class Effect {
	private Lambda apply;
	private Lambda repeat;
	private Lambda remove;
	
	public Effect(Lambda apply, Lambda repeat, Lambda remove){
		this.apply = apply;
		this.repeat = repeat;
		this.remove = remove;
	}
	
	public Effect(){
		
	}
	
	public void apply(){
		apply.exec("");
	}
	
	public void repeat(){
		repeat.exec("");
	}
	
	public void remove(){
		remove.exec("");
	}
}