/**Name: Joseph Tassone
 * Course: COSC3407
 * Description: The class creates a simulated process which holds a priority,
 * burstTime, and id.
 */

public class Process{
	
	private double priority;
	private double burstTime;
	private String processId;
	private double turnAroundTime = 0;
	private double waitTime = 0;
	//Burst time constantly lowers, so staticBurstTime is kept as a reference
	private final double staticBurstTime;
	//Tie flag is set in the event two processes have the same priority
	private boolean tie = false;
	
	//Constructor initializes process with id, burstTime, and priority
	public Process(String processId, double burstTime, double priority) {
		this.processId = processId;
		this.burstTime = burstTime;
		this.priority = priority;
		this.staticBurstTime = burstTime;
	}
	
	//Returns whether it is a tied with another process 
	public boolean isTie() {
		return tie;
	}
	
	//Sets the tie flag
	public void setTie(boolean tie) {
		this.tie = tie;
	}
	
	//Returns StaticBurstTime
	public double getStaticBurstTime() {
		return staticBurstTime;
	}
	
	//Returns turn around time
	public double getTurnAroundTime() {
		return turnAroundTime;
	}
	
	//Sets the turnaround time
	public void setTurnAroundTime(double turnAroundTime) {
		this.turnAroundTime = turnAroundTime;
	}
	
	//Returns waiting time
	public double getWaitingTime() {
		return waitTime;
	}
	
	//Sets the waiting time
	public void setWaitingTime(double waitingTime) {
		this.waitTime = waitingTime;
	}
	
	//Returns priority
	public double getPriority() {
		return priority;
	}
	
	//Returns non static burst time
	public double getBurstTime() {
		return burstTime;
	}
	
	//Sets the burst time
	public void setBurstTime() {
		this.burstTime = burstTime - 1;
	}
	
	//returns the id of the process
	public String getProcessId() {
		return processId;
	}
	
	//Sets the printing style for the process
	public String toString() {
		return processId + ", " + burstTime + ", " + priority + ", " + turnAroundTime + ", " + waitTime; 
	}
}
