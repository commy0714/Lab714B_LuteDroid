package com.umi.getip;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.ActivityManager; 
import android.app.ActivityManager.RunningServiceInfo; 
import android.content.ComponentName; 
import android.content.Context; 
import android.net.ConnectivityManager; 
import android.net.NetworkInfo; 
import android.net.NetworkInfo.State; 
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo; 
import android.net.wifi.WifiManager; 
import android.text.TextUtils; 
import android.text.format.Formatter;

import java.io.IOException; 
import java.net.Inet4Address;
import java.net.InetAddress; 
import java.net.NetworkInterface; 
import java.net.ServerSocket; 
import java.net.SocketException; 
import java.util.Enumeration; 
import java.util.List; 

public class MainActivity extends Activity {

	private Button getIP;
	private TextView result;
	private TextView result2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		getIP = (Button)findViewById(R.id.button1);
		result = (TextView)findViewById(R.id.TextView01);
		result2 = (TextView)findViewById(R.id.TextView02);
		
		getIP.setOnClickListener(getIPClick);
		
	}
	
	private Button.OnClickListener getIPClick = new Button.OnClickListener (){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				
				Utils.getMACAddress("wlan0");
				Utils.getMACAddress("eth0");
				result.setText(Utils.getIPAddress(true)); // IPv4
			//	result2.setText(Utils.getIPAddress(false)); // IPv6 
				
				
			
			}
		
			
			
		};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
