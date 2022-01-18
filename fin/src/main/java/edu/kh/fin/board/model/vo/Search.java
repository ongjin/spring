package edu.kh.fin.board.model.vo;

public class Search {
	private String sk; // 검색 키(제목, 내용, 제목 + 내용, 작성자)
	private String sv; // 검색 값
	private String ct; // 카테고리
	public Search() {
		// TODO Auto-generated constructor stub
	}
	public String getSk() {
		return sk;
	}
	public void setSk(String sk) {
		this.sk = sk;
	}
	public String getSv() {
		return sv;
	}
	public void setSv(String sv) {
		this.sv = sv;
	}
	public String getCt() {
		return ct;
	}
	public void setCt(String ct) {
		this.ct = ct;
	}
	
}
