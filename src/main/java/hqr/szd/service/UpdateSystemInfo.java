package hqr.szd.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import hqr.szd.dao.TaMasterCdRepo;
import hqr.szd.domain.TaMasterCd;

@Service
public class UpdateSystemInfo {

	@Autowired
	private TaMasterCdRepo tmc;
	
	@CacheEvict(value= {"cacheSysInfo","cacheGlobalInd","cacheDefaultPwd","cacheLicense","cacheOfficeUser","cacheOfficeUserSearch"}, allEntries = true)
	public boolean updateInfo(String keyTy, String cd, String decode) {
		boolean flag = false;
		Optional<TaMasterCd> opt = tmc.findById(keyTy);
		//can find -> update
		if(opt.isPresent()) {
			TaMasterCd enti = opt.get();
			enti.setCd(cd);
			enti.setDecode(decode);
			enti.setLastUpdateId("szd");
			tmc.saveAndFlush(enti);
			flag = true;
		}
		//can find -> insert
		else {
			TaMasterCd enti = new TaMasterCd();
			enti.setKeyTy(keyTy);
			enti.setCd(cd);
			enti.setDecode(decode);
			enti.setLastUpdateId("szd");
			enti.setLastUpdateDt(new Date());
			tmc.saveAndFlush(enti);
			flag = true;
		}
		return flag;
	}
	
}
