package hqr.szd.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import hqr.szd.dao.SzdInviteInfoRepo;
import hqr.szd.dao.TaUserRepo;
import hqr.szd.domain.SzdInviteInfo;
import hqr.szd.domain.TaUser;

@Service
public class CreateUserByInviteCd {
	
	@Autowired
	private SzdInviteInfoRepo tii;
	
	@Autowired
	private TaUserRepo tur;
	
	@Value("${UA}")
    private String ua;
	
	@CacheEvict(value= {"cacheOfficeUser","cacheInviteInfo","cacheOfficeUserSearch"}, allEntries = true)
	public String createCommonUser(String inviteCd, String userId, String password ){
		String resultMsg = "失败";
		Optional<SzdInviteInfo> opt = tii.findById(inviteCd);
		if(opt.isPresent()) {
			SzdInviteInfo tiiDo = opt.get();
			
			Date currentDt = new Date();
			Date startDt = tiiDo.getStartDt();
			Date endDt = tiiDo.getEndDt();
			
			if(startDt!=null&&startDt.after(currentDt)) {
				resultMsg = "此邀请码尚未生效";
				return resultMsg;
			}
			
			if(endDt!=null&&endDt.before(currentDt)) {
				resultMsg = "此邀请码已过期";
				return resultMsg;
			}
			
			if("1".equals(tiiDo.getInviteStatus())){
				//update the invite code to in-progress
				tiiDo.setInviteStatus("2");
				tii.save(tiiDo);
				
				int check = tur.chkUserId(userId);
				if(check==0) {
					TaUser enti = new TaUser();
					enti.setUserId(userId);
					enti.setPasswd(password);
					enti.setAcctRole("1");
					tur.saveAndFlush(enti);
					tiiDo.setResult(userId);
					tiiDo.setInviteStatus("3");
					tii.saveAndFlush(tiiDo);
					resultMsg = "0|"+userId;
				}
				else {
					tiiDo.setInviteStatus("1");
					tii.saveAndFlush(tiiDo);
					resultMsg = "此用户名已被占用";
				}
			}
			else if("2".equals(tiiDo.getInviteStatus())){
				resultMsg = "此邀请码正被使用中";
			}
			else if("3".equals(tiiDo.getInviteStatus())){
				resultMsg = "此邀请码已使用";
			}
			else if("4".equals(tiiDo.getInviteStatus())){
				resultMsg = "此邀请码使用出现错误";
			}
			else {
				resultMsg = "无效的邀请码状态";
			}
		}
		else {
			resultMsg = "无效的邀请码";
		}
		
		return resultMsg;
	}
}
