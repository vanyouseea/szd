package hqr.szd.ctrl;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import hqr.szd.service.DeleteUserDnsRecords;
import hqr.szd.service.GetUserDomainMapInfo;
import hqr.szd.service.UpdateUserDomainMapInfo;

@Controller
public class UserDomainTabCtrl {
	
	@Autowired
	private GetUserDomainMapInfo gdmi;
	
	@Autowired
	private DeleteUserDnsRecords dnr;
	
	@Autowired
	private UpdateUserDomainMapInfo uudm;
	
	@RequestMapping(value = {"/tabs/userdomain.html"})
	public String dummy() {
		if(hasAccess()) {
			return "tabs/userdomain";
		}
		else {
			return "error/403";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getUserDomains"})
	public String getUserDomains(String page, String rows) {
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
			return gdmi.getUserDnsRecords(intPage, intRows);
		}
		else {
			return "403";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = {"/deleteUserDnsRecords"})
	public String deleteUserDnsRecords(@RequestParam(name="seqNos") String seqNos) {
		if(hasAccess()) {
			return dnr.deleteRecords(seqNos);
		}
		else {
			return "403";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = {"/updateUserDnsRecords"})
	public String updateUserDnsRecords(@RequestParam(name="seqNo") int seqNo,
			@RequestParam(name="prefix") String prefix,
			@RequestParam(name="type") String type,
			@RequestParam(name="ip") String ip,
			@RequestParam(name="proxied") boolean proxied) {
		type = "A";
		if(hasAccess()) {
			return uudm.updateDnsRecords(seqNo, prefix, type, ip, proxied);
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
