package ahn.seong.min;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Test_client extends JFrame {

	User_Main uMain; // new 생성자 없이 가져오기
	User[] user;
	Scanner scan;
	
	JPanel jp1, jp2;
	JButton jb;
	JTextField jtf1, jtf2;
	JLabel j_num, j_count;

	
	JLabel j_id,j_name;
	
	JTextField jtf_id, jtf_name;
	
	public Test_client() {
		
		jp1 = new JPanel();
		jp2 = new JPanel();
		jb = new JButton("확인");
		jtf1 = new JTextField(10);
		jtf2 = new JTextField(10);
		j_num = new JLabel("자리");
		j_count = new JLabel("count");
		j_id = new JLabel("아이디");
		jtf_id = new JTextField(10);
		j_name = new JLabel("이름");
		jtf_name = new JTextField(10);
		
		
		jp2.setLayout(new GridLayout(0, 2));
		
		jp2.add(j_id);
		jp2.add(jtf_id);
		jp2.add(j_name);
		jp2.add(jtf_name);
		
		jp2.add(j_num);
		jp2.add(jtf1);
		jp2.add(j_count);
		jp2.add(jtf2);
		jp2.add(jb);
		
		
		
		jp1.add(jp2);
		
		add(jp1);
		setVisible(true);
		setBounds(1000, 10, 300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = Integer.parseInt(jtf1.getText());
				
				uMain.user_arr[i-1].count = Integer.parseInt(jtf2.getText());
				uMain.user_arr[i-1].name = jtf_name.getText();
				uMain.user_arr[i-1].id = jtf_id.getText();
			}
		});
		
	
		/*
		scan = new Scanner(System.in);
		while (true) {
			System.out.println("=======================");
			System.out.print("자리 : ");
			int i = scan.nextInt();
			System.out.print("숫자 : ");
			uMain.user_arr[i-1].count = scan.nextInt();
		}
		*/

	}

}
