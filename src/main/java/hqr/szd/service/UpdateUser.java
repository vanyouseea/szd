package hqr.szd.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hqr.szd.dao.TaUserRepo;
import hqr.szd.domain.TaUser;

@Service
public class UpdateUser {
	
	@Autowired
	private TaUserRepo tur;
	
	public void enableDisableUser(String uids, boolean status) {
		String arrs [] = uids.split(",");	
		for (String string : arrs) {
			Optional<TaUser> opt = tur.findById(Integer.parseInt(string));
			if(opt.isPresent()) {
				TaUser user = opt.get();
				if(!"9".equals(user.getAcctRole())) {
					user.setAcctStatus(status?1:0);
					tur.saveAndFlush(user);
					System.out.println("enable/disable user:"+user.getUserId()+" successfully");
				}
				else {
					System.out.println("allow to enable/disable admin user");
				}
			}
		}
	}
	
}
