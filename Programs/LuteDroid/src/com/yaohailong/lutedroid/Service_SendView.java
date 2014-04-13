package com.yaohailong.lutedroid;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class Service_SendView extends Service {
	String IP;
	Boolean B = true;
	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		Intent notifyIntent = new Intent(Service_SendView.this,MainActivity.class);
		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent appIntent = PendingIntent.getActivity(Service_SendView.this,0,notifyIntent,0);
		Notification notification = new Notification();
		notification.flags = Notification.FLAG_FOREGROUND_SERVICE;
		notification.icon = R.drawable.ic_launcher;
		notification.tickerText = "遠端服務進行中";
		notification.defaults = Notification.DEFAULT_ALL;
		notification.setLatestEventInfo(Service_SendView.this,"LuteDroid","遠端服務進行中",appIntent);
		//notificationManager.notify("lutedroid",0,notification);
		
		Toast.makeText(getApplicationContext(),"Service Start",Toast.LENGTH_SHORT).show();
		Bundle bundle = intent.getExtras();
		IP = bundle.getString("IP");
		new Thread(new SOCKET()).start();		
		return START_STICKY;
	}
	@Override
	public void onDestroy() {
		B = false;
		Toast.makeText(getApplicationContext(),"Service Stop",Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}
	private class SOCKET implements Runnable{
		@Override
		public void run() {
			Socket SC = null;
			DataOutputStream DOS = null;
			Process process;
			try {
				SC = new Socket(IP,12345);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			while(B){
				try {
					DOS = new DataOutputStream(SC.getOutputStream());
					process = Runtime.getRuntime().exec("rm sdcard/000.png");
					process.waitFor();
					process = Runtime.getRuntime().exec("screenshot -i sdcard/000.png");
					process.waitFor();
					
					Log.d("XXXXXXXXX","開始讀檔");
					File F = new File("sdcard/000.png");
					FileInputStream FIS = new FileInputStream(F);
					byte[] buf = new byte[2048];
					DOS.writeUTF("SEND");
					
					DataOutputStream DOS2 = new DataOutputStream(new Socket(IP,12346).getOutputStream());
					int num = FIS.read(buf);
					while (num != -1) {
						DOS2.write(buf,0,num);
						DOS2.flush();
						num = FIS.read(buf);
					}
					//DOS2.write(null);
					FIS.close();
					DOS2.flush();
					DOS2.close();
					Log.d("XXXXXXXXX","關");
					
					Thread.sleep(200);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				DOS.writeUTF("END");
				DOS.close();
				SC.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}

