import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class MainFrame extends JFrame implements ActionListener{
	ProjectTable PT;
	JLabel LB_name;
	JLabel LB_team_leader;
	JLabel LB_team_member;
	JLabel LB_tim;
	JLabel LB_umi;
	JLabel LB_long;
	JLabel LB_teacher;
	JLabel LB_team;
	JButton B_enter;
	JButton B_exit;
	
	public static void main(String[] args) {
		new MainFrame().start();
	}

	public MainFrame(){
		PT = new ProjectTable();
		PT.MF = this;
		LB_name = new JLabel("Android 遠端即時操控系統");
		LB_name.setFont(new Font("標楷體",0,35));
		LB_team_leader = new JLabel("組長");
		LB_team_member = new JLabel("組員");
		LB_tim = new JLabel("AM001871    黃騰嶢");
		LB_umi = new JLabel("AM001909    王海慧");
		LB_long = new JLabel("AM001959    彭俊龍");
		LB_teacher = new JLabel("指導教授                    黃信貿");
		LB_team = new JLabel("真理資工    嶢海龍©");
		LB_team.setFont(new Font(null,0,12));
		B_enter = new JButton("Enter");
		B_exit = new JButton("Exit");

		LB_name.setBounds(35, 20, 430, 30);
		LB_team_leader.setBounds(150, 70, 50, 30);
		LB_team_member.setBounds(150, 100, 50, 30);
		LB_tim.setBounds(240, 70, 140, 30);
		LB_umi.setBounds(240, 100, 140, 30);
		LB_long.setBounds(240, 130, 140, 30);
		LB_teacher.setBounds(170, 180, 200, 30);
		LB_team.setBounds(380, 245, 150, 30);
		B_enter.setBounds(100, 220, 100, 30);
		B_exit.setBounds(300, 220, 100, 30);
		
		B_enter.addActionListener(this);
		B_enter.setCursor(new Cursor(Cursor.HAND_CURSOR));
		B_exit.addActionListener(this);
		B_exit.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		add(LB_name);
		add(LB_team_leader);
		add(LB_team_member);
		add(LB_tim);
		add(LB_umi);
		add(LB_long);
		add(LB_teacher);
		add(LB_team);
		add(B_enter);
		add(B_exit, BorderLayout.CENTER);
	}
	
	public void start() {
		this.setSize(500,300);
		this.setLocationRelativeTo(this);
		this.setLayout(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("Android 遠端即時操控系統");
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(B_enter)){
			if(PT.CF != null){
				PT.CF.resume();
				this.setVisible(false);
			}else{
				this.setVisible(false);
				new ConnectFrame(PT).start();
			}
		}else if(e.getSource().equals(B_exit)){
			System.exit(0);
		}
	}
}
