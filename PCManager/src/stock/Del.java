package stock;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Del {
	JFrame jf;
	JPanel jp1, jp2;
	JLabel jLabel1;
	JTextField name;
	JButton input, exit;	
	
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	
	public Del() {
		jf = new JFrame();
		jLabel1 = new JLabel("��ǰ��");
		name = new JTextField(10);
		input = new JButton("����");
		exit = new JButton("�ݱ�");
		
		jp1 = new JPanel();
		jp2 = new JPanel();
		
		
		
		
		jp1.add(jLabel1);
		jp1.add(name);
		jp2.add(input);
		jp2.add(exit);
		
		
		jf.add(jp1,BorderLayout.CENTER);
		jf.add(jp2,BorderLayout.SOUTH);
		
		jf.setBounds(100, 100, 150, 150);
		jf.setVisible(true);
		
		input.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				 init();
				  try {
					String sql = "delete from product where cate_name =?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, name.getText().trim());
					int result = pstmt.executeUpdate();
					if(result>0){
						JOptionPane.showMessageDialog(null, name.getText().trim() + " �����Ǿ����ϴ�");
						name.setText("");
					}else{
						JOptionPane.showMessageDialog(null, "��������");	
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "��������");// TODO: handle exception
				}
			}
		});
				
		exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				jf.dispose();
				
			}
		});
	
	}
	public void init(){
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			String url = "jdbc:oracle:thin:@203.236.209.49:1521:xe";
			String user = "hr";
			String password ="1111";
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			System.out.println("���ӿ���");
		}
	}
	
	
}
