package other;

public class Main {
    
    static int numBooks = 0;
    public static void main(String[] args) {
	System.out.println("FROM MAIN ******************");
        Calculator calculator = new Calculator(3, 4);
	calculator.add();	
	calculator.multiply();
    }

}
