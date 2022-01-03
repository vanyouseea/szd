package hqr.szd.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import hqr.szd.dao.SzdInviteInfoRepo;
import hqr.szd.domain.SzdInviteInfo;

@Service
public class MassCreateInviteCd {
	
	@Autowired
	private SzdInviteInfoRepo tii;
	
	@Value("${UA}")
    private String ua;
	
	private SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
	
	@CacheEvict(value="cacheInviteInfo", allEntries = true)
	public String create(int count, String startDt, String endDt, int userDomainCnt) {
		String result = "";
		
		for(int i=0;i<count;i++) {
			SzdInviteInfo enti = new SzdInviteInfo();
			enti.setInviteId(UUID.randomUUID().toString());
			if(startDt!=null) {
				try {
					enti.setStartDt(yyyyMMdd.parse(startDt));
				} catch (ParseException e) {}
			}
			if(endDt!=null) {
				try {
					enti.setEndDt(yyyyMMdd.parse(endDt));
				} catch (ParseException e) {}
			}
			enti.setInviteStatus("1");
			enti.setUserDomainCnt(userDomainCnt);
			tii.save(enti);
		}
		tii.flush();
		result = "成功创建"+count+"个邀请码";
		
		return result;
	}
	
}
