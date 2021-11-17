package hqr.szd.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import hqr.szd.dao.TaMasterCdRepo;
import hqr.szd.domain.TaMasterCd;

@Service
public class GetGlobalInd {
	@Autowired
	private TaMasterCdRepo tmcRepo;
	
	@Cacheable(value="cacheGlobalInd")
	public String getIndicator() {
		String ind = "N";
		Optional<TaMasterCd> opt = tmcRepo.findById("GLOBAL_REG");
		if(opt.isPresent()) {
			TaMasterCd tmc = opt.get();
			ind = tmc.getCd();
		}
		else {
			ind= "Y";
		}
		return ind;
	}
}
