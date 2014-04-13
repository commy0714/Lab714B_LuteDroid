import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.text.html.HTML;


public class ConnectFrame extends JFrame implements ActionListener{
	ProjectTable PT;
	JLabel LB_statement;
	JLabel LB_ip;
	JLabel LB_port;
	JButton B_question;
	JButton B_return;
	
	public ConnectFrame(ProjectTable PT){
		this.PT = PT;
		PT.CF = this;
		try {
			LB_statement = new JLabel("等待連線中。。。");
			LB_statement.setFont(new Font(null,0,30));
			LB_ip = new JLabel("本地IP：    "+Inet4Address.getLocalHost().getHostAddress());
			LB_ip.setFont(new Font(null,1,14));
			LB_port = new JLabel("PORT ：    12345");
			LB_port.setFont(new Font(null,1,14));
			B_question = new JButton(new ImageIcon(this.getClass().getResource("/Question.jpg")));
			B_return = new JButton(new ImageIcon(this.getClass().getResource("/Return.jpg")));
			
			LB_statement.setBounds(160, 70, 200, 35);
			LB_ip.setBounds(160, 120, 200, 30);
			LB_port.setBounds(160, 160, 200, 30);
			B_question.setBounds(460, 240, 30, 30);
			B_return.setBounds(5, 240, 30, 30);
			
			B_question.addActionListener(this);
			B_question.setCursor(new Cursor(Cursor.HAND_CURSOR));
			B_return.addActionListener(this);
			B_return.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			add(LB_statement);
			add(LB_ip);
			add(LB_port);
			add(B_question);
			add(B_return);
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(this,"無法取得IP\n請確認您的網路連線!","網路異常",JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start(){
		this.setSize(500, 300);
		this.setLocationRelativeTo(this);
		this.setLayout(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("Android 遠端即時操控系統>>Waiting For Connect...");
		this.setVisible(true);
		
		new Connection(PT);
	}
	
	public void resume(){
		this.setVisible(true);
	}
	
	public void connected(){
		this.setVisible(false);
		new PhoneFrame(PT).start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(B_question)){
			JOptionPane.showMessageDialog(this,"請開啟手機端程式\n輸入本地IP位置後即可連線","連線說明",JOptionPane.QUESTION_MESSAGE);
		}else if(e.getSource().equals(B_return)){
			this.setVisible(false);
			PT.MF.setVisible(true);
		}		
	}
}
