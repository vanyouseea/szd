package hqr.szd.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hqr.szd.dao.TaUserRepo;
import hqr.szd.domain.TaUser;
import hqr.szd.service.GetSharedDomain;
import hqr.szd.service.UpdateMyDomainMapInfo;

@Controller
public class SharedDomainCtrl {
	
	@Autowired
	private TaUserRepo tur;
	
	@Autowired
	private GetSharedDomain gsd;

	@Autowired
	private UpdateMyDomainMapInfo uudm;
	
	@RequestMapping(value = {"/tabs/sharedomain.html"})
	public String dummy() {
		return "tabs/sharedomain";
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getSharedDomain"})
	public String getDomain(String page, String rows) {
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
		
		return gsd.getAllDomainInfo(intRows, intPage);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/updateMyDnsRecords"})
	public String updateDnsRecords(@RequestParam(name="seqNo",required = false) String seqNo,
			@RequestParam(name="domainSeqNo") int domainSeqNo,
			@RequestParam(name="prefix") String prefix,
			@RequestParam(name="type") String type,
			@RequestParam(name="ip") String ip,
			@RequestParam(name="proxied") boolean proxied) {
		
		UserDetails ud = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		TaUser tu = tur.getUserById(ud.getUsername());
		int userSeqNo = tu.getSeqNo();
		return uudm.updateDnsRecords(seqNo, domainSeqNo,userSeqNo, prefix, type, ip, proxied );
	}

	
}
