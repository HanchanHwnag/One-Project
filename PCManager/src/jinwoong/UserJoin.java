package jinwoong;


import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import VO.User_VO;



public class UserJoin extends JFrame{

	JPanel centerPanel,northPanel,southPanel;
	JPanel idPanel,namePanel,passwordPanel,passwordComfirmPanel,
	addressPanel,birthdayPanel,phoneNumberPanel;
	
	JLabel northLabel,idLabel,passwordLabel,passwordComfirmLabel,nameLabel,
	addressLabel,birthdayLabel,phoneNumberLabel;
	
	JLabel passwordCheckLabel;
	
	JButton redundancyCheckButton,submitButton;
	
	JTextField idField,addressField,nameField,
	           phoneNumberField1,phoneNumberField2,phoneNumberField3;
	
	JPasswordField passwordField,passwordConfirmField;	
	
	JComboBox<String> yearComboBox,monthComboBox,dateComboBox;	
	Vector<String> yearData,monthData,dateData;
	
	
	String seletedYear,selectedMonth,selectedDate;
	
	
	
	
	boolean redundancyCheck;	
	
	
	ObjectOutputStream oos;
	ObjectInputStream ois;
	Socket s;
	
	public UserJoin(){		
   
		try{
			s=new Socket("localhost", 7777);
			oos= new ObjectOutputStream(s.getOutputStream());
			ois=new ObjectInputStream(s.getInputStream());
		}catch(Exception e){
			e.printStackTrace();
		}
		//����г�
		northPanel = new JPanel();
		northPanel.add(northLabel = new JLabel("ȸ������"));		
		
		//�߰��г�		
		
		
		idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));		
		
		idPanel.add(idLabel=new JLabel(String.format("%-17s", "���̵� ")));
		idPanel.add(idField=new JTextField(10));
		idPanel.add(redundancyCheckButton=new JButton("�ߺ��˻�"));
		
		
		
		passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));	
		passwordPanel.add(passwordLabel=new JLabel(String.format("%-13s", "��й�ȣ")));
		passwordPanel.add(passwordField=new JPasswordField(10));
		
		
		passwordComfirmPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		passwordComfirmPanel.add(passwordComfirmLabel=new JLabel(String.format("%-7s", "��й�ȣȮ��")));
		passwordComfirmPanel.add(passwordConfirmField=new JPasswordField(10));
		passwordComfirmPanel.add(passwordCheckLabel=new JLabel(""));
		
		namePanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		namePanel.add(nameLabel=new JLabel(String.format("%-20s", "�̸�")));
		namePanel.add(nameField=new JTextField(15));
		
		
		addressPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		addressPanel.add(addressLabel=new JLabel(String.format("%-20s", "�ּ�")));
		addressPanel.add(addressField=new JTextField(15));
		
		
		
		
		birthdayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		birthdayPanel.add(birthdayLabel=new JLabel(String.format("%-13s", "�������")));
		
		
		GregorianCalendar calendar = new GregorianCalendar();
		int year=calendar.get(Calendar.YEAR);
		yearData=new Vector<>();
		monthData=new Vector<>();
		dateData=new Vector<>();
		
	
		
		for(int i=year; i>year-100;i--){
			yearData.add(i+"��");
		}
		
		for(int i=1;i<13;i++){
			if(i < 10)
				monthData.add("0"+i+"��");
			else
				monthData.add(i+"��");
		}
		
		for(int i=1;i<32;i++){
			if(i < 10)
				dateData.add("0" + i + "��");
			else
				dateData.add(i+"��");
		}		
		
		birthdayPanel.add(yearComboBox=new JComboBox<>(yearData));
		birthdayPanel.add(monthComboBox=new JComboBox<>(monthData));
		birthdayPanel.add(dateComboBox=new JComboBox<>(dateData));
		
		
		phoneNumberPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		phoneNumberPanel.add(phoneNumberLabel=new JLabel(String.format("%-16s", "�޴���")));
		phoneNumberPanel.add(phoneNumberField1=new JTextField(4));
		phoneNumberPanel.add(new JLabel("-"));
		phoneNumberPanel.add(phoneNumberField2=new JTextField(4));
		phoneNumberPanel.add(new JLabel("-"));
		phoneNumberPanel.add(phoneNumberField3=new JTextField(4));
		
		
		centerPanel=new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));		
		centerPanel.add(idPanel);
		centerPanel.add(passwordPanel);
		centerPanel.add(passwordComfirmPanel);
		centerPanel.add(namePanel);
		centerPanel.add(addressPanel);
		centerPanel.add(birthdayPanel);
		centerPanel.add(phoneNumberPanel);
		///////////////////////////////////////////////
		
		
		//�ϴ��г�
		southPanel = new JPanel();
		southPanel.add(submitButton=new JButton("���ԿϷ�"));		
		
		add(northPanel,"North");
		add(centerPanel,"Center");
		add(southPanel,"South");
		
		setVisible(true);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100,100,380,380);
		setResizable(false);
		
		
		
		
		
		
		
		//���ԿϷ��ư
		submitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String id=idField.getText();
				String name=nameField.getText();
				
				String password=new String(passwordField.getPassword());
				String passwordComfirm=new String(passwordConfirmField.getPassword());
				String address=addressField.getText();
				
				String birthday=seletedYear.substring(0,seletedYear.length()-1)+
						"/"+selectedMonth.substring(0,selectedMonth.length()-1)+
						"/"+selectedDate.substring(0,selectedDate.length()-1);
				
				String phoneNumber1=phoneNumberField1.getText();
				String phoneNumber2=phoneNumberField2.getText();
				String phoneNumber3=phoneNumberField3.getText();
				String phoneNumber=phoneNumber1+phoneNumber2+phoneNumber3;
				

				
				
				if(id.length()<1){
					JOptionPane.showMessageDialog(null, "���̵� �Է����ּ���");
					idField.requestFocus();
					return;
				}
				if(redundancyCheck==false){
					JOptionPane.showMessageDialog(null, "�ߺ�üũ�� ���ּ���");
					return;
				}
				
				if(password.length()<1){
					JOptionPane.showMessageDialog(null, "��й�ȣ���Է����ּ���");
					passwordField.requestFocus();
					return;
				}
				
				if(passwordComfirm.length()<1){
					JOptionPane.showMessageDialog(null, "��й�ȣȮ���� �Է����ּ���");
					passwordConfirmField.requestFocus();
					return;
				}
				
				if(name.length()<1){
					JOptionPane.showMessageDialog(null, "�̸��� �Է����ּ���");
					nameField.requestFocus();
					return;
				}

				if(address.length()<1){
					JOptionPane.showMessageDialog(null, "�ּҸ� �Է����ּ���");
					addressField.requestFocus();
					return;
				}
				if(seletedYear==null){
					JOptionPane.showMessageDialog(null, "�⵵�� �������ּ���");
					
					return;
				}
				if(selectedMonth==null){
					JOptionPane.showMessageDialog(null, "���� �������ּ���");
					return;
				}
				if(selectedDate==null){
					JOptionPane.showMessageDialog(null, "���� �������ּ���");
					return;
				}
				
				
				if(phoneNumber1.length()!=3){
					JOptionPane.showMessageDialog(null, "�޴��� ��ȣ�� ����� �Է����ּ���");
					phoneNumberField1.requestFocus();
					return;
				}
				if(phoneNumber2.length()<3){
					JOptionPane.showMessageDialog(null, "�޴��� ��ȣ�� ������ �Է����ּ���");
					phoneNumberField2.requestFocus();
					return;
				}
				if(phoneNumber3.length()<4){
					JOptionPane.showMessageDialog(null, "�޴��� ��ȣ�� ������ �Է����ּ���");
					phoneNumberField3.requestFocus();
					return;
				}		
				

				User_VO user=new User_VO();
				user.setId(id);
				user.setName(name);
				user.setPwd(password);
				user.setAddress(address);
				user.setBirth(birthday);
				user.setPhone(phoneNumber);
				int result=0;
				
				Protocol p=new Protocol();
				p.setCmd(100);
				p.setUser(user);
				try {
					oos.writeObject(p);
					oos.flush();
					
					 result=(int)ois.readObject();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
				
				if(result>0){
					JOptionPane.showMessageDialog(null, "ȸ�����ԿϷ�");
					dispose();
				}else{
					JOptionPane.showMessageDialog(null, "ȸ�����Խ���");
				}								
				
			}
		});
		
		//�ߺ�üũ��ư
		redundancyCheckButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Protocol p = new Protocol();
				User_VO user= new User_VO();
				String id=idField.getText();
				user.setId(id);
				p.setCmd(200);
				p.setUser(user);
				String msg=null;
				try{
					oos.writeObject(p);
					oos.flush();
					
					p=(Protocol)ois.readObject();
					
					switch (p.getCmd()) {
					case 201:
						redundancyCheck=true;
						msg="��� ������ ���̵�";
						break;
					case 202:
						redundancyCheck=false;
						msg="��� �Ұ����� �ƾƵ�";
						break;

					default:msg="201 202�ƴ�";						
						break;
					}
					
					JOptionPane.showMessageDialog(null, msg);
					
				}catch(Exception ee){
					ee.printStackTrace();
				}
			}
		});
		
		
		/////////////////////////////////////////////////
		passwordConfirmField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String password=new String(passwordField.getPassword());
				String passwordConfirm=new String(passwordConfirmField.getPassword());
				
				if(password.equals(passwordConfirm)){
					passwordCheckLabel.setForeground(new Color(0,0,255));
					passwordCheckLabel.setText("��й�ȣ�� �����ϴ�.");
					submitButton.setEnabled(true);
				}else{
					passwordCheckLabel.setForeground(new Color(255,0,0));
					passwordCheckLabel.setText("��й�ȣ�� �ٸ��ϴ�.");
					submitButton.setEnabled(false);
				}
			}
		});
		
		

		
		idField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				redundancyCheck=false;
			}
		});
		
		
		
		Item item =new Item();
		yearComboBox.addItemListener(item);
		monthComboBox.addItemListener(item);
		dateComboBox.addItemListener(item);
		
		
		
		
		
	}//������
	
	
	
	
	
	
	class Item implements ItemListener{
		
		public void itemStateChanged(ItemEvent e) {
			JComboBox jcb=(JComboBox)e.getSource();
			
			if(e.getStateChange()==e.SELECTED){
				
				if(jcb==yearComboBox){
					seletedYear=(String) jcb.getSelectedItem();
					
				}else if(jcb==monthComboBox){
					selectedMonth=(String) jcb.getSelectedItem();
				
				}else if(jcb==dateComboBox){
					selectedDate=(String)jcb.getSelectedItem();
									
				}
			}			
		}
	}
	
	
	
	public static void main(String[] args) {
		new UserJoin();
	}
}
