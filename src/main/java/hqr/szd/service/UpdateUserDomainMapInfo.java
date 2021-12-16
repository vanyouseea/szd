package hqr.szd.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hqr.szd.dao.SzdUserDomainMapRepo;
import hqr.szd.domain.SzdUserDomainMap;

@Service
public class UpdateUserDomainMapInfo {
	
	@Autowired
	private SzdUserDomainMapRepo sudmr;
	
	public void updateDnsRecords(int seqNo, String prefix, String type, String ip, boolean proxied ) {
		Optional<SzdUserDomainMap> opt = sudmr.findById(seqNo);
		if(opt.isPresent()) {
			SzdUserDomainMap enti = opt.get();
			enti.setSeqNo(seqNo);
			enti.setPrefix(prefix);
			enti.setType("A");
			enti.setIp(ip);
			enti.setProxied(proxied?1:0);
			enti.setCreateDt(new Date());
			sudmr.saveAndFlush(enti);
		}
		else {
			System.out.println("不存在的id:"+seqNo);
		}
	}
	
}
