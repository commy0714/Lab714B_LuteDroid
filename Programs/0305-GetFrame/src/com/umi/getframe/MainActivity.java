package com.umi.getframe;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		DataOutputStream DOS = null;
		FileInputStream buf = null;
		FileOutputStream frame = null;
		try {
			
			Process process = Runtime.getRuntime().exec("su");
			Log.d("XXXX", "1");
			DOS = new DataOutputStream(process.getOutputStream());
			Log.d("XXXX", "2");
			DOS.writeBytes("chmod 777 /dev/graphics/fb0"); //framebuffer 的暫存檔 給driver用的
			Log.d("XXXX", "3");
			DOS.flush();
			Log.d("XXXX", "4");
		//	process.waitFor(); 
			Log.d("XXXX", "5");
			buf = new FileInputStream(new File("/dev/graphics/fb0")); //raw 格式的圖片檔 
			Log.d("XXXX", "6");
			frame = new FileOutputStream(new File("sdcard/MyFrame.png"));
			Log.d("XXXX", "7");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}

		Log.d("XXXX", "8");
		

		DisplayMetrics dm = new DisplayMetrics();
	            Display display = getWindowManager().getDefaultDisplay();
	            display.getMetrics(dm);
	            int screenWidth = dm.widthPixels; 
	            int screenHeight = dm.heightPixels; 
	            int pixelformat = display.getPixelFormat();
	            PixelFormat localPixelFormat1 = new PixelFormat();
	            PixelFormat.getPixelFormatInfo(pixelformat, localPixelFormat1);
	            int deepth = localPixelFormat1.bytesPerPixel;
	            byte[] piex = new byte[800 * 480 * deepth];
	            DataInputStream dStream = new DataInputStream(buf);
	            try {
					dStream.readFully(piex);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            int[] colors = new int[screenHeight * screenWidth];
	            
	            for (int m = 0; m < colors.length; m++) {
	                int r = (piex[m * 4] & 0xFF);
	                int g = (piex[m * 4 + 1] & 0xFF);
	                int b = (piex[m * 4 + 2] & 0xFF);
	                int a = (piex[m * 4 + 3] & 0xFF);
	                colors[m] = (a << 24) + (r << 16) + (g << 8) + b;
	            }
	            
	            ByteArrayOutputStream bos = new ByteArrayOutputStream();
	            Bitmap MyFrame = Bitmap.createBitmap(colors, screenWidth, screenHeight, Bitmap.Config.RGB_565);
	            MyFrame.compress(CompressFormat.PNG, 0, bos);
	            byte[] bitmapdata = bos.toByteArray();

	            
	            try {
					frame.write(bitmapdata);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
	            
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
