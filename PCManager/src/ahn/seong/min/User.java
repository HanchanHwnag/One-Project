package ahn.seong.min;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class User extends JPanel implements Runnable{
	JPanel m_jp;
	JPanel jp_1, // 
		   jp_2,
		   jp_3,
		   jp_4;
	
	/*
	  	//flag �����
	  	
	  	//��������(boolean.?
	  	
	  	int on_off_flag;
	  	
	  	
	 */
	
	//JPanel ;
	JLabel j_id, // ���̵� :  
	j_name, // �̸� :
	j_remainTime, // �����ð� :
	j_remainMoney, // �����ݾ� :
	j_pcnum; // �ڸ���ȣ :

	JLabel jLabel_Id, //���� ���̵�
		   jLabel_name, //���� �̸�
		   jLabel_remainTime, //�����ð�
		   jLabel_remainMoney, //������
		   jLabel_pcNum; //������� �ǽ�����(�������� ���̰��ؾ���)

	
	int pcNum; // ������ pc�ڸ�
	String id, // pc�� �ɾ��ִ� user���̵�
		   name;// user �Ǹ�
	int remainTime,
	    remainMoney;	
	int count = 0;
	
	//�������� arrrrrrrrrrrrr ����

	Thread thread;	
	
	public User(int pcNum) {
		this.pcNum = pcNum;
		m_jp = new JPanel();
		m_jp.setLayout(new GridLayout(0, 2));
	//	m_jp.setLayout(new BorderLayout());
		
		j_id = new JLabel        ("���̵�         ");		
		jLabel_Id = new JLabel();
		j_name = new JLabel      ("�̸�            ");
		jLabel_name = new JLabel();
		j_remainTime = new JLabel("�����ð�       ");
		jLabel_remainTime = new JLabel();
		jLabel_remainTime.setFont(new Font("����",Font.BOLD, 20));
	    j_remainMoney = new JLabel("�����ݾ�    ");
		jLabel_remainMoney = new JLabel();
		j_pcnum = new JLabel("No. ");
		jLabel_pcNum = new JLabel("    "+(this.pcNum+1)+"");
		
		m_jp.add(j_pcnum);
		m_jp.add(jLabel_pcNum);
		m_jp.add(j_id);
		m_jp.add(jLabel_Id);
		m_jp.add(j_name);
		m_jp.add(jLabel_name);
		m_jp.add(j_remainTime);
		m_jp.add(jLabel_remainTime);
		m_jp.add(j_remainMoney);
		m_jp.add(jLabel_remainMoney);
		
		
		
		//jLabel_Id = new JLabel();
		jLabel_Id.setFont(new Font("Arial",Font.BOLD, 10));
		
		//m_jp.add(new JLabel((this.pcNum+1)+"��"),BorderLayout.NORTH);
		//m_jp.add(jLabel_Id, BorderLayout.CENTER);
		
		add(m_jp);
		
		//Scanner scan = new Scanner(System.in);
		//count = scan.nextInt();
		
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}

	public User(){
		System.out.println(count);
	}
	
	
	@Override
	public void run() {
	//Scanner scan = new Scanner(System.in);
	//vo����, ���̵�, pc�ѹ� ��������
	
		while(true){
			count--;
			//System.out.println(thread.getName());
			//jLabel_Id.setText(count+"");
			if(count>= 0){
			jLabel_remainTime.setText(count+"");
			jLabel_Id.setText(id);
			jLabel_name.setText(name);
			m_jp.setBackground(Color.WHITE);
			}else {
				count = 0;
				jLabel_remainTime.setText("");
				jLabel_Id.setText("");
				jLabel_name.setText("");
				m_jp.setBackground(Color.DARK_GRAY);
				
				//flag�� 0���� . 
				// thread ����?
			}
			//���⼭ �Լ��� �޾Ƶ���, ������ �Է��� �ٷ� ������ ����?
			//if(count <=0)break; //�÷��� �˻�
			
			try {	
				thread.sleep(1000);
			} catch (Exception e) {
				System.out.println(e);				
			}
		}
	}
}
