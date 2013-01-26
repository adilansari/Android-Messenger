package edu.buffalo.cse.cse486586.simplemessenger;

import java.io.*;
import java.net.*;
import android.util.Log;

public class StartListening implements Runnable {
	
	static final String ERRORTAG= "Adil error";
	ServerSocket servSocket= null;
	
	StartListening(int tcpPort) {
		try {
			servSocket= new ServerSocket(tcpPort);
		} catch (IOException e) {
			Log.e(ERRORTAG, ""+e.getMessage());
		}
	}
	
	@Override
	public void run() {
		boolean isConnected= false;
		while(!isConnected) {
			try {
				Socket sock1= servSocket.accept();
				isConnected= true;
				AcceptStream as= new AcceptStream(sock1);
				Thread t2= new Thread((Runnable) as);
				t2.start();
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(ERRORTAG, ""+e.getMessage());
			}
		}
	}

}
