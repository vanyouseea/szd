package hqr.szd.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hqr.szd.dao.SzdUserDomainMapRepo;
import hqr.szd.dao.TaUserRepo;
import hqr.szd.domain.SzdUserDomainMap;
import hqr.szd.domain.TaUser;

@Service
public class UpdateMyDomainMapInfo {
	
	@Autowired
	private TaUserRepo tur;
	
	@Autowired
	private SzdUserDomainMapRepo sudmr;
	
	public String updateDnsRecords(String seqNo, int domainSeqNo, int userSeqNo, String prefix, String type, String ip, boolean proxied,String curDomain ) {
		String res = "";
		
		SzdUserDomainMap enti = new SzdUserDomainMap();
		//insert
		if(seqNo==null||"".equals(seqNo)) {
			Optional<TaUser> opt = tur.findById(userSeqNo);
			if(opt.isPresent()) {
				TaUser user = opt.get();
				int curDo = user.getCurSubDomain();
				int maxDo = user.getMaxSubDomain();
				if(maxDo>curDo) {
					user.setCurSubDomain(curDo+1);
					tur.saveAndFlush(user);
					
					enti.setUserSeqNo(userSeqNo);
					enti.setDomainSeqNo(domainSeqNo);
					enti.setPrefix(prefix);
					enti.setType("A");
					enti.setIp(ip);
					enti.setProxied(proxied?1:0);
					enti.setCreateDt(new Date());
					enti.setCurDomain(curDomain);
					sudmr.saveAndFlush(enti);
				}
				else {
					res = "用户子域超过限制 (当前:"+curDo+"/最大:"+maxDo+")";
				}
			}
			else {
				res = "用户不存在";
			}
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
			enti.setCurDomain(curDomain);
			sudmr.saveAndFlush(enti);
		}
		return res;
	}
	
}
