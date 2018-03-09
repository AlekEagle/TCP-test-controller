package com.alekeagle.newappproject;

import com.alekeagle.newappproject.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.app.*;
import android.os.*;
import android.view.WindowManager;
import android.os.AsyncTask;
import java.util.ArrayList;
import android.util.Log;
import com.alekeagle.newappproject.TCPClient;
import android.widget.*;
import android.view.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import android.content.*;
import java.util.*;public class Secure extends Activity
{
    public static String suMessage;
    public static String suIp;
    public static int suPort;
	public String isEitFull;
	public String isEdtFull;
//    public String ip;
//    public int port;
//    public String message;
	public ListView mList;
	private ArrayList<String> arrayList;
	private TCPClient mTcpClient;
	public TextView textView;
    private MyCustomAdapter mAdapter;
    private MainActivity mA;
    public boolean enableLooper = false;
    public Secure() {
        super();
    }
    
//    public Secure(String message, String ip, int port){
//        this.message = message;
//        this.ip = ip;
//        this.port = port;
//    }
    
//    public void setMessage(String message){

//        this.message = message;

//    }

//    public String getMessage() {
//        return message;
//    }
//    public void setIp(String ip){
//        this.ip = ip;
//    }
//    public String getIp(){
//        return ip;
//    }

//    public void setPort(int port){
//        this.port = port;
//    }
//    public int getPort(){
//        return port;
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_custom_dialog:
                showChangeIPPortDialog();
                return true;
            case R.id.exitMenu:
                finish();
                mA.closeApp(); 
                return true;
            case R.id.aboutMenu:
                showAboutAppDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    
	}
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.sub);
		arrayList = new ArrayList<String>();
		// set a change listener on the SeekBar
        
        SeekBar seekBar = (SeekBar)findViewById(R.id.subSeekBar1);
		final TextView textView = (TextView)findViewById(R.id.subTextView1);
		mList = (ListView)findViewById(R.id.list);
        mAdapter = new MyCustomAdapter(this, arrayList);
		mList.setAdapter(mAdapter);
		LayoutInflater inflater = this.getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
		final EditText eit = (EditText) dialogView.findViewById(R.id.edit1);
        final EditText edt = (EditText) dialogView.findViewById(R.id.edit2);
        SharedPreferences.Editor editor = getPreferences(0).edit();
        editor.putString("text", "");
		editor.putInt("selection-start", eit.getSelectionStart());
		editor.putInt("selection-end", eit.getSelectionEnd());
		editor.commit();
		SharedPreferences.Editor otherEditor = getPreferences(1).edit();
		otherEditor.putString("word", "");
		otherEditor.putInt("selection-start", edt.getSelectionStart());
		otherEditor.putInt("selection-end", edt.getSelectionEnd());
		otherEditor.commit();
		

		
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
							 WindowManager.LayoutParams.FLAG_SECURE);
							 
    

	seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
		
		public int skBar;
		

        @Override
        public void onProgressChanged(SeekBar seekBar, int skBar, boolean fromUser) {
            // updated continuously as the user slides the thumb
            textView.setText(String.valueOf(skBar));
            
        String message = Integer.toString(skBar);
            new connectTask().execute("");
            if (message != null) {
                arrayList.add("c: " + message);
                mAdapter.notifyDataSetChanged();
                mTcpClient.sendMessage(message);
                }
        }
        
        
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }
        
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
//				setMessage(message);
                
			}
            
    	  // called after the user finishes moving the SeekBar
		  });
	}
	public class connectTask extends AsyncTask<String,String,TCPClient> {
        
        @Override
        protected TCPClient doInBackground(String... message) {
            if (!enableLooper){
                Looper.prepare();
                enableLooper = true;
            }
            
//we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                    @Override
//here the messageReceived method is implemented
                    public void messageReceived(String message) {
//this method calls the onProgressUpdate
                        publishProgress(message);
                    }
                });
            mTcpClient.run();
            return null;
        }
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
//in the arrayList we add the messaged received from server
            arrayList.add(values[0]);
