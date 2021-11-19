package hqr.szd.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hqr.szd.dao.SzdDomainRepo;
import hqr.szd.domain.SzdDomain;

@Service
public class UpdateDomain {

	@Autowired
	private SzdDomainRepo sdr;
	
	public void saveDomains(String seqNos) {
		String strArr[] = seqNos.split(",");
		for (String str : strArr) {
			int seqNo = Integer.parseInt(str);
			Optional<SzdDomain> opt = sdr.findById(seqNo);
			if(opt.isPresent()) {
				SzdDomain enti = opt.get();
				String shared = enti.getShared();
				if("是".equals(shared)) {
					enti.setShared("否");
				}
				else if("否".equals(shared)) {
					enti.setShared("是");
				}
				sdr.saveAndFlush(enti);
			}
		}
	}
}
