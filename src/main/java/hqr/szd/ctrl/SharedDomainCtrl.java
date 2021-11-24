package hqr.szd.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hqr.szd.service.DeleteDomain;
import hqr.szd.service.GetDomain;
import hqr.szd.service.GetDomainFromCf;
import hqr.szd.service.GetSharedDomain;
import hqr.szd.service.UpdateDomain;

@Controller
public class SharedDomainCtrl {
	
	@Autowired
	private GetSharedDomain gsd;

	
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
	
}
