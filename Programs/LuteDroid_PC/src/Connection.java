import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Connection{
	ProjectTable PT;
	ServerSocket SS;
	Socket SC;
	DataInputStream DIS;
	DataOutputStream DOS;
	
	Socket SC_status,SC_picctrl,SC_audio,SC_clipboard,SC_file;
	DataInputStream DIS_status,DIS_pic,DIS_audio,DIS_clipboard,DIS_file;
	DataOutputStream DOS_status,DOS_ctrl,DOS_audio,DOS_clipboard,DOS_file;
	Boolean isConnected;
	
	public Connection(ProjectTable PT){
		this.PT = PT;
		PT.C = this;
		
		try {
			SC_status = new ServerSocket(PT.Port_status).accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//===============Socket_Status Start===============
	private class Status implements Runnable{
		DataInputStream DIS;
		DataOutputStream DOS;
		public Status(Socket SC){
			try {
				DIS = new DataInputStream(new BufferedInputStream(SC.getInputStream()));
				DOS = new DataOutputStream(new BufferedOutputStream(SC.getOutputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			while(isConnected){
				try {
					String status = DIS.readUTF();
					if(status == "Initialize"){
						
					}else if(status == "Information"){
						
					}else if(status == ""){
						
					}else if(status == ""){
						
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void sendStatus(String S){
			if(isConnected){
				try {
					DOS_status.writeUTF(S);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//===============Socket_Status Stop===============
	
	//===============Socket_PicCtrl Start===============
	private class PicCtrl implements Runnable{
		DataInputStream DIS;
		DataOutputStream DOS;
		public PicCtrl(Socket SC){
			try {
				DIS = new DataInputStream(new BufferedInputStream(SC.getInputStream()));
				DOS = new DataOutputStream(new BufferedOutputStream(SC.getOutputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			while(isConnected){
				try {
					if(DIS.readUTF().equals("START")){
						ByteArrayOutputStream BAOS = new ByteArrayOutputStream(2048);
						byte[] buf = new byte[2048];
						int num = DIS.read(buf);
						while (num < 2048) {
							BAOS.write(buf,0,num);
							num = DIS.read(buf);
						}
						BAOS.flush();
						PT.PF.I = Toolkit.getDefaultToolkit().createImage(BAOS.toByteArray());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void sendControl(String S){
			if(isConnected){
				try {
					DOS.writeUTF(S);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//===============Socket_PicCtrl Stop===============
	
	//===============Socket_Audio Start===============
	private class Audio_send implements Runnable{
		BufferedOutputStream OS;
		public Audio_send(Socket SC){
			try {
				OS = new BufferedOutputStream(SC.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			while(!isConnected){
				try {
					byte tempBuffer[] = new byte[2048];
					tempBuffer = readAudio(tempBuffer, 0, tempBuffer.length);
					OS.write(tempBuffer, 0, tempBuffer.length);
					OS.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		byte[] readAudio(byte[] b,int start,int stop){
			return null;
		}
	}
	private class Audio_catch implements Runnable{
		BufferedInputStream IS;
		public Audio_catch(Socket SC){
			try {
				IS = new BufferedInputStream(SC.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			while(!isConnected){
				try {
					byte tempBuffer[] = new byte[2048];
					int len = IS.read(tempBuffer, 0, tempBuffer.length);
					if(len > 0)
						playAudio(tempBuffer, 0, len);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		int playAudio(byte[] b,int start,int stop){
			return 0;
		}
	}
	//===============Socket_Audio Stop===============
	
	//===============Socket_ClipBoard Start===============
	private class Clipboard implements Runnable{
		DataInputStream DIS;
		DataOutputStream DOS;
		public Clipboard(Socket SC){
			try {
				DIS = new DataInputStream(new BufferedInputStream(SC.getInputStream()));
				DOS = new DataOutputStream(new BufferedOutputStream(SC.getOutputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			while(isConnected){
				try {
					if(DIS.readUTF().equals("CHANGE")){
						PT.Phone_clipboard = DIS.readUTF();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void sendChange(String S){
			if(isConnected){
				try {
					DOS.writeUTF("CHANGE");
					DOS.writeUTF(S);
					DOS.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//===============Socket_ClipBoard Stop===============
	
	//===============Socket_File Start===============
	
	//===============Socket_File Stop===============

	public void run() {
		try {
			SS = new ServerSocket(PT.PC_status_port);
			SC = SS.accept();
			isConnected = true;
			PT.CF.connected();
			DIS = new DataInputStream(new BufferedInputStream(SC.getInputStream()));
			DOS = new DataOutputStream(new BufferedOutputStream(SC.getOutputStream()));
			while(true){
				String Str = DIS.readUTF();
				if (Str.equals("SEND")) {
					catchImg();
				}else if(Str.equals("END")){
					DIS.close();
					SC.close();
					SS.close();
					JOptionPane.showConfirmDialog(PT.PF,"手機端已斷線","連線中斷",JOptionPane.CLOSED_OPTION);
					System.exit(0);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void catchImg() {
		try {
			ServerSocket SS1 = new ServerSocket(PT.PC_connect_img_port);
			Socket SC1 = SS1.accept();
			DataInputStream DIS = new DataInputStream(new BufferedInputStream(SC1.getInputStream()));
			ByteArrayOutputStream BAOS = new ByteArrayOutputStream(2048);
			byte[] buf = new byte[2048];
			int num = DIS.read(buf);
			while (num != -1) {
				BAOS.write(buf,0,num);
				num = DIS.read(buf);
			}
			BAOS.flush();
			PT.PF.I = Toolkit.getDefaultToolkit().createImage(BAOS.toByteArray());
			DIS.close();
			SC1.close();
			SS1.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
