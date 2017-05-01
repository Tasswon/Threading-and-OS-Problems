/**Name: Joseph Tassone
 * Description: Simulates a process scheduling algorithm using FCFS, SJF, non-preemptive
 * priority, round robin.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class QuestionOne {
	public static void main(String [] args) throws FileNotFoundException { 
		//Scanner used for inputting the name of the text file
		Scanner input1 = new Scanner(System.in);
		
		//Sets the name (location) of the text file to be processed
		System.out.print("Enter the location of the file: ");
		String location = input1.next();
		
		//The following is used to take in the file and set the processes
		
		
		File file = new File(location);
		Scanner input = new Scanner(file);
		
		//Initial array list that takes in the full file details
		ArrayList<String> starter = new ArrayList();
		String value = "";
		
		//Loops until the end of the file
		while(input.hasNext()) {
			value = input.nextLine();
			starter.add(value);
		}
		input.close();
		
		//Temporary array lists used to store process details for their creation
		String [] processId = ((String) starter.get(0)).split(" ");
		ArrayList burstTime = new ArrayList();
		ArrayList priority = new ArrayList();
		
		//Loop progresses and adds the process details from the starter to their respective lists
		for(int i = 1; i <= processId.length; i++) {
			String temp1 = starter.get(i).substring(0, (starter.get(i).indexOf(1)) == (char)32 ? 1 : 2);
			burstTime.add((Double.parseDouble(temp1)));
			
			String temp2 = ((String) starter.get(i)).substring(2);
			priority.add((Double.parseDouble(temp2)));
		}

		//Everything below this point is the actual OS assignment
		
		//The following array lists are used by each respective scheduling algorithms
		ArrayList fcfsArray = new ArrayList();
		ArrayList shfArray = new ArrayList();
		ArrayList priorityArray = new ArrayList();
		ArrayList rrArray = new ArrayList();
		
		//For loop fills the array lists with the newly created processes to be tested below
		for(int j = 0; j < processId.length; j++) {
			fcfsArray.add(new Process(processId[j], (double)burstTime.get(j), (double)priority.get(j)));
			shfArray.add(new Process(processId[j], (double)burstTime.get(j), (double)priority.get(j)));
			priorityArray.add(new Process(processId[j], (double)burstTime.get(j), (double)priority.get(j)));
			rrArray.add(new Process(processId[j], (double)burstTime.get(j), (double)priority.get(j)));
		}
		
		//Method calls for each scheduling algorithm (sets average waiting times to variables)
		System.out.println("--------------------------------------------------------");
		double waitingOne = FCFS(fcfsArray);
		System.out.println("--------------------------------------------------------");
		double waitingTwo = SJF(shfArray);
		System.out.println("--------------------------------------------------------");
		double waitingThree = Priority(priorityArray);
		System.out.println("--------------------------------------------------------");
		double waitingFour = RoundRobin(rrArray);
		System.out.println("--------------------------------------------------------");
		
		//Separates the scheduling algorithm with the smallest average waiting time
		double [] check = {waitingOne, waitingTwo, waitingThree, waitingFour};
		Arrays.sort(check);
		
		double min = check[0];
		
		//Prints out which scheduling algorithm had the smallest average waiting time
		if(waitingOne == min) {
			System.out.println("\nThe process with the minimum waiting time is FCFS with " + waitingOne);
		}
		else if(waitingTwo == min) {
			System.out.println("\nThe process with the minimum waiting time is SJF with " + waitingTwo);
		}
		else if(waitingThree == min) {
			System.out.println("\nThe process with the minimum waiting time is Priority with " + waitingThree);
		}
		else {
			System.out.println("\nThe process with the minimum waiting time is Round Robin with " + waitingFour);
		}
	}
	
	//Method used to simulate a FCFS scheduling algorithm
	public static double FCFS(final ArrayList testCase) {
		ArrayList<Process> actualWork = testCase;
		
		int counter = 0; //Simulated timer for the algorithm
		int tracker = 0; //Used to set which process performs work
		boolean flag = true;
		double averageWaitingTime = 0;
		
		System.out.println("FCFS:");
		System.out.print(counter + " - P" + actualWork.get(tracker).getProcessId());
		
		//loop continues until the flag is false (once all burst times are 0)
		while(flag != false) {
			//If the process' burst time isn't zero reduce by one, and increment counter
			if(actualWork.get(tracker).getBurstTime() != 0) {
				actualWork.get(tracker).setBurstTime();
				counter++;
			}
			//If the process' burst time is zero go to next process; set turnaround and waiting times
			else {
				actualWork.get(tracker).setTurnAroundTime(counter);
				tracker++;
				actualWork.get(tracker).setWaitingTime(counter);
				System.out.print(" - " + counter + " - P" + actualWork.get(tracker).getProcessId());
			}
			
			//Checks whether all processes have completed 
			flag = (timeTest(actualWork) == actualWork.size() ? flag = false : (flag = true));
		}
		
		//Set the last process' turnaround time
		actualWork.get(tracker).setTurnAroundTime(counter);
		System.out.print(" - " + counter + "\n");
		
		//Print out each process' turnaround and waiting times
		for(int j = 0; j < actualWork.size(); j++) {
			System.out.println("P" + (j+1) + ": " + "\t" + "Waiting Time: " + actualWork.get(j).getWaitingTime() 
					+ "\t" + "TurnAround Time: " + actualWork.get(j).getTurnAroundTime());
			averageWaitingTime += actualWork.get(j).getWaitingTime();
		}
		
		//Return the average waiting time for the algorithm
		return averageWaitingTime/actualWork.size();
	}
	
	//Method used to simulate a SJF scheduling algorithm
	public static double SJF(final ArrayList testCase) {
		ArrayList<Process> actualWork = testCase;
		
		double min = actualWork.get(0).getBurstTime(); //Used to determine process with the smallest burst time
		int counter = 0; //Simulated timer for the algorithm
		int tracker = 0; //Used to set which process performs work
		boolean flag = true;
		double averageWaitingTime = 0;
		
		//Determines first shortest job and sets tracker based on this
		for(int i = 0; i < actualWork.size(); i++) {
			if (min > actualWork.get(i).getBurstTime()){
				min = actualWork.get(i).getBurstTime();
				tracker = i;
			}
		}
		
		System.out.println("SJF:");
		System.out.print(counter + " - P" + actualWork.get(tracker).getProcessId());
		
		//loop continues until the flag is false (once all burst times are 0)
		while(flag != false) {
			
			//If the process' burst time is zero, look for next shortest job
			//Sets the turn around time
			if(actualWork.get(tracker).getBurstTime() == 0) {
				actualWork.get(tracker).setTurnAroundTime(counter);
				min = Integer.MAX_VALUE;
				for(int i = 0; i < actualWork.size(); i++) {
					
					//Skips the process' that have already completed
					if(actualWork.get(i).getBurstTime() == 0) {
						continue;
					}
					
					//Compares the jobs and determine the next minimum, and sets tracker based on this
					if (min > actualWork.get(i).getBurstTime()){
						tracker = i;
						min = actualWork.get(i).getBurstTime();
					}
				}
				//Sets the waiting time
				actualWork.get(tracker).setWaitingTime(counter);
				System.out.print(" - " + counter + " - P" + actualWork.get(tracker).getProcessId());
			}
			
			//If the job hasn't completed then reduce the burst time and increment the counter
			actualWork.get(tracker).setBurstTime();
			counter++;
			
			//Checks whether all processes have completed 
			flag = (timeTest(actualWork) == actualWork.size() ? flag = false : (flag = true));
		}
		
		//Set the last process' turnaround time
		actualWork.get(tracker).setTurnAroundTime(counter);
		System.out.print(" - " + counter + "\n");
		
		//Print out each process' turnaround and waiting times
		for(int j = 0; j < actualWork.size(); j++) {
			System.out.println("P" + (j+1) + ": " + "\t" + "Waiting Time: " + actualWork.get(j).getWaitingTime() 
					+ "\t" + "TurnAround Time: " + actualWork.get(j).getTurnAroundTime());
			averageWaitingTime += actualWork.get(j).getWaitingTime();
		}
		
		//Return the average waiting time for the algorithm
		return averageWaitingTime/actualWork.size();
	}
	
	//Method used to simulate a Priority scheduling algorithm
	public static double Priority(final ArrayList testCase) {
		ArrayList<Process> actualWork = testCase;
		
		double max = actualWork.get(0).getPriority(); //Used to determine process with the highest priority
		int counter = 0; //Simulated timer for the algorithm
		int tracker = 0; //Used to set which process performs work
		boolean flag = true;
		double averageWaitingTime = 0;
		
		//Determines first highest priority job and sets tracker based on this
		for(int i = 0; i < actualWork.size(); i++) {
			if (max < actualWork.get(i).getPriority()){
				max = actualWork.get(i).getPriority();
				tracker = i;
			}
		}
		
		System.out.println("Priority:");
		System.out.print(counter + " - P" + actualWork.get(tracker).getProcessId());
		
		//loop continues until the flag is false (once all burst times are 0)
		while(flag != false) {
			
			//If the process' burst time is zero, look for next highest priority
			//Sets the turn around time
			if(actualWork.get(tracker).getBurstTime() == 0) {
				actualWork.get(tracker).setTurnAroundTime(counter);
				max = Integer.MIN_VALUE;
				
				//Determines the next process to run
				for(int i = 0; i < actualWork.size(); i++) {
					
					//Skips the process' that have already completed
					if(actualWork.get(i).getBurstTime() == 0) {
						continue;
					}
				
					//Priority ties are broken based on the one with the lower burst time
					if(max == actualWork.get(i).getPriority()) {
						if(actualWork.get(tracker).getBurstTime() > actualWork.get(i).getBurstTime()) {
							tracker = i;
							max = actualWork.get(i).getPriority();
						}
					}
					
					//Sets the new max, and changes the tracker based on this
					else if(max < actualWork.get(i).getPriority()){
						tracker = i;
						max = actualWork.get(i).getPriority();
					}
				}
				
				//Sets the waiting time
				actualWork.get(tracker).setWaitingTime(counter);
				System.out.print(" - " + counter + " - P" + actualWork.get(tracker).getProcessId());
			}
			
			//If the job hasn't completed then reduce the burst time and increment the counter
			actualWork.get(tracker).setBurstTime();
			counter++;
			
			//Checks whether all processes have completed 
			flag = (timeTest(actualWork) == actualWork.size() ? flag = false : (flag = true));
		}
		
		//Set the last process' turnaround time
		actualWork.get(tracker).setTurnAroundTime(counter);
		System.out.print(" - " + counter + "\n");
		
		//Print out each process' turnaround and waiting times
		for(int j = 0; j < actualWork.size(); j++) {
			System.out.println("P" + (j+1) + ": " + "\t" + "Waiting Time: " + actualWork.get(j).getWaitingTime() 
					+ "\t" + "TurnAround Time: " + actualWork.get(j).getTurnAroundTime());
			averageWaitingTime += actualWork.get(j).getWaitingTime();
		}
		
		//Return the average waiting time for the algorithm
		return averageWaitingTime/actualWork.size();
	}
	
	//Method used to simulate a Round Robin scheduling algorithm
	public static double RoundRobin(final ArrayList<Process> testCase) {
		
		//Utilizes a queue to perform the actual round robin work
		Queue<Process> actualWork = new LinkedList<Process>();
		
		//Adds each process to the scheduling queue
		for(int j = 0; j < testCase.size(); j++) {
			actualWork.add(testCase.get(j));
		}
		
		//Stores the completed processes (once removed from the queue)
		ArrayList<Process> finalCase = new ArrayList();
		
		int timeQuantum = 2; //How long a process is allowed to perform oer cycle
		int counter = 0; //Simulated timer for the algorithm
		boolean flag = true;
		double averageWaitingTime = 0;
		
		System.out.println("Round Robin:");
		System.out.print(counter + " - P" + actualWork.peek().getProcessId());
		
		//Continues until the queue is empty
		while(flag != false) {
			
			//Performs work for the length of the time quantum
			for(int i = 0; i < timeQuantum; i++) {
				
				//If the burst time becomes zero then the loop is broken (process is done)
				if(actualWork.peek().getBurstTime() == 0) {
					break;
				}
				
				//The process reduces it's burst time and the counter is incremented
				else {
					actualWork.peek().setBurstTime();
					counter++;
				}
			}
			
			//If the process isn't completed then move it to the back of the queue
			if(actualWork.peek().getBurstTime() != 0) {
				Process temp = actualWork.remove();
				actualWork.add(temp);
			}
			
			//Remove it and set the turn around/waiting times, and add it to the final case array list
			else {
				actualWork.peek().setTurnAroundTime(counter);
				actualWork.peek().setWaitingTime(counter - actualWork.peek().getStaticBurstTime());
				finalCase.add(actualWork.remove());
			}
			
			if(!(actualWork.isEmpty())) {
				System.out.print(" - " + counter + " - P" + actualWork.peek().getProcessId());
			}
			
			//Set the flag if the queue is empty
			flag = (actualWork.isEmpty() ? flag = false : (flag = true));
		}
		
		//Print out each process' turnaround and waiting times
		System.out.print(" - " + counter + "\n");
		for(int j = 0; j < finalCase.size(); j++) {
			System.out.println("P" + finalCase.get(j).getProcessId() + ": " + "\t" + "Waiting Time: " + finalCase.get(j).getWaitingTime() 
					+ "\t" + "TurnAround Time: " + finalCase.get(j).getTurnAroundTime());
			averageWaitingTime += finalCase.get(j).getWaitingTime();
		}	
		
		//Return the average waiting time for the algorithm
		return averageWaitingTime/finalCase.size();
	}
	
	//Method used to check if all the burst times have become zero (exception is round robin)
	public static int timeTest(final ArrayList<Process> check) {
		int timeTest = 0;
		for(int i = 0; i < check.size(); i++) {
			if(check.get(i).getBurstTime() == 0) {
				timeTest++;
			}
		}
		return timeTest;
	}
}
