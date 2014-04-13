package com.example.audiotest0412;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	AudioManager am = null;
	AudioRecord record = null;
	AudioTrack track = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setVolumeControlStream(AudioManager.MODE_IN_COMMUNICATION);
		
		init();
		
		(new Thread() {
			@Override
			public void run() {
				recordAndPlay();
			}
		}).start();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void init() {
		int min = AudioRecord.getMinBufferSize(8000,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
		
		record = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION,
				8000, AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT, min);

		int maxJitter = AudioTrack.getMinBufferSize(8000,
				AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
		
		track = new AudioTrack(AudioManager.MODE_IN_COMMUNICATION, 8000,
				AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
				maxJitter, AudioTrack.MODE_STREAM);
	}

	private void recordAndPlay() {
		short[] lin = new short[1024];
		int num = 0;
		am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		am.setMode(AudioManager.MODE_IN_COMMUNICATION);
		
		record.startRecording();
		track.play();
		
		while (true) {
			num = record.read(lin, 0, 1024);
			track.write(lin, 0, num);
		}
	}

	boolean isSpeaker = false;

	public void modeChange(View view) {
		Button modeBtn = (Button) findViewById(R.id.modeBtn);
		if (isSpeaker == true) {
			am.setSpeakerphoneOn(false);
			isSpeaker = false;
			modeBtn.setText("Call Mode");
		} else {
			am.setSpeakerphoneOn(true);
			isSpeaker = true;
			modeBtn.setText("Speaker Mode");
		}
	}

	boolean isPlaying = true;

	public void play(View view) {
		Button playBtn = (Button) findViewById(R.id.playBtn);
		if (isPlaying) {
			record.stop();
			track.pause();
			isPlaying = false;
			playBtn.setText("Play");
		} else {
			record.startRecording();
			track.play();
			isPlaying = true;
			playBtn.setText("Pause");
		}
	}
}

