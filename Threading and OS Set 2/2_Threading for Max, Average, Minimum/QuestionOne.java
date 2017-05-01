/**Name: Joseph Tassone
 * Description: The program is passed a series of numbers and uses threading
 * to output the average, maximum value, and minimum value. 
 */

import java.util.Scanner;

public class QuestionOne {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		
		System.out.print("Enter the number of statistical values you'd like to use: ");
		int number = input.nextInt();
		int [] array = new int[number];
		
		System.out.print("Enter your values: ");
		for(int i = 0; i < array.length; i++) {
			int value = input.nextInt();
			array[i] = value;
		}
		
		Runnable average = new Average(array);
		Runnable max = new Max(array);
		Runnable min = new Min(array);

		Thread thread1 = new Thread(average);
		Thread thread2 = new Thread(max);
		Thread thread3 = new Thread(min);
		
		//Threads run in parallel 
		thread1.start();
		thread2.start();
		thread3.start();
	}
}

//Average class takes in an array and calls run to determine the average
class Average implements Runnable { 

	int [] array;
	public Average(int[] array) {
		this.array = array;
	}

	public void run() {
		double total = 0;
		double average = 0;
		for (int i = 0; i < array.length; i++) {
			total += array[i];
		}
		average = total / array.length;
		System.out.printf("%s%.2f\n","The average for the list is: ", average);
	}
}

//Max class takes in an array and calls run to determine the max
class Max implements Runnable { 

	int [] array;
	public Max(int[] array) {
		this.array = array;
	}

	public void run() {
		int max = array[0];
		for (int i = 1; i < array.length; i++) {
			if(max < array[i]) {
				max = array[i];
			}
		}
		System.out.println("The max for the list is: " + max);
	}
}

//Min class takes in an array and calls run to determine the min
class Min implements Runnable { 

	int [] array;
	public Min(int[] array) {
		this.array = array;
	}

	public void run() {
		int min = array[0];
		for (int i = 1; i < array.length; i++) {
			if(min > array[i]) {
				min = array[i];
			}
		}
		System.out.println("The min for the list is: " + min);
	}
}