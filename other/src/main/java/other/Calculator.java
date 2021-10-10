package other;

import java.util.*;

public class Calculator {
    int sum, mul;
    int firstDigit, secondDigit;
    public Calculator(int firstDigit, int secondDigit){
    	this.firstDigit=firstDigit;
    	this.secondDigit= secondDigit;
//	List<Integer> l2 = new ArrayList<Integer>();
    }
    
    public void  add() {
	//agent.Utility.callPrint(5);
	sum = firstDigit+secondDigit;
int x=5 ;
	List l2 = new ArrayList();
	l2.add(2);
	l2.add(13);
	l2.add("shanto");
	//l2.add(3);
//	agent.Utility.testIntrumentation(l2, 3);
	System.out.println("SUM = ");
    } 
    public int  multiply() {
	mul = firstDigit*secondDigit;
	System.out.println("Multiply = " +mul);
	return mul;
    }

   public void view1(){
   	String name="SHANTO";
	System.out.println("Hi VIEW1" );
   }
    
}
