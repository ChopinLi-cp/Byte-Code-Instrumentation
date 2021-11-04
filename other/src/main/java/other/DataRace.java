package other;

import java.util.*;
import java.util.Collections;


public  class DataRace{
	private	static 	ArrayList list=new ArrayList();  
//	static List<String> countries = new ArrayList<String>();
	static void test(){
		
//	 	countries.add("ABC 122");
//		Collections.sort(countries);
		System.out.println("Hello ");
		Thread t1 = new Thread()
		{
			public void run(){
				Request1();
			}
		};
		t1.start();
		
		Thread t2 = new Thread(){
		public	void run(){
				Request2();
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
//		list.contains("Request1 Start");
	}

	static void Request1(){
		System.out.println("Request1 Start");
		for(int i=0;i<5;i++){
			list.add("Request 1");
	//		Collections.sort(list);
			System.out.println("Request1 End");
		
			list.contains("Request1 Start");
		}
	}


	static void Request2(){
		System.out.println("Request2 Start");
		
		for(int i=0;i<5;i++){
			list.add("Request 2");
	//		Collections.sort(list);			
			System.out.println("Request2 End");
		}
	}

}
