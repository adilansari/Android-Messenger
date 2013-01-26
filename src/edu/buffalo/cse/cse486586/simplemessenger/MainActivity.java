package edu.buffalo.cse.cse486586.simplemessenger;

import java.net.*;
import java.io.*;
import java.net.Socket;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.*;

public class MainActivity extends Activity {

	private Handler uiHandle= new Handler();
	static final String TAG= "Adil";
	static final String ERRORTAG= "Adil error";
	int recvPort= 10000;
	int sendPort;
	String ipAddr= "10.0.2.2";
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //new thread to create connection
        new Thread(new Runnable() {
			public void run() {
		    	String portStr = get_portStr();
		    	Log.v(TAG, portStr);
		    	
				if(portStr.equals("5554")) {
					sendPort= 11112;
				}
				else if(portStr.equals("5556")) {
					sendPort= 11108;
				}
				else
					Log.v(TAG, "AVD portStr is neither 5554 nor 5556");
				
				Log.v(TAG, "recvPort: "+Integer.toString(recvPort)+" sendPort: "+Integer.toString(sendPort));
			}        	
        }).start();
        
        //start listener thread in another program
        /*StartListening sl= new StartListening(recvPort);
        Thread t1= new Thread((Runnable) sl);
        t1.start();*/
        
        //listener embedded in this class only
        new Thread(new Runnable() {
        	public void run() {
        		Socket sock1= null;
        		DataInputStream din= null;
        		ServerSocket servSocket= null;
        		try {
					servSocket= new ServerSocket(recvPort);
					Log.v(TAG, "Server Socket port: "+Integer.toString(servSocket.getLocalPort()));
				} catch (IOException e) {
					Log.v(ERRORTAG, ""+e.getMessage());
					e.printStackTrace();
				}
        		
        		while(true) {
        			try {
        				sock1= servSocket.accept();
        				Log.v(TAG, "sock1: "+sock1.getInetAddress().getHostAddress()+sock1.isConnected()+Integer.toString(sock1.getLocalPort())+Integer.toString(sock1.getPort()));
        				//din = new DataInputStream(sock1.getInputStream());
        				BufferedReader br= new BufferedReader(new InputStreamReader(sock1.getInputStream()));
        				String str= br.readLine();
        				Log.v(TAG, "recvd msg: "+str);
        				updateTextView("recvd: "+str);
        				} 
        			
        			catch (IOException e) {
        				Log.v(ERRORTAG, ""+e.getMessage());
        				e.printStackTrace();
        			}
        			finally {
        				if (din!= null)
        					try {
        						din.close();
        					} catch (IOException e) {
        						Log.v(ERRORTAG, ""+e.getMessage());
        						e.printStackTrace();
        					}
        				if(sock1!=null)
        					try {
        						sock1.close();
        					} catch (IOException e) {
        						Log.v(ERRORTAG, ""+e.getMessage());
        						e.printStackTrace();
        					}	
        			}
        		}
        	}
        }).start();
        
        
        
        //now just create a thread to send a message
        final EditText editText= (EditText) findViewById(R.id.editText1);
		
        editText.setOnKeyListener(new OnKeyListener(){

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				return false;
			}
        	
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void sendMessage(View view) {
    	EditText et= (EditText) findViewById(R.id.editText1);
    	final String msg= et.getText().toString();
    	//updateTextView("sent: " + msg);
    	
    	new Thread(new Runnable() {
    		public void run() {
    			Socket sock= null;
    			try {
    				sock= new Socket(ipAddr, sendPort);
    				Log.v(TAG, "send sock: "+sock.getInetAddress().getHostAddress()+sock.isConnected()+Integer.toString(sock.getPort()));
    			} catch (UnknownHostException e) {
    				Log.v(ERRORTAG, ""+e.getMessage());
    				e.printStackTrace();
    			} catch (IOException e) {
    				Log.v(ERRORTAG, ""+e.getMessage());
    				e.printStackTrace();
    			}
    			
    			try {
    				PrintWriter out= new PrintWriter(sock.getOutputStream(),true);
    				out.println(msg);
    				updateTextView("sent: " + msg);
    				}
    				catch (IOException e) {
    					Log.v(ERRORTAG, ""+e.getMessage());
    					e.printStackTrace();
    				}
    		}
    		
    	}).start();
    	
    	/*SendStream ss= new SendStream(ipAddr, sendPort, msg);
    	Thread t3= new Thread((Runnable) ss);
    	t3.start();*/
    }
    
    //get port number
    public String get_portStr() {
    	TelephonyManager tel = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
    	String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
    	return portStr;
    }
    
    public void updateTextView(String message) {
    	final String msg= message;
    	uiHandle.post(new Runnable() {
    		public void run() {
    			TextView textView = (TextView)findViewById(R.id.textView1);
    	    	Log.v(TAG, "updating textview");
    	    	textView.append(msg);
    	    	Log.v(TAG, "updated textview");
    	    	textView.setMovementMethod(ScrollingMovementMethod.getInstance());
    		}
    	});
    }
}

