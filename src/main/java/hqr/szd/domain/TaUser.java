package hqr.szd.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames="userId")})
public class TaUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int seqNo;
	private String userId;
	private String passwd;
	private String cfAuthEmail;
	private String cfAuthKey;
	public int getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getCfAuthEmail() {
		return cfAuthEmail;
	}
	public void setCfAuthEmail(String cfAuthEmail) {
		this.cfAuthEmail = cfAuthEmail;
	}
	public String getCfAuthKey() {
		return cfAuthKey;
	}
	public void setCfAuthKey(String cfAuthKey) {
		this.cfAuthKey = cfAuthKey;
	}
}