/**Name: Joseph Tassone
 * Description: A list of integers is split in half and sorted by two separate
 * threads. Another thread then merges and sorts the two lists.
 */

import java.util.Arrays;
import java.util.Scanner;

public class QuestionThree {
	public static void main(String[] args) throws InterruptedException {
		Scanner input = new Scanner(System.in);
		
		System.out.print("Enter the size of the list: ");
		int number = input.nextInt();
		int array [] = new int[number];
		
		System.out.print("Enter the values: ");
		for(int i = 0; i < array.length;i++) {
			array[i] = input.nextInt();
		}
		
		//The user generated list is split into two seperate lists
		int arraySub1 [] = Arrays.copyOfRange(array, 0, array.length / 2);
		int arraySub2 [] = Arrays.copyOfRange(array, (array.length / 2), array.length);
		
		//Half one is sorted 
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				sort(arraySub1);
				System.out.println("Sub List One: " + Arrays.toString(arraySub1));
			}
		});
		
		//Half two is sorted
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				sort(arraySub2);
				System.out.println("Sub List Two: " + Arrays.toString(arraySub2));
			}
		});
		
		//Both halfs are merged and sorted 
		Thread t3 = new Thread(new Runnable() {
			public void run() {
				merge(array, arraySub1, arraySub2);
				System.out.println("\nCompleted List Merged: " + Arrays.toString(array));
				Arrays.sort(array);
				System.out.println("Completed List Sorted: " + Arrays.toString(array));
			}
		});
		
		//Thread one and two start, and thread three waits for them to finish
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		t3.start();
	}
	
	//Method sorts the inputed values 
	public static void sort(int array[]) {
		Arrays.sort(array);
	}
	
	//Method merges the two lists and sorts the final one
	public static void merge(int original[], int array1[], int array2[]) {
		for(int i = 0; i < array1.length; i++) {
			original[i] = array1[i];
		}
		
		int number = 0;
		for(int i = original.length / 2; i < original.length; i++) {
			original[i] = array2[number++];
		}
	}
}