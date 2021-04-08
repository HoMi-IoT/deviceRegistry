package deviceRegistry;

public class Task {
	private String command;
	private String type;
	private String[] variables;
	private boolean done;
	private Object ret;


	public Task(String com, String t, String[] var) {
		this.command = com;
		this.type = t;
		this.variables = var;
		this.done = false;
		this.ret = null;
	}

	public String getCommand() {
		return this.command;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String[] getVariables() {
		return this.variables;
	}
	
	public void complete() {
		this.done = true;
	}
	
	public boolean isComplete() {
		return this.done;
	}
	
	public void setReturn(Object ret) {
		this.ret = ret;
	}
	
	public Object getReturn() {
		return this.ret;
	}
}
