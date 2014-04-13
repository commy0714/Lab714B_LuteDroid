import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.TextField;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Server extends JFrame{
	Image I = null;
	TextField TF;
	JButton JB;
	Thread T_repaint;

	public static void main(String[] args) {
		new Server();
	}

	public Server() {
		setSize(600, 900);
		setLocationRelativeTo(this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setResizable(false);
		setTitle("ScreenShotServer");

		try {
			TF = new TextField("等待連線中...,本地IP:" + Inet4Address.getLocalHost()	+ ",PORT:12345");
		} catch (HeadlessException | IOException e) {
			e.printStackTrace();
		}
		TF.setBounds(10,10,450,30);

		new Thread(new startServer(TF, this)).start();
		/*T_repaint = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						repaint();
				}
			}
		});*/

		add(TF);
		setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		System.out.println("Paint");
		g.drawImage(I,55,80,this);
	}
}

class startServer implements Runnable {
	TextField TF;
	Server S;

	public startServer(TextField TF, Server S) {
		this.TF = TF;
		this.S = S;
	}

	@Override
	public void run() {
		try {
			ServerSocket SS = new ServerSocket(12345);
			Socket SC = SS.accept();
			DataInputStream DIS = new DataInputStream(new BufferedInputStream(SC.getInputStream()));
			DataOutputStream DOS = new DataOutputStream(SC.getOutputStream());
			TF.setText("已連線成功,遠端IP:" + SC.getInetAddress());
			//S.T_repaint.start();
			while (true) {
				DOS.writeUTF("SEND");
				DOS.flush();
				Thread.sleep(250);
				catchImg(DIS);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void catchImg(DataInputStream DIS) {
		ServerSocket SS;
		try {
			//SS = new ServerSocket(12346);
			//Socket SC = SS.accept();
			//DataInputStream DIS2 = new DataInputStream(new BufferedInputStream(SC.getInputStream()));
			System.out.println("傳~~");
			ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
			
			byte[] buf = new byte[2048];
			int num = DIS.read(buf);
			while (true) {
				BAOS.write(buf);
				if(num < 2048) break;
				num = DIS.read(buf);
			}
			//DIS.read(buf,0,buf.length);
			//BAOS.write(buf,0,buf.length);
			
			BAOS.flush();
			S.I = Toolkit.getDefaultToolkit().createImage(BAOS.toByteArray());
			S.repaint();
			System.out.println("傳完囉~~");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}