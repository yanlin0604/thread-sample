package com.thread;

import java.io.PrintStream;
import java.net.Socket;

/**
 * 
 * @author Juanjuan
 *
 */
public class Handler implements Runnable {
	private final Socket socket;

	Handler(Socket socket) {
		this.socket = socket;
	}

	//read and service request on socket
	public void run() {
		try {
			System.out.println(Thread.currentThread().getName() + "Executing ...");
			PrintStream out = new PrintStream(socket.getOutputStream());

			// socket.setSoTimeout(100);

			// Browser output response Information
			out.println("HTTP/1.1 200 OK");
			out.println("Content-Type:text/html;charset:GBK");
			out.println();
			out.println("<HTML><BODY>" + "<center>" + "</center>HTTP protocol test Server, current time:"
					+ new java.util.Date() + "</pre></BODY></HTML>");
			out.flush();
			out.close();
			socket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}