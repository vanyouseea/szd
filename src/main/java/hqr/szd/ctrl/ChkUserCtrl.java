package hqr.szd.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hqr.szd.service.ChkUser;

@RestController
public class ChkUserCtrl {
	
	@Autowired
	private ChkUser cu;
	
	@RequestMapping(value = "/chkUserId")
	public String chkUserId(@RequestParam(name="userid") String userid) {
		return cu.checkCanReg(userid);
	}
}
