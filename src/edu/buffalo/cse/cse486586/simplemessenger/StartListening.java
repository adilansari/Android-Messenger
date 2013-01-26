package edu.buffalo.cse.cse486586.simplemessenger;

import java.io.*;
import java.net.*;
import android.util.Log;

public class StartListening extends MainActivity implements Runnable {
	
	static final String ERRORTAG= "Adil SL-error";
	ServerSocket servSocket;
	Socket sock1= null;
	DataInputStream din= null;
	
	StartListening(int tcpPort) {
		try {
			servSocket= new ServerSocket(tcpPort);
		} catch (IOException e) {
			Log.e(ERRORTAG, ""+e.getMessage());
		}
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				sock1= servSocket.accept();
				din = new DataInputStream(sock1.getInputStream());
				String str= din.readUTF();
				updateTextView("recvd: " + str);
				} 
			
			catch (IOException e) {
				e.printStackTrace();
				Log.e(ERRORTAG, ""+e.getMessage());
			}
			
			finally {
				if (din!= null)
					try {
						din.close();
					} catch (IOException e) {
						Log.e(ERRORTAG, ""+e.getMessage());
						e.printStackTrace();
					}
				if(sock1!=null)
					try {
						sock1.close();
					} catch (IOException e) {
						Log.e(ERRORTAG, ""+e.getMessage());
						e.printStackTrace();
					}	
			}
		}
	}
}
