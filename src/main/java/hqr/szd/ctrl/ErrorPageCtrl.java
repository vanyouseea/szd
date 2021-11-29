package hqr.szd.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorPageCtrl {
	@RequestMapping(value = {"/error/403.html"})
	public String dummy403() {
		return "error/403";
	}
	
	@RequestMapping(value = {"/error/401.html"})
	public String dummy401() {
		return "error/401";
	}
}
