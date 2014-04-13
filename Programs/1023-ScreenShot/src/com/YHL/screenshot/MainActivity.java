package com.YHL.screenshot;

import java.io.IOException;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
	ImageView IV;
	Button B;
	String Path = "/sdcard/000.png";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			Process process = Runtime.getRuntime().exec("chmod 777 dev/graphics/fb0");
			process.waitFor();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setContentView(R.layout.activity_main);
		IV = (ImageView)findViewById(R.id.IV1);
		B = (Button)findViewById(R.id.B1);
		B.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Process process;
				try {
					process = Runtime.getRuntime().exec("rm sdcard/000.png");
					process.waitFor();
					process = Runtime.getRuntime().exec("screenshot -i sdcard/000.png");
					process.waitFor();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				Bitmap bm = BitmapFactory.decodeFile(Path, options);
				IV.setImageBitmap(bm);
				/*try {
					BufferedReader BR = new BufferedReader(new FileReader(Path));
					char[] image = null;
					BR.read(image);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}*/
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
