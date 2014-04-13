import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class newClient {
	static Socket SC;
	static SourceDataLine sline;
	
	public static void main(String[] args) {
		try {
			SC = new Socket("127.0.0.1",8888);
			playAudio();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void playAudio() {
		Runnable runner = new Runnable() {
			public void run() {
				try {
					InputStream in = SC.getInputStream();
					Thread playTread = new Thread();
					
					int count;
					byte[] buffer = new byte[10000];
					while ((count = in.read(buffer, 0, buffer.length)) != -1) {
						PlaySentSound(buffer, playTread);
					}
				} catch (IOException e) {
					System.err.println("I/O problems:" + e);
					System.exit(-3);
				}
			}
		};

		Thread playThread = new Thread(runner);
		playThread.start();
	}

	private static void PlaySentSound(final byte buffer[], Thread playThread) {
		synchronized (playThread) {
			Runnable runnable = new Runnable() {
				public void run() {
					try {
						InputStream input = new ByteArrayInputStream(buffer);
						final AudioFormat format = getAudioFormat();
						final AudioInputStream ais = new AudioInputStream(input, format, buffer.length / format.getFrameSize());
						DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
						sline = (SourceDataLine) AudioSystem.getLine(info);
						sline.open(format);
						sline.start();
						Float audioLen = (buffer.length / format.getFrameSize()) * format.getFrameRate();

						int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
						byte buffer2[] = new byte[bufferSize];
						int count2;

						ais.read(buffer2, 0, buffer2.length);
						sline.write(buffer2, 0, buffer2.length);
						sline.flush();
						sline.drain();
						sline.stop();
						sline.close();
						buffer2 = null;

					} catch (IOException e) {
					} catch (LineUnavailableException e) {
					}
				}
			};
			playThread = new Thread(runnable);
			playThread.start();
		}

	}
	
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
}
