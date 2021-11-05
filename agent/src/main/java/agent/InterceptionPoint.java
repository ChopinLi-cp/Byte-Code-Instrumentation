package agent;

public class InterceptionPoint{
	long threadId;
	int opId;
	String location;
	long opTime;
	int activeThdNum;
	int lineNumber;
        String className;
        String methodName;
	int localHitCount;
	int globalHitCount;
	int delayCredit;
	int objId;
	boolean trapped;

	/*public InterceptionPoint(long threadId, int opId, String location, long time){
		this.threadId=threadId;
		this.opId=opId;
		this.location = location;
		this.time = time;
	}*/

	public InterceptionPoint(int objId, long threadId, String className, String methodName, int lineNumber, int opId, long  opTime){
		this.threadId = threadId;
                this.className = className;
                this.lineNumber = lineNumber;
                this.methodName = methodName;
                this.opId = opId;
	        this.opTime = opTime;
		this.objId = objId;
	}


	public void setObjectId(int objId){
                this.objId = objId;
        }
        public int getObjectId(){
                return this.objId;
        }


	public void setThreadId(long threadId){
		this.threadId = threadId;
	}
	public long getThreadId(){
		return this.threadId;
	}

	public void setOpID(int Op){
                this.opId = Op;
        }
	public int getOpId(){
		return this.opId;
	} 

        public void setClassName(String className){
                this.className = className;
        }
	public String getClassName(){
                return this.className;
        }


        public void setLineNumber(int lineNumber){
                this.lineNumber = lineNumber;
        }
        public int getLineNumber(){
                return this.lineNumber;
        }
	
	public void setMethodName(String methodName){
                this.methodName= methodName;
        }
        public String getMethodName(){
                return this.methodName;
        }

        public String getLocation(){
                return this.methodName + " " +this.className + " " + this.lineNumber;
        }

	public void setOpTime(long opTime){
	       this.opTime = opTime;
	}       
	public long getOpTime(){
                return this.opTime;
        }

	public void setGlobalHitCount(int globalHitCount){
	       this.globalHitCount= globalHitCount;
	}       
	public long getGlobalHitCount(){
                return this.globalHitCount;
        }


	public void setLocalHitCount(int localHitCount){
	       this.localHitCount = localHitCount;
	}       
	public long getLocalHitCount(){
                return this.localHitCount;
        }

  	/*public String getLocation(){
                return this.location;
        }*/
	public long getTimeMillis(){
		return this.opTime;
	}
	public int getActiveThdNum(){
		return this.activeThdNum;
	}

	public void setActiveThdNum(int activeThdNum){
		this.activeThdNum = activeThdNum;
	}

	public void setDelayCredit(int delayCredit){
		this.delayCredit = delayCredit;
	}

	public int getDelayCredit(){
		return this.delayCredit;
	}

	public void setTrapped(boolean trapped){
		this.trapped = trapped;
	}
	public boolean getTrapped(){
		return this.trapped;
	}

	public String toString(){
            return this.className + "|" + this.methodName + "|" + this.lineNumber + "|" + this.globalHitCount+ "|"+ this.localHitCount;
        }

}
