package hqr.szd.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SzdInviteInfo {
	@Id
	private String inviteId;
	private Date startDt;
	private Date endDt;
	/*
	 * 1 - Enabled
	 * 2 - In-progress
	 * 3 - Success
	 * 4 - Failure
	 */
	private String inviteStatus = "1";
	private int userDomainCnt = 1;
	private String result;
	public String getInviteId() {
		return inviteId;
	}
	public void setInviteId(String inviteId) {
		this.inviteId = inviteId;
	}
	public Date getStartDt() {
		return startDt;
	}
	public void setStartDt(Date startDt) {
		this.startDt = startDt;
	}
	public Date getEndDt() {
		return endDt;
	}
	public void setEndDt(Date endDt) {
		this.endDt = endDt;
	}
	public String getInviteStatus() {
		return inviteStatus;
	}
	public void setInviteStatus(String inviteStatus) {
		this.inviteStatus = inviteStatus;
	}
	public int getUserDomainCnt() {
		return userDomainCnt;
	}
	public void setUserDomainCnt(int userDomainCnt) {
		this.userDomainCnt = userDomainCnt;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
}
