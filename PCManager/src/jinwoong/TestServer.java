package jinwoong;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import VO.Product_VO;
import VO.User_VO;
import dbConnect.ConnectDB;

/*회원가입용 간이 테스트 서버입니다
 * 
 */
public class TestServer implements Runnable {

	ServerSocket ss;
	Socket s;
	ArrayList<Client_Thread> clientList = new ArrayList<>();
	User_Main uMain;
	boolean flag;

	public TestServer() {
		try {
			System.out.println("Server대기중");
			ss = new ServerSocket(7777);
			new Thread(this).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new TestServer();
		new User_Main();
	}

	@Override
	public void run() {
		while (true) {
			try {
				s = ss.accept();
				Client_Thread ct = new Client_Thread(s, this);
				clientList.add(ct);
				ct.start();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class Client_Thread extends Thread {

		Socket s;
		TestServer t;
		ObjectInputStream ois;
		ObjectOutputStream oos;
		Connection conn;
		PreparedStatement pstmt;
		ResultSet rs;
		boolean isChatOpen = false;

		JTextArea jta = null;
		JScrollPane jsp = null;
		JPanel jp1 = null;
		JTextField jtf1 = null;
		JButton jb1 = null;
		JFrame j = null;
		User_VO user = null;

		public Client_Thread(Socket s, TestServer t) {
			this.s = s;
			this.t = t;

			try {
				ois = new ObjectInputStream(s.getInputStream());
				oos = new ObjectOutputStream(s.getOutputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void run() {
			while (true) {
				try {
					Protocol sp;
					User_VO vo;
					Protocol p = (Protocol) ois.readObject();
					ArrayList<Product_VO> list;
					String sql;
					int result;

					switch (p.getCmd()) {
					case 100:// 회원가입
						joinUser(p.getUser());
						break;
					case 200:// 중복체크
						redundancyCheck(p.getUser().getId());
						break;
					case 300:// 대화받음
						getMessage(p);
						break;
					case 400:
						userLogin(p);
						inputInfo();
						break;
					case 500:
						userLogout(p);
						offcom(p);
						break;
					case 1000: // 상품 데이터 보내기
						list = new ArrayList<>();
						selectProduct(list);
						sp = new Protocol();
						sp.setCmd(1000);
						sp.setList(list);
						send(sp);
						break;
					case 2000: // 상품 결제
						list = p.getList();
						vo = p.getUser();
						new ProductList(this, list, vo);
						break;
					case 5001: // 1000원 / 60분
						int price = 1000;
						int time = 60 * 60;
						updateTime(p.getUser(), time, price);		
						break;
					case 5002:// 5000원 / 310
						price = 5000;
						time = 310 * 60;
						updateTime(p.getUser(), time, price);
						break;
					case 5003:// 10000원/640
						price = 10000;
						time = 640 * 60;
						updateTime(p.getUser(), time, price);		
						break;
					case 5004:// 50000원/3180
						price = 50000;
						time = 3180 * 60;
						updateTime(p.getUser(), time, price);
						break;
					}
				} catch (Exception e) {
				}
			}

		}

		public void offcom(Protocol p) {
			String pcname = s.getInetAddress().getHostAddress();

			
			int pcn = Integer.parseInt(pcname.substring(12))-41;
			if(pcn == 0)
				pcn = 31;
			
			uMain.user_arr[pcn - 1].name = "";
			uMain.user_arr[pcn - 1].id = "";
			uMain.user_arr[pcn - 1].count = 0;
		}

		public void inputInfo(int val, String id) {
			String pcname = "";
			
			for(int i=0; i<clientList.size(); i++) {
				if(clientList.get(i).user.getId().equals(id)){
					pcname = clientList.get(i).s.getInetAddress().getHostAddress();
					int pcn = Integer.parseInt(pcname.substring(12))-41;
					if(i==0)
						pcn = 31;
					
					uMain.user_arr[pcn - 1].ptime = val;
				}
			}
		}
		public void inputInfo() {
			String pcname = s.getInetAddress().getHostName();
			String pcip = s.getInetAddress().getHostAddress();
			
			//-41
			int pcn =  Integer.parseInt(pcip.substring(12)) -41;
			if(pcn == 0)
				pcn = 31;
			//int pcn = Integer.parseInt(pcname.substring(4));
			
			String id = user.getId();
			
			uMain.user_arr[pcn - 1].count = user.getRemain_time();
			uMain.user_arr[pcn - 1].name = user.getName();
			uMain.user_arr[pcn - 1].id = id;
		}

		public void userLogout(Protocol p) {
			String sql = "update client set remain_time=?, connected=0 where idx=?";

			try {
				pstmt = ConnectDB.conn.prepareStatement(sql);
				pstmt.setInt(1, p.getUser().getRemain_time());
				pstmt.setInt(2, p.getUser().getIdx());
				pstmt.executeUpdate();

			} catch (Exception e) {
				e.getMessage();
			}

		}

		public void userLogin(Protocol p) {
			String sql = "select * from client where id=? and pwd=?";

			try {
				pstmt = ConnectDB.conn.prepareStatement(sql);
				pstmt.setString(1, p.getUser().getId());
				pstmt.setString(2, p.getUser().getPwd());
				rs = pstmt.executeQuery();

				if (rs.next()) {
					if (rs.getInt(9) == 1) {
						p.setCmd(403); // 로그인실패,이미접속중
						
					} else if ((rs.getInt(9) == 0)) {						
						
						p.getUser().setIdx(rs.getInt(1));
						p.getUser().setName(rs.getString(2));
						p.getUser().setId(rs.getString(3));
						p.getUser().setPwd(rs.getString(4));
						p.getUser().setBirth(rs.getString(5));
						p.getUser().setPhone(rs.getString(6));
						p.getUser().setRemain_time(rs.getInt(7));
						p.getUser().setAddress(rs.getString(8));
						
						
						if(p.getUser().getRemain_time() > 0) {
							p.getUser().setConnected(1);

							String sql2 = "update client set connected=1 where idx=?";
							pstmt = ConnectDB.conn.prepareStatement(sql2);
							pstmt.setInt(1, rs.getInt(1));
						}
						int result = pstmt.executeUpdate();
						
						if (result != -1) {
							p.setCmd(401);// 로그인성공
							user = p.getUser();
						} else {
							p.setCmd(402); // 로그인실패
						}		
					}

				} else {
					p.setCmd(402); // 로그인실패
				}

				oos.writeObject(p);
				oos.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		public void sendMsg(String text) {
			Protocol p = new Protocol();
			p.setCmd(300);
			p.setMsg("관리자 : " + text);
			jta.append(p.getMsg() + System.getProperty("line.separator"));
			jtf1.setText("");
			try {
				oos.writeObject(p);
				oos.flush();
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		public void getMessage(Protocol p) {
			String comName = s.getInetAddress().getHostName();

			if (isChatOpen == false) {
				j = new JFrame();
				j.setTitle(comName + "님과의 대화");
				jp1 = new JPanel();
				jta = new JTextArea(5, 20);
				jsp = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				jta.setLineWrap(true);
				jtf1 = new JTextField(20);
				jb1 = new JButton("전송");

				jp1.add(jtf1);
				jp1.add(jb1);
				j.add(jsp, "Center");
				j.add(jp1, "South");
				j.setVisible(true);
				j.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				j.setBounds(100, 100, 400, 300);
				isChatOpen = true;

				int len = jta.getDocument().getLength();
				jta.setCaretPosition(len);
				
				j.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						isChatOpen = false;
					}
				});

				jb1.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						String text = jtf1.getText();
						sendMsg(text);
						int len = jta.getDocument().getLength();
						jta.setCaretPosition(len);

					}
				});

				jtf1.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						jb1.doClick();

					}
				});
			}

			String msg = p.getMsg();
			msg = comName + " : " + msg;
			p.setMsg(msg);
			jta.append(msg + System.getProperty("line.separator"));

			try {
				oos.writeObject(p);
				oos.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void joinUser(User_VO user) {
			try {

				String sql = "insert into client(idx,name,id,pwd,birth,phone,address)"
						+ " values(client_seq.nextval ,?,?,?,?,?,?)";
				int result = 0;
				try {

					pstmt = ConnectDB.conn.prepareStatement(sql);

					pstmt.setString(1, user.getName());
					pstmt.setString(2, user.getId());
					pstmt.setString(3, user.getPwd());
					pstmt.setString(4, user.getBirth());
					pstmt.setString(5, user.getPhone());
					pstmt.setString(6, user.getAddress());
					result = pstmt.executeUpdate();

					oos.writeObject(result);
					oos.flush();

				} catch (SQLException e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void redundancyCheck(String id) {
			String sql = "select id from USER2 where ID=?";

			try {
				pstmt = ConnectDB.conn.prepareStatement(sql);
				pstmt.setString(1, id);
				rs = pstmt.executeQuery();
				Protocol p = new Protocol();

				boolean result = rs.next();

				if (result == true) { // 불가능한 아이디
					p.setCmd(202);
				} else if (result == false) { // 사용 가능한 아이디
					p.setCmd(201);
				}
				oos.writeObject(p);
				oos.flush();

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		public void selectProduct(ArrayList<Product_VO> list) {
			try {
				String sql = "select * from product";
				pstmt = ConnectDB.conn.prepareStatement(sql);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					Product_VO product = new Product_VO();
					product.setIdx(rs.getInt(1));
					product.setCate_code(rs.getInt(2));
					product.setCate_name(rs.getString(3));
					product.setCate_price(rs.getInt(4));
					product.setCate_stock(rs.getInt(5));
					product.setCate_img(rs.getString(6));

					list.add(product);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void send(Protocol p) {
			try {
				oos.writeObject(p);
				oos.flush();
			} catch (Exception e) {

			}
		}

		public int updateTime(User_VO user, int chargedTime, int chargedMoney) {

			String sql = "select * from client where id=?";
			PreparedStatement pstmt;
			int result = -1;
			User_VO u = new User_VO();
			ResultSet rs;

			try {
				pstmt = ConnectDB.conn.prepareStatement(sql);
				pstmt.setString(1, user.getId());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					u.setIdx(rs.getInt(1));
					u.setName(rs.getString(2));
					u.setId(rs.getString(3));
					u.setPwd(rs.getString(4));
					u.setBirth(rs.getString(5));
					u.setPhone(rs.getString(6));
					u.setRemain_time(rs.getInt(7));
					u.setAddress(rs.getString(8));
					u.setConnected(rs.getInt(9));
				}
				switch (u.getConnected()) {
				case 0: // 비접속
					// 시간 추가하기
					pstmt = ConnectDB.conn.prepareStatement("update client set remain_time=? where id=?");
					pstmt.setInt(1, u.getRemain_time() + chargedTime);
					pstmt.setString(2, u.getId());
					pstmt.executeUpdate();

					break;
				case 1: // 접속중
					for (int i = 0; i < clientList.size(); i++) {
						if (clientList.get(i).user.getId().equals(u.getId())) {
							Protocol p = new Protocol();
							p.setCmd(5000);
							p.setMsg(chargedTime + "");
							try {
								clientList.get(i).oos.writeObject(p);
								clientList.get(i).oos.flush();
							} catch (Exception e) {
								e.printStackTrace();
							}
							inputInfo(chargedTime, user.getId());
						}
					}
					break;

				}
				// 매출 추가하기
				pstmt = ConnectDB.conn.prepareStatement("insert into money values(0,sysdate,'시간충전',?,?)");
				pstmt.setInt(1, chargedMoney);
				pstmt.setString(2, u.getId());
				pstmt.executeUpdate();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result;
		}// "update client product set reamin_time=? where id=?"

	}// 클라이언트쓰레드 내부클래스

	class ProductList extends JFrame {

		JPanel panel, panel2;
		Product_VO vo;
		JTable table;
		DefaultTableModel model;
		JScrollPane jsp;
		int len;
		int sum = 0;
		JButton btnAgree, btnCancle;
		User_VO uvo;

		public ProductList(Client_Thread ct, ArrayList<Product_VO> list, User_VO uvo) {

			panel = new JPanel(new BorderLayout());
			panel2 = new JPanel(new GridLayout(3, 2));

			this.uvo = uvo;

			btnAgree = new JButton("승인");
			btnAgree.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub

					Protocol sp = new Protocol();
					int result;
					sp.setCmd(3000);

					result = updateProduct(list);

					if (result == -1)
						sp.setMsg("결제 실패하셨습니다. 관리자에게 문의해주세요");
					else
						sp.setMsg("결제되셨습니다");
					ct.send(sp);
					dispose();
				}
			});
			btnCancle = new JButton("취소");
			btnCancle.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub

				}
			});

			len = list.size();
			String[] title = { "상품명", "금액", "갯수" };

			model = new DefaultTableModel(title, 0) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};

			table = new JTable(model);
			table.setBorder(BorderFactory.createEtchedBorder());

			for (int i = 0; i < len; i++) {
				vo = list.get(i);
				model.addRow(new Object[] { vo.getCate_name(), vo.getCate_price(), vo.getCate_stock() });
				sum += vo.getCate_price() * vo.getCate_stock();
			}

			panel2.setBackground(Color.WHITE);
			panel2.add(new JLabel("주문자"));
			panel2.add(new JLabel(s.getInetAddress().getHostName()));
			panel2.add(new JLabel("총 가격"));
			panel2.add(new JLabel(sum + "원"));
			panel2.add(btnAgree);
			panel2.add(btnCancle);

			jsp = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

			panel.add(jsp, BorderLayout.CENTER);
			panel.add(panel2, BorderLayout.SOUTH);

			add(panel);
			setBounds(100, 100, 300, 300);
			setVisible(true);
		}

		public int updateProduct(ArrayList<Product_VO> list) {
			PreparedStatement pstmt;
			String sql = "select * from product";
			int result = -1;
			Product_VO p;
			ResultSet rs;
			ArrayList<Product_VO> updateData = new ArrayList<>();
			Calendar cal = Calendar.getInstance();
			boolean flag = false;

			try {
				pstmt = ConnectDB.conn.prepareStatement(sql);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					for (int i = 0; i < list.size(); i++) {
						p = list.get(i);
						if (rs.getInt("idx") == p.getIdx()) {
							if (rs.getInt("cate_stock") >= p.getCate_stock()) {
								Product_VO tvo = new Product_VO();
								tvo.setCate_name(p.getCate_name());
								tvo.setIdx(p.getIdx());
								tvo.setCate_stock(rs.getInt("cate_stock") - p.getCate_stock());
								updateData.add(tvo);
							} else {
								flag = true;
							}
						}
					}
				}

				if (flag) {
					result = -1;
				} else {
					result = 1;
					sql = "update product set CATE_STOCK = ? where IDX = ?";
					pstmt = ConnectDB.conn.prepareStatement(sql);

					for (int i = 0; i < updateData.size(); i++) {
						pstmt.setInt(1, updateData.get(i).getCate_stock());
						pstmt.setInt(2, updateData.get(i).getIdx());
						pstmt.executeUpdate();
					}

					String today = cal.get(Calendar.YEAR) + "/";
					if (cal.get(Calendar.MONTH) + 1 < 10)
						today += "0";
					today += (cal.get(Calendar.MONTH) + 1) + "/";

					if (cal.get(Calendar.DAY_OF_MONTH) < 10)
						today += "0";
					today += cal.get(Calendar.DAY_OF_MONTH);

					sql = "insert into money values(0, ?, ?, ?, ?)";
					pstmt = ConnectDB.conn.prepareStatement(sql);
					for (int i = 0; i < list.size(); i++) {
						Product_VO vo = list.get(i);
						pstmt.setString(1, today);
						pstmt.setString(2, vo.getCate_name());
						pstmt.setInt(3, vo.getCate_price() * vo.getCate_stock());
						pstmt.setString(4, uvo.getId());
						pstmt.executeUpdate();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return result;
		}

	}
}
