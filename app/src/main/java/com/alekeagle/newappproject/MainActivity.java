package com.alekeagle.newappproject;

import android.app.*;
import android.os.*;
import android.view.*;
import android.content.*;
import android.widget.*;
import android.util.Log;

public class MainActivity extends Activity
{
    
    
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
	}
	public void onComposeButtonClick(View view) 
    {
    	Intent intent = new Intent(this, Secure.class);
    	startActivity(intent);
	}
    public void closeApp(){
        finish();
    }
}
