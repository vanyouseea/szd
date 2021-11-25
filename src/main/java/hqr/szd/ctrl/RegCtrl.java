package hqr.szd.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import hqr.szd.domain.TaUser;
import hqr.szd.service.GetGlobalInd;
import hqr.szd.service.RegUser;

@Controller
public class RegCtrl {
	
	@Autowired
	private RegUser su;
	
	@Autowired
	private GetGlobalInd ggl;
	
	@RequestMapping(value = "/reg", method = RequestMethod.POST)
	public String regUser(@RequestParam(name="userid") String userid,@RequestParam(name="pwd") String pwd) {
		String ind = ggl.getIndicator();
		if("Y".equals(ind)){
			System.out.println("Allow to reg admin");
			TaUser user = new TaUser();
			user.setUserId(userid);
			user.setPasswd(pwd);
			user.setAcctRole("9");
			su.save(user);
		}
		else {
			System.out.println("Not allow to reg admin");
		}
		return "login";
	}
	
	@RequestMapping(value = "/reg", method = RequestMethod.GET)
	public String regUser() {
		String ind = ggl.getIndicator();
		if(!"Y".equals(ind)){
			return "login";
		}
		else {
			return "reg";
		}
	}
	
}
