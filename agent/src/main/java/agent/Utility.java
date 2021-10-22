package agent;

import java.util.*;
import java.util.Iterator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
public class Utility{
	private static Map<Integer, ThreadAndOp> globalMap;
	static int count=0;
	//private static HashMap<Long,Integer> threadOpIdPair;// = new HashMap<Integer,Integer>();
	static ThreadAndOp threadOpIdPair;
	public static String callPrint(){
		System.out.println("HELLO FROM Utility Instrumentation");
		return "";

	}

	public static boolean addProxy(List list, Object obj, int lineNumber, String className){
		System.out.println("Line Number " +lineNumber+ " className " + className);
		int hashcode = System.identityHashCode(list);

		Thread currentThread = Thread.currentThread();
		long currentThreadId = currentThread.getId();
		
		System.out.println("ADD CurrentThreadId = "+currentThreadId);
	
		Op opAdd = new Op();
		opAdd.setOpID(1);	
			
	//	EnumSet[] op_names= EnumSet.values();
	//	int op_id=0;		
		/*for (EnggggmSet enum_op_name : op_names) {
			String op_name=enum_op_name.name();
			if(op_name.equals("ADD"))	
			{
				System.out.println("NAME*****************" + op_name);
				op_id=enum_op_name.getValue();
			}
        	}
	        if(op_id>0){*/
       // 	    	System.out.println(op_id + "ADD HAppened ");
		OnCall(hashcode, currentThreadId, opAdd);
		//}
		boolean ret = list.add(obj);

		return ret;
	}

	public static boolean containsProxy(List list, Object obj, int lineNumber, String className){
		int hashcode = System.identityHashCode(list);
		//System.out.println("hashCode = "+hashcode);

		Thread currentThread = Thread.currentThread();
		long currentThreadId = currentThread.getId();
		
		//System.out.println("Contains************** CurrehtThreadId = "+currentThreadId);
                Op opContains = new Op();
		opContains.setOpID(2);	
		
		//System.out.println("ONCALL********");
		OnCall( hashcode, currentThreadId, opContains);
                //}
		boolean ret= list.contains(obj);	
		
                return ret;
	}

	public static void delay(){
		try{
		Thread.sleep(100);
		}
		catch (InterruptedException e) {
                        System.out.println("Exception " );
                        e.printStackTrace();
                }

	}

	public static void set_trap(int obj_id, long thread_id, Op operation){
		int op_id= operation.getOpID();
		if(globalMap.isEmpty()){
			threadOpIdPair = new ThreadAndOp(thread_id, op_id);
                        globalMap.put(obj_id, threadOpIdPair);
			System.out.println("*** Addition done for empty Map ****");
		}
		else {
                         //System.out.println("======> Global Table will be updated " );
                         threadOpIdPair = new ThreadAndOp(thread_id, op_id);
                         globalMap.put(obj_id, threadOpIdPair);
			 
                         System.out.println("======> Global Table Update is done " );
                }
	}
	

	public static synchronized void OnCall(int obj_id, long thread_id, Op operation){
		boolean violation = check_for_trap (obj_id , thread_id, operation );
		if(should_delay(operation)){
			set_trap(obj_id, thread_id, operation);
			delay();
		//	clear_trap(thread_id, obj_id, op_id);
		}
	}

	public static void getMapInstance(){
		if(globalMap==null) 
		{
			System.out.println("NEW INSTANCE................");
                        globalMap = new HashMap<>();
		}
	}
	public static boolean check_for_trap(int obj_id, long thread_id, Op operation){
		
		int op_id= operation.getOpID();
		boolean violation=false;
		getMapInstance();	
		if(globalMap.isEmpty())	{
			System.out.println("*** Global Map is Empty ****" );
		}
		else {				         
			long threadId;
                	int objId, opId;
                	for (Map.Entry<Integer, ThreadAndOp> globalMapEntry : globalMap.entrySet()) {
                        	objId = globalMapEntry.getKey();
				threadId= threadOpIdPair.getThreadId();
				opId= threadOpIdPair.getOpId();
				System.out.println("*** ELSE ****" +" obj_id= " + obj_id + " "+ objId + " thread id = "+thread_id + " op_id "+ op_id );
               			if( objId == obj_id && threadId != thread_id){
					System.out.println("ENTRY==");
					if(opId == 1 || op_id ==1){
						System.out.println("======>Lock is needed" );
						violation=true;
					}
					else {
						System.out.println("======> Global Table will be updated " );
					}
				}
                	}
		}
		return violation;
	}
	/*private static void  printHashMap(){
		long th_id;
		int obj_id, op_id;
		for (Map.Entry<Long, HashMap<Integer, Integer>> globalMapEntry : globalMap.entrySet()) {
			long thread_id = globalMapEntry.getKey();
			obj_id = 0;
			op_id = 0;
			for (Map.Entry<Integer, Integer> objOpIdPairEntry : globalMapEntry.getValue().entrySet()) {
				obj_id = objOpIdPairEntry.getKey();
				op_id = objOpIdPairEntry.getValue();
			}
			System.out.println("***FROM PRINT **** thread_id = "+ thread_id + " obj_id = " +obj_id + " op_id = " +op_id);
		}
	}*/
	public static boolean should_delay(Op op_id){
		return true;
	}

	

}
