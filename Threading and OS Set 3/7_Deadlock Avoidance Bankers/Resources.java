/**Name: Joseph Tassone
 * Description: The class creates a simulated process which multiple resource scenario
 * consideration.
 * matrix).
 */

public class Resources {
	//Resources a through d 
	private int a;
	private int b;
	private int c;
	private int d;
	private int affectedProcess;
	private int processNum;
	
	//Constructor sets the intial a through d resources
	public Resources(int a, int b, int c, int d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

	public int getD() {
		return d;
	}

	public void setD(int d) {
		this.d = d;
	}
	
	//Returns the affected process (used when requests are made)
	public int getAffectedProcess() {
		return affectedProcess;
	}

	//Sets the affected process (used when requests are made)
	public void setAffectedProcess(int affectedProcess) {
		this.affectedProcess = affectedProcess;
	}
	
	//Returns the process associated with these resources
	public int getProcessNum() {
		return processNum;
	}
	
	//Sets the process associated with these resources
	public void setProcessNum(int processNum) {
		this.processNum = processNum;
	}
	
	//Set printing style
	public String toString() {
		return "A: " + a + "\tB: " + b + "\tC: " + c + "\tD: " + d;
	}
}
