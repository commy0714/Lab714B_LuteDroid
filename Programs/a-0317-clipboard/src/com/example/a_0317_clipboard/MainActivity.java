package com.example.a_0317_clipboard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.hardware.input.InputManager;
import android.util.Log;
import android.view.InputDevice;
import android.view.InputEvent;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends Activity {
	EditText ET;
	Button B;
	TextView TV;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ET = (EditText)findViewById(R.id.editText1);
		B = (Button)findViewById(R.id.button1);
		TV = (TextView)findViewById(R.id.textView1);
		final ClipboardManager CM = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
		B.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ClipData C = ClipData.newPlainText("text",ET.getText());
				CM.setPrimaryClip(C);
				TV.setText(get(CM));
				
				InputManager i = (InputManager)getSystemService(INPUT_SERVICE);
				try {
					Method m = i.getClass().getMethod("injectInputEvent");
					
					long now = SystemClock.uptimeMillis();
			        KeyEvent k = new KeyEvent(now, now, KeyEvent.ACTION_DOWN, 139/*MENU*/, 0, 0,KeyCharacterMap.VIRTUAL_KEYBOARD, 0, 0, InputDevice.KEYBOARD_TYPE_NONE);
			        KeyEvent k2 = new KeyEvent(now, now, KeyEvent.ACTION_UP, 139/*MENU*/, 0, 0,KeyCharacterMap.VIRTUAL_KEYBOARD, 0, 0, InputDevice.KEYBOARD_TYPE_NONE);
			        m.invoke(k, 2);
			        m.invoke(k2, 2);
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private String get(ClipboardManager CM){
		ClipData.Item CI = CM.getPrimaryClip().getItemAt(0);
		return CI.getText().toString();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}	
}
