package com.YHL.screenshotservice;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class Service_SendView extends IntentService {
	public Service_SendView() {
		super("Service_SendView");
	}

	String IP;
	Boolean B = true;

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
/*
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(getApplicationContext(), "Service Start",
				Toast.LENGTH_SHORT).show();
		Bundle bundle = intent.getExtras();
		IP = bundle.getString("IP");
		new Thread(new SOCKET()).start();
		return START_STICKY;
	}
*/
/*	@Override
	public void onDestroy() {
		B = false;
		Toast.makeText(getApplicationContext(), "Service Stop",
				Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}
*/
	private class SOCKET implements Runnable {
		@Override
		public void run() {
			Socket SC = null;
			DataOutputStream DOS = null;
			DataInputStream DIS = null;
			try {
				/*
				 * process = Runtime.getRuntime().exec("su"); DataOutputStream D
				 * = new DataOutputStream(process.getOutputStream());
				 * D.writeBytes("chmod 777 /dev/graphics/fb0\n");
				 * D.writeBytes("exit\n"); D.flush();
				 */
				SC = new Socket(IP, 12345);
			} catch (IOException e) {
				e.printStackTrace();
			}

			DataOutputStream dos;
			DataInputStream fb0 = null;
			try {
				Process process = Runtime.getRuntime().exec("su");
				dos = new DataOutputStream(process.getOutputStream());
				dos.writeBytes("chmod 777 /dev/graphics/fb0");
				dos.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			DisplayMetrics dm = new DisplayMetrics();
			WindowManager WM = (WindowManager) getSystemService(WINDOW_SERVICE);
			Display display = WM.getDefaultDisplay();
			display.getMetrics(dm);
			int screenWidth = dm.widthPixels;
			int screenHeight = dm.heightPixels;
			/*int pixelformat = display.getPixelFormat();
			PixelFormat localPixelFormat1 = new PixelFormat();
			PixelFormat.getPixelFormatInfo(pixelformat, localPixelFormat1);*/
			int deepth = 4;//localPixelFormat1.bytesPerPixel;
			byte[] piex;int[] colors;int r,g,b,a;
			byte[] bu;
			try {
				DOS = new DataOutputStream(new BufferedOutputStream(SC.getOutputStream()));
				DIS = new DataInputStream(SC.getInputStream());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			while (B) {
				piex = new byte[800 * 480 * deepth];
				try {
					fb0 = new DataInputStream(new FileInputStream(new File("/dev/graphics/fb0")));
					fb0.readFully(piex);
					fb0.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				colors = new int[screenHeight * screenWidth];
				for (int m = 0; m < colors.length; m++) {
					r = (piex[m * 4] & 0xFF);
					g = (piex[m * 4 + 1] & 0xFF);
					b = (piex[m * 4 + 2] & 0xFF);
					a = (piex[m * 4 + 3] & 0xFF);
					colors[m] = (a << 24) + (r << 16) + (g << 8) + b;
				}
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				Bitmap MyFrame = Bitmap.createBitmap(colors, screenWidth, screenHeight, Bitmap.Config.RGB_565);
				MyFrame.compress(CompressFormat.PNG, 20, bos);
				ByteArrayInputStream bai = new ByteArrayInputStream(bos.toByteArray());
				
				try {
					if(DIS.readUTF().equals("SEND")){
						byte[] buf = new byte[2048];
						int num = bai.read(buf);
						while (num != -1) {
							DOS.write(buf, 0, num);
							DOS.flush();
							num = bai.read(buf);
						}
						//DOS.write(ba,0,ba.length);DOS.flush();
					}
				} catch (IOException e) {
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

	@Override
	protected void onHandleIntent(Intent intent) {
		Toast.makeText(getApplicationContext(), "Service Start",Toast.LENGTH_SHORT).show();
		Bundle bundle = intent.getExtras();
		IP = bundle.getString("IP");
		new Thread(new SOCKET()).start();	
	}
}
