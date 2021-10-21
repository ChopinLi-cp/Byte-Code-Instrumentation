package agent;

public class ThreadAndOp{
	long threadId;
	int opId;
	public ThreadAndOp(Long threadId, int opId){
		this.threadId=threadId;
		this.opId=opId;
	}
	public long getThreadId(){
		return this.threadId;
	}
	public int getOpId(){
		return this.opId;
	} 
	
}
