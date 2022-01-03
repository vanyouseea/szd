package hqr.szd.service;

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
public class DeleteUserDnsRecords {
	@Autowired
	private TaUserRepo tur;
	
	@Autowired
	private SzdUserDomainMapRepo sdmr;
	
	@Autowired
	private DeleteDnsRecordInCf ddr;
	
	@Autowired
	private SzdDomainRepo sdr;
	
	public String deleteRecords(String seqNos) {
		String res = "";
		String strArr[] = seqNos.split(",");
		for (String str : strArr) {
			int seqNo = Integer.parseInt(str);
			Optional<SzdUserDomainMap> opt = sdmr.findById(seqNo);
			if(opt.isPresent()) {
				SzdUserDomainMap enti = opt.get();
				String oldZoneNm = enti.getPrefix() + "." + enti.getCurDomain(); //p1.aqe.baidu.com
				List<SzdDomain> list1 = sdr.findBySubZoneAndUserSeqNo(oldZoneNm, enti.getUserSeqNo());
				if(list1!=null&&list1.size()>0) {
					SzdDomain szdDomain = list1.get(0);
					res = ddr.delete(szdDomain.getZoneId(), szdDomain.getSubZoneId());
					
					//null means succ
					if("".equals(res)) {
						sdmr.deleteById(seqNo);
						sdr.delete(szdDomain);
						Optional<TaUser> opt1 = tur.findById(enti.getUserSeqNo());
						if(opt1.isPresent()) {
							TaUser user = opt1.get();
							user.setCurSubDomain(user.getCurSubDomain()-1);
							tur.saveAndFlush(user);
						}
					}
				}
				else {
					res = "未找到对应的子域信息:"+oldZoneNm+",仅删除map表";
					Optional<TaUser> opt1 = tur.findById(enti.getUserSeqNo());
					if(opt1.isPresent()) {
						TaUser user = opt1.get();
						user.setCurSubDomain(user.getCurSubDomain()-1);
						tur.saveAndFlush(user);
					}
					sdmr.deleteById(seqNo);
				}
			}
		}
		
		return res;
	}
}
