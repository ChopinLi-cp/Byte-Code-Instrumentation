package dataRace;

import org.junit.Test;

public class DataRaceTest {
	
static	boolean buggy;
    static void testMethod(final DataRace datarace){
	buggy = false;
        System.out.println("Hello ");
	for(int i = 0; i<=2 ;i++){
		Thread t1 = new Thread()
		{
		    public void run(){
		      String[] rt =  datarace.Request3();
		      for (String s : rt) {
			if (s == null) {
				buggy = true;
				throw new NullPointerException();
				}
			 }
		    }
		};
        	t1.start();
		try {
            		t1.join();
        	} catch (InterruptedException e) {
            		System.out.println("Exception " );
            		e.printStackTrace();
        	}
	}

	for(int i = 0; i<=2 ;i++){
		Thread t2 = new Thread(){
		    public void run(){
			String[] rt = datarace.Request4();
			for (String s : rt) {
			    if (s == null){
				    buggy = true;			
				    throw new NullPointerException();   
			    }
			}
		    }
		};
		t2.start();
	
		try {
		    t2.join();
		} catch (InterruptedException e) {
		    System.out.println("Exception " );
		    e.printStackTrace();
		}
	}
        // list.contains("Request1 Start");
    }

    @Test
    public void test1(){
        // StopWatch stopWatch = new StopWatch();
        // stopWatch.start();
        DataRace dtRace = new DataRace();
        long startTime = System.currentTimeMillis();
        testMethod(dtRace);
        // stopWatch.stop();
        long estimatedTime = System.nanoTime() - startTime;
        System.out.println("Elaspsed Time (ms) : " + estimatedTime);
    }
}
