/**Name: Joseph Tassone
 * Description: Simulates deadlock avoidance using banker's algorithm. Takes in information 
 * (resource allocation) from a file, and uses it to determine if a system is in a safe state. 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class QuestionThree {
	public static void main(String[] args) throws FileNotFoundException {
		
		//Scanner used for inputting the name of the text file
		Scanner input1 = new Scanner(System.in);
		
		//Sets the name (location) of the text file to be processed
		System.out.print("Enter the location of the file: ");
		String location = input1.next();
		
		//The following is used to take in the file and set the processes
		
		File file = new File(location);
		Scanner input = new Scanner(file);
		
		//Array lists represent the allocation, max, available, and need matrices 
		ArrayList<Resources> allocation = new ArrayList();
		ArrayList<Resources> max = new ArrayList();
		ArrayList<Resources> available = new ArrayList();
		ArrayList<Resources> need = new ArrayList();
		
		//Hold process requests which will modify the other matrices
		ArrayList<Resources> requests = new ArrayList();
		int counter = 1;
		
		//Sets the A, B, C, and D resources; and puts them into a resource object in side the available matrix
		int tempA = input.nextInt();
		int tempB = input.nextInt();
		int tempC = input.nextInt();
		int tempD = input.nextInt();
		available.add(new Resources(tempA, tempB, tempC, tempD));
		
		input.nextLine();
		
		//Loop continues while the end of the file hasn't been reached and adds resources to the allocation and max matrices
		while(input.hasNext()) {
			tempA = input.nextInt();
			tempB = input.nextInt();
			tempC = input.nextInt();
			tempD = input.nextInt();
			allocation.add(new Resources(tempA, tempB, tempC, tempD));
			
			tempA = input.nextInt();
			tempB = input.nextInt();
			tempC = input.nextInt();
			tempD = input.nextInt();
			max.add(new Resources(tempA, tempB, tempC, tempD));
			
			//If the processes have been reached then the loop is broken
			if(counter == 5) {
				input.nextLine();
				break;
			}
			counter++;
		}
		
		//Adds process requests from the file to the request matrix
		while(input.hasNext()) {
			int tempProcess = input.nextInt();
			tempA = input.nextInt();
			tempB = input.nextInt();
			tempC = input.nextInt();
			tempD = input.nextInt();
			requests.add(new Resources(tempA, tempB, tempC, tempD));
			requests.get(requests.size()-1).setAffectedProcess(tempProcess);
		}
		input.close();
		
		//Uses the max and allocation matrices, to set the values of the need matrix
		System.out.println("The Need matrix: ");
		for(int i = 0; i < 5; i++) {
			need.add(new Resources((max.get(i).getA()- allocation.get(i).getA()), (max.get(i).getB()- allocation.get(i).getB()), 
				(max.get(i).getC()- allocation.get(i).getC()), (max.get(i).getD()- allocation.get(i).getD())));
			System.out.println(need.get(i).toString());
			need.get(i).setProcessNum(i);
		}
		
		System.out.print("----------------------------------------------");
		System.out.println("\nChecking for Initial Safe State: ");
		
		//Checks and prints whether a system is intitially in a safe state
		if(safeState(allocation, available, need) == false) {
			System.out.println("The system isn't in a safe state!");
		}
		else {
			System.out.println("The system is in a safe state!");
		}
		
		//Loops through all the requests and checks if it stays in a safe state 
		for(int j = 0; j < requests.size(); j++) {
			System.out.println("----------------------------------------------");
			System.out.println("Request Made: " + requests.get(j).toString());
			if(requestMade(allocation, available, need, max, requests.get(j)) == false) {
				System.out.println("No Safe State: the request cannot be granted!");
			}
			else {
				System.out.println("Safe State: the request can be granted!");
			}
		}
	}
	
	//Checks whether a system is in a safe state (returns true or false)
	public static boolean safeState(ArrayList<Resources> allocation, ArrayList<Resources> available, ArrayList<Resources> need) {
		
		//Makes a copy of the matrices to ensure no data change to the originals
		ArrayList<Resources> tempAllocation = new ArrayList(allocation);
		ArrayList<Resources> tempAvailable =  new ArrayList(available);
		ArrayList<Resources> tempNeed =  new ArrayList(need);	
		boolean flag = true; //Flag defaults to true (value identifies if it's a safe state)
		
		//Used to represent the changing available once a process has performed it's work (returned resources)
		Resources temp = new Resources(available.get(0).getA(), available.get(0).getB(), available.get(0).getC(), available.get(0).getD());
		
		//Loops for the number of processes
		for(int j = 0; j < 5; j++) {
			
			//Loops for the size of the need matrix
			for(int z = 0; z < need.size(); z++) {
				
				//Checks if the available has enough to meet the need (can't be negative)
				int tA = temp.getA() - tempNeed.get(z).getA();
				int tB = temp.getB() - tempNeed.get(z).getB();
				int tC = temp.getC() - tempNeed.get(z).getC();
				int tD = temp.getD() - tempNeed.get(z).getD();
				
				//If none of the resources are negative the available is updated and flag is set to true
				if(tA >= 0 && tB >= 0 && tC >= 0 && tD >= 0) {
					temp.setA(temp.getA() + tempAllocation.get(z).getA());
					temp.setB(temp.getB() + tempAllocation.get(z).getB());
					temp.setC(temp.getC() + tempAllocation.get(z).getC());
					temp.setD(temp.getD() + tempAllocation.get(z).getD());
					flag = true;
					System.out.println("Process P" + tempNeed.get(z).getProcessNum() + " runs: "+ temp.toString());
					
					//Each time a process complete's it's removed from need and allocation
					tempNeed.remove(z);
					tempAllocation.remove(z);
					break;
				}
				
				//If the available wasn't able to match the needs then the flag is set to false
				else {
					flag = false;
				}
			}
		}
		return flag;
	}
	
	//Method used by requests to check if it's in a safe state (returns true or false)
	public static boolean requestMade(ArrayList<Resources> allocation, ArrayList<Resources> available, ArrayList<Resources> need, 
									ArrayList<Resources> max, Resources request) {
		
		//Makes a copy of the matrices to ensure no data change to the originals
		ArrayList<Resources> tempAllocation =  new ArrayList(allocation);
		ArrayList<Resources> tempAvailable =  new ArrayList(available);
		ArrayList<Resources> tempNeed =  new ArrayList(need);
		ArrayList<Resources> tempMax =  new ArrayList(max);
		
		//Sets which process has made the request
		int processNum = request.getAffectedProcess();
		
		//Modifies the available matrix (for a specific process) based on the request
		tempAvailable.get(0).setA(tempAvailable.get(0).getA() - request.getA());
		tempAvailable.get(0).setB(tempAvailable.get(0).getB() - request.getB());
		tempAvailable.get(0).setC(tempAvailable.get(0).getC() - request.getC());
		tempAvailable.get(0).setD(tempAvailable.get(0).getD() - request.getD());
		
		//Returns false if any of the available resources are put into the negative
		if(tempAvailable.get(0).getA() < 0 || tempAvailable.get(0).getB() < 0 || tempAvailable.get(0).getC() < 0 || tempAvailable.get(0).getD() < 0) {
			return false;
		}
		
		//Modifies the allocation matrix (for a specific process) based on the request
		tempAllocation.get(processNum).setA(tempAllocation.get(processNum).getA() + request.getA());
		tempAllocation.get(processNum).setB(tempAllocation.get(processNum).getB() + request.getB());
		tempAllocation.get(processNum).setC(tempAllocation.get(processNum).getC() + request.getC());
		tempAllocation.get(processNum).setD(tempAllocation.get(processNum).getD() + request.getD());
		
		//Modifies the need matrix (for a specific process) based on the request
		tempNeed.get(processNum).setA(tempMax.get(processNum).getA()- tempAllocation.get(processNum).getA());
		tempNeed.get(processNum).setB(tempMax.get(processNum).getB()- tempAllocation.get(processNum).getB());
		tempNeed.get(processNum).setC(tempMax.get(processNum).getC()- tempAllocation.get(processNum).getC());
		tempNeed.get(processNum).setD(tempMax.get(processNum).getD()- tempAllocation.get(processNum).getD());
		
		//Returns false if any of the need resources are put into the negative
		if(tempNeed.get(0).getA() < 0 || tempNeed.get(0).getB() < 0 || tempNeed.get(0).getC() < 0 || tempNeed.get(0).getD() < 0) {
			return false;
		}
		
		//Calls the safeState method to check whether a process is in a safe state after the updates have been made by the request
		if(safeState(tempAllocation, tempAvailable, tempNeed) == false) {
			return false;
		}
		else {
			return true;
		}
	}
}
