package hqr.szd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hqr.szd.dao.SzdDomainRepo;
import hqr.szd.dao.SzdUserDomainMapRepo;
import hqr.szd.domain.SzdDomain;
import hqr.szd.domain.SzdUserDomainMap;

@Service
public class UpdateUserDomainMapInfo {
	
	@Autowired
	private SzdUserDomainMapRepo sudmr;
	
	@Autowired
	private SzdDomainRepo sdr;
	
	@Autowired
	private UpdateDnsRecordInCf udr;
	
	public String updateDnsRecords(int seqNo, String prefix, String type, String ip, boolean proxied ) {
		String res = ""	;
		Optional<SzdUserDomainMap> opt = sudmr.findById(seqNo);
		if(opt.isPresent()) {
			SzdUserDomainMap enti = opt.get();
			String oldZoneNm = enti.getPrefix() + "." + enti.getCurDomain(); //p1.aqe.baidu.com
			enti.setPrefix(prefix);
			
			//aqe.baidu.com or a1.aqe.baidu.com -> prefix = aqe or a1.aqe
			String arrs[] = enti.getCurDomain().split("\\.");
			if(arrs.length>=3) {
				for (int i=0;i<arrs.length-2;i++) {
					prefix = prefix + "." + arrs[i];
				}
			}
			
			List<SzdDomain> list1 = sdr.findBySubZoneAndUserSeqNo(oldZoneNm, enti.getUserSeqNo());
			if(list1!=null&&list1.size()>0) {
				SzdDomain domainEnti = list1.get(0);
				String zoneId = domainEnti.getZoneId();
				String subZoneId = domainEnti.getSubZoneId();
				System.out.println("zoneId is "+zoneId+" subzoneId is "+subZoneId+", prefix is "+prefix+", Domain:"+domainEnti.getZone());
				
				List<SzdDomain> list2 = sdr.findBySubZone(prefix+"."+domainEnti.getZone());
				if(list2==null||list2.size()==0) {
				 	res = udr.update(zoneId, subZoneId,"A", prefix, ip, proxied);
				 	
					//no content means succ
				 	if("".equals(res)) {
						enti.setSeqNo(seqNo);
						enti.setType("A");
						enti.setIp(ip);
						enti.setProxied(proxied?1:0);
						sudmr.saveAndFlush(enti);
						
						domainEnti.setSubZone(prefix+"."+domainEnti.getZone());
						sdr.saveAndFlush(domainEnti);
				 	}
				}
				else {
					//res = "此前缀已存在请重新选择";
					String oldIP = enti.getIp();
					if(ip.equals(oldIP)) {
						res = "此前缀/IP已存在请重新选择";
					}
					else {
					 	res = udr.update(zoneId, subZoneId,"A", prefix, ip, proxied);
					 	
						//no content means succ
					 	if("".equals(res)) {
							enti.setSeqNo(seqNo);
							enti.setType("A");
							enti.setIp(ip);
							enti.setProxied(proxied?1:0);
							sudmr.saveAndFlush(enti);
							
							domainEnti.setSubZone(prefix+"."+domainEnti.getZone());
							sdr.saveAndFlush(domainEnti);
					 	}
					}
				}
			}
			else {
				res = "未找到对应的子域信息:"+oldZoneNm;
			}
		}
		else {
			res = "不存在的id:"+seqNo;
		}
		return res;
	}
	
}
