package dataRace;

import java.util.*;
import java.util.Collections;


public class DataRace {
	private	static ArrayList list=new ArrayList();
	private static  String[] rep;
	private static int[] testIntArr;
/*	public static void Request1(){
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
	} */

	public static String[] Request3(){
		System.out.println("Request3 Start");
		ArrayList arr3 = new ArrayList();
		for(int i=0;i<5;i++){
			arr3.add("Request 3");
			System.out.println("Request3 End");
		}
		
		rep = new String[5];
		arr3.toArray(rep);
		testIntArr = new int[2];
		testIntArr[0] =5;
		return rep;
	}

	public static String[] Request4(){
		System.out.println("Request4 Start");
		ArrayList arr4 = new ArrayList();
		for(int i=0;i<5;i++){
			arr4.add("Request 4");
			System.out.println("Request4 End");
		}
		rep = new String[5];
		arr4.toArray(rep);
		return rep;
	}



}
