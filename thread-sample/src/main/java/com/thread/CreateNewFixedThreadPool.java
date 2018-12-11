package com.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Here is a sketch of a network service in which threads in a thread pool service incoming requests. 
 * It uses the preconfigured Executors.newFixedThreadPool(int) factory method
 * @author Juanjuan
 *
 */
public class CreateNewFixedThreadPool implements Runnable {
	private final ServerSocket serverSocket;
	private final ExecutorService pool;

	/**
	 * 
	 * @param port
	 *            port
	 * @param poolSize
	 *            Thread pool Size
	 * @throws IOException
	 */
	public CreateNewFixedThreadPool(int port, int poolSize) throws IOException {
		serverSocket = new ServerSocket(port);
		//Creates a thread pool that reuses a fixed number of threads operating off a shared unbounded queue.
		pool = Executors.newFixedThreadPool(poolSize);
		//Creates a thread pool that reuses a fixed number of threads operating off a shared unbounded queue, using the provided ThreadFactory to create new threads when needed.
		//pool = Executors.newFixedThreadPool(poolSize, new SimpleThreadFactory());
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
			new Thread(new CreateNewFixedThreadPool(9803, 5)).start();
		} catch (IOException e) {
			System.err.println("error");
			e.printStackTrace();
		}
	}
}





