package jinwoong;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import javax.script.ScriptContext;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.xml.ws.Holder;

import VO.Product_VO;
import VO.User_VO;

public class OrderProduct {
	JFrame jf;
	JPanel jpLeft, jpCenter, jpUp, jpDown;
	
	// 옆 화면
	JButton[] btn;
	String[] items = {"품목", "스낵", "음료", "커피", "라면", "세트"};
	
	// 가운데 윗 화면
	JTable table;
	DefaultTableModel model;
	JScrollPane jspPro;
	String[] title = {"번호", "상품명", "가격", "수량선택"};
	JComboBox<Integer>[] jcbData;
	Integer[] orderNum = {0,1,2,3,4,5,6,7,8,9,10};
	ImageIcon icon;
	JLabel label;
	JButton btnInput;
	
	// 가운데 아래 화면
	JPanel jpDown_1;
	JButton jbSend, jbCancle;
	JTable downTable;
	DefaultTableModel downModel;
	JScrollPane jspDown;
	String[] title2 = {"번호", "선택 품목", "수량", "합산 가격"};
	JTextField jtfCost;
	
	// Network
	Client_Main client;
	
	// DB
	String user = "system";
	String pwd = "oracle";
	String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	Connection c;
	Statement stmt;
	ResultSet rs;
	String sql;
	int i;
	
	ArrayList<Product_VO> list;

	public OrderProduct(ArrayList<Product_VO> list, Client_Main client) {
		jf = new JFrame("주문화면");
		//jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Socket 통신을 위한 변수
		this.list = list;
		this.client = client;
		
		jpLeft = new JPanel(new GridLayout(items.length, 1));
		btn = new JButton[items.length];
		for(int i=0; i<btn.length; i++) {
			btn[i] = new JButton(items[i]);
			final int j = i-1;
			// 해당 버튼 클릭 시, 해당 요소만 출력(스넥, 음료...)
			btn[i].addActionListener(new ActionListener() {
					
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					select(j);
				}
			});
			
			jpLeft.add(btn[i]);
		}
		
		jpCenter = new JPanel(new GridLayout(2, 1));
		
		// 가운데 윗화면
		jpUp = new JPanel(new BorderLayout());
		// 이미지 크기 고정
		icon = new ImageIcon("src\\images\\Tiger.png");
		Image img = icon.getImage().getScaledInstance(150, 230, Image.SCALE_DEFAULT);
		label = new JLabel(new ImageIcon(img));
		jtfCost = new JTextField(5);
		
