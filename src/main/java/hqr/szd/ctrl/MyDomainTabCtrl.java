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
import hqr.szd.service.DeleteMyDnsRecords;
import hqr.szd.service.GetMyDomainMapInfo;

@Controller
public class MyDomainTabCtrl {
	
	@Autowired
	private TaUserRepo tur;
	
	@Autowired
	private GetMyDomainMapInfo gmdm;
	
	@Autowired
	private DeleteMyDnsRecords dmdr;
	
	@RequestMapping(value = {"/tabs/mydomain.html"})
	public String dummy() {
		return "tabs/mydomain";
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getMyDomains"})
	public String getMyDomains(String page, String rows) {
		
		UserDetails ud = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		TaUser enti = tur.getUserById(ud.getUsername());
		int mySeqNo = enti.getSeqNo();
		
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

		return gmdm.getUserDnsRecords(mySeqNo, intPage, intRows);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/deleteMyDnsRecords"})
	public void deleteMyDnsRecords(@RequestParam(name="seqNos") String seqNos) {
		UserDetails ud = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		TaUser enti = tur.getUserById(ud.getUsername());
		int mySeqNo = enti.getSeqNo();
		dmdr.deleteRecords(mySeqNo, seqNos);
	}
	
}
