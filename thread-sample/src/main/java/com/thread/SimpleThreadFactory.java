package com.thread;

import java.util.concurrent.ThreadFactory;

/**
 * 
 * @author Juanjuan
 *
 */
public class SimpleThreadFactory implements ThreadFactory {
	public Thread newThread(Runnable r) {
		final Thread thread = new Thread(r, "Thread from the SimpleThreadFactory"); 
        thread.setDaemon(true);
        return thread;
	}
}
