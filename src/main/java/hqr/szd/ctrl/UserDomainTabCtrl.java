package hqr.szd.ctrl;

import java.security.Principal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import hqr.szd.service.DeleteDnsRecords;
import hqr.szd.service.GetUserDomainMapInfo;

@Controller
public class UserDomainTabCtrl {
	
	@Autowired
	private GetUserDomainMapInfo gdmi;
	@Autowired
	private DeleteDnsRecords dnr;
	
	@RequestMapping(value = {"/tabs/userdomain.html"})
	public String dummy() {
		return "tabs/userdomain";
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getUserDomains"})
	public String getOfficeUser(String page, String rows) {
		
		UserDetails ud = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Collection<? extends GrantedAuthority>  cl = ud.getAuthorities();
		System.out.println("Req user is "+ud.getUsername());
		for (GrantedAuthority grantedAuthority : cl) {
			System.out.println("Role is "+grantedAuthority.getAuthority());
		}
		
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

		return gdmi.getUserDnsRecords(intPage, intRows);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/deleteDnsRecords"})
	public void deleteDnsRecords(@RequestParam(name="seqNos") String seqNos) {
		dnr.deleteRecords(seqNos);
	}
	
}
