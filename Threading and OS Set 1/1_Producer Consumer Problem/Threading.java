/* Name: Joseph Tassone
 * Date: October 20, 2016
 * Description: Defines a thread, the buffer, and the producer/consumer methods
 */

import java.util.LinkedList;

public class Threading {
	
	//Buffer is represented as a linked list
	private LinkedList<Integer> list = new LinkedList<Integer>();
	private final int LIMIT = 10;
	//Provides lock for the synchronized block
	private Object lock = new Object();

	//Method fills the buffer until it's full
	public void producer () throws InterruptedException {
		int value = 0;
		//Producer acquires the intrinsic lock for the method's object and releases it when it returns
		synchronized(lock) {
			while(list.size() != LIMIT) {
				list.add(value);
				System.out.println("Bread Amount: " + list.size());
			}
			System.out.println("The baker has a full stock!");
			lock.wait();
			lock.notify();
		}

	}
	
	//Method empties the buffer until it's empty
	public void consumer() throws InterruptedException {
		
		//Comsumer acquires the intrinsic lock for the method's object and releases it when it returns
		synchronized(lock) {
			while(!list.isEmpty()){
				list.removeFirst();
				System.out.println("Bread Amount: " + list.size()); 
			}
			System.out.println("The baker is out of bread!");
			lock.wait();
			lock.notify();
		}

	}
}
