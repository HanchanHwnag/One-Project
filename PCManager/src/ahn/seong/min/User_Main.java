package ahn.seong.min;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class User_Main extends JFrame{

	final static int PC_NUM = 20;
	JPanel[] jp;
	static User[] user_arr; //static ���� ������, �ٸ�Ŭ�������� ���������� ���
	JPanel mjp;
	User user;
	public User_Main() {
		//user = new User();
		// pc�ڸ� vo
		// user���� vo ���ε��� �����		
		jp = new JPanel[PC_NUM];
		
		user_arr = new User[PC_NUM]; //
		mjp = new JPanel();
		mjp.setLayout(new GridLayout(4, 5));
		
		for (int i = 0; i < PC_NUM; i++) {
			//user = new User(i); //����
			user_arr[i] = new User(i); 
			jp[i] = user_arr[i];
			jp[i].setBackground(Color.WHITE);
		}
		
		for(int i = 0; i<PC_NUM;i++){
			mjp.add(user_arr[i]);
		}
		add(mjp);
		setVisible(true);
		setBounds(10,10,750,580);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
/*
		Scanner scan = new Scanner(System.in);
		while (true) {
			System.out.println("=======================");
			System.out.print("�ڸ� : ");
			int i = scan.nextInt();
			System.out.print("���� : ");
			user_arr[i-1].count = scan.nextInt();
		}//�ӽ÷� ������ �����尪 ������ ���� �ҽ�
 */
	}
}
