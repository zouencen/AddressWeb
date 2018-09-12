package com.usts.lib.model;
public class NodeInformation {
	//"PID","QDZ","PY","ZXJD","ZXWD","XJXZQH","CQJLX","MPHPID","ZHPID","MPH","ZH","SHSJ","HM","DZLX","CZBZ"
	private String pid = "";//PID
	/**
	 * 地址信息
	 */
	private String qdz = "";//QDZ
	private String py = ""; //PY
	private String zxjd = "";//ZXJD
	private String zxwd = "";//ZXWD
	private String xjxzqh = "";//XJXZQH
	private String cqjlx = "";//CQJLX
	private String mphpid = "";//MPHPID
	private String zhpid = "";//ZHPID
	private String mph = "";//MPH
	private String zh = "";//ZH
	private String shsj = "";//SHSJ
	private String hm = "";//HM
	private String dzlx = "";//DZLX
	private String czbz = "";//CZBZ
//	String addressNode[] = new String[]{getPid(),getQdz(),getPy(),getZxjd(),getZxwd(),getXjxzqh(),
//			getCqjlx(),getMphpid(),getZhpid(),getMph(),getZh(),getShsj(),getHm(),getDzlx(),getCzbz(),getCzbz()};
//	
//	
//	
//	public AddressNode(String[] addressNode) {
//		super();
//		this.addressNode = addressNode;
//	}
	
	public String getPid() {
		return pid;
	}
	public NodeInformation() {
		super();
		// TODO Auto-generated constructor stub
	}
	public NodeInformation(String pid, String qdz, String py, String zxjd,
		String zxwd, String xjxzqh, String cqjlx, String mphpid, String zhpid,
		String mph, String zh, String shsj, String hm, String dzlx, String czbz) {
	super();
	this.pid = pid;
	this.qdz = qdz;
	this.py = py;
	this.zxjd = zxjd;
	this.zxwd = zxwd;
	this.xjxzqh = xjxzqh;
	this.cqjlx = cqjlx;
	this.mphpid = mphpid;
	this.zhpid = zhpid;
	this.mph = mph;
	this.zh = zh;
	this.shsj = shsj;
	this.hm = hm;
	this.dzlx = dzlx;
	this.czbz = czbz;
}
	public void setPid(String pid) {
		this.pid = pid;
	}
	/**获取地址
	 * @return
	 */
	public String getQdz() {
		return qdz;
	}
	public void setQdz(String qdz) {
		this.qdz = qdz;
	}
	public String getPy() {
		return py;
	}
	public void setPy(String py) {
		this.py = py;
	}
	public String getZxjd() {
		return zxjd;
	}
	public void setZxjd(String zxjd) {
		this.zxjd = zxjd;
	}
	public String getZxwd() {
		return zxwd;
	}
	public void setZxwd(String zxwd) {
		this.zxwd = zxwd;
	}
	public String getXjxzqh() {
		return xjxzqh;
	}
	public void setXjxzqh(String xjxzqh) {
		this.xjxzqh = xjxzqh;
	}
	public String getCqjlx() {
		return cqjlx;
	}
	public void setCqjlx(String cqjlx) {
		this.cqjlx = cqjlx;
	}
	public String getMphpid() {
		return mphpid;
	}
	public void setMphpid(String mphpid) {
		this.mphpid = mphpid;
	}
	public String getZhpid() {
		return zhpid;
	}
	public void setZhpid(String zhpid) {
		this.zhpid = zhpid;
	}
	public String getMph() {
		return mph;
	}
	public void setMph(String mph) {
		this.mph = mph;
	}
	public String getZh() {
		return zh;
	}
	public void setZh(String zh) {
		this.zh = zh;
	}
	public String getShsj() {
		return shsj;
	}
	public void setShsj(String shsj) {
		this.shsj = shsj;
	}
	public String getHm() {
		return hm;
	}
	public void setHm(String hm) {
		this.hm = hm;
	}
	public String getDzlx() {
		return dzlx;
	}
	public void setDzlx(String dzlx) {
		this.dzlx = dzlx;
	}
	public String getCzbz() {
		return czbz;
	}
	public void setCzbz(String czbz) {
		this.czbz = czbz;
	}

	@Override
	public String toString() {
		return "pid:" + pid + ", qdz:" + qdz + ", py:" + py
				+ ", zxjd:" + zxjd + ", zxwd:" + zxwd + ", xjxzqh:" + xjxzqh
				+ ", cqjlx:" + cqjlx + ", mphpid:" + mphpid + ", zhpid:"
				+ zhpid + ", mph:" + mph + ", zh:" + zh + ", shsj:" + shsj
				+ ", hm:" + hm + ", dzlx:" + dzlx + ", czbz:" + czbz + "";
	}
	
}
