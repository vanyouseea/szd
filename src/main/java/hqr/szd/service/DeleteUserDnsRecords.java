package hqr.szd.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hqr.szd.dao.SzdUserDomainMapRepo;
import hqr.szd.dao.TaUserRepo;
import hqr.szd.domain.SzdUserDomainMap;
import hqr.szd.domain.TaUser;

@Service
public class DeleteUserDnsRecords {
	@Autowired
	private TaUserRepo tur;
	@Autowired
	private SzdUserDomainMapRepo sdmr;
	
	public void deleteRecords(String seqNos) {
		String strArr[] = seqNos.split(",");
		for (String str : strArr) {
			int seqNo = Integer.parseInt(str);
			Optional<SzdUserDomainMap> opt = sdmr.findById(seqNo);
			if(opt.isPresent()) {
				SzdUserDomainMap enti = opt.get();
				sdmr.deleteById(seqNo);
				Optional<TaUser> opt1 = tur.findById(enti.getUserSeqNo());
				if(opt1.isPresent()) {
					TaUser user = opt1.get();
					user.setCurSubDomain(user.getCurSubDomain()-1);
					tur.saveAndFlush(user);
				}
			}
		}
	}
}
