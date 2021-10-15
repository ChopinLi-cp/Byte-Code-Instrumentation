package other;

public class Main{

public static void main (String[] args){
//              StopWatch stopWatch = new StopWatch();
//              stopWatch.start();
		DataRace dtRace= new DataRace();
                long startTime = System.currentTimeMillis();
                dtRace.test();
        //      stopWatch.stop();       
                long estimatedTime = System.nanoTime() - startTime;
                System.out.println("Elaspsed Time (ms) : " +estimatedTime);

        }


}
