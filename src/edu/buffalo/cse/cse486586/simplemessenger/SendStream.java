package edu.buffalo.cse.cse486586.simplemessenger;

import java.net.*;
import java.io.*;

import android.util.Log;

public class SendStream implements Runnable{
	
	Socket sock;
	String message;
	static final String ERRORTAG= "Adil SS-error";
	
	SendStream(String ipAddr, int tcpPort, String message) {
		this.message= message;
		try {
			sock= new Socket(ipAddr, tcpPort);
		} catch (UnknownHostException e) {
			Log.e(ERRORTAG, ""+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(ERRORTAG, ""+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			DataOutputStream dout= new DataOutputStream(sock.getOutputStream());
			dout.writeUTF(message);
			dout.flush();
			}
			catch (IOException e) {
				Log.e(ERRORTAG, ""+e.getMessage());
				e.printStackTrace();
			}
	}
	
}
