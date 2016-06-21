package stock;

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
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class Goods {
	JFrame jf;
	JPanel jpUp, jpCt, snack, drink, noodle, coffee, set;
	JPanel jpUp1;
	JPanel jpCt1, jpCt2, jpCt3;
	JTable table1;
	JScrollPane jspCt1;
	Object[] title = {"상품명", "금액", "재고", "비고" };
	DefaultTableModel model;
	JButton snackBtn, drinkBtn, noodleBtn, coffeeBtn, setBtn, allBtn,  inputBtn, insertBtn, delBtn;
	
	
	// DB
	String user = "hr";
	String pwd = "1111";
	String url = "jdbc:oracle:thin:@203.236.209.49:1521:xe";
	Connection c;
	Statement stmt;
	ResultSet rs;
	
	//String sql;
	int cateNum;
	
	public Goods() {
		jf = new JFrame();
		jf.setBackground(Color.WHITE);
		
		
		jpUp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		
		jpUp.add(new JLabel("상품정보"));
		jpUp.setBackground(Color.WHITE);
		
		
		
				
		// 중앙
		jpCt = new JPanel(new BorderLayout());
				
		// 수정 불가하도록 설정
		model = new DefaultTableModel(title, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		
		
		table1 = new JTable(model);
		TableRowSorter tSorter = new TableRowSorter(table1.getModel());
		table1.setBorder(BorderFactory.createEtchedBorder());
		table1.setAutoCreateRowSorter(true);
		table1.setRowSorter(tSorter);
				
		
        
		
		jspCt1 = new JScrollPane(table1,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		snackBtn = new JButton("스넥");
		
		snackBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cls();
				select("select * from product where cate_code=0");
				
			}
		});
		
		drinkBtn = new JButton("음료");
		
        drinkBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cls();
				select("select * from product where cate_code=1");
				
			}
		});
		noodleBtn = new JButton("커피");
		
        noodleBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cls();
				select("select * from product where cate_code=2");
				
			}
		});
		coffeeBtn = new JButton("라면");
		
        coffeeBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cls();
				select("select * from product where cate_code=3");
				
			}
		});
		setBtn = new JButton("셋트");
		
        setBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cls();
				select("select * from product where cate_code=4");
				
			}
		});
		
		allBtn = new JButton("전체");
		allBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cls();
				select("select * from product");
				
			}
		});
		
		inputBtn = new JButton("상품등록");
				
		inputBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Input();
				
			}
		});
		insertBtn = new JButton("재고변경");
		insertBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new Insert();
				
			}
		});
		
		delBtn = new JButton("상품삭제");
		delBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new Del();
				
			}
		});
		
		jpUp1 = new JPanel();
		jpUp1.add(snackBtn);
		jpUp1.add(drinkBtn);
		jpUp1.add(noodleBtn);
		jpUp1.add(coffeeBtn);
		jpUp1.add(setBtn);
		jpUp1.add(allBtn);
		
		jpCt2 = new JPanel();
		jpCt2.add(inputBtn);
		jpCt2.add(insertBtn);
		jpCt2.add(delBtn);
		
		jpCt.add(jspCt1, BorderLayout.CENTER);
		jpCt.add(jpCt2, BorderLayout.SOUTH);
		
		
		jf.add(jpUp, BorderLayout.NORTH);
		jf.add(jpUp1, BorderLayout.CENTER);
		jf.add(jpCt, BorderLayout.SOUTH);
		jf.setVisible(true);
		jf.pack();
	}
	
	public void setJPanel(JPanel jp) {
		jp.setBackground(Color.WHITE);
		jp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
	// 데이터 불러오기
	public void select(String sql){		
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			
			c = DriverManager.getConnection(url, user, pwd);
			stmt = c.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				model.addRow(new Object[]{
						rs.getString("cate_name"), 
						rs.getInt("cate_price"), 
						rs.getInt("cate_stock"),
						
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
	public void cls(){
		DefaultTableModel model = (DefaultTableModel)table1.getModel();

		model.setNumRows(0);
	}
	
}
