import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class Disktree extends JFrame implements TreeSelectionListener{
	DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
	JTree tree = new JTree(rootNode);
	DefaultMutableTreeNode rootNode2 = new DefaultMutableTreeNode();
	JTree tree2 = new JTree(rootNode2);

	public Disktree(){
		setTitle("file transport");
		setSize(740, 500);
		setLayout(null);
		
		tree.addTreeSelectionListener(this);
		JScrollPane js = new JScrollPane(tree,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		js.setBounds(20, 50, 300, 400);
		JScrollPane js2 = new JScrollPane(tree2,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		js2.setBounds(400, 50, 300, 400);

		File[] files = File.listRoots();
		for(File file : files){
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(file);
			if(file.isDirectory()){
				File[] f = file.listFiles();
				for(File ff : f){
					node.add(new DefaultMutableTreeNode(ff));
				}
			}
			rootNode.add(node);
		}
		tree.expandPath(new TreePath(rootNode));
		
		try {
			ServerSocket SS = new ServerSocket(12345);
			Socket SC = SS.accept();
			ObjectInputStream OIS = new ObjectInputStream(SC.getInputStream());
			File sroot = (File)OIS.readObject();
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(sroot);
			File sff[] = (File[])OIS.readObject();
			for(File sf : sff){
				node.add(new DefaultMutableTreeNode(sf));
			}
			rootNode2.add(node);
			tree2.expandPath(new TreePath(rootNode2));
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		
		JButton b1 = new JButton(new ImageIcon("1.jpg"));
		b1.setBounds(135, 5, 40, 40);
		JButton b2 = new JButton(new ImageIcon("2.jpg"));
		b2.setBounds(530, 5, 40, 40);
		JButton b3 = new JButton(new ImageIcon("3.jpg"));
		b3.setBounds(340, 180, 40, 40);
		JButton b4 = new JButton(new ImageIcon("4.jpg"));
		b4.setBounds(340, 230, 40, 40);
		
		add(js);
		add(js2);
		add(b1);
		add(b2);
		add(b3);
		add(b4);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JOptionPane.showMessageDialog(this, "檔案傳送成功\nWindows to Android");
	}

	public static void main(String[] args){
		new Disktree();
	}

	@Override
	public void valueChanged(TreeSelectionEvent e){
		TreePath path = e.getPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
				.getLastPathComponent();

		Object userObject = node.getUserObject();
		if(!(userObject instanceof File)){
			return;
		}
		File folder = (File) userObject;
		if(!folder.isDirectory()){
			return;
		}
		File[] files = folder.listFiles();
		for(File file : files){
			node.add(new DefaultMutableTreeNode(file));
		}

	}
}