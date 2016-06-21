package jinwoong;

import java.io.Serializable;
import java.util.ArrayList;

import VO.Product_VO;
import VO.User_VO;

public class Protocol implements Serializable{

	private int cmd;
	private String msg;
	private ArrayList<Product_VO> list;
	private User_VO user;
	private int host;
	public Protocol(){}
	public Protocol(int cmd, String msg, User_VO user) {
		super();
		this.cmd = cmd;
		this.msg = msg;
		this.user = user;
	}
	public int getCmd() {
		return cmd;
	}
	public void setCmd(int cmd) {
		this.cmd = cmd;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public User_VO getUser() {
		return user;
	}
	public void setUser(User_VO user) {
		this.user = user;
	}
	public ArrayList<Product_VO> getList() {
		return list;
	}
	public void setList(ArrayList<Product_VO> list) {
		this.list = list;
	}
}
