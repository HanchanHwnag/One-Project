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
	static User[] user_arr; //static 으로 변경함, 다른클래스에서 범용적으로 사용
	JPanel mjp;
	User user;
	public User_Main() {
		//user = new User();
		// pc자리 vo
		// user정보 vo 따로따로 만들기		
		jp = new JPanel[PC_NUM];
		
		user_arr = new User[PC_NUM]; //
		mjp = new JPanel();
		mjp.setLayout(new GridLayout(4, 5));
		
		for (int i = 0; i < PC_NUM; i++) {
			//user = new User(i); //수정
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
			System.out.print("자리 : ");
			int i = scan.nextInt();
			System.out.print("숫자 : ");
			user_arr[i-1].count = scan.nextInt();
		}//임시로 각각의 스레드값 변경을 위한 소스
 */
	}
}
