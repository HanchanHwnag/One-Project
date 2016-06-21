package VO;

import java.io.Serializable;

public class Product_VO implements Serializable{
	private int idx;
	private int cate_code;
	private String cate_name;
	private int cate_price;
	private int cate_stock;
	private String cate_img;
	
	public Product_VO() {}
	public Product_VO(int idx, int cate_code, String cate_name, int cate_price, int cate_stock, String cate_img) {
		super();
		this.idx = idx;
		this.cate_code = cate_code;
		this.cate_name = cate_name;
		this.cate_price = cate_price;
		this.cate_stock = cate_stock;
		this.cate_img = cate_img;
	}

	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public int getCate_code() {
		return cate_code;
	}
	public void setCate_code(int cate_code) {
		this.cate_code = cate_code;
	}
	public String getCate_name() {
		return cate_name;
	}
	public void setCate_name(String cate_name) {
		this.cate_name = cate_name;
	}
	public int getCate_stock() {
		return cate_stock;
	}
	public void setCate_stock(int cate_stock) {
		this.cate_stock = cate_stock;
	}
	public String getCate_img() {
		return cate_img;
	}
	public void setCate_img(String cate_img) {
		this.cate_img = cate_img;
	}
	public int getCate_price() {
		return cate_price;
	}
	public void setCate_price(int cate_price) {
		this.cate_price = cate_price;
	}
}
