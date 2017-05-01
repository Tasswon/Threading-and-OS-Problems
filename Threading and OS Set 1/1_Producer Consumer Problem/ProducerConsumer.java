/* Name: Joseph Tassone
 * Date: October 20, 2016
 * Description: Starts the producer and consumer threads 
 */

public class ProducerConsumer {
	public static void main(String[] args) throws InterruptedException {
		
		Threading test = new Threading();
		while(true) {
			
			//Create producer thread and run it once initiated
			Thread t1 = new Thread(new Runnable() {
				public void run() {
					try {
						test.producer();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			
			//Create consumer thread and run it once initiated
			Thread t2 = new Thread(new Runnable() {
				public void run() {
					try {
						test.consumer();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			
			//Initiate the threads and have them sleep after each iteration
			t1.start();
			t1.sleep(3000);
			t2.start();
			t2.sleep(3000);
		}
	}
}
