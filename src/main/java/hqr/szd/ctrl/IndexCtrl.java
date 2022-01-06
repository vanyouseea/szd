package hqr.szd.ctrl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import hqr.szd.dao.TaUserRepo;
import hqr.szd.domain.TaUser;
import hqr.szd.service.CallBackService;
import hqr.szd.service.GetGlobalInd;
import hqr.szd.service.GetUserSubDomainInfo;

@Controller
public class IndexCtrl {
	
	@Autowired
	private GetGlobalInd ggl;
	
	@Autowired
	private CallBackService cbs;
	
	@Autowired
	private TaUserRepo tur;
	
	@Autowired
	GetUserSubDomainInfo gusd;
	
	@RequestMapping(value = {"/","/index.html"})
	public String index(HttpServletRequest request) {
		String ind = ggl.getIndicator();
		//request.getSession().setAttribute("tmc", tmc);
		if("Y".equals(ind)){
			System.out.println("Allow to reg admin");
			return "reg";
		}
		else {
			System.out.println("Not allow to reg admin");
			return "login";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getSubDomainInfo"})
	public String getSubDomainInfo(){
		UserDetails ud = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		TaUser enti = tur.getUserById(ud.getUsername());
		int mySeqNo = enti.getSeqNo();
		return gusd.getSubDomainCntInfo(mySeqNo);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/callback"})
	public String callback(HttpServletRequest request, @RequestBody(required=false) String body ){
		return cbs.handleCallBack(request, body);
	}
	
}
