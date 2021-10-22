package agent;

import java.util.*;
import java.util.Iterator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
public class Utility{
	private static Map<Integer, List <ThreadAndOp>> globalMap;
	
	private static List<ThreadAndOp> threadOpList;
//	private static Map<Integer, ThreadAndOp> globalMap;
	//static int count=0;
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
		OnCall(hashcode, currentThreadId, opAdd);
		boolean ret = list.add(obj);

		return ret;
	}

	public static boolean containsProxy(List list, Object obj, int lineNumber, String className){
		int hashcode = System.identityHashCode(list);

		Thread currentThread = Thread.currentThread();
		long currentThreadId = currentThread.getId();
		
                Op opContains = new Op();
		opContains.setOpID(2);	
		
		OnCall( hashcode, currentThreadId, opContains);
		boolean ret= list.contains(obj);	
		
                return ret;
	}

	public static void delay(){
		try{
		Thread.sleep(10);
		}
		catch (InterruptedException e) {
                        System.out.println("Exception " );
                        e.printStackTrace();
                }
	}

	public static  synchronized  void set_trap(int obj_id, long thread_id, int op_id){
		//int op_id= operation.getOpID();
		if (globalMap.containsKey(obj_id)){
                        threadOpList = globalMap.get(obj_id);
                        threadOpList.add(new ThreadAndOp(thread_id, op_id));
                        globalMap.put(obj_id, threadOpList);
                        System.out.println("Global Table is updated obj_id= " + obj_id + " thread id = "+thread_id + " op_id "+ op_id );
		}

	   	else {
			threadOpList = new ArrayList<>();
			threadOpList.add(new ThreadAndOp(thread_id, op_id));
                	globalMap.put(obj_id, threadOpList);
			System.out.println(" Addition for empty Map  obj_id= " + obj_id + " thread id = "+thread_id + " op_id "+ op_id );
		}
	}

	public static synchronized void clear_trap(int obj_id, long thread_id, int op_id){
        	for (Map.Entry<Integer,List< ThreadAndOp>> globalMapEntry : globalMap.entrySet()) {
                        int objId = globalMapEntry.getKey();
			List<ThreadAndOp> thOpList = globalMap.get(objId);
                        for (int i =0; i<thOpList.size();i++){
                        	ThreadAndOp thOpEntity=thOpList.get(i);
                               	long threadId= thOpEntity.getThreadId();
				int opId= thOpEntity.getOpId();
				if(objId == obj_id && threadId == thread_id && op_id == opId){
					thOpList.remove(i);
					globalMap.put(objId, thOpList);
					System.out.println("*** One Object Removed ****" +" obj_id= " + obj_id + " "+ objId + " thread id = "+thread_id + " op_id "+ op_id );
					printHashMap();
				}
			}
		}	
	}

	public static  void OnCall(int obj_id, long thread_id, Op operation){
		boolean violation = check_for_trap (obj_id , thread_id, operation );
		int op_id= operation.getOpID();
		if(should_delay(op_id)){
			set_trap(obj_id, thread_id, op_id);
			delay();
			clear_trap(obj_id, thread_id, op_id);
			//printHashMap();
		}
	}

	public static synchronized void getMapInstance(){
		if(globalMap==null) 
		{
			System.out.println("NEW INSTANCE................");
                        globalMap = new HashMap<>();
		//	List<ThreadAndOp> threadOpList = new ArrayList<>();
		}
	}
	public static synchronized  boolean check_for_trap(int obj_id, long thread_id, Op operation){
		int op_id= operation.getOpID();
		boolean violation=false;
		getMapInstance();	
		if(globalMap.isEmpty())	{
			System.out.println("*** Global Map is Empty From check_for_trap****" );
		}
		else {				         
			long threadId;
                	int objId, opId;
                	for (Map.Entry<Integer, List <ThreadAndOp>> globalMapEntry : globalMap.entrySet()) {
                        	objId = globalMapEntry.getKey();
				List<ThreadAndOp> thOpList = globalMap.get(objId);
				for (int i =0; i<thOpList.size();i++){
					ThreadAndOp thOpEntity=thOpList.get(i);
					threadId= thOpEntity.getThreadId();
					opId= thOpEntity.getOpId();
					System.out.println(" obj_id= " + obj_id + " "+ objId + " thread id = "+thread_id + " op_id "+ op_id );
               				if( objId == obj_id && threadId != thread_id){
						//	System.out.println("ENTRY==");
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
		}
		return violation;
	}
	private static synchronized void  printHashMap(){
		long th_id;
		int obj_id, op_id;

		System.out.println("Printing ***************");
		 for (Map.Entry<Integer, List <ThreadAndOp>> globalMapEntry : globalMap.entrySet()) {
                 	obj_id = globalMapEntry.getKey();
                        List<ThreadAndOp> thOpList = globalMap.get(obj_id);
                        for (int i =0; i<thOpList.size();i++){
                        	ThreadAndOp thOpEntity=thOpList.get(i);
                                th_id= thOpEntity.getThreadId();
                                op_id= thOpEntity.getOpId();
                                System.out.println("Print** obj_id= " + obj_id + " thread id = "+th_id + " op_id "+ op_id );
			}
		 }
	}
	public static boolean should_delay(int op_id){
		return true;
	}

	

}
