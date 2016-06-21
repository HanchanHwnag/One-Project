package VO;

import java.io.Serializable;

public class User_VO implements Serializable{
	private int idx;
	private String name;
	private String id;
	private String pwd;
	private String birth;
	private String phone;
	private int remain_time;
	private int connected;
	private String address;
	
	public User_VO(){}
	public User_VO(int idx, String name, String id, String pwd, String birth, String phone, int remain_time,
			int connected, String address) {
		super();
		this.idx = idx;
		this.name = name;
		this.id = id;
		this.pwd = pwd;
		this.birth = birth;
		this.phone = phone;
		this.remain_time = remain_time;
		this.connected = connected;
		this.address = address;
	}
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getRemain_time() {
		return remain_time;
	}
	public void setRemain_time(int remain_time) {
		this.remain_time = remain_time;
	}
	public int getConnected() {
		return connected;
	}
	public void setConnected(int connected) {
		this.connected = connected;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	

	
	
}
