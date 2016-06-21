package chargeServer;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import VO.User_VO;
import dbConnect.ConnectDB;
import jinwoong.Client_Main;
import jinwoong.Protocol;



public class GUITest extends JFrame{
	//ī��
	CardLayout cards;
	JPanel cardPanel,idCard,selectCard,successCard;
	BufferedImage image;
	//card1
	int credit=0;
	String id;
	JLabel creditLabel;
	JPanel welcomePane,inputPane,headerPane;
	JTextField idInputField;
	JButton confirmBtn;
	
	//card2
	JLabel idLabel;
	JRadioButton time1Btn,time2Btn,time3Btn,time4Btn;
	JButton chargeBtn;
	int cmd;
	
	//card3
	JButton doneBtn;
	
	
	//DB
	PreparedStatement pstmt;
	ResultSet rs;
	
	ObjectOutputStream oos;
	ObjectInputStream ois;
	Socket s;
	VO.User_VO user;
	
	Protocol p;
	
	public GUITest() {
		
		try{
//			s=new Socket("203.236.209.49", 7777);
			s=new Socket("localhost", 7777);
			oos=new ObjectOutputStream(s.getOutputStream());
			ois=new ObjectInputStream(s.getInputStream());
		}catch(Exception e){
			e.printStackTrace();
		}

		user= new User_VO();
		
		try {
			image = ImageIO.read(new File("./src/images/Actors1.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cards = new CardLayout();
		cardPanel = new JPanel(cards);
		cardPanel.add(idCard=new JPanel());
		cardPanel.add(selectCard=new JPanel());
		cardPanel.add(successCard=new JPanel());
		
		idCard.setLayout(new BorderLayout());
		
		idCard.add(headerPane=new JPanel(),BorderLayout.NORTH);
		headerPane.add(creditLabel=new JLabel("���� ���Ե� �ݾ� : " + credit + "��"));
		idCard.add(inputPane=new JPanel(new FlowLayout(FlowLayout.CENTER,1,80)),BorderLayout.CENTER);
		
		inputPane.add(new JLabel("���̵� �Է��ϼ���"));
		inputPane.add(idInputField=new JTextField(15));
		idCard.add(confirmBtn=new JButton("Ȯ��"),BorderLayout.SOUTH);
		confirmBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cards.next(cardPanel);
			}
		});
		ButtonGroup bg=new ButtonGroup();
		
		time1Btn=new JRadioButton("1,000�� : 1�ð� 00��");
		time2Btn=new JRadioButton("5,000�� : 5�ð� 10��");
		time3Btn=new JRadioButton("10,000�� : 10�ð� 40��");
		time4Btn=new JRadioButton("50,000�� : 53�ð� 00��");
		bg.add(time1Btn);
		bg.add(time2Btn);
		bg.add(time3Btn);
		bg.add(time4Btn);
		
		selectCard.setLayout(new GridLayout(6, 6));
		
		selectCard.add(idLabel=new JLabel("ID : "+ id));
		selectCard.add(time1Btn);
		selectCard.add(time2Btn);
		selectCard.add(time3Btn);
		selectCard.add(time4Btn);
		selectCard.add(chargeBtn=new JButton("����"));
		time1Btn.setSelected(true);
		chargeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int confirm=JOptionPane.showConfirmDialog(selectCard, "�����Ͻðڽ��ϱ�?", "����Ȯ��",JOptionPane.YES_NO_OPTION);
				cards.next(cardPanel);
			}
		});
		
		successCard.setLayout(new BorderLayout());
		successCard.add(new JLabel("������ �Ϸ�Ǿ����ϴ�",SwingConstants.CENTER),BorderLayout.CENTER);
		successCard.add(doneBtn=new JButton("�Ϸ�"),BorderLayout.SOUTH);
		doneBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		
		add(cardPanel);
		setTitle("�ǽù� �ð� ������");
		setVisible(true);
		setBounds(100, 100, 400, 300);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	public void checkId(String id) {
		String sql="select id from client where id=?";
		try{
			pstmt = ConnectDB.conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			boolean result=rs.next();
			if(result){
				idLabel.setText("ID : "+id);
				cards.next(cardPanel);
				
			}else if(!result){	//�������� �ʴ� ����
				JOptionPane.showMessageDialog(idCard, "�������� �ʴ� ���̵��Դϴ�");
				idInputField.setText("");
				idInputField.requestFocus();
			}				
		
			user.setId(id);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void charge(Protocol p){
		
//		Protocol p=new Protocol(cmd, msg, user);
		try {

			oos.writeObject(p);
			oos.flush();
		} catch (Exception e) {

		}
		
		
	}
	public static void main(String[] args) {
		new GUITest();
	}
	
}



/*
	5001 : 1000 / 60
	5002 : 5000 / 310
	5003 : 10000 / 640
	5004 : 50000 / 3180
*/