package hqr.szd.ctrl;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginCtrl {

	@RequestMapping(value = "/loginPage", method = RequestMethod.GET)
	public String goLogin() {
		return "login";
	}
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String goHome() {
		return "home";
	}
	
	
}
