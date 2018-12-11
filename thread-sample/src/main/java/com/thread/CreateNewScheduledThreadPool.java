package com.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import static java.util.concurrent.TimeUnit.*;

/**
 * Here is a class with a method that sets up a ScheduledExecutorService to beep every ten seconds for an hour
 * @author Juanjuan
 *
 */
public class CreateNewScheduledThreadPool {

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	

	public void beepForAnHour() {
		final Runnable beeper = new Runnable() {
			public void run() {
				System.out.println("beep");
			}
		};
		final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 10, 10, SECONDS);
		scheduler.schedule(new Runnable() {
			public void run() {
				beeperHandle.cancel(true);
			}
		}, 60 * 60, SECONDS);
	}

	public static void main(String[] args) {
		CreateNewScheduledThreadPool createNewScheduledThreadPool = new CreateNewScheduledThreadPool();
		createNewScheduledThreadPool.beepForAnHour();
	}

}
