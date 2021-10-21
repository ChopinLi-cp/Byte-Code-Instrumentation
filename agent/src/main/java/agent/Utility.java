package agent;

import java.util.*;
import java.util.Iterator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
public class Utility{
	private static HashMap<Integer, ThreadAndOp> globalMap;
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
		/*for (EnumSet enum_op_name : op_names) {
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
		
		System.out.println("Contains CurrentThreadId = "+currentThreadId);
/*		EnumSet[] op_names= EnumSet.values();
                int op_id=0;  
		for (EnumSet enum_op_name : op_names) {
                        String op_name=enum_op_name.name();
                        if(op_name.equals("CONTAINS"))       
                        {
				
                        	System.out.println("NAME*****************" + op_name);
                                op_id=enum_op_name.getValue();
                        }
                }
 
                if(op_id>0){    */
                Op opContains = new Op();
		opContains.setOpID(2);	
		
		System.out.println("ONCALL********");
		OnCall( hashcode, currentThreadId, opContains);
                //}
		boolean ret= list.contains(obj);	
		
                return ret;
	}

	

	public static void OnCall(int obj_id, long thread_id, Op operation){
		System.out.println("ONCALL********");
		check_for_trap (obj_id , thread_id, operation );
/*		if(should_delay(op_id)){
			set_trap(thread_id, obj_id, op_id);
			delay();
			clear_trap(thread_id, obj_id, op_id);
		}*/
	}

	public static void getMapInstance(){
		if(globalMap==null) {
                        globalMap = new HashMap<>();
			System.out.println("INITIALIZATION");
                        //threadOpIdPair = new HashMap<Long,Integer>();
                        //System.out.println ("Value Added in EMPTY HASHMAP "+ obj_id + " "+ thread_id + " " );
		//	return 	globalMap;
		}
		//else
		//	return globalMap;
	}
	public static void check_for_trap(int obj_id, long thread_id, Op operation){
		
		int op_id= operation.getOpID();
		//ThreadAndOp threadOpIdPair;
		
		getMapInstance();
		boolean x= globalMap.containsValue(obj_id);
		System.out.println ("Bool " +x);
		if(!globalMap.containsValue(obj_id)) 
		{
			threadOpIdPair = new ThreadAndOp(thread_id, op_id);
			//threadOpIdPair.put(thread_id, op_id);
                        globalMap.put(obj_id, threadOpIdPair);
			
			System.out.println("*** FROM CHECK TRAP ****" +" obj_id= " + obj_id + " thread id = "+thread_id + " op_id"+ op_id);
		}
//		op_id= operation.getOpID();
		//if(globalMap==null) {
			//globalMap = new HashMap<Integer, HashMap<Long, Integer>>();
			//threadOpIdPair = new HashMap<Long,Integer>();			
			//System.out.println ("Value Added in EMPTY HASHMAP "+ obj_id + " "+ thread_id + " " );
//			threadOpIdPair.put(thread_id, op_id);
 //                       globalMap.put(obj_id, threadOpIdPair);
       //                 printHashMap();

		//}
		else {				         
			long threadId;
                	int objId, opId;
                	for (Map.Entry<Integer, ThreadAndOp> globalMapEntry : globalMap.entrySet()) {
                        	objId = globalMapEntry.getKey();
				threadId= threadOpIdPair.getThreadId();
				opId= threadOpIdPair.getOpId();
System.out.println("*** ELSE FROM CHECK TRAP ****" +" obj_id= " + obj_id + " thread id = "+thread_id + " op_id "+ op_id );
       /*                 	threadId = 0;
                        	opId = 0;
                        	for (Map.Entry<Long, Integer> threadOpIdPairEntry : globalMapEntry.getValue().entrySet()) {
                                	threadId = threadOpIdPairEntry.getKey();
                                	opId = threadOpIdPairEntry.getValue();
                        	}*/
			//System.out.println("***FROM ELSE **** threadId = "+ threadId + "thread_id = "+ thread_id + " objId= "+objId+ " obj_id = " +obj_id + " opId = "+ opId + " op_id =     " +op_id);
               			if( objId == obj_id && threadId != thread_id){
					if(opId == 1 || op_id ==1){
//		        		System.out.println("***FROM PRINT **** thread_id = "+ thread_id + " obj_id = " +obj_id + " op_id = " +op_id);
						System.out.println("======>Lock is needed" );
					}
					else {
						System.out.println("======> Global Table will be updated " );
						threadOpIdPair = new ThreadAndOp(thread_id, op_id);
			                        globalMap.put(obj_id, threadOpIdPair);

						//threadOpIdPair.put(thread_id,  op_id);
			                        //globalMap.put(obj_id, threadOpIdPair);
					}
				}
				/*else {
                                                System.out.println("======> Global Table will be updated " );
		//				threadOpIdPair.put(thread_id, op_id);
			                      //  globalMap.put(obj_id, threadOpIdPair);
						threadOpIdPair = new ThreadAndOp(thread_id, op_id);
                                                globalMap.put(obj_id, threadOpIdPair);

                                }*/
                	}
		}

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
	public static boolean should_delay(int op_id){
		return true;
	}

	

}
