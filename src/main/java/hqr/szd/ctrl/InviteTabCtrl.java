package hqr.szd.ctrl;

import java.io.File;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
		return "tabs/invite";
	}
	
	@RequestMapping(value = {"/tabs/dialogs/createInviteCd.html"})
	public String dummy2(HttpServletRequest req) {
		return "tabs/dialogs/createInviteCd";
	}
	
	@RequestMapping(value = {"/refer.html"})
	public String dummy3() {
		return "refer";
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getInvite"})
	public String getInvite(String page, String rows) {
		int intPage = 1;
		int intRows = 10;
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
			System.out.println("Invalid row, force it to 10");
		}
		
		return gii.getAllInviteInfo(intRows, intPage);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/massCreateInviteCd"})
	public String massCreateInviteCd(@RequestParam(name="count") String countStr,
			@RequestParam(name="startDt",required = false) String startDt,
			@RequestParam(name="endDt",required = false) String endDt) {
		
		System.out.println("startDt:"+startDt+" endDt:"+endDt);
		int count = 10;
		try {
			count = Integer.parseInt(countStr);
		}
		catch (Exception e) {}
		
		return mci.create(count, startDt, endDt);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/delInviteCds"})
	public String deleteInviteInfo(@RequestParam(name="uuids") String uuids) {
		dii.deleteInviteCd(uuids);
		return "已删除";
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
		ei.exportInvite();
		
		return export(new File("export_invite_info.csv"));
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
	
}