// notify the adapter that the data set has changed. This means that new message received
// from server was added to the list
            mAdapter.notifyDataSetChanged();
        }
	}

    public void showChangeIPPortDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);
		
        final EditText eit = (EditText) dialogView.findViewById(R.id.edit1);
        final EditText edt = (EditText) dialogView.findViewById(R.id.edit2);
		SharedPreferences prefs = getPreferences(0); 
        String restoredText = prefs.getString("text", null);
        if (restoredText != null) {
            eit.setText(restoredText, TextView.BufferType.EDITABLE);

            int selectionStart = prefs.getInt("selection-start", -1);
            int selectionEnd = prefs.getInt("selection-end", -1);
            if (selectionStart != -1 && selectionEnd != -1) {
                eit.setSelection(selectionStart, selectionEnd);
            }
        }
		SharedPreferences otherPrefs = getPreferences(1);
		String otherRestoredText = otherPrefs.getString("word", null);
        if (otherRestoredText != null) {
            edt.setText(otherRestoredText, TextView.BufferType.EDITABLE);

            int selectionStart = otherPrefs.getInt("selection-start", -1);
            int selectionEnd = otherPrefs.getInt("selection-end", -1);
            if (selectionStart != -1 && selectionEnd != -1) {
                edt.setSelection(selectionStart, selectionEnd);
            }
        }
//		String port = edt.getText().toString();
//		String ipo = eit.getText().toString();
//		int intPort = Integer.valueOf(port);
//		if (isEitFull != null) {
//			eit.setText(ipo);
//		}
//		if (isEdtFull != null) {
//			edt.setText(intPort);
//		}
        dialogBuilder.setTitle("General settings");
        dialogBuilder.setMessage("IP and port settings");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
					SharedPreferences.Editor editor = getPreferences(0).edit();
					editor.putString("text", eit.getText().toString());
					editor.putInt("selection-start", eit.getSelectionStart());
					editor.putInt("selection-end", eit.getSelectionEnd());
					editor.commit();
					SharedPreferences.Editor otherEditor = getPreferences(1).edit();
					otherEditor.putString("word", edt.getText().toString());
					otherEditor.putInt("selection-start", edt.getSelectionStart());
					otherEditor.putInt("selection-end", edt.getSelectionEnd());
					otherEditor.commit();
					String ipo = eit.getText().toString();
                    String port = edt.getText().toString();
                    int intPort = Integer.valueOf(port);
					suPort = intPort;
					suIp = ipo;
                    Log.i("MainActivity", "Sent vars.");
                    
                    
                    //do something with edt.getText().toString();
                }
            });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //pass
                }
            });
        AlertDialog b = dialogBuilder.create();
        b.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                               WindowManager.LayoutParams.FLAG_SECURE);
        b.show();
    }
    public void showAboutAppDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.about_dialog, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("About TCPTest Slider for android");
        dialogBuilder.setMessage("About the app");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //nada
                }
            });
        AlertDialog b = dialogBuilder.create();
        
        b.show();
    }
}
class TCPClient {
    private Secure sec;
    private String serverMessage;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = true;
    private PrintWriter out;
    private  BufferedReader in;
//    Secure ure = new Secure("goat","goat",12);

    
    public void sendMessage(String wierdMessageThing){
        String debugPort = Integer.toString(sec.suPort);
        Log.d("TCPClient", "sec.suIP " + sec.suIp);
        Log.d("TCPClient", "debugPort " + debugPort);
        Log.d("TCPClient", "wierdMessageThing " + wierdMessageThing);
        if (out != null && !out.checkError()) {
            out.println(wierdMessageThing);
            out.flush();
            out.close();
        }
    }
    
    /**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TCPClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }


    /**
     * Sends the message entered by client to the server
//     * @param message text entered by client
     */
//    public void sendPort(int port){
//      Log.i("TCPClient", "Recieved Port");
//      int SERVERPORT = 0 + port;
//      }
    public void stopClient(){
        mRun = false;
    }
    public void run(){
        mRun = true;
        try {
//here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(sec.suIp);
            Log.i("TCP Client", "C: Connecting...");
//create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, sec.suPort);
            try {
//send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                Log.i("TCP Client", "C: Sent.");
                Log.i("TCP Client", "C: Done.");
//receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//in this while the client listens for the messages sent by the server
                while (mRun) {
                    serverMessage = in.readLine();
                    if (serverMessage == null && mMessageListener == null) {
//call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverMessage);
                    }
                    serverMessage = null;
                }
                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");
            } catch (Exception e) {
                Log.e("TCP", "S: Error", e);
            } finally {
//the socket must be closed. It is not possible to reconnect to this socket
// after it is closed, which means a new socket instance has to be created.
                socket.close();
            }
        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
        }
    }

//Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
//class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
    
}
