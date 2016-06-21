package memberlist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class MemberList {
	JFrame jf;
	JPanel jpUp, jpCt;
	
	// 위 화면
	JPanel jpUp1, jpUp2, jpUp3;
	JTextField jtfSearch, jtfRestTime;
	JTextField[] jtfArr; // 0->Num, 1->Name, 2->Id, 3->Pwd, 4->Birth, 5->Phone
	String[] listData = {"회원번호", "이름", "아이디", "패스워드", "생일", "휴대폰"};
	
	// 아래 화면
	JPanel jpCt1, jpCt2, jpCt3;
	
	JTable table;
	JScrollPane jspCt;
	Object[] title = {"회원 번호", "아이디", "이름", "남은 시간", "나이", "접속중"};
	DefaultTableModel model;
	JButton resetBtn;
	
	// DB
	String user = "hr";
	String pwd = "1111";
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	Connection c;
	Statement stmt;
	ResultSet rs;
	String sql;
	int i;
	
	public MemberList() {
		jf = new JFrame();
		//jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setBackground(Color.WHITE);
		
		jpUp = new JPanel(new BorderLayout());
		setJPanel(jpUp);
		jpUp1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		setJPanel(jpUp1);
		jpUp2 = new JPanel(new GridLayout(3, 4, 10, 10));
		setJPanel(jpUp2);
		jpUp3 = new JPanel();
		setJPanel(jpUp3);
		
		jtfArr = new JTextField[6];
		for(i=0; i<jtfArr.length; i++) {
			jtfArr[i] = new JTextField(10);
			jtfArr[i].setEditable(false);
		}
	
		jpUp1.add(new JLabel("개인정보"));
		for(i=0; i<jtfArr.length; i++) {
			JLabel temp = new JLabel(listData[i]);
			jpUp2.add(temp);
			jpUp2.add(jtfArr[i]);
		}
		
		jpUp3.add(new JLabel("현재 남은 시간 : "));
		jtfRestTime = new JTextField(10);
		jtfRestTime.setEditable(false);
		jpUp3.add(jtfRestTime);
		
		jpUp.add(jpUp1, BorderLayout.NORTH);
		jpUp.add(jpUp2, BorderLayout.CENTER);
		jpUp.add(jpUp3, BorderLayout.SOUTH);
		
		// 중앙
		jpCt = new JPanel(new BorderLayout());
		jpCt1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jpCt1.add(new JLabel("회원 리스트"));
		setJPanel(jpCt1);
		jpCt2 = new JPanel(new BorderLayout());
		
		// 수정 불가하도록 설정
		model = new DefaultTableModel(title, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		table = new JTable(model);
		table.setBorder(BorderFactory.createEtchedBorder());
		
		// 정렬
		table.setAutoCreateRowSorter(true);
		TableRowSorter tSorter = new TableRowSorter(table.getModel());
		table.setRowSorter(tSorter);
		
		// 테이블 데이터를 클릭 시 이벤트
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				Integer num = (Integer)table.getValueAt(table.getSelectedRow(), 0);
				String sql = "select * from client where idx=" + num;

				try {
					Class.forName("oracle.jdbc.OracleDriver");
					
					c = DriverManager.getConnection(url, user, pwd);
					stmt = c.createStatement();
					rs = stmt.executeQuery(sql);
					
					while(rs.next()) {
						// 0->Num, 1->Name, 2->Id, 3->Pwd, 4->Birth, 5->Phone
						jtfArr[0].setText(String.valueOf(rs.getInt("idx"))); 
						jtfArr[1].setText(rs.getString("name"));
						jtfArr[2].setText(rs.getString("id"));
						jtfArr[3].setText(rs.getString("pwd"));
						jtfArr[4].setText(rs.getString("birth"));
						jtfArr[5].setText(rs.getString("phone"));
						jtfRestTime.setText(rs.getString("remain_time"));
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				} finally {
					try {
						if(rs != null) rs.close();
						if(stmt != null) stmt.close();
						if(c != null) c.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		});
		select();
		
		jspCt = new JScrollPane(table,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		resetBtn = new JButton("갱신");
		resetBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				model.setNumRows(0);
				select();
			}
		});
		
		jpCt2.add(jspCt, BorderLayout.CENTER);
		jpCt2.add(resetBtn, BorderLayout.SOUTH);
		jpCt.add(jpCt1, BorderLayout.NORTH);
		jpCt.add(jpCt2, BorderLayout.CENTER);
		

		jf.add(jpUp, BorderLayout.NORTH);
		jf.add(jpCt, BorderLayout.CENTER);
		jf.setVisible(true);
		jf.pack();
	}
	
	public void setJPanel(JPanel jp) {
		jp.setBackground(Color.WHITE);
		jp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
	// 데이터 불러오기
	public void select(){		
		String sql = "select * from client";
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			
			c = DriverManager.getConnection(url, user, pwd);
			stmt = c.createStatement();
			rs = stmt.executeQuery(sql);
			
			String connected;
			while(rs.next()) {
				if(rs.getInt("connected") == 0)
					connected = "비접속";
				else
					connected = "접속";
				model.addRow(new Object[]{
						rs.getInt("idx"), 
						rs.getString("id"), 
						rs.getString("name"),
						rs.getInt("remain_time"), 
						String.valueOf(calculate(rs.getString("birth"))), 
						connected
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(c != null) c.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public int calculate(String date) {
		Calendar c = Calendar.getInstance();
		String[] dd = date.split("/");
		
		return c.get(Calendar.YEAR) - Integer.parseInt(dd[0]) + 1;
	}
}
