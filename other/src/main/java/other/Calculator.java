package other;

public class Calculator {
    int sum, mul;
    int firstDigit, secondDigit;
    public Calculator(int firstDigit, int secondDigit){
    	this.firstDigit=firstDigit;
    	this.secondDigit= secondDigit;
    }
    
    public int  add() {
	sum = firstDigit+secondDigit;
	System.out.println("SUM = " +sum);
	return sum;
    } 
   public int  multiply() {
	mul = firstDigit*secondDigit;
	System.out.println("Multiply = " +mul);
	return mul;
    }
    
}
