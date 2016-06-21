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


public class Insert {
	JFrame jf;
	JPanel jp1, jp2, jpN, jp3, jp4, jpS, jp5;
	JLabel jLabel1, jLabel2, jLabel3, jLabel4;
	JTextField name, price, stock;
	JButton input, exit;	
	
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
		
	public Insert() {
		jf = new JFrame();
		jLabel1 = new JLabel("상품명");
		name = new JTextField(10);
		jLabel2 = new JLabel("가  격");
		price = new JTextField(10);
		jLabel3 = new JLabel("재  고");
		stock = new JTextField(10);
		input = new JButton("갱신");
		exit = new JButton("닫기");
		
		jp1 = new JPanel();
		jp2 = new JPanel();
		jp3 = new JPanel();
		jp4 = new JPanel();
		jp5 = new JPanel();
		jpN = new JPanel();
		
		jpN.setLayout(new BoxLayout(jpN,BoxLayout.Y_AXIS));
		
		jp1.add(jLabel1);
		jp1.add(name);
		jp2.add(jLabel2);
		jp2.add(price);
		jp3.add(jLabel3);
		jp3.add(stock);
		jp5.add(input);
		jp5.add(exit);
		
		jpN.add(jp1);
		jpN.add(jp2);
		jpN.add(jp3);
		jf.add(jpN,BorderLayout.CENTER);
		jf.add(jp5,BorderLayout.SOUTH);
		
		jf.setBounds(100, 100, 150, 300);
		jf.setVisible(true);
		
		input.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				init();
				try {
					String sql = "update product set cate_price=?,cate_stock=? where cate_name=?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, Integer.parseInt(price.getText().trim()));
					pstmt.setInt(2, Integer.parseInt(stock.getText().trim()));
					pstmt.setString(3, name.getText().trim());
					int result =  pstmt.executeUpdate();
					if(result>0){
						name.setText("");
						price.setText("");
						stock.setText("");
												
					}else{
						JOptionPane.showMessageDialog(null, "갱신실패2");	
					}
				} catch (Exception e2) {
					JOptionPane.showConfirmDialog(null, "갱신실패2");	
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
			System.out.println("접속오류");
		}
	}
	
	
}
