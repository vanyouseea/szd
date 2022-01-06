package hqr.szd.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hqr.szd.dao.TaUserRepo;
import hqr.szd.domain.TaUser;

@Service
public class GetUserSubDomainInfo {
	
	@Autowired
	private TaUserRepo tur;
	
	public String getSubDomainCntInfo(int seqNo) {
		String res = "NA|NA";
		
		Optional<TaUser> opt = tur.findById(seqNo);
		
		if(opt.isPresent()) {
			TaUser user = opt.get();
			res = user.getCurSubDomain() + "|" + user.getMaxSubDomain();
		}
		
		return res;
	}
	
}
