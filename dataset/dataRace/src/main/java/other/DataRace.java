package dataRace;

import java.util.*;
import java.util.Collections;


public class DataRace {
	private	static ArrayList list=new ArrayList();
	// static List<String> countries = new ArrayList<String>();

	public static void Request1(){
		System.out.println("Request1 Start");
		for(int i=0;i<5;i++){
			list.add("Request 1");
			// Collections.sort(list);
			System.out.println("Request1 End");
		
			list.contains("Request1 Start");
		}
	}


	public static void Request2(){
		System.out.println("Request2 Start");
		
		for(int i=0;i<5;i++){
			list.add("Request 2");
	//		Collections.sort(list);			
			System.out.println("Request2 End");
		}
	}

}
