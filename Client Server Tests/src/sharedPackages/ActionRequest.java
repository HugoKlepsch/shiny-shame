package sharedPackages;

public class ActionRequest {
	private int action;
	private Message message;
	private long index;
	
	public ActionRequest(int action){
		this.action = action;
	}
	
	public ActionRequest(int action, Message message){
		this.action = action;
		this.message = message;
	}
	
	public ActionRequest(int action, long index){
		this.action = action;
		this.index = index;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}
	
	

}
