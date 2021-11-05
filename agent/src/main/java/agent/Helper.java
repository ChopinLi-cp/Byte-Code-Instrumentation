package agent;

public class Helper{
	public static InterceptionPoint createInstance(Object obj, String  className, String methodName, int lineNumber, int op){

		long opTime = System.currentTimeMillis();
                //System.out.println("Line Number " +lineNumber+ "method name = " + methodName+ " className " + className);
                int hashCode = System.identityHashCode(obj);

                Thread currentThread = Thread.currentThread();
                long currentThreadId = currentThread.getId();

                System.out.println("ADD CurrentThreadId = "+currentThreadId);

                //long opTime = System.currentTimeMillis();
                //System.out.println("Time = " +time1);
                //boolean ret = list.add(obj);


		InterceptionPoint interception = new InterceptionPoint(hashCode, currentThreadId, className, methodName, lineNumber, op, opTime);
		return interception;
	}

}

