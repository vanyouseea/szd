package hqr.szd.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import hqr.szd.dao.TaUserRepo;

@Service
public class ChkUser {
	@Autowired
	private TaUserRepo tup;
	
	@Cacheable(value="cacheTaUser")
	public String checkCanReg(String userid) {
		int cnt = tup.chkUserId(userid);
		if(cnt>0) {
			return "N";
		}
		else{
			return "Y";
		}
	}
}
