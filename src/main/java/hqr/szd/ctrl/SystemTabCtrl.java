package hqr.szd.ctrl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hqr.szd.service.DeleteSystemInfo;
import hqr.szd.service.GetSystemInfo;
import hqr.szd.service.ResetSystemInfo;
import hqr.szd.service.UpdateSystemInfo;

@Controller
public class SystemTabCtrl {
	
	@Autowired
	private GetSystemInfo gsi;
	
	@Autowired
	private DeleteSystemInfo dsi;
	
	@Autowired
	private UpdateSystemInfo usi;
	
	@Autowired
	private ResetSystemInfo rsi;
	
	@RequestMapping(value = {"/tabs/system.html"})
	public String dummy() {
		if(hasAccess()) {
			return "tabs/system";
		}
		else {
			return "error/403";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getSystemInfo"}, method = RequestMethod.POST)
	public String getSystemInfo() {
		if(hasAccess()) {
			return gsi.getAllSystemInfo();
		}
		else {
			return "403";
		}
	}	
	
	@ResponseBody
	@RequestMapping(value = {"/deleteSystemInfo"}, method = RequestMethod.POST)
	public boolean deleteSystemInfo(@RequestParam(name="keyTy") String keyTy) {
		if(hasAccess()) {
			return dsi.deletePk(keyTy);
		}
		else {
			return false;
		}
	}
	
	@ResponseBody
	@RequestMapping(value = {"/resetSystemInfo"}, method = RequestMethod.POST)
	public boolean resetSystemInfo() {
		if(hasAccess()) {
			return rsi.executeSql();
		}
		else {
			return false;
		}
	}
	
	@ResponseBody
	@RequestMapping(value = {"/updateSystemInfo"}, method = RequestMethod.POST)
	public boolean updateSystemInfo(@RequestParam(name="keyTy") String keyTy,
			@RequestParam(name="cd") String cd,
			@RequestParam(name="decode") String decode) {
		if(hasAccess()) {
			return usi.updateInfo(keyTy, cd, decode);
		}
		else {
			return false;
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
