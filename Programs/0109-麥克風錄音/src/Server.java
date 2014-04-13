import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Server extends JFrame{
	boolean stopCapture = false;
	
	OutputStream OutputStream;
	AudioFormat audioFormat;
	TargetDataLine targetDataLine;
	AudioInputStream audioInputStream;
	SourceDataLine sourceDataLine;
	
	public static void main(String[] args) {
		new Server();
	}
	
	public Server(){
		final JButton captureBtn = new JButton("Capture");
		final JButton stopBtn = new JButton("Stop");

		captureBtn.setEnabled(true);
		stopBtn.setEnabled(false);

		// Register anonymous listeners
		captureBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				captureBtn.setEnabled(false);
				stopBtn.setEnabled(true);
				captureAudio();
			}// end actionPerformed
		}// end ActionListener
		);// end addActionListener()
		getContentPane().add(captureBtn);

		stopBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				captureBtn.setEnabled(true);
				stopBtn.setEnabled(false);
				stopCapture = true;
			}// end actionPerformed
		}// end ActionListener
		);// end addActionListener()
		getContentPane().add(stopBtn);

		getContentPane().setLayout(new FlowLayout());
		setTitle("Capture Server");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(250, 90);
		setVisible(true);
		
		try {
			captureBtn.setEnabled(false);
			stopBtn.setEnabled(false);
			ServerSocket SS = new ServerSocket(8888);
			Socket SC = SS.accept();
			OutputStream = new BufferedOutputStream(SC.getOutputStream());
			captureBtn.setEnabled(true);
			stopBtn.setEnabled(false);
			captureAudio();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private void captureAudio() {
		try {
			audioFormat = getAudioFormat();
			DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
			targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			targetDataLine.open(audioFormat);
			targetDataLine.start();

			Thread captureThread = new Thread(new CaptureThread());
			captureThread.start();
		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}// end catch
	}// end captureAudio method
	
	private AudioFormat getAudioFormat() {
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
	
	class CaptureThread extends Thread {
		byte tempBuffer[] = new byte[10000];

		public void run() {
			stopCapture = false;
			try {// Loop until stopCapture is set
				while (!stopCapture) {
					int cnt = targetDataLine.read(tempBuffer, 0,tempBuffer.length);
					if (cnt > 0) {
						OutputStream.write(tempBuffer, 0, cnt);
						System.out.println(cnt);
					}// end if
				}// end while
				//OutputStream.close();
			} catch (Exception e) {
				System.out.println(e);
				System.exit(0);
			}// end catch
		}// end run
	}// end inner class CaptureThread
}
