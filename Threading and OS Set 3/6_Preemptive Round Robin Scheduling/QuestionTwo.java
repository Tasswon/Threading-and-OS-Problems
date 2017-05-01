/**Name: Joseph Tassone
 * Description: Simulates a process scheduling algorithm using preemptive, round robin. A file
 * is taken in with the information for the simulation.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class QuestionTwo {
	public static void main(String[] args) throws FileNotFoundException {
		
		//Scanner used for inputting the name of the text file
		Scanner input1 = new Scanner(System.in);
		
		//Sets the name (location) of the text file to be processed
		System.out.print("Enter the location of the file: ");
		String location = input1.next();
		
		//The following is used to take in the file and set the processes
		
		File file = new File(location);
		Scanner input = new Scanner(file);
		
		int processNum = 0;
		int processCount = input.nextInt(); //Sets the number of processes
		Process [] pArray = new Process[processCount]; //array which holds the process objects
		int [] arrivalTime = new int[processCount]; //array which holds the process arrival times
		
		input.nextLine();
		
		//Determines the priority, burst time, arrival times; and adds them to the respective array/list
		while(input.hasNext()) {
			int tempPriority = input.nextInt();
			int tempBurst = input.nextInt();
			int tempArrival = input.nextInt();
			
			arrivalTime[processNum] = tempArrival;
			pArray[processNum] = (new Process(Integer.toString(++processNum), tempBurst, tempPriority));
		}
		input.close();
		
		//Everything below this point is the actual OS assignment
		
		int timeQuantum = 10; //Amount of time a process is allowed to perform in the case of ties
		double cpuRate = 0; 
		int processNumber = pArray.length; //
		int counter = 0; //Used to time the processes
		
		//A priority queue is used to simulate the algorithm (comparator set to compare priorities)
		PriorityQueue<Process> pqueue = new PriorityQueue<Process>(6, new Comparator<Process>() {
	        public int compare(Process p1, Process p2) {
	            return (p2.getPriority() > p1.getPriority() ? 1 : -1);
	        }
	    });
		
		//Holds the processes once they've completed their work
		ArrayList<Process> container = new ArrayList();
		
		//Extra process which performs when the queue is empty
		Process pIdle = new Process("Idle", Integer.MAX_VALUE, 0);
		
		//Adds pIdle and the first process to the queue
		pqueue.add(pIdle);
		pqueue.add(pArray[0]);
		
		System.out.println("Preemptive Round Robin Scheduling Algorithm:");
		System.out.print(counter + " - P" + pqueue.peek().getProcessId());
		
		//Continues while the container array list isn't filled with all the completed processes
		while(container.size() != pArray.length) {
			
			//Process continues for it's arrival time or until interrupted
			for(int i = 1; i < arrivalTime.length; i++) {
				
				//Checks if the burst time is zero; and if it is sets the turnaround/waiting times, resets time quantum, and adds old process to container
				if(pqueue.peek().getBurstTime() == 0) {
					pqueue.peek().setTurnAroundTime(counter - arrivalTime[Integer.parseInt(pqueue.peek().getProcessId()) - 1]);
					pqueue.peek().setWaitingTime(pqueue.peek().getTurnAroundTime()
								- pqueue.peek().getStaticBurstTime());
					container.add(pqueue.poll());
					timeQuantum = 10;
					System.out.print(" - " + counter + " - P" + pqueue.peek().getProcessId());
				}
				
				//Checks if two processes share the same priority and if they do their tie flags are set
				if(counter == arrivalTime[i] && pqueue.peek().getPriority() == pArray[i].getPriority()) {
					pqueue.peek().setTie(true);
					pArray[i].setTie(true);
					
					//Sets proper positioning of tied processes so each gets a turn, and time quantum can be considered
					Process tempSpecial = pqueue.poll();
					pqueue.add(pArray[i]);
					pqueue.add(tempSpecial);
				}
				
				//If the counter equals a process' arrival time, add it to the queue to perform work
				else if(counter == arrivalTime[i]) {
					
					//Sets the proper ordering if there is a tie
					if(pArray[i].getPriority() > pqueue.peek().getPriority() && pqueue.peek().isTie() == true) {
						Process temp1 = pqueue.poll();
						Process temp2 = pqueue.poll();
						pqueue.add(temp1);
						pqueue.add(temp2);
					}
					
					//Whether or not there is a tie, a process is added to the queue and time quantum is reset
					pqueue.add(pArray[i]);
					timeQuantum = 10;
					System.out.print(" - " + counter + " - P" + pqueue.peek().getProcessId());
				}
			}
			
			//If the time quantum is 0 (or less) and there's a tie then reorder the processes, so the next one can perform
			if(timeQuantum <= 0 && pqueue.peek().isTie() == true) {
				timeQuantum = 9;
				Process temp1 = pqueue.poll();
				Process temp2 = pqueue.poll();
				pqueue.add(temp1);
				pqueue.add(temp2);
				System.out.print(" - " + counter + " - P" + pqueue.peek().getProcessId());
				pqueue.peek().setBurstTime();
			}
			
			//If the burst time isn't zero then reduce by one and lower the time quantum (regardless of ties)
			else if(pqueue.peek().getBurstTime() != 0) {
				pqueue.peek().setBurstTime();
				timeQuantum--;
			}
			
			//increment the counter
			counter++;
		}
		
		System.out.println();
		double totalCPUUtilization = 0;
		
		//Print out all the process turnaround and waiting times
		for(int j = 0; j < container.size(); j++) {
			System.out.println("P" + container.get(j).getProcessId() + ": " + "\t "+ "TurnAround Time: " 
						+ container.get(j).getTurnAroundTime() + "\t "+ "Waiting Time: " + container.get(j).getWaitingTime());
			totalCPUUtilization =  totalCPUUtilization + (container.get(j).getTurnAroundTime() - container.get(j).getWaitingTime());
		}
		
		//Print out the cpu utlization rate (as a percentage)
		System.out.println("The CPU Utlization Rate is: " + (totalCPUUtilization/(counter - 1) * 100) + "%");
	}
}

