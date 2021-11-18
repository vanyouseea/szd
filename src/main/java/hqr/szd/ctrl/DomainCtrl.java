package hqr.szd.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import hqr.szd.service.GetDomain;
import hqr.szd.service.GetDomainFromCf;

@Controller
public class DomainCtrl {
	
	@Autowired
	private GetDomain gd;
	
	@Autowired
	private GetDomainFromCf gdcf;
	
	@RequestMapping(value = {"/tabs/domain.html"})
	public String dummy() {
		return "tabs/domain";
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getDomain"})
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
		
		return gd.getAllDomainInfo(intRows, intPage);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getDomainFromCf"})
	public String getDomainFromCf() {
		return gdcf.initDomainInfo();
	}
}
