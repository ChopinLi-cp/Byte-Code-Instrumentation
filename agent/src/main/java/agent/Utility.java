package agent;

import java.util.*;
import java.util.Iterator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
public class Utility{
	public static String callPrint(int x, int y){
		System.out.println("HELLO FROM Utility Instrumentation");
		return "";

	}

	public static String testIntrumentation(List m, Object obj){
		m.add(obj);
		int size = m.size();
		System.out.println("SIZE = "+size);
		return "";
	}
}
