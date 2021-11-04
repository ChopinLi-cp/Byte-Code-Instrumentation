package agent;

import java.util.*;
import java.util.Iterator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
public class Utility{
	private static Map<Integer, LinkedList <InterceptionPoint>> globalTrapList = new HashMap<>();
	private static Map<Integer, LinkedList<InterceptionPoint>>  lastInterceptionPointForOBJ = new HashMap<>();	
	private static int windowSize = 5;
	private static double delayProbability = 1;
	private static double decayFactor = 0.1;
	private static int planDistance = 10000000;
	private static int lastTPWindow = 5;
	private static double DelayProbabilityAtDangerousInterceptionPoint = 0.99;
	private static List<String> dangerousTPPairs = new ArrayList<String>();
	private static HashMap<String, Integer> hitTime = new HashMap<String, Integer>();
	private static List<String> blacklist = new ArrayList<String>();
	private static HashMap<Integer, InterceptionPoint> perThreadBlockedTP = new HashMap<>();
	private static HashMap<Long, InterceptionPoint> perThreadLastTP = new HashMap<>();
	private static LinkedList<InterceptionPoint> globalTPHistory = new LinkedList<>();
	private static int historyWindowSize = 32;
	private static HashMap<Long, Integer> perThreadTPCount = new HashMap<>();

	public static boolean addProxy(List list, Object obj, int lineNumber, String methodName,  String className){
		System.out.println("Line Number " +lineNumber+ "method name = " + methodName+ " className " + className);
		int hashcode = System.identityHashCode(list);

		Thread currentThread = Thread.currentThread();
		long currentThreadId = currentThread.getId();
		
		System.out.println("ADD CurrentThreadId = "+currentThreadId);
		
		long opTime = System.currentTimeMillis();
		//System.out.println("Time = " +time1);
                boolean ret = list.add(obj);
	
	//	Op opAdd = new Op(1, methodName, className, lineNumber, opTime );
		InterceptionPoint interception = new InterceptionPoint();
		interception.setClusterInfo(currentThreadId, className, methodName, lineNumber, 0, opTime); 
		OnCall(hashcode, interception);
		
		return ret;
	}

