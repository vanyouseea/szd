package hqr.szd.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SzdDomain {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int seqNo;
	private String shared = "否";
	//if not set, 0 belongs to admin
	private int userSeqNo = 0;
	private String subZoneId;
	private String subZone;
	private String zoneId;
	private String zone;
	private Date createDt;
	public int getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	public String getShared() {
		return shared;
	}
	public void setShared(String shared) {
		this.shared = shared;
	}
	public int getUserSeqNo() {
		return userSeqNo;
	}
	public void setUserSeqNo(int userSeqNo) {
		this.userSeqNo = userSeqNo;
	}
	public String getSubZoneId() {
		return subZoneId;
	}
	public void setSubZoneId(String subZoneId) {
		this.subZoneId = subZoneId;
	}
	public String getSubZone() {
		return subZone;
	}
	public void setSubZone(String subZone) {
		this.subZone = subZone;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public Date getCreateDt() {
		return createDt;
	}
	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}
}
