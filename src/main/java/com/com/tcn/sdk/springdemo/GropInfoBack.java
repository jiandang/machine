package com.com.tcn.sdk.springdemo;

public class GropInfoBack {
	private int m_iID = -1;
	private int m_iGrpID = -1;//柜子编号
	private String m_strShowText = "";


	public int getID() {
		return m_iID;
	}
	public void setID(int iD) {
		m_iID = iD;
	}

	public int getGrpID() {
		return m_iGrpID;
	}
	public void setGrpID(int iD) {
		m_iGrpID = iD;
	}

	public String getShowText() {
		return m_strShowText;
	}
	public void setShowText(String text) {
		m_strShowText = text;
	}
}
