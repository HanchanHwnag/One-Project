package jinwoong;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import VO.Product_VO;
import VO.User_VO;

public class Client_Main extends JFrame{

	JPanel infoPanel,chatPanel;
	JPanel infoOrderPanel,infoTimePanel;
	JPanel chatSouthPanel;
	JPanel aa, bb;
	
	JLabel remainTimeLabel1,remainTimeLabel2,remainTimeLabel3;
	JLabel infoJlabel;
	JButton orderButton;
	
	JTextArea chatJta;
	JScrollPane chatJsp;
	JTextField chatField;
	JButton chatButton,exitButton;
	
	Socket s;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	User_VO user;
	
	public Client_Main(Socket s,ObjectOutputStream oos, ObjectInputStream ois,User_VO user){
		try{
			this.s=s;
			this.oos=oos;
			this.ois=ois;
			this.user=user;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		infoPanel = new JPanel(new BorderLayout());
		infoTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		infoOrderPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		bb = new JPanel(new BorderLayout());
		aa= new JPanel(new FlowLayout(FlowLayout.LEFT));
		aa.add(remainTimeLabel1=new JLabel("남은시간 : "));
		aa.add(remainTimeLabel2=new JLabel(""));

		bb.add(infoTimePanel, BorderLayout.NORTH);
		bb.add(aa, BorderLayout.CENTER);
		
		infoTimePanel.add(infoJlabel=new JLabel("아이디 : "+user.getId()));
		
		infoJlabel.setFont(new Font("돋움", Font.BOLD, 15));
		remainTimeLabel1.setFont(new Font("돋움", Font.BOLD, 15));
		remainTimeLabel2.setFont(new Font("돋움", Font.BOLD, 15));
		
		infoOrderPanel.add(remainTimeLabel3=new JLabel(""));
		infoOrderPanel.add(exitButton=new JButton("사용종료"));
		infoOrderPanel.add(orderButton=new JButton("주문"));
		
		orderButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Protocol p = new Protocol();
				p.setCmd(1000);
				send(p);
			}
		});
		
		infoPanel.add(bb,"North");
		infoPanel.add(infoOrderPanel,"South");
		//////////////////////////////
		chatPanel=new JPanel(new BorderLayout());
		chatSouthPanel=new JPanel();
		
		chatJta = new JTextArea(5,20);
		chatJsp= new JScrollPane(chatJta,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		chatSouthPanel.add(chatField=new JTextField(20));
		chatSouthPanel.add(chatButton=new JButton("전송"));
		
		chatPanel.add(chatJsp,"Center");
		chatPanel.add(chatSouthPanel,"South");
		
		///////////////////////
		
		add(infoPanel,"North");
		add(chatPanel,"Center");
		Dimension d= Toolkit.getDefaultToolkit().getScreenSize();
		
		setVisible(true);
		setBounds(d.width-300,0,300,500);
		setResizable(false);
		//setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		
		new ChatThread().start();
		new TimeThread().start();
		
		chatButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String content=chatField.getText();
				try{
					Protocol p = new Protocol();
					p.setCmd(300);
					p.setMsg(chatField.getText());
					User_VO user=new User_VO();					
					
					p.setUser(user);
					
					oos.writeObject(p);
					oos.flush();
					
					chatField.setText("");
					int len = chatJta.getDocument().getLength();
					chatJta.setCaretPosition(len);
				}catch(Exception ee){
					ee.printStackTrace();
				}
			}
		});	
		
		
		exitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Protocol p =new Protocol();
				p.setUser(user);
				p.setCmd(500);//로그아웃
				
				try {
					oos.writeObject(p);
					oos.flush();
					
				} catch (Exception e2) {
					e2.getMessage();
					
				}
				
				new UserLogin();
				user.setId("");
				user.setIdx(0);
				user.setName("");
				user.setPhone("");
				user.setPwd("");
				
				dispose();
			}
		});
		
		chatField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				chatButton.doClick();
				
			}
		});
		
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				//exitButton.doClick();
			}
			@Override
			public void windowClosing(WindowEvent e) {
				exitButton.doClick();
			}
				
				
		});
	}//생성자

	public void send(Protocol p){
		try {
			oos.writeObject(p);
			oos.flush();
		} catch (Exception e) {

		}
	}
	
	class TimeThread extends Thread{
		@Override
		public void run() {			
			
			while(true){
				try{
					Thread.sleep(1000);
				
				}catch(Exception e){
					
				}
				user.setRemain_time(user.getRemain_time()-1);
				
				int hours=user.getRemain_time()/3600;
				int minutes=user.getRemain_time()%3600/60;
				int seconds=user.getRemain_time()%3600%60;
				String time=hours+":"+minutes+":"+seconds;
				remainTimeLabel2.setText(time+"초");
				
				if(user.getRemain_time()==5){	
					
					remainTimeLabel3.setForeground(Color.RED);
					remainTimeLabel3.setText("5분남았습니다.");
						
				}
				if(user.getRemain_time()<=0){
					exitButton.doClick();
					break;
				}
			}//while				
		}
	}
	
	class ChatThread extends Thread{
		@Override
		public void run() {
			while(true){
				try{
					Protocol p=(Protocol)ois.readObject();
					
					switch(p.getCmd()){
					case 300:
						String msg=p.getMsg();
						chatJta.append(msg+System.getProperty("line.separator"));
						int len = chatJta.getDocument().getLength();
						chatJta.setCaretPosition(len);
						break;
					case 1000: // 상품 주문
						ArrayList<Product_VO> list = p.getList();					
						new OrderProduct(list, Client_Main.this);
						break;	
					case 3000: // 상품 결제
						JOptionPane.showMessageDialog(null, p.getMsg());
						break;
					case 5000:
						
						int time=Integer.parseInt(p.getMsg());
						user.setRemain_time(user.getRemain_time()+time);
						break;
					}					
				}catch(Exception e){
					e.printStackTrace();
				}
			}//while
		
		}//run
	}//ChatThread
	
	
	
}
