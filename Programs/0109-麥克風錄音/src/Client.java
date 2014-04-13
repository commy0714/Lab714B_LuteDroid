import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class Client {
	static InputStream InputStream;
	static AudioFormat audioFormat;
	static AudioInputStream audioInputStream;
	static SourceDataLine sourceDataLine;
	
	public static void main(String[] args) {
		try {
			Socket SC = new Socket("127.0.0.1",8888);
			InputStream = SC.getInputStream();
			playAudio();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void playAudio() {
		try {
			AudioFormat audioFormat = getAudioFormat();
			audioInputStream = new AudioInputStream(InputStream,audioFormat, audioFormat.getFrameSize());
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
			sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			sourceDataLine.open(audioFormat);
			sourceDataLine.start();

			Thread playThread = new Thread(new PlayThread());
			playThread.start();
		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}// end catch
	}// end playAudio
	
	private static AudioFormat getAudioFormat() {
		float sampleRate = 8000.0F;
		// 8000,11025,16000,22050,44100
		int sampleSizeInBits = 16;
		// 8,16
		int channels = 1;
		// 1,2
		boolean signed = true;
		// true,false
		boolean bigEndian = false;
		// true,false
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
				bigEndian);
	}// end getAudioFormat
	
	static class PlayThread extends Thread {
		byte tempBuffer[] = new byte[10000];

		public void run() {
			try {
				int cnt;
				while (true) {
					cnt = audioInputStream.read(tempBuffer, 0,tempBuffer.length);
					if (cnt > 0) {
						sourceDataLine.write(tempBuffer, 0, cnt);
					}// end if
				}// end while
			} catch (Exception e) {
				System.out.println(e);
				System.exit(0);
			} finally{
				sourceDataLine.drain();
				sourceDataLine.close();
				System.out.println("close");
			}
		}// end run
	}// end inner class PlayThread
}