	public static boolean containsProxy(List list, Object obj, int lineNumber, String methodName, String className){
		int hashcode = System.identityHashCode(list);

		Thread currentThread = Thread.currentThread();
		long currentThreadId = currentThread.getId();
	
		
		long opTime = System.currentTimeMillis();
		//System.out.println("Time From containsProxy = " +opTime);
		boolean ret= list.contains(obj);	

               // Op opContains = new Op(2, methodName,  className, lineNumber, opTime);
	        InterceptionPoint interception = new InterceptionPoint();
 		interception.setClusterInfo(currentThreadId, className, methodName, lineNumber, 1, opTime); 
		OnCall(hashcode, interception);
		
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

	public static  synchronized  void set_trap(int obj_id, InterceptionPoint interception){
		LinkedList<InterceptionPoint> threadOpList;
		if (!globalTrapList.containsKey(obj_id)){
			threadOpList = new LinkedList<>();
			globalTrapList.put(obj_id, threadOpList);
		}

		threadOpList = globalTrapList.get(obj_id);
		threadOpList.add(interception);
	}

	public static synchronized void clear_trap(int obj_id, InterceptionPoint interception){
		if(globalTrapList.containsKey(obj_id)){	
			List<InterceptionPoint> thOpList = globalTrapList.get(obj_id);
			for (int i =0; i<thOpList.size();i++){
				InterceptionPoint thOpEntity=thOpList.get(i);
				long threadId= thOpEntity.getThreadId();
				int opId= thOpEntity.getOpId();
				if(threadId == interception.getThreadId() && opId == 0){
					thOpList.remove(i);
				//	HashSet<string> dangerousTPPairs = new HashSet<string>();			
					System.out.println("***CLeared ****" +" obj_id= " + obj_id + " " + " thread id = "+threadId + " op_id "+ opId );
			//		printHashMap();
				}
			}
		}	
	}
	public static synchronized void FindRacingTP(int obj_id, InterceptionPoint interception){
	     List<InterceptionPoint> list = new ArrayList<InterceptionPoint>();	
		
		System.out.println("Entry FindRacingTP");
	     if(lastInterceptionPointForOBJ.containsKey(obj_id)){

		System.out.println("Within 1st IF FindRacingTP");
			LinkedList<InterceptionPoint> qList = lastInterceptionPointForOBJ.get(obj_id);

			int listSize = qList.size();
			for (int i =0; i<listSize;i++){
				InterceptionPoint thOpEntity=qList.get(i);
				long threadId= thOpEntity.getThreadId();
				int opId= thOpEntity.getOpId();
				if((threadId != interception.getThreadId()) && (opId == 0 || interception.getOpId() == 0)) {
					long existingTime = thOpEntity.getTimeMillis();
					if ((existingTime + planDistance) >  interception.getTimeMillis()){
						if ((thOpEntity.getActiveThdNum() > 1) || (interception.getActiveThdNum() > 1)){
							list.add(thOpEntity);
						}
					}
				}
			}

			qList.add(interception);
			if(qList.size() > lastTPWindow){
				qList.removeFirst();	
			}
		}
		else {

			System.out.println("Within ELSE FindRacingTP");

			LinkedList<InterceptionPoint> threadOpList = new LinkedList<>();
			lastInterceptionPointForOBJ.put(obj_id, threadOpList);
		}

		//update Dangerous Pair Lis
		for(int i= 0; i<list.size();i++){
			InterceptionPoint tp2=list.get(i);
			String st = interception.ToString() + " ! " + tp2.ToString();
			String st2 = tp2.ToString() + " ! " + interception.ToString();
			String shortst = GetPairID(st, st2);
			String pairname = GetPairID(interception.getLocation(), tp2.getLocation());

		/*	if (blacklist.contains(pairname)){
                    		// the blacklist contains the bugs found in previous ROUNDS. DebugLog.Log("Skip blacked pair " + shortst);
                   		 continue;
               		 }*/
			
			if (hitTime.containsKey(interception.getLocation()) && hitTime.containsKey(tp2.getLocation()) && (hitTime.get(interception.getLocation()) + hitTime.get(tp2.getLocation()) >= 10) && dangerousTPPairs.contains(shortst)){
                        	continue;
                    	}

			// the hittime is how many time this dangerous pair has caused trap. It is used for decay.
			hitTime.put(interception.getLocation(), 0);
			hitTime.put(tp2.getLocation(), 0);
                    	dangerousTPPairs.add(shortst);

		}

	}	

	private void LoadKnownBuggyInterceptionPoints(String file)
        {

	}

	private static String GetPairID(String s1, String s2)
        {
            return s1.compareTo(s2) > 0 ? s1 + " " + s2 : s2 + " " + s1;
        }


	private static void UpdateTPHistory(int obj_id, InterceptionPoint interception){

		long thdid = interception.getThreadId();
		perThreadLastTP.put(thdid, interception);
		globalTPHistory.add(interception);
		if(perThreadTPCount.containsKey(thdid)){
			int count = perThreadTPCount.get(thdid);
			count++;
		}
		else {
			perThreadTPCount.put(thdid, 1);
		}

		if( globalTPHistory.size()  > historyWindowSize){
			InterceptionPoint tp = globalTPHistory.removeFirst();
			int count = perThreadTPCount.get(tp.getThreadId());
			if(count == 0){
				perThreadTPCount.remove(tp.getThreadId());
			}
		}

		interception.setActiveThdNum(perThreadTPCount.size());
	}

	public static void OnCall(int obj_id, InterceptionPoint interception){
		UpdateTPHistory(obj_id, interception);
		System.out.println("After UpdateTPHistory");
		FindRacingTP (obj_id, interception);

		System.out.println("After FindRacingTP");
		RemoveDependentInterceptionPoints(interception);
		boolean violation = check_for_trap (obj_id , interception);
		//int op_id= operation.getOpID();
		if(should_delay(obj_id, interception)){
			set_trap(obj_id, interception);
			delay();
			clear_trap(obj_id, interception);
			//printHashMap();
		}
	}
	public static synchronized  boolean check_for_trap(int obj_id, InterceptionPoint interception){
		boolean violation=false;
		/*if(globalTrapList==null) 
		{
			System.out.println("NEW INSTANCE................");
                        globalTrapList = new HashMap<>();
		}*/

		if(globalTrapList.isEmpty())	{
			System.out.println("*** Global Map is Empty From check_for_trap****" );
		}
		else {			
       			long thread_id = interception.getThreadId();
			int op_id = interception.getOpId();			
			long threadId;
                	int objId, opId;
			if(globalTrapList.containsKey(obj_id)){	
				LinkedList<InterceptionPoint> interceptionPointList = globalTrapList.get(obj_id);
				for (int i =0; i<interceptionPointList.size();i++){
					InterceptionPoint interception2=interceptionPointList.get(i);
					threadId= interception2.getThreadId();
					opId= interception2.getOpId();
					System.out.println(" obj_id= " + obj_id + " " + " thread id = "+thread_id + " op_id "+ op_id );
               				if((threadId != thread_id) && (opId == 0 || op_id == 0)) {
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
	private static synchronized void  printHashMap(){
		long th_id;
		int obj_id, op_id;

		System.out.println("Printing ***************");
		 for (Map.Entry<Integer, LinkedList <InterceptionPoint>> globalTrapListEntry : globalTrapList.entrySet()) {
                 	obj_id = globalTrapListEntry.getKey();
                        List<InterceptionPoint>  thOpList = globalTrapList.get(obj_id);
                        for (int i =0; i<thOpList.size();i++){
                        	InterceptionPoint thOpEntity=thOpList.get(i);
                                th_id= thOpEntity.getThreadId();
                                op_id= thOpEntity.getOpId();
                                System.out.println("Print** obj_id= " + obj_id + " thread id = "+th_id + " op_id "+ op_id );
			}
		 }
	}
	public static boolean should_delay(int objId, InterceptionPoint interception){
		//1. Check in the global list to get the last 20 entries
		// // // 2. It will calculate HB inference (Will do later)
		// 3. Identify Potential conflicts with the last 20 entries by
		// 	a. check th1 != th2, obj1 == obj2, any(op_id) ==1
		// 4. If there are no conflict between two TSVD points, the delay probability will decrease
		// Will look -> CheckForBugsAndStartTrap,
		//
		//
		// Tasks:
		//1.  At this moment, I will find last 5 entries in the global map and If I find any match with the current thread id,
		//obj_id and op id, I will return true. Otherwise return false.
		//2. Next step, setting the Time stamp for each entries. 
		
	/*	synchronized(Utility.class){
			if(globalTrapList.containsKey(objId)){
				List<InterceptionPoint>  thOpList = globalTrapList.get(objId);
				int listSize=thOpList.size();
				if(listSize> windowSize)
				{
					List<InterceptionPoint>  tail = thOpList.subList(Math.max(listSize - windowSize , 0), listSize);
					for(int i=0; i<tail.size(); i++){
						ThreadAndOp thOpEntity = tail.get(i);
						long th_id= thOpEntity.getThreadId();
						int op_id= thOpEntity.getOpId();
						if(thId != th_id){
							if(op_id == 0 || interception.getOpId()==0){
								System.out.println("####LAST windowSize Entries = th_id " + th_id );
								return true;
							}
						}
					}
				}
			}
		}*/

		

		boolean ret = false;
		double prob = 1.0;	
		int sleepDuration = 0;
		int danger = -1;

		if(globalTrapList.containsKey(objId)){
			LinkedList<InterceptionPoint> thOpList = globalTrapList.get(objId);
			for (int i=0; i<thOpList.size(); i++){
				InterceptionPoint thOpEntity = thOpList.get(i);
				String location = thOpEntity.getLocation();
				danger = IsInsideDangerList(location);
			}
		}
				
		if (interception.getOpId()==0)
                    {
                       /* if(trapPlan != null) {
				prob = trapPlan.delayProbability;
			}
			else {
				prob = this.delayProbability;
			}*/
			Random rand = new Random();

                        if (danger >= 0)
                        {
                                prob = DelayProbabilityAtDangerousInterceptionPoint - (decayFactor * danger);
                        }

                        if (rand.nextDouble() <= prob)
                        {
				ret = true;
			}
		    }
		return ret;
	}


	private static void RemoveDependentInterceptionPoints(InterceptionPoint tp)
        {
           // lock (this.perThreadLastTP)
            {
                if (perThreadBlockedTP.containsKey(tp.getThreadId()))
                {
                    // find the torchpint that blocked this thread if exists.
                    InterceptionPoint tp2 = perThreadBlockedTP.get(tp.getThreadId());

                    // delaycredit is k means a InterceptionPoint block the next-k operation in other thread
		    int valDelayCredit = tp2.getDelayCredit();
                    if ( valDelayCredit > 0)
                    {
                    	RemoveDangerItem(tp.getLocation(), tp2.getLocation());

                        // DebugLog.Log("HB Order " + tp.Tostring() + " " + tp2.Tostring());
			valDelayCredit--; 
                        tp2.setDelayCredit(valDelayCredit); 
                    }
                    else
                    {
                        perThreadBlockedTP.remove(tp.getThreadId());
                    }
                }
            }
        }

	private static void RemoveDangerItem(String tpstr1, String tpstr2)
        {
            String st = GetPairID(tpstr1, tpstr2);
            dangerousTPPairs.remove(st);

        }

	private static int IsInsideDangerList(String tpstr)
        {
           // lock (dangerousTPPairs)
	   synchronized(Utility.class)
            {
                if (hitTime.containsKey(tpstr))
                {
                    int count = hitTime.get(tpstr) + 1;
		    return count;
                }
            }

            return -1;
        }
}
