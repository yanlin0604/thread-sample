package com.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Here is a sketch of a network service in which threads in a thread pool service incoming requests. 
 * It uses the preconfigured Executors.newWorkStealingPool factory method
 * @author Juanjuan
 *
 */
public class CreateNewWorkStealingPool implements Runnable {
	
	private final ServerSocket serverSocket;
	private final ExecutorService pool;

	/**
	 * @param port
	 *            port
	 * @param parallelism
	 *            the targeted parallelism level
	 * @throws IOException
	 */
	public CreateNewWorkStealingPool(int port, int parallelism) throws IOException {
		serverSocket = new ServerSocket(port);
		/**
		 * Creates a thread pool that maintains enough threads to support the given parallelism level,
		 *  and may use multiple queues to reduce contention. The parallelism level corresponds to the maximum number of threads actively engaged in,
		 *   or available to engage in, task processing. The actual number of threads may grow and shrink dynamically. 
		 *   A work-stealing pool makes no guarantees about the order in which submitted tasks are executed.
		 */
		pool = Executors.newWorkStealingPool(parallelism);
	}
	
	/**
	 * @param port
	 *            port
	 * @throws IOException
	 */
	public CreateNewWorkStealingPool(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		//Creates a work-stealing thread pool using all available processors as its target parallelism level.
		pool = Executors.newWorkStealingPool();
	}

	public void run() { // run the service
		try {
			for (;;) {
				System.out.println(Thread.currentThread().getName());
				pool.execute(new Handler(serverSocket.accept()));
			}
		} catch (Exception ex) {
			System.err.println("error");
			shutdownAndAwaitTermination(pool);
		}
	}

	/* The following method shuts down an ExecutorService in two phases, 
	 * first by calling shutdown to reject incoming tasks, and then calling shutdownNow, 
	 * if necessary, to cancel any lingering tasks
	 * */
	void shutdownAndAwaitTermination(ExecutorService pool) {
		pool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!pool.awaitTermination(60, TimeUnit.SECONDS))
					System.err.println("Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}
	
	public static void main(String[] args) {
		try {
			new Thread(new CreateNewWorkStealingPool(9803, 6)).start();
		} catch (IOException e) {
			System.err.println("error");
			e.printStackTrace();
		}
	}
}
