package hqr.szd.ctrl;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hqr.szd.service.CreateUserByInviteCd;
import hqr.szd.service.DeleteInviteInfo;
import hqr.szd.service.ExportInvites;
import hqr.szd.service.GetInviteInfo;
import hqr.szd.service.MassCreateInviteCd;

@Controller
public class InviteTabCtrl {
	
	@Autowired
	private GetInviteInfo gii;
	
	@Autowired
	private MassCreateInviteCd mci;
	
	@Autowired
	private DeleteInviteInfo dii;
	
	@Autowired
	private CreateUserByInviteCd coubi;
	
	@Autowired
	private ExportInvites ei;
	
	@RequestMapping(value = {"/tabs/invite.html"})
	public String dummy() {
		if(hasAccess()) {
			return "tabs/invite";
		}
		else {
			return "error/403";
		}
	}
	
	@RequestMapping(value = {"/tabs/dialogs/createInviteCd.html"})
	public String dummy2(HttpServletRequest req) {
		if(hasAccess()) {
			return "tabs/dialogs/createInviteCd";
		}
		else {
			return "error/403";
		}
	}
	
	@RequestMapping(value = {"/refer.html"})
	public String dummy3() {
		return "refer";
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getInvite"})
	public String getInvite(String page, String rows) {
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
			return gii.getAllInviteInfo(intRows, intPage);
		}
		else {
			return "403";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = {"/massCreateInviteCd"})
	public String massCreateInviteCd(@RequestParam(name="count") String countStr,
			@RequestParam(name="startDt",required = false) String startDt,
			@RequestParam(name="endDt",required = false) String endDt,
			@RequestParam(name="userDomainCnt",required = false) int userDomainCnt) {
		
		System.out.println("startDt:"+startDt+" endDt:"+endDt + " userDomainCnt:"+userDomainCnt);
		int count = 10;
		try {
			count = Integer.parseInt(countStr);
		}
		catch (Exception e) {}
		
		if(hasAccess()) {
			return mci.create(count, startDt, endDt, userDomainCnt);
		}
		else {
			return "403";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = {"/delInviteCds"})
	public String deleteInviteInfo(@RequestParam(name="uuids") String uuids) {
		if(hasAccess()) {
			dii.deleteInviteCd(uuids);
			return "已删除";
		}
		else {
			return "403";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = {"/createUserByInviteCd"})
	public String createUserByInviteCd(@RequestParam(name="inviteCd") String inviteCd,
			@RequestParam(name="userId") String userId,
			@RequestParam(name="password") String password) {
		return coubi.createCommonUser(inviteCd, userId, password);
	}
	
	@RequestMapping(value = {"/exportInvites"}, method = RequestMethod.GET)
	public ResponseEntity<FileSystemResource> exportApps(){
		if(hasAccess()) {
			ei.exportInvite();
			return export(new File("export_invite_info.csv"));
		}
		else {
			return null;
		}
	}
	
	public ResponseEntity<FileSystemResource> export(File file) { 
		HttpHeaders headers = new HttpHeaders();
	    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
	    headers.add("Content-Disposition", "attachment; filename=export_invite_info.csv");
	    headers.add("Pragma", "no-cache");
	    headers.add("Expires", "0");
	    headers.add("Last-Modified", new Date().toString());
	    headers.add("ETag", String.valueOf(System.currentTimeMillis()));
	 
	    return ResponseEntity.ok().headers(headers) .contentLength(file.length()) .contentType(MediaType.parseMediaType("text/csv")) .body(new FileSystemResource(file));
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
