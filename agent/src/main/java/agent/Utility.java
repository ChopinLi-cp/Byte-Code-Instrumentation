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

/*if (obj == null) {
//            return null;
System.out.println("null");
        }
else if (obj instanceof Iterable) {
//            return (Iterable<?>) obj;

System.out.println("Iterable");
        }
else if (obj.getClass().isArray()) {

System.out.println("Array");
}
else if (obj instanceof Collection<?>){

System.out.println("Collection");
}

else if (obj instanceof Map<?,?>){

System.out.println("Coll");
}
*/
/*int x= (int) obj;
System.out.println("OBJ = " +x);
System.out.println("OBJECT[0] = " +obj.getClass().getName());*/
//System.out.println("OBJECT[0] = " +obj.length);

return "";
}
}
