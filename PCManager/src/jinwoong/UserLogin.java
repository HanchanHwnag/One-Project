package jinwoong;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import VO.User_VO;

public class UserLogin extends JFrame{

	JPanel idPanel,passwordPanel,buttonPanel;
	JPanel loginPanel,northPanel,southPanel;
	
	JLabel idLabel,passwordLabel;
	JTextField idField;
	JPasswordField passwordField;
	JButton loginButton,joinButton;
	
	ObjectOutputStream oos;
	ObjectInputStream ois;
	Socket s;
	public UserLogin(){
		
		try{
			s=new Socket("203.236.209.41", 7777);
			oos=new ObjectOutputStream(s.getOutputStream());
			ois=new ObjectInputStream(s.getInputStream());
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("UserLogin()���Ͽ��ῡ��");
		}
		
		idPanel=new JPanel();
		idPanel.add(idLabel=new JLabel("���̵�     "));		
		idPanel.add(idField=new JTextField(10));
		
		passwordPanel=new JPanel();
		passwordPanel.add(passwordLabel=new JLabel("��й�ȣ"));
		passwordPanel.add(passwordField=new JPasswordField(10));
		
		buttonPanel = new JPanel();
		buttonPanel.add(loginButton=new JButton("�α���"));
		buttonPanel.add(joinButton=new JButton("ȸ������"));
		
		loginPanel=new JPanel();
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
		
		loginPanel.add(idPanel);
		loginPanel.add(passwordPanel);
		loginPanel.add(buttonPanel);
		
		northPanel=new JPanel();	
		southPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		
		southPanel.add(new JPanel());
		southPanel.add(loginPanel);		
		
		
		add(northPanel,BorderLayout.NORTH);		
		add(southPanel,"South");		
		
		
		Dimension d= Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, d.width,d.height);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);		
		
		
		
		loginButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String id=idField.getText();
				String pwd=new String(passwordField.getPassword());
				
				if(id.length()<1){
					JOptionPane.showMessageDialog(null, "���̵� �Է����ּ���");
					idField.requestFocus();
					return;
				}
				if(pwd.length()<1){
					JOptionPane.showMessageDialog(null, "��й�ȣ�� �Է����ּ���");
					passwordField.requestFocus();
					return;
				}
				
				
				Protocol p1 =new Protocol();
				VO.User_VO user= new User_VO();
				user.setId(id);
				user.setPwd(pwd);
				p1.setUser(user);
				p1.setCmd(400);
				
				
				
				try{
					oos.writeObject(p1);
					oos.flush();
					
					Protocol p2=(Protocol)ois.readObject();

					if(p2.getCmd()==401) {
						if(p2.getUser().getRemain_time() > 0){						
							new Client_Main(s,oos,ois,p2.getUser());							
							dispose();							
						}else if(p2.getUser().getRemain_time() <= 0){							
							JOptionPane.showMessageDialog(null, "�������ּ���");
						}					
					}					
					else if(p2.getCmd()==402 ){					
						JOptionPane.showMessageDialog(null, "���̵� �Ǵ� ��й�ȣ�� Ʋ�Ƚ��ϴ�.");						
					}else if(p2.getCmd()==403){
						JOptionPane.showMessageDialog(null, "�̹� ������� ���̵� �Դϴ�.");	
					}
					
					
				}catch(Exception e1){
					
				}
			}
		});
		
		
		passwordField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				loginButton.doClick();
				
			}
		});
		
		joinButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new UserJoin();
				
			}
		});
		
	}
	
	public static void main(String[] args) {
		new UserLogin();
	}
	
	
}
