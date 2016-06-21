package jinwoong;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.StyledEditorKit.BoldAction;

import accounting.Accounting;
import chargeServer.Charge_Client;
import memberlist.MemberList;
import stock.Goods;

public class User_Main extends JFrame{

	final static int PC_NUM = 31;
	JPanel[] jp;
	static User[] user_arr; //static 으로 변경함, 다른클래스에서 범용적으로 사용
	JPanel mjp,jp1,jp2;//메인 패널
	
	JButton jb1, jb2,jb3,jb4;
	
	User user;
	public User_Main() {
		
		jb1 = new JButton("회원관리") ;
				jb2 = new JButton("회계관리");
				jb3 = new JButton("재고관리");
				jb4 = new JButton("시간추가");
		
		jp1 = new JPanel();
		jp1.setLayout(new BorderLayout());
		jp2 = new JPanel();
		//user = new User();
		// pc자리 vo
		// user정보 vo 따로따로 만들기		
		jp = new JPanel[PC_NUM];
		
		user_arr = new User[PC_NUM]; //
		mjp = new JPanel();
		mjp.setLayout(new GridLayout(6, 6));
		
		for (int i = 0; i < PC_NUM; i++) {
			//user = new User(i); //수정
			user_arr[i] = new User(i); 
			jp[i] = user_arr[i];
			jp[i].setBackground(Color.WHITE);
		}
		
		for(int i = 0; i<PC_NUM;i++){
			mjp.add(user_arr[i]);
		}
		
		jp2.add(jb1);
		jp2.add(jb2);
		jp2.add(jb3);
		jp2.add(jb4);
		jb1.setFont(new Font("돋움", Font.BOLD	, 30));
		jb1.setBackground(Color.WHITE);
		jb1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new MemberList();
			}
		});
		jb2.setFont(new Font("돋움", Font.BOLD	, 30));
		jb2.setBackground(Color.WHITE);
		jb2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new Accounting();
			}
		});
		jb3.setFont(new Font("돋움", Font.BOLD	, 30));
		jb3.setBackground(Color.WHITE);
		jb3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new Goods();
				
			}
		});
		jb4.setFont(new Font("돋움", Font.BOLD	, 30));
		jb4.setBackground(Color.WHITE);
		
		jb4.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new Charge_Client();
			}
		});
		jp1.add(mjp ,BorderLayout.CENTER);
		jp1.add(jp2, BorderLayout.SOUTH);
		add(jp1);
		
		setVisible(true);
		setBounds(10,10,1500,850);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	}
}
