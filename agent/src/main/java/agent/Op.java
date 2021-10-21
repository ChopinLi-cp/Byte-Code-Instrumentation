package agent;

public class Op{
//	public Op(){
		int operation;
		String className;
		int lineNumber;
//	}

	public int getOpID(){
		return this.operation;
	}


	public String getClassName(){
		return this.className;
	}


	public int getLineNumber(){
		return this.lineNumber;
	}


	public void setOpID(int Op){
		this.operation=Op;	
	}


	public void setClassName(String className){
		this.className = className;
	}

	public void setLineNumber(int lineNumber){
		this.lineNumber = lineNumber;
	}
}
