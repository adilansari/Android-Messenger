package edu.buffalo.cse.cse486586.simplemessenger;

import java.io.*;
import java.net.*;

public class AcceptStream extends MainActivity implements Runnable {
	
	Socket sock;
	
	AcceptStream(Socket sock) {
		this.sock= sock;
	}
	
	public void run() {
		boolean b=true;
		while (b) {
			DataInputStream din;
			try {
				din = new DataInputStream(sock.getInputStream()); 
				String str= din.readUTF();
				//find a way to sort out this matter to update text view by calling a method of mainactivity
				updateTextView("recvd: " + str);
				din.close();
			}
			catch (IOException e) {
				b=false;
				e.printStackTrace();
			}
		}
	}
}
