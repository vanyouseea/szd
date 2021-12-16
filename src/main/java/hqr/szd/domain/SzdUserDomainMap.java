package hqr.szd.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SzdUserDomainMap {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int seqNo;
	private int userSeqNo;
	private int domainSeqNo;
	//only allow type A
	private String type="A";
	private String prefix;
	private String ip;
	/*
	 * 0 - false
	 * 1 - true
	 */
	private int proxied = 0;
	private String curDomain;
	private Date createDt;
	public int getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	public int getUserSeqNo() {
		return userSeqNo;
	}
	public void setUserSeqNo(int userSeqNo) {
		this.userSeqNo = userSeqNo;
	}
	public int getDomainSeqNo() {
		return domainSeqNo;
	}
	public void setDomainSeqNo(int domainSeqNo) {
		this.domainSeqNo = domainSeqNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getProxied() {
		return proxied;
	}
	public void setProxied(int proxied) {
		this.proxied = proxied;
	}
	public Date getCreateDt() {
		return createDt;
	}
	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}
	public String getCurDomain() {
		return curDomain;
	}
	public void setCurDomain(String curDomain) {
		this.curDomain = curDomain;
	}
}
