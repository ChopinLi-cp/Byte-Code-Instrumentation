package agent;
import java.util.*;
import agent.Helper;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Proxy{
	static Utility util; 
	public Proxy(){
		util = new Utility();
	}
 	public static boolean add(List list, Object obj, int lineNumber, String methodName,  String className){

                //long opTime = System.currentTimeMillis();
               // System.out.println("Line Number " +lineNumber+ "method name = " + methodName+ " className " + className);
               // int hashcode = System.identityHashCode(list);

                //Thread currentThread = Thread.currentThread();
                //long currentThreadId = currentThread.getId();
    
               // System.out.println("ADD CurrentThreadId = "+currentThreadId);
                    
                //long opTime = System.currentTimeMillis();
                //System.out.println("Time = " +time1);
                //boolean ret = list.add(obj);
            
        //      Op opAdd = new Op(1, methodName, className, lineNumber, opTime );
                    
//                InterceptionPoint interception = new InterceptionPoint(currentThreadId, className, methodName, lineNumber, 0, opTime); 
		InterceptionPoint interception = Helper.createInstance(list, className, methodName, lineNumber, 0);
                util.onCall(interception);

                boolean ret = list.add(obj);
                    
                return ret;
        }

	public static boolean contains(List list, Object obj, int lineNumber, String methodName, String className){

                //long opTime = System.currentTimeMillis();
                //int hashcode = System.identityHashCode(list);

               // Thread currentThread = Thread.currentThread();
               // long currentThreadId = currentThread.getId();

                //System.out.println("Time From containsProxy = " +opTime);
                //boolean ret= list.contains(obj);

               // Op opContains = new Op(2, methodName,  className, lineNumber, opTime);
                //InterceptionPoint interception = new InterceptionPoint(currentThreadId, className, methodName, lineNumber, 1, opTime);


		InterceptionPoint interception = Helper.createInstance(list, className, methodName, lineNumber, 1);
		//InterceptionPoint interception = Helper.createInstance(currentThreadId, className, methodName, lineNumber, 0, opTime);
                util.onCall(interception);

                boolean ret= list.contains(obj);
                return ret;
        }

		

}
