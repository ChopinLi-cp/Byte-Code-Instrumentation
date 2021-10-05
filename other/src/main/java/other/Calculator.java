package other;

public class Calculator {
    int sum, mul;
    int firstDigit, secondDigit;
    public Calculator(int firstDigit, int secondDigit){
    	this.firstDigit=firstDigit;
    	this.secondDigit= secondDigit;
    }
    
    public void  add() {
	sum = firstDigit+secondDigit;
	System.out.println("SUM = " +sum);
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
