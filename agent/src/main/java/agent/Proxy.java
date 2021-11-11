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
		InterceptionPoint interception = Helper.createInstance(list, className, methodName, lineNumber, Operation.WRITE);
                util.onCall(interception);

                boolean ret = list.add(obj);
                    
                return ret;
        }

	public static boolean contains(List list, Object obj, int lineNumber, String methodName, String className){
		InterceptionPoint interception = Helper.createInstance(list, className, methodName, lineNumber, Operation.READ);
		//InterceptionPoint interception = Helper.createInstance(currentThreadId, className, methodName, lineNumber, 0, opTime);
                util.onCall(interception);

                boolean ret= list.contains(obj);
                return ret;
        }


	public static int size(List list,  int lineNumber, String methodName, String className){
		InterceptionPoint interception = Helper.createInstance(list, className, methodName, lineNumber, Operation.READ);
                util.onCall(interception);
                int ret= list.size();
                return ret;
        }

 	 public static boolean  isEmpty(List list,  int lineNumber, String methodName, String className){
                InterceptionPoint interception = Helper.createInstance(list, className, methodName, lineNumber, Operation.READ);
                util.onCall(interception);
                boolean ret= list.isEmpty();
                return ret;
        }


 	public static boolean remove(List list, Object obj, int lineNumber, String methodName,  String className){
		InterceptionPoint interception = Helper.createInstance(list, className, methodName, lineNumber, Operation.WRITE);
                util.onCall(interception);
		System.out.println("ISSSSSSSSSSSSSSSSSSSSSSSSSS REMOVEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
                boolean ret = list.remove(obj);
                return ret;
        }





	public static boolean add(ArrayList list, Object obj, int lineNumber, String methodName,  String className){
		InterceptionPoint interception = Helper.createInstance(list, className, methodName, lineNumber, Operation.WRITE);
                util.onCall(interception);

                boolean ret = list.add(obj);
                    
                return ret;
        }

	public static boolean contains(ArrayList list, Object obj, int lineNumber, String methodName, String className){
		InterceptionPoint interception = Helper.createInstance(list, className, methodName, lineNumber, Operation.READ);
		//InterceptionPoint interception = Helper.createInstance(currentThreadId, className, methodName, lineNumber, 0, opTime);
                util.onCall(interception);

                boolean ret= list.contains(obj);
                return ret;
        }


	public static int size(ArrayList list,  int lineNumber, String methodName, String className){
		InterceptionPoint interception = Helper.createInstance(list, className, methodName, lineNumber, Operation.READ);
                util.onCall(interception);
                int ret= list.size();
                return ret;
        }

 	 public static boolean isEmpty(ArrayList list,  int lineNumber, String methodName, String className){
                InterceptionPoint interception = Helper.createInstance(list, className, methodName, lineNumber, Operation.READ);
                util.onCall(interception);
                boolean ret= list.isEmpty();
                return ret;
        }


 	public static boolean remove(ArrayList list, Object obj, int lineNumber, String methodName,  String className){
		InterceptionPoint interception = Helper.createInstance(list, className, methodName, lineNumber, Operation.WRITE);
                util.onCall(interception);
		System.out.println("ArrayLIST ISSSSSSSSSSSSSSSSSSSSSSSSSS REMOVEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
                boolean ret = list.remove(obj);
                return ret;
        }



}
