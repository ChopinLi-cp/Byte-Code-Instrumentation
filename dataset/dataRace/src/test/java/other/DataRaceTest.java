package dataRace;

import org.junit.Test;

public class DataRaceTest {
    static void testMethod(final DataRace datarace){

        // countries.add("ABC 122");
        // Collections.sort(countries);
        System.out.println("Hello ");
        Thread t1 = new Thread()
        {
            public void run(){
                datarace.Request1();
            }
        };
        t1.start();

        Thread t2 = new Thread(){
            public	void run(){
                datarace.Request2();
            }
        };
        t2.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            System.out.println("Exception " );
            e.printStackTrace();
        }

        try {
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Exception " );
            e.printStackTrace();
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
