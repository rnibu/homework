

import java.util.concurrent.Semaphore;
/**
 * This program simulates the functions of a Post Office that 
 * only allows 10 people inside at a time and has 3 postal 
 * workers to serve those customers. There are total 50
 * customers all together and they are randomly assigned 
 * tasks that they need to carry out each with their own 
 * assigned times
 * @author ryann
 *
 */
class Project2 {

	static final int NUM_CUSTOMERS = 50;
	static final int MAX_CUST = 10;
	static final int NUM_WORKERS = 3;
	static final int NUM_TASKS = 3;
	static final int STAMPS = 0;
	static final int LETTER = 1;
	static final int PACKAGE = 2;
	static Semaphore customersInside = new Semaphore(MAX_CUST);
	static Semaphore postalWorkerSem = new Semaphore(NUM_WORKERS);
	static Semaphore scalesSem = new Semaphore(1);
	static boolean flagger = true;
	static int numCustomersWaiting = 0;
	
	/**
	 * main function thats a driver for the program
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		//Creates customer threads and starts them
		Customer[] customers = new Customer[NUM_CUSTOMERS];
		for (int i = 0; i < NUM_CUSTOMERS; i++) {
			customers[i] = new Customer(i);
			customers[i].start();
		}
		//Creates postal worker threads and starts them
		PostalWorker[] postalWorkers = new PostalWorker[NUM_WORKERS];
		for (int i = 0; i < NUM_WORKERS; i++) {
			postalWorkers[i] = new PostalWorker(i);
			postalWorkers[i].start();
		}
		//signals the while loops in the run functions to stop
		flagger = false;

		/**
		 * Joins all the threads together
		 */
		for (int i = 0; i < NUM_WORKERS; i++) {
			postalWorkers[i].join();
			System.out.println("Joined Post Worker " + i);

		}
		for (int i = 0; i < NUM_CUSTOMERS; i++) {
			customers[i].join();
			System.out.println("Joined Customer " + i);
		}
	}

	/**
	 * Customer thread class that handles the
	 *  customer tasks and assignment
	 *
	 */
	static class Customer extends Thread {
		int id;
		int task;

		
		Customer(int id) {
			this.id = id;
			task = (int) (Math.random() * NUM_TASKS);
		}

		public void run() {
			while (flagger) {
				try {
					//checks how many customers inside
					wait(customersInside);
					System.out.println("Customer " + id + " enters post office");

					//increments how many customers are waiting to be served
					numCustomersWaiting++;

					//checks postal worker availability
					wait(postalWorkerSem);
					System.out.println("Postal worker " + id % NUM_WORKERS + " serving customer " + id);

					//waits for 1 sec according to table
					if (task == STAMPS) {
						Thread.sleep(1000);
						System.out.println(
								"Customer " + id + " asks postal worker " + id % NUM_WORKERS + " to buy stamps");
						System.out.println("Customer " + id + " finished buying stamps");
					//waits for 1.5 sec according to table
					} else if (task == LETTER) {
						Thread.sleep(1500);
						System.out.println(
								"Customer " + id + " asks postal worker " + id % NUM_WORKERS + " to mail a letter");
						System.out.println("Customer " + id + " finished mailing a letter");
					//waits for 2 sec according to table
					//checks for availability of scales and signals when scale is released
					} else if (task == PACKAGE) {
						Thread.sleep(2000);
						System.out.println(
								"Customer " + id + " asks postal worker " + id % NUM_WORKERS + " to mail a package");
						wait(scalesSem);
						System.out.println("Scales in use by postal worker " + id % NUM_WORKERS);
						System.out.println("Scales released by postal worker " + id % NUM_WORKERS);
						signal(scalesSem);
						System.out.println("Customer " + id + " finished mailing a package");
					}

					//release postl workers
					signal(postalWorkerSem);
					System.out.println("Customer " + id + " leaves post office");
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					signal(customersInside);
				}
			}
		}

		void wait(Semaphore s) {
			try {
				s.acquire();
			} catch (InterruptedException e) {

			}
		}

		void signal(Semaphore s) {

			s.release();

		}
	}

	/**
	 * handles the postal workers and waits inside loop until worker is available
	 *
	 */
	static class PostalWorker extends Thread {
		int id;

		PostalWorker(int id) {
			this.id = id;
		}

		public void run() {
			while (flagger) {

				/*
				 * checks for postal workers and checks how many customers are inside
				 */
				wait(postalWorkerSem);
				int numCustomers = customersInside.availablePermits();
				if (numCustomers == 0) {
					signal(postalWorkerSem);
					continue;
				}
				//sets the customer id based on order they came in
				int customerId = (id + numCustomers) % numCustomers;

				signal(postalWorkerSem);
			}
		}

		void wait(Semaphore s) {
			try {
				s.acquire();
			} catch (InterruptedException e) {

			}
		}

		void signal(Semaphore s) {

			s.release();

		}
	}

}
