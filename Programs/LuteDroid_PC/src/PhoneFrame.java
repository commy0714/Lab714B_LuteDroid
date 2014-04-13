import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PhoneFrame extends JFrame implements ActionListener,WindowListener,MouseListener{
	ProjectTable PT;
	Thread T_repaint;
	Image I;
	JPanel JP;
	JButton B_home;
	JButton B_menu;
	JButton B_back;

	public PhoneFrame(ProjectTable PT){
		this.PT = PT;
		PT.PF = this;
		JP = new ImgPanel();
		JP.setBounds(PT.PC_image_x, PT.PC_image_y, PT.PC_image_width, PT.PC_image_height);
		JP.addMouseListener(this);
		JP.setDoubleBuffered(true);
		B_home = new JButton("Home");
		B_menu = new JButton("Menu");
		B_back = new JButton("Back");
		B_home.setBounds(PT.PC_button_spacing,PT.PC_frame_height-60, 100, 30);
		B_menu.setBounds(PT.PC_button_spacing*2+100,PT.PC_frame_height-60, 100, 30);
		B_back.setBounds(PT.PC_button_spacing*3+200,PT.PC_frame_height-60, 100, 30);
		B_home.addActionListener(this);
		B_menu.addActionListener(this);
		B_back.addActionListener(this);
		add(JP);
		add(B_home);
		add(B_menu);
		add(B_back);
		
		T_repaint = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					repaint();
				}
			}
		});
	}
	
	public void start(){
		this.setSize(PT.PC_frame_width,PT.PC_frame_height);
		this.setLocationRelativeTo(this);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setLayout(null);
		this.setResizable(false);
		this.setTitle("Android 遠端即時操控系統");
		this.addWindowListener(this);
		this.addMouseListener(this);
		this.setVisible(true);
		T_repaint.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}
	@Override
	public void windowActivated(WindowEvent arg0) {
	}
	@Override
	public void windowClosed(WindowEvent arg0) {
	}
	@Override
	public void windowClosing(WindowEvent arg0) {
		int i = JOptionPane.showConfirmDialog(this,"你確定要離開連線並關閉程式嗎？","離開？",JOptionPane.OK_CANCEL_OPTION);
		if(i == JOptionPane.YES_OPTION){
			System.exit(0);
		}
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}
	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}
	@Override
	public void windowIconified(WindowEvent arg0) {
	}
	@Override
	public void windowOpened(WindowEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getX()>PT.PC_image_x && e.getX()<PT.PC_image_x+PT.PC_image_width){
			if(e.getY()>PT.PC_image_y && e.getY()<PT.PC_image_y+PT.PC_image_height){
				System.out.println("Press:"+e.getX()+","+e.getY());
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getX()>PT.PC_image_x && e.getX()<PT.PC_image_x+PT.PC_image_width){
			if(e.getY()>PT.PC_image_y && e.getY()<PT.PC_image_y+PT.PC_image_height){
				System.out.println("Release:"+e.getX()+","+e.getY());
			}
		}
	}
	
	class ImgPanel extends JPanel{
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(I,0,0,PT.PC_image_width,PT.PC_image_height,this);
		}
	}
}
