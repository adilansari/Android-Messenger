package edu.buffalo.cse.cse486586.simplemessenger;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.*;

public class MainActivity extends Activity {

	static final String TAG= "Adil";
	int recvPort, sendPort;
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
					recvPort= 11108;
				}
				else if(portStr.equals("5556")) {
					sendPort= 11108;
					recvPort= 11112;
				}
				else
					Log.v(TAG, "AVD portStr is neither 5554 nor 5556");
				
				Log.v(TAG, "recvPort"+Integer.toString(recvPort));
			}        	
        }).start();
        
        //start listener thread
        StartListening sl= new StartListening(recvPort);
        Thread t1= new Thread((Runnable) sl);
        t1.start();
        
        //now just create a thread to send a message
        final EditText editText= (EditText) findViewById(R.id.editText1);
		
        editText.setOnKeyListener(new OnKeyListener(){

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// send message method call here
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
    	//send message code here linked to the send button
    }
    
    //get port number
    public String get_portStr() {
    	TelephonyManager tel = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
    	String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
    	return portStr;
    }
    
    public void updateTextView(String msg) {
    	TextView textView = (TextView)findViewById(R.id.textView1);
    	textView.append(msg);
    	textView.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
}