		// 수량 영역만 수정할 수 있도록 허용
		model = new DefaultTableModel(title, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				if(column == 3)
					return super.isCellEditable(row, column);
				else
					return false;
			}
		};
		table = new JTable(model); 
		table.setBorder(BorderFactory.createEtchedBorder());
		table.addMouseListener(new MouseAdapter() {
		
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				String num = (String)table.getValueAt(table.getSelectedRow(), 0);
				num = num.substring(0, num.length()-1);
				String turl = searchImg(num);
				// 이미지 파일의 경로를 받아서 변경
				icon = new ImageIcon("src\\images\\" + turl);
				Image img = icon.getImage().getScaledInstance(150, 230, Image.SCALE_DEFAULT);
				label.setIcon(new ImageIcon(img));
			}
		});

		select(-1);
		jspPro = new JScrollPane(table,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		btnInput = new JButton("담기");
		btnInput.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = model.getRowCount();
				int col = model.getColumnCount();
				
				// downModel.setNumRows(0);
				int count = 0;
				int cost = Integer.parseInt(jtfCost.getText());
				boolean flag;
				
				for(int i=0; i<row; i++) {
					if(model.getValueAt(i, col-1) != null && (int)model.getValueAt(i, col-1) != 0) {
						flag = true;
						for(int j=0; j<downModel.getRowCount(); j++) {
							if(downModel.getValueAt(j, 0).equals(model.getValueAt(i, 0))){
								cost -= (int)downModel.getValueAt(j, 3);
								downModel.removeRow(j);
								downModel.addRow(new Object[]{
									table.getValueAt(i, 0),
									table.getValueAt(i, 1),
									table.getValueAt(i, 3),
									(int)table.getValueAt(i, 3) * (int)table.getValueAt(i, 2)
								});
								cost += (int)table.getValueAt(i, 3) * (int)table.getValueAt(i, 2);
								count ++;
								flag = false;
								break;
							}
						}
						
						if(flag) {
							count ++;
							downModel.addRow(new Object[]{
								table.getValueAt(i, 0),
								table.getValueAt(i, 1),
								table.getValueAt(i, 3),
								(int)table.getValueAt(i, 3) * (int)table.getValueAt(i, 2)
							});
							cost += (int)table.getValueAt(i, 3) * (int)table.getValueAt(i, 2); 
						}
					}
				}
				jtfCost.setText(cost+"");
				if(count == 0)
					JOptionPane.showMessageDialog(null, "상품을 담아주세요");
				
				for(int i=0; i<row; i++) 
					model.setValueAt(0, i, col-1);
			}
		});
		
		jpUp.add(label, "West");
		jpUp.add(jspPro, "Center");
		jpUp.add(btnInput, "South");
		
		// 가운데 아래 화면
		jpDown = new JPanel(new BorderLayout());
		jpDown_1 = new JPanel();
		downModel = new DefaultTableModel(title2, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		downTable = new JTable(downModel);
		jspDown = new JScrollPane(downTable,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		jbCancle = new JButton("취소");
		jbCancle.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				downModel.setNumRows(0);
				jtfCost.setText(0+"");
			}
		});
		jbSend = new JButton("결제하기");
		jbSend.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = downModel.getRowCount();
				int col = downModel.getColumnCount();
				
				ArrayList<String> idxList = new ArrayList<>();
				ArrayList<Integer> indexList = new ArrayList<>();
				
				for(int i=0; i<row; i++) {
					String idx = (String)downModel.getValueAt(i, 0);
					idx = idx.substring(0,idx.length()-1);
					idxList.add(idx);
					indexList.add(i);
				}
				
				pay(idxList, indexList);
				jf.dispose();
			}
		});
		
		jtfCost.setText(0+"");
		
		jpDown.add(jspDown, BorderLayout.CENTER);
		jpDown_1.add(new JLabel("총 결제금액 : "));
		jpDown_1.add(jtfCost);
		jtfCost.setEnabled(false);
		jpDown_1.add(jbCancle);
		jpDown_1.add(jbSend);
		jpDown.add(jpDown_1, BorderLayout.SOUTH);
		
		jpCenter.add(jpUp);
		jpCenter.add(jpDown);
		
		jf.add(jpLeft, BorderLayout.WEST);
		jf.add(jpCenter, BorderLayout.CENTER);
		jf.setBounds(100,100,500,500);
		jf.setVisible(true);
	}
	
	// 데이터 불러오기
	public void select(int cmd){
		Product_VO product;
		int count = 0;
		if(cmd != -1) {
			for(int i=0; i<list.size(); i++) {
				product = list.get(i);
				if(product.getCate_code() == cmd)
					count++;
			}
		} else {
			count = list.size();
		}
		
		model.setNumRows(0);
		jcbData = new JComboBox[count];
		
		int index = 0;
		for(int i=0; i<list.size(); i++) {
			if(cmd == -1) {
				product = list.get(i);
				jcbData[index] = new JComboBox<>(orderNum);
				model.addRow(new Object[]{
					String.valueOf(product.getIdx()) + product.getCate_code(),
					product.getCate_name(),
					product.getCate_price()
				});
				table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(jcbData[index]));
				jcbData[index].setSelectedItem(0);
				index++;
			} else {
				if(list.get(i).getCate_code() == cmd) {
					product = list.get(i);
					jcbData[index] = new JComboBox<>(orderNum);
					model.addRow(new Object[]{
						String.valueOf(product.getIdx()) + product.getCate_code(),
						product.getCate_name(),
						product.getCate_price()
					});
					table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(jcbData[index]));
					jcbData[index].setSelectedItem(0);
					index++;
				} 
			}
		}
		// 콤보 박스 초기화
		int row = model.getRowCount();
		int col = model.getColumnCount();
		for(int i=0; i<row; i++) {
			table.setValueAt(0, i, col-1);
		}
	}
	
	public void pay(ArrayList<String> idxList, ArrayList<Integer> indexList){
		
		ArrayList<Product_VO> productList = new ArrayList<>();
		Product_VO product;
		for(int i=0; i<list.size(); i++) {
			product = list.get(i);
			for(int j=0; j<idxList.size(); j++) {
				if(product.getIdx() == Integer.parseInt(idxList.get(j))){
					Product_VO p = new Product_VO();
					p.setIdx(product.getIdx());
					p.setCate_price(product.getCate_price());
					p.setCate_code(product.getCate_code());
					p.setCate_img(product.getCate_img());
					p.setCate_name(product.getCate_name());
					p.setCate_stock((Integer)downModel.getValueAt(indexList.get(j), 2));
					productList.add(p);
					break;
				}
			}
		}
		
		Protocol p = new Protocol();
		p.setCmd(2000);
		p.setList(productList);
		User_VO vo = new User_VO();
		vo.setId(client.user.getId());
		p.setUser(vo);
		client.send(p);
	}
	
	public String searchImg(String num) {
		
		String turl = "";
		for(int i=0; i<list.size(); i++) {
			Product_VO product = list.get(i);
			
			if(product.getIdx() == Integer.parseInt(num)){
				turl = product.getCate_img();
				break;
			}
		}
		
		return turl;
	}
}
