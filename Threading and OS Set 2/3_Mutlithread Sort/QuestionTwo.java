/**Name: Joseph Tassone
 * Description: User enters the number of Fibonacci numbers to generate, and the program 
 * uses one thread to generate them, and the other to print them.
 */

import java.util.ArrayList;
import java.util.Scanner;

public class QuestionTwo {
	public static void main(String[] args) throws InterruptedException {
		Scanner input = new Scanner(System.in);
		
		Threading test = new Threading();
		
		System.out.print("Enter the number of fibonacci numbers to print: ");
		int number = input.nextInt();
		
		//Thread one generates the fibonacci numbers depending on the user input
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				try {
					test.fib(number);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		//Thread two prints out the fibonacci numbers that have been generated
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				try {
					test.print();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		//Thread one starts and the second waits for the first to complete
		t1.start();
		t1.join();
		t2.start();
	}
	
	//Thread class contains the methods which perform the work (generate and print)
	static class Threading {
		
		private ArrayList<Integer> list = new ArrayList<Integer>();

		public void fib (int n) throws InterruptedException {
			for(int i = 0; i < n; i++) {
				list.add(fibonacci(i));
			}
		}

		private int fibonacci(int n) {
			if (n <= 1){
				return n;
			}
			else {
				return fibonacci(n-1) + fibonacci(n-2);
			}
		}

		public void print() throws InterruptedException {
			for(int i = 0; i < list.size(); i++) {
				System.out.println("fib(" + i + ")= " + list.get(i));
			}
		}
	}
}