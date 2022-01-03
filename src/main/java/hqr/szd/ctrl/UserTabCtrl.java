package hqr.szd.ctrl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
		if(hasAccess()) {
			return "tabs/user";
		}
		else {
			return "error/403";
		}
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

		if(hasAccess()) {
			return gi.getUsers(intPage, intRows);
		}
		else {
			return "403";
		}
	}
	
	private boolean hasAccess() {
		UserDetails ud = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Collection<? extends GrantedAuthority> cl = ud.getAuthorities();
		for (GrantedAuthority ga : cl) {
			String role = ga.getAuthority();
			if(role.indexOf("ADMIN")>=0) {
				return true;
			}
		}
		return false;
	}
}
