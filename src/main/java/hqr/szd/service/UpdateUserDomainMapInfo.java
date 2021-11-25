package hqr.szd.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hqr.szd.dao.SzdUserDomainMapRepo;
import hqr.szd.domain.SzdUserDomainMap;

@Service
public class UpdateUserDomainMapInfo {
	
	@Autowired
	private SzdUserDomainMapRepo sudmr;
	
	public boolean updateDnsRecords(String seqNo, int domainSeqNo, int userSeqNo, String prefix, String type, String ip, boolean proxied ) {
		boolean flag = false;
		
		SzdUserDomainMap enti = new SzdUserDomainMap();
		//insert
		if(seqNo==null||"".equals(seqNo)) {
			enti.setUserSeqNo(userSeqNo);
			enti.setDomainSeqNo(domainSeqNo);
			enti.setPrefix(prefix);
			enti.setType("A");
			enti.setIp(ip);
			enti.setProxied(proxied?1:0);
			enti.setCreateDt(new Date());
			sudmr.saveAndFlush(enti);
			flag = true;
		}
		//update
		else {
			enti.setSeqNo(Integer.parseInt(seqNo));
			enti.setUserSeqNo(userSeqNo);
			enti.setDomainSeqNo(domainSeqNo);
			enti.setPrefix(prefix);
			enti.setType("A");
			enti.setIp(ip);
			enti.setProxied(proxied?1:0);
			enti.setCreateDt(new Date());
			sudmr.saveAndFlush(enti);
			flag = true;
		}
		return flag;
	}
	
}
