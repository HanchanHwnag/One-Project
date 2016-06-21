package jinwoong;

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
	  	//flag 선언부
	  	
	  	//접속정보(boolean.?
	  	
	  	int on_off_flag; //boolean으로 하지 않는다.
	 */
	
	//JPanel ;
	JLabel j_id, // 아이디 :  
	j_name, // 이름 :
	j_remainTime, // 남은시간 :
	j_remainMoney, // 남은금액 :
	j_pcnum; // 자리번호 :

	JLabel jLabel_Id, //유저 아이디
		   jLabel_name, //유저 이름
		   jLabel_remainTime, //남은시간
		   jLabel_remainMoney, //남은돈
		   jLabel_pcNum; //사용중인 피시정보(고정으로 보이게해야함)

	
	int pcNum; // 고정된 pc자리
	String id, // pc에 앉아있는 user아이디
		   name;// user 실명
	int remainTime,
	    remainMoney;	
	int count = 0;
	int ptime = 0;
	//유저정보 arrrrrrrrrrrrr 생성

	Thread thread;	
	
	public User(int pcNum) {
		this.pcNum = pcNum;
		m_jp = new JPanel();
		m_jp.setLayout(new GridLayout(0, 2));
	//	m_jp.setLayout(new BorderLayout());
		
		j_id = new JLabel        ("아이디         ");		
		jLabel_Id = new JLabel();
		j_name = new JLabel      ("이름            ");
		jLabel_name = new JLabel();
		j_remainTime = new JLabel("남은시간       ");
		jLabel_remainTime = new JLabel();
		jLabel_remainTime.setFont(new Font("Arial",Font.BOLD, 15));
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
//		m_jp.add(j_remainMoney);
//		m_jp.add(jLabel_remainMoney);
		
		
		
		//jLabel_Id = new JLabel();
		jLabel_Id.setFont(new Font("Arial",Font.BOLD, 10));
		
		//m_jp.add(new JLabel((this.pcNum+1)+"번"),BorderLayout.NORTH);
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
	//vo생성, 아이디, pc넘버 가져오기
	
		while(true){
			/*
			 * flag값 받아서 if문체크 후 while문 돌린다.
			 * label while문 thread 소멸 안시키고 돌리기
			 * 
			 * switch case 사용 다른 while 문 돌리기
			 * 
			 * */
			count--;

			//System.out.println("ptime : " + ptime);
			count += ptime;
			ptime = 0;
			//System.out.println(thread.getName());
			//jLabel_Id.setText(count+"");
			if(count>= 0){
				int hours=count/3600;
				int minutes=count%3600/60;
				int secondes=count%3600%60;

				jLabel_remainTime.setText(hours+":"+minutes+":"+secondes);
				jLabel_Id.setText(id);
				jLabel_name.setText(name);
				m_jp.setBackground(Color.LIGHT_GRAY);
			}else {
				count = 0;//카운트값 말고 flag값을 받아서 if문 혹은 switch case문으로 변경해야함
				jLabel_remainTime.setText("");
				jLabel_Id.setText("");
				jLabel_name.setText("");
				m_jp.setBackground(Color.DARK_GRAY);
			}
			//여기서 함수로 받아도됨, 변수값 입력후 바로 설정도 가능?
			//if(count <=0)break; //플레그 검사
			
			try {	
				thread.sleep(1000);//값 변경해야함 sleep값 없음으로, count값 변경하는 클래스 따로 빼야함.
			} catch (Exception e) {
				System.out.println(e);				
			}
		}
	}
}
