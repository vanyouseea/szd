package hqr.szd.ctrl;


import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
		UserDetails ud = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Collection<? extends GrantedAuthority> cl = ud.getAuthorities();
		for (GrantedAuthority ga : cl) {
			String role = ga.getAuthority();
			System.out.println(ud.getUsername()+"role:"+role);
			if(role.indexOf("ADMIN")>=0) {
				System.out.println("Go to home_admin");
				return "home_admin";
			}
		}
		System.out.println("Go to home_user");
		return "home_user";
	}
	
	
}
