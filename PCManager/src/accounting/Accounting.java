package accounting;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class Accounting {
	JFrame jf;
	
	// 오른쪽 화면(중앙)
	JPanel jpR, jpR1, jpR2;
	JTable tableS, tableE;
	JScrollPane jspR1, jspR2;
	Object[] titleSales = {"유 형", "일 자", "회원 번호", "금 액", "내 역"};
	Object[] titleExpenditure = {"유 형", "일 자", "금 액", "내 역"};
	DefaultTableModel modelSales, modelExpenditure;
	
	// 위쪽 화면
	JPanel jpUp, jpUp1, jpUp2, jpUp3;
	JTextField jtfDate, jtfAdmin, jtfSales, jtfExpenditure;
	JButton jbSearch, jbTotal;
	
	// DB
	String user = "hr";
	String pwd = "1111";
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	Connection c;
	Statement stmt;
	ResultSet rs;
	String sql;
	int i;
	
	public Accounting() {
		jf = new JFrame("회계 관리");
		// jf.setDefaultCloseOperation(JFrame);
		
		jpR = new JPanel(new GridLayout(2, 1));
		jpR1 = new JPanel(new BorderLayout());
		jpR2 = new JPanel(new BorderLayout());
		
		modelSales = new DefaultTableModel(titleSales, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		tableS = new JTable(modelSales);
		tableS.setBorder(BorderFactory.createEtchedBorder());
		modelExpenditure = new DefaultTableModel(titleExpenditure, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		tableS.setAutoCreateRowSorter(true);
		TableRowSorter tSorter = new TableRowSorter(tableS.getModel());
		tableS.setRowSorter(tSorter);
		tableE = new JTable(modelExpenditure);
		tableE.setBorder(BorderFactory.createEtchedBorder());
		tableE.setAutoCreateRowSorter(true);
		TableRowSorter tSorter2 = new TableRowSorter(tableE.getModel());
		tableE.setRowSorter(tSorter2);
		
		jspR1 = new JScrollPane(tableS,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		jpR1.add(new JLabel("매 출"), BorderLayout.NORTH);
		jpR1.add(jspR1, BorderLayout.CENTER);
		
		jspR2 = new JScrollPane(tableE,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		jpR2.add(new JLabel("지 출"), BorderLayout.NORTH);
		jpR2.add(jspR2, BorderLayout.CENTER);
		
		jpUp = new JPanel(new BorderLayout());
		
		jpUp1 = new JPanel(new GridLayout(1, 4));
		jpUp1.add(new JLabel("검색할 일 자"));
		jtfDate = new JTextField(10);
		jtfDate.setForeground(Color.LIGHT_GRAY);
		jtfDate.setText("00/00/00");
		jtfDate.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				if(jtfDate.getText().trim() == null || jtfDate.getText().trim().equals("")) {
					jtfDate.setForeground(Color.LIGHT_GRAY);
					jtfDate.setText("00/00/00");
				}
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				jtfDate.setForeground(Color.BLACK);
				jtfDate.setText("");
			}
		});
		
		jpUp1.add(jtfDate);
		jpUp1.add(new JLabel("관 리 자"));
		jtfAdmin = new JTextField(10);
		jtfAdmin.setForeground(Color.LIGHT_GRAY);
		jtfAdmin.setText("10001");
		jpUp1.add(jtfAdmin);
		jtfAdmin.setEnabled(false);
		jtfAdmin.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				if(jtfAdmin.getText().trim() == null || jtfAdmin.getText().trim().equals("")) {
					jtfAdmin.setForeground(Color.LIGHT_GRAY);
					jtfAdmin.setText("10001");
				}
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				jtfAdmin.setForeground(Color.BLACK);
				jtfAdmin.setText("");
			}
		});
		
		jpUp2 = new JPanel(new GridLayout(1, 4));
		jpUp2.add(new JLabel("총 매출"));
		jtfSales = new JTextField(10);
		jtfSales.setEditable(false);
		jpUp2.add(jtfSales);
		jpUp2.add(new JLabel("총 지출"));
		jtfExpenditure = new JTextField(10);
		jpUp2.add(jtfExpenditure);
		jtfExpenditure.setEditable(false);
		
		jpUp3 = new JPanel(new GridLayout(1, 2));
		jbSearch = new JButton("검 색");
		jbSearch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				if(jtfDate.getForeground() == Color.LIGHT_GRAY && jtfAdmin.getForeground() == Color.LIGHT_GRAY){
					JOptionPane.showMessageDialog(null, "일자를 입력해주세요");
				} else if(jtfAdmin.getForeground() == Color.LIGHT_GRAY) { // Date
					select(1, jtfDate.getText(), null);
				} 
			}
		});
		jbTotal = new JButton("전 체");
		jbTotal.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				select(0, null, null);
			}
		});
		jpUp3.add(jbSearch);
		jpUp3.add(jbTotal);
		
		jpUp.add(jpUp1, BorderLayout.NORTH);
		jpUp.add(jpUp2, BorderLayout.CENTER);
		jpUp.add(jpUp3, BorderLayout.SOUTH);
		
		jpR.add(jpR1);
		jpR.add(jpR2);
		
		jf.add(jpUp, BorderLayout.NORTH);
		jf.add(jpR, BorderLayout.CENTER);
		jf.setVisible(true);
		jf.setBounds(100,100, 500, 700);
		select(0, null, null);
		jf.requestFocus();
	}
	 
	private void select(int cmd, String value, String value2) { // 0 -> 모두 추출, 1 -> 일자, 2 -> 관리자
		modelSales.setNumRows(0);
		modelExpenditure.setNumRows(0);
		String sql;
		
		if(cmd == 0) {
			sql = "select * from money";
		} else {
			sql = "select * from money where today='" + value + "'";
		}
		
		int sumS=0, sumE=0;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			
			c = DriverManager.getConnection(url, user, pwd);
			stmt = c.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				if(rs.getInt("code") == 0) { // 매입
					modelSales.addRow(new Object[] {
						rs.getInt("code"),
						rs.getString("today"),
						rs.getString("id"),
						rs.getInt("money"),
						rs.getString("history")
					});
					sumS += rs.getInt("money");
				}
				if(rs.getInt("code") == 1) { // 지출
					modelExpenditure.addRow(new Object[] {
						rs.getInt("code"),
						rs.getString("today"),
						rs.getInt("money"),
						rs.getString("history")
					});
					sumE += rs.getInt("money");
				}
			}
			
			jtfExpenditure.setText(String.valueOf(sumE));
			jtfSales.setText(String.valueOf(sumS));
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
}
