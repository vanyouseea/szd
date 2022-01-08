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

import hqr.szd.dao.TaUserRepo;
import hqr.szd.domain.TaUser;
import hqr.szd.service.DeleteDomain;
import hqr.szd.service.GetDomain;
import hqr.szd.service.GetDomainFromCf;
import hqr.szd.service.UpdateDomain;

@Controller
public class DomainCtrl {
	
	@Autowired
	private GetDomain gd;
	
	@Autowired
	private GetDomainFromCf gdcf;
	
	@Autowired
	private UpdateDomain ud;
	
	@Autowired
	private DeleteDomain dd;
	
	@Autowired
	private TaUserRepo tur;
	
	@RequestMapping(value = {"/tabs/domain.html"})
	public String dummy() {
		if(hasAccess()) {
			return "tabs/domain";
		}
		else {
			return "error/403";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getDomain"})
	public String getDomain(String page, String rows) {
		if(hasAccess()) {
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
			
			return gd.getAllDomainInfo(intRows, intPage);
		}
		else {
			return "403";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getDomainFromCf"})
	public String getDomainFromCf() {
		if(hasAccess()) {
			UserDetails ud = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			TaUser tu = tur.getUserById(ud.getUsername());
			int userSeqNo = tu.getSeqNo();
			return gdcf.initDomainInfo(userSeqNo);
		}
		else {
			return "403";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = {"/updateDomain"})
	public void updateDomain(@RequestParam(name="seqNos") String seqNos) {
		if(hasAccess()) {
			ud.saveDomains(seqNos);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = {"/deleteDomain"})
	public void deleteDomain(@RequestParam(name="seqNos") String seqNos) {
		if(hasAccess()) {
			dd.deleteDomains(seqNos);
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
