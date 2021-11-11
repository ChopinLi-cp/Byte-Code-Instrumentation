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
	private static HashMap<Long, InterceptionPoint> perThreadBlockedTP = new HashMap<>();
	public static  Map<InterceptionPoint, InterceptionPoint> resultInterception = new HashMap<>();
	private static HashMap<Long, InterceptionPoint> perThreadLastTP = new HashMap<>();
	private static LinkedList<InterceptionPoint> globalTPHistory = new LinkedList<>();
	private static int historyWindowSize = 32;
	private static HashMap<Long, Integer> perThreadTPCount = new HashMap<>();
	private static int inferSize = 5;
	private static boolean isTrapActive;
	private static int delayPerDangerousInterceptionPoint = 100;
	private static double inferLimit = 0.5;

	public static void delay(){
		try{
			Thread.sleep(100);
		}
		catch (InterruptedException e) {
                        System.out.println("Exception " );
                        e.printStackTrace();
                }
	}

	public static  synchronized  void setTrap(InterceptionPoint interception){
		int obj_id = interception.getObjectId();
		LinkedList<InterceptionPoint> threadOpList;
		if (!globalTrapList.containsKey(obj_id)){
			threadOpList = new LinkedList<>();
			globalTrapList.put(obj_id, threadOpList);
		}

		threadOpList = globalTrapList.get(obj_id);
		threadOpList.add(interception);
		isTrapActive = true;
	}

	public static synchronized void clearTrap(InterceptionPoint interception){
		int obj_id = interception.getObjectId();
		LinkedList<InterceptionPoint> threadOpList;
		if(globalTrapList.containsKey(obj_id)){
		/* try{
                         Thread.sleep(100);
                 }
                 catch (InterruptedException e) {
                          System.out.println("Exception " );
                         e.printStackTrace();
                  }*/

		globalTrapList.remove(obj_id);
		interception.setDelayCredit(inferSize);

	//	int size = perThreadLastTP.size();
		int index = 0;  
		for (Map.Entry<Long, InterceptionPoint> x : perThreadLastTP.entrySet())
		{
			Long threadId = x.getKey();
			InterceptionPoint tp1 =  x.getValue();
			if ((interception.getTimeMillis() + ( delayPerDangerousInterceptionPoint * inferLimit)) > tp1.getTimeMillis())
                            {
                                perThreadBlockedTP.put(threadId, interception);
				System.out.println("CLEAR TRAP +++++++++++++++++++++++&&&&&&&");
                            }
			index ++;
		}
		interception.setTrapped (true);

		//perThreadLastTP.forEach((k,v)->System.out.println("Key: " + k + "Value:@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " + v));
		/* for (Map.Entry<Long, InterceptionPoint> pair: perThreadLastTP.entrySet()) {
			 System.out.format("key: %s, value: %d%n jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj", pair.getKey(), pair.getValue());
        	}*/



		//I will remove the key with thw obj_id	
			/*List<InterceptionPoint> thOpList = globalTrapList.get(obj_id);

			for (int i =0; i<thOpList.size();i++){
				InterceptionPoint thOpEntity=thOpList.get(i);
				long threadId= thOpEntity.getThreadId();
				int opId= thOpEntity.getOpId();
				/*if(threadId == interception.getThreadId()){
					thOpList.remove(i);
				//	HashSet<string> dangerousTPPairs = new HashSet<string>();			
					System.out.println("***CLeared ****" +" obj_id= " + obj_id + " " + " thread id = "+threadId + " op_id "+ opId );
			//		printHashMap();
				}
			}*/
			isTrapActive = false;
		}	
	}
	public static synchronized void findRacingTP(InterceptionPoint interception){
	     List<InterceptionPoint> list = new ArrayList<InterceptionPoint>();	
		
		System.out.println("Entry FindRacingTP");
		int obj_id = interception.getObjectId();
	        if(lastInterceptionPointForOBJ.containsKey(obj_id)){

		        System.out.println("Within 1st IF FindRacingTP");
			LinkedList<InterceptionPoint> qList = lastInterceptionPointForOBJ.get(obj_id);

			int listSize = qList.size();
			//for (int i =0; i<listSize;i++)
			for (InterceptionPoint thOpEntity: qList){
			//	InterceptionPoint thOpEntity=qList.get(i);
				long threadId= thOpEntity.getThreadId();
				int opId= thOpEntity.getOpId();
				if((threadId != interception.getThreadId()) && (opId == Operation.WRITE || interception.getOpId() == Operation.WRITE)) {
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
			threadOpList.add(interception);
			lastInterceptionPointForOBJ.put(obj_id, threadOpList);
		}

		//update Dangerous Pair List
		//for(int i= 0; i<list.size();i++)
		for (InterceptionPoint tp2: list){
			//InterceptionPoint tp2=list.get(i);
			String st = interception.toString() + " ! " + tp2.toString();
			String st2 = tp2.toString() + " ! " + interception.toString();
			String shortst = GetPairID(st, st2);
			String pairname = GetPairID(interception.getLocation(), tp2.getLocation());

			if (hitTime.containsKey(interception.getLocation()) && hitTime.containsKey(tp2.getLocation()) && (hitTime.get(interception.getLocation()) + hitTime.get(tp2.getLocation()) >= 10) && dangerousTPPairs.contains(shortst)){
                        	continue;
                    	}

			// the hittime is how many time this dangerous pair has caused trap. It is used for decay.
			hitTime.put(interception.getLocation(), 0);
			hitTime.put(tp2.getLocation(), 0);
                    	dangerousTPPairs.add(shortst);

		}

	}	

	private static  synchronized  String GetPairID(String s1, String s2)
        {
            return s1.compareTo(s2) > 0 ? s1 + " " + s2 : s2 + " " + s1;
        }


	private static synchronized void updateTPHistory(InterceptionPoint interception){
		
		long thdid = interception.getThreadId();
		perThreadLastTP.put(thdid, interception);
		globalTPHistory.add(interception);
		if(perThreadTPCount.containsKey(thdid)){
			int count = perThreadTPCount.get(thdid);
			count++;
			perThreadTPCount.put(thdid, count);
		}
		else {
			perThreadTPCount.put(thdid, 1);
		}

		if( globalTPHistory.size()  > historyWindowSize){
			InterceptionPoint tp = globalTPHistory.removeFirst();
			int count = perThreadTPCount.get(tp.getThreadId());
			count--;
			perThreadTPCount.put(thdid, count);
			if(count == 0){
				perThreadTPCount.remove(tp.getThreadId());
			}
		}

		interception.setActiveThdNum(perThreadTPCount.size());
	}

	public static synchronized  boolean checkForTrap(InterceptionPoint interception){
		boolean bugFound =false;
		if(globalTrapList.isEmpty())	{
			System.out.println("*** Global Map is Empty From check_for_trap****" );
		}
		else {			
			int obj_id = interception.getObjectId();
       			long thread_id = interception.getThreadId();
			int op_id = interception.getOpId();			
			long threadId;
                	int objId, opId;
			if(globalTrapList.containsKey(obj_id)){	
				LinkedList<InterceptionPoint> interceptionPointList = globalTrapList.get(obj_id);
			//	for (int i =0; i<interceptionPointList.size();i++){
				for (InterceptionPoint interception2: interceptionPointList){
				//	InterceptionPoint interception2=interceptionPointList.get(i);
					threadId= interception2.getThreadId();
					opId= interception2.getOpId();
					System.out.println(" obj_id= " + obj_id + " " + " thread id = "+thread_id + " op_id "+ op_id );
               				if((threadId != thread_id) && (opId == Operation.WRITE || op_id == Operation.WRITE)) {
						System.out.println("======>Lock is needed" );
						bugFound = true;
						resultInterception.put(interception, interception2);
					}
					else {
						System.out.println("======> Global Table will be updated " );
					}
					
				}
                	}
		}
		return  bugFound ;
	}
	private static synchronized void  printHashMap(){
		long th_id;
		int obj_id, op_id;

		System.out.println("Printing ***************");
		 for (Map.Entry<Integer, LinkedList <InterceptionPoint>> globalTrapListEntry : globalTrapList.entrySet()) {
                 	obj_id = globalTrapListEntry.getKey();
                        List<InterceptionPoint>  thOpList = globalTrapList.get(obj_id);
                       // for (int i =0; i<thOpList.size();i++){
			for ( InterceptionPoint thOpEntity: thOpList){
                        	//InterceptionPoint thOpEntity=thOpList.get(i);
                                th_id= thOpEntity.getThreadId();
                                op_id= thOpEntity.getOpId();
                                System.out.println("Print** obj_id= " + obj_id + " thread id = "+th_id + " op_id "+ op_id );
			}
		 }
	}
	public static  synchronized  boolean shouldDelay(InterceptionPoint interception){
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
		int objId = interception.getObjectId();
		//System.out.println("==================***Danger Initial = " +danger);
		if(globalTrapList.containsKey(objId)){
			LinkedList<InterceptionPoint> thOpList = globalTrapList.get(objId);
		//	for (int i=0; i<thOpList.size(); i++){
			for( InterceptionPoint thOpEntity: thOpList){
			//	InterceptionPoint thOpEntity = thOpList.get(i);
				String location = thOpEntity.getLocation();
				danger = IsInsideDangerList(location);
				System.out.println("==================***Danger = " +danger);
			}
		}


				
		if ((danger >= 0 || !isTrapActive ) && (interception.getOpId()==Operation.WRITE))
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
				//System.out.println("%%%%%%%%%%%Should Delay is True");
				ret = true;
			}
		    }

		System.out.println("%%%%%%%%%%%Should Delay is =  "+ret);
		return ret;
	}


	private static  synchronized  void removeDependentInterceptionPoints(InterceptionPoint tp)
        {


		//System.out.println("Within RemoveDependentInterception +++++++++++++++++++++++&&&&&&&");
                if (perThreadBlockedTP.containsKey(tp.getThreadId()))
                {
                    // find the torchpint that blocked this thread if exists.
                    InterceptionPoint tp2 = perThreadBlockedTP.get(tp.getThreadId());

		System.out.println("Within RemoveDependentInterception +++++++++++++++++++++++");
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

	private static  synchronized  void RemoveDangerItem(String tpstr1, String tpstr2)
        {
            String st = GetPairID(tpstr1, tpstr2);
            dangerousTPPairs.remove(st);
        }

	private static  synchronized int IsInsideDangerList(String tpstr)
        {
                if (hitTime.containsKey(tpstr))
                {
                    int count = hitTime.get(tpstr) + 1;
		    hitTime.put(tpstr, count);
		    return count;
                }

            return -1;
        }

	public static  void onCall(InterceptionPoint interception){
		isTrapActive = false;
		updateTPHistory(interception);
		System.out.println("After UpdateTPHistory");
		findRacingTP (interception);
		System.out.println("After FindRacingTP");
		removeDependentInterceptionPoints(interception);
//		System.out.println("After RemoveDependentInterception");
		boolean  bugFound = checkForTrap (interception);
		//int op_id= operation.getOpID();
		if(!bugFound && shouldDelay(interception))
		{
			setTrap(interception);
			delay();
			clearTrap(interception);
			//printHashMap();
		}
	}
}
