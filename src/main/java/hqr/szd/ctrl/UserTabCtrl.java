package hqr.szd.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import hqr.szd.service.GetUserInfo;

@Controller
public class UserTabCtrl {
	
	@Autowired
	private GetUserInfo gi;
	
	@RequestMapping(value = {"/tabs/user.html"})
	public String dummy() {
		return "tabs/user";
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getUsers"})
	public String getOfficeUser(String page, String rows) {
		int intPage = 1;
		int intRows = 100;
		try {
			intPage = Integer.valueOf(page);
		}
		catch (Exception e) {
			System.out.println("Invalid page, force it to 1");
		}
		try {
			intRows = Integer.valueOf(rows);
		}
		catch (Exception e) {
			System.out.println("Invalid row, force it to 100");
		}

		return gi.getUsers(intPage, intRows);
	}
	
}
