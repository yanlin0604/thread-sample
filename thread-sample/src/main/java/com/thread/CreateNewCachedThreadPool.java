package com.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Here is a sketch of a network service in which threads in a thread pool service incoming requests. 
 * It uses the preconfigured Executors.newCachedThreadPool() factory method
 * @author Juanjuan
 *
 */
public class CreateNewCachedThreadPool implements Runnable {
	private final ServerSocket serverSocket;
	private final ExecutorService pool;

	/**
	 * 
	 * @param port
	 *            port
	 * @throws IOException
	 */
	public CreateNewCachedThreadPool(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		/**
		 * Creates a thread pool that creates new threads as needed, but will reuse previously constructed threads when they are available. 
		 * These pools will typically improve the performance of programs that execute many short-lived asynchronous tasks. 
		 * Calls to execute will reuse previously constructed threads if available. If no existing thread is available, 
		 * a new thread will be created and added to the pool. Threads that have not been used for sixty seconds are terminated and removed from the cache. 
		 * Thus, a pool that remains idle for long enough will not consume any resources. 
		 * Note that pools with similar properties but different details (for example, timeout parameters) may be created using ThreadPoolExecutor constructors.
		 */
		pool = Executors.newCachedThreadPool();
		/**
		 * Creates a thread pool that creates new threads as needed, but will reuse previously constructed threads when they are available, 
		 * and uses the provided ThreadFactory to create new threads when needed.
		 */
		//pool = Executors.newCachedThreadPool(new SimpleThreadFactory());
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

	/* 
	 * The following method shuts down an ExecutorService in two phases, 
	 * first by calling shutdown to reject incoming tasks, and then calling shutdownNow, 
	 * if necessary, to cancel any lingering tasks
	 */
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
			new Thread(new CreateNewCachedThreadPool(9803)).start();
		} catch (IOException e) {
			System.err.println("error");
			e.printStackTrace();
		}
	}
}



