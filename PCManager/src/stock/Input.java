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


public class Input {
	Goods g;
	JFrame jf;
	JPanel jp1, jp2, jpN, jp3, jp4, jpS, jp5, jp6;
	JLabel jLabel1, jLabel2, jLabel3, jLabel4, jLabel5;
	JTextField cate, name, price, stock, etc;
	JButton input, exit;	
	
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	
	public Input() {
		
		jf = new JFrame();
		jLabel1 = new JLabel("카테고리");
		cate = new JTextField(10);
		jLabel2 = new JLabel("상품명");
		name = new JTextField(10);
		jLabel3 = new JLabel("가  격");
		price = new JTextField(10);
		jLabel4 = new JLabel("재  고");
		stock = new JTextField(10);
		jLabel5 = new JLabel("이 미 지");
		etc = new JTextField(10);
		input = new JButton("등록");
		exit = new JButton("닫기");
		
		jp1 = new JPanel();
		jp2 = new JPanel();
		jp3 = new JPanel();
		jp4 = new JPanel();
		jp5 = new JPanel();
		jp6 = new JPanel();
		jpN = new JPanel();
		
		jpN.setLayout(new BoxLayout(jpN,BoxLayout.Y_AXIS));
		
		jp1.add(jLabel1);
		jp1.add(cate);
		jp2.add(jLabel2);
		jp2.add(name);
		jp3.add(jLabel3);
		jp3.add(price);
		jp4.add(jLabel4);
		jp4.add(stock);
		jp5.add(jLabel5);
		jp5.add(etc);
		jp6.add(input);
		jp6.add(exit);
		
		jpN.add(jp1);
		jpN.add(jp2);
		jpN.add(jp3);
		jpN.add(jp4);
		jpN.add(jp5);
		jf.add(jpN,BorderLayout.CENTER);
		jf.add(jp6,BorderLayout.SOUTH);
		
		jf.setBounds(100, 100, 150, 350);
		jf.setVisible(true);
		
		input.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				init();
				try {
					String sql = "insert into product values(product_seq.nextval,?,?,?,?,?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, Integer.parseInt(cate.getText().trim()));
					pstmt.setString(2, name.getText().trim());
					pstmt.setInt(3, Integer.parseInt(price.getText().trim()));
					pstmt.setInt(4, Integer.parseInt(stock.getText().trim()));
					pstmt.setString(5, etc.getText().trim());
					
					int result = pstmt.executeUpdate();
					if(result>0){
						cate.setText("");
						name.setText("");
						price.setText("");
						stock.setText("");
						etc.setText("");
												
					}else{
						JOptionPane.showConfirmDialog(null, "삽입실패");
					}
				} catch (Exception e2) {
					System.out.println(e2);
					JOptionPane.showMessageDialog(null, "입력오류");
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
