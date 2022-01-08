package hqr.szd.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hqr.szd.dao.SzdDomainRepo;
import hqr.szd.dao.SzdUserDomainMapRepo;
import hqr.szd.dao.TaUserRepo;
import hqr.szd.domain.SzdDomain;
import hqr.szd.domain.SzdUserDomainMap;
import hqr.szd.domain.TaUser;

@Service
public class UpdateMyDomainMapInfo {
	
	@Autowired
	private TaUserRepo tur;
	
	@Autowired
	private SzdDomainRepo sdr;
	
	@Autowired
	private SzdUserDomainMapRepo sudmr;
	
	@Autowired
	private CreateDnsRecordInCf cnr;
	
	@Autowired
	private UpdateDnsRecordInCf udr;
	
	public String updateDnsRecords(String seqNo, int domainSeqNo, int userSeqNo, String prefix, String type, String ip, boolean proxied,String curDomain ) {
		String res = "";
		//insert
		if(seqNo==null||"".equals(seqNo)) {
			SzdUserDomainMap enti = new SzdUserDomainMap();
			enti.setPrefix(prefix);
			Optional<TaUser> opt = tur.findById(userSeqNo);
			if(opt.isPresent()) {
				TaUser user = opt.get();
				int curDo = user.getCurSubDomain();
				int maxDo = user.getMaxSubDomain();
				if(maxDo>curDo) {
					Optional<SzdDomain> zonEnti = sdr.findById(domainSeqNo);
					if(zonEnti.isPresent()) {
						SzdDomain sd = zonEnti.get();
						//if exist subzoneid then get subzoneName, else use zoneid
						String zoneId = sd.getZoneId();
						if(sd.getSubZoneId()!=null) {
							String subZone = sd.getSubZone();
							//aqe.baidu.com or a1.aqe.baidu.com -> prefix = aqe or a1.aqe
							String arrs[] = subZone.split("\\.");
							if(arrs.length>=3) {
								for (int i=0;i<arrs.length-2;i++) {
									prefix = prefix + "." + arrs[i];
								}
							}
						}
						
						System.out.println("zoneId is "+sd.getZoneId()+", prefix is "+prefix+", Domain:"+sd.getZone());
						
						List<SzdDomain> list2 = sdr.findBySubZone(prefix+"."+sd.getZone());
						if(list2==null||list2.size()==0) {
						 	res = cnr.create(zoneId,"A", prefix, ip, proxied, userSeqNo);
							//no content means succ
						 	if("".equals(res)) {
								user.setCurSubDomain(curDo+1);
								tur.saveAndFlush(user);
								
								enti.setUserSeqNo(userSeqNo);
								enti.setDomainSeqNo(domainSeqNo);
								enti.setType("A");
								enti.setIp(ip);
								enti.setProxied(proxied?1:0);
								enti.setCreateDt(new Date());
								enti.setCurDomain(curDomain);
								sudmr.saveAndFlush(enti);
						 	}
						}
						else {
							res = "此前缀已存在请重新选择";
						}
					}
					else {
						res = "无效的（子）域";
					}
				}
				else {
					res = "用户子域超过限制 (当前:"+curDo+"/最大:"+maxDo+")";
				}
			}
			else {
				res = "用户不存在";
			}
		}
		//update, do not allow user modify prefix and type
		else {
			Optional<SzdUserDomainMap> opt = sudmr.findById(Integer.parseInt(seqNo));
			if(opt.isPresent()) {
				SzdUserDomainMap enti = opt.get();
				String oldZoneNm = enti.getPrefix() + "." + enti.getCurDomain(); //p1.aqe.baidu.com
				
				List<SzdDomain> list1 = sdr.findBySubZoneAndUserSeqNo(enti.getPrefix() + "." + enti.getCurDomain(), enti.getUserSeqNo());
				if(list1!=null&&list1.size()>0) {
					SzdDomain domainEnti = list1.get(0);
					String zoneId = domainEnti.getZoneId();
					String subZoneId = domainEnti.getSubZoneId();
					
					//aqe.baidu.com or a1.aqe.baidu.com -> prefix = aqe or a1.aqe
					String arrs[] = (enti.getPrefix() + "." + enti.getCurDomain()).split("\\.");
					prefix = "";
					if(arrs.length>=3) {
						for (int i=0;i<arrs.length-2;i++) {
							prefix =  prefix + "." + arrs[i] ;
						}
					}
					prefix = prefix.substring(1);
					
					System.out.println("zoneId is "+zoneId+" subzoneId is "+subZoneId+", prefix is "+prefix+", Domain:"+domainEnti.getZone());
					
				 	res = udr.update(zoneId, subZoneId,"A", prefix, ip, proxied);
				 	
					//no content means succ
				 	if("".equals(res)) {
						enti.setIp(ip);
						enti.setProxied(proxied?1:0);
						sudmr.saveAndFlush(enti);
				 	}
				}
				else {
					res = "未找到对应的子域信息:"+oldZoneNm;
				}
			}
			else {
				res = "不存在的id:"+seqNo;
			}
		}
		return res;
	}
	
	
	
	
}
