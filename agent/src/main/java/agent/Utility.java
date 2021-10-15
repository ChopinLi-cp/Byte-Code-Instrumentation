package agent;

import java.util.*;
import java.util.Iterator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
public class Utility{
	private static HashMap<Long, HashMap<Integer, Integer>> globalMap;// = new HashMap<Long, HashMap<Integer, Integer>>();
	private static HashMap<Integer,Integer> objOpIdPair;// = new HashMap<Integer,Integer>();

	public static String callPrint(){
		System.out.println("HELLO FROM Utility Instrumentation");
		return "";

	}

	public static String addProxy(List list, Object obj){
		int hashcode = System.identityHashCode(list);

		Thread currentThread = Thread.currentThread();
		long currentThreadId = currentThread.getId();
		
		System.out.println("ADD CurrentThreadId = "+currentThreadId);
		//m.add(obj);
		EnumSet[] op_names= EnumSet.values();
		int op_id=0;		
		for (EnumSet enum_op_name : op_names) {
			String op_name=enum_op_name.name();
			if(op_name.equals("ADD"))	
			{
				System.out.println("NAME*****************" + op_name);
				op_id=enum_op_name.getValue();
			}
        	}
	        if(op_id>0){
       // 	    	System.out.println(op_id + "ADD HAppened ");
			OnCall(currentThreadId, hashcode, op_id);
		}
		list.add(obj);

		return "";
	}

	public static String containsProxy(List list, Object obj){
		int hashcode = System.identityHashCode(list);
		//System.out.println("hashCode = "+hashcode);

		Thread currentThread = Thread.currentThread();
		long currentThreadId = currentThread.getId();
		
		System.out.println("Contains CurrentThreadId = "+currentThreadId);
		EnumSet[] op_names= EnumSet.values();
                int op_id=0;  
		for (EnumSet enum_op_name : op_names) {
                        String op_name=enum_op_name.name();
                        if(op_name.equals("CONTAINS"))       
                        {
				
                        	System.out.println("NAME*****************" + op_name);
                                op_id=enum_op_name.getValue();
                        }
                }
 
                if(op_id>0){    
                        
			OnCall(currentThreadId, hashcode, op_id);
                }
		list.add(obj);	
		
                return "";
	}

	

	public static void OnCall(long thread_id, int obj_id, int op_id){
		check_for_trap ( thread_id , obj_id , op_id );
/*		if(should_delay(op_id)){
			set_trap(thread_id, obj_id, op_id);
			delay();
			clear_trap(thread_id, obj_id, op_id);
		}*/
	}
	public static void check_for_trap(long thread_id, int obj_id, int op_id){
		System.out.println("*** FROM CHECK TRAP **** thread id = "+thread_id + " obj_id " + obj_id + " op_id " +op_id);
		if(globalMap==null) {
			globalMap = new HashMap<Long, HashMap<Integer, Integer>>();
			objOpIdPair = new HashMap<Integer,Integer>();			
			System.out.println ("Value Added in EMPTY HASHMAP "+ thread_id + " " + obj_id + " " + op_id);
			objOpIdPair.put(obj_id, op_id);
                        globalMap.put(thread_id, objOpIdPair);
       //                 printHashMap();

		}
		else {				         
			long threadId;
                	int objId, opId;
                	for (Map.Entry<Long, HashMap<Integer, Integer>> globalMapEntry : globalMap.entrySet()) {
                        	threadId = globalMapEntry.getKey();
                        	objId = 0;
                        	opId = 0;
                        	for (Map.Entry<Integer, Integer> objOpIdPairEntry : globalMapEntry.getValue().entrySet()) {
                                	objId = objOpIdPairEntry.getKey();
                                	opId = objOpIdPairEntry.getValue();
                        	}
			//System.out.println("***FROM ELSE **** threadId = "+ threadId + "thread_id = "+ thread_id + " objId= "+objId+ " obj_id = " +obj_id + " opId = "+ opId + " op_id =     " +op_id);
               			if( objId == obj_id && threadId == thread_id){
					if(opId == 1 || op_id ==1){
//		        		System.out.println("***FROM PRINT **** thread_id = "+ thread_id + " obj_id = " +obj_id + " op_id = " +op_id);
						System.out.println("======>Lock is needed" );
					}
					else {
						System.out.println("======> Global Table will be updated " );
						objOpIdPair.put(obj_id, op_id);
			                        globalMap.put(thread_id, objOpIdPair);
					}
				}
				else {
                                                System.out.println("======> Global Table will be updated " );
                                                objOpIdPair.put(obj_id, op_id);
                                                globalMap.put(thread_id, objOpIdPair);
                                }
                	}
		}

	}
	private static void  printHashMap(){
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
	}
	public static boolean should_delay(int op_id){
		return true;
	}

	

}
