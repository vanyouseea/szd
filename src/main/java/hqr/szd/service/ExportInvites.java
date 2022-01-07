package hqr.szd.service;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hqr.szd.dao.SzdInviteInfoRepo;
import hqr.szd.domain.SzdInviteInfo;

@Service
public class ExportInvites {
	
	@Autowired
	private SzdInviteInfoRepo tii;
	
	public boolean exportInvite(){
		boolean flag = false;
		try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("export_invite_info.csv"), "GB2312"))){
			bw.write("邀请码,生效时间,失效时间,允许子域数,状态,结果"+System.getProperty("line.separator"));
			
			List<SzdInviteInfo> list = tii.findAll();
			for (SzdInviteInfo en : list) {
				StringBuilder sb = new StringBuilder();
				sb.append(en.getInviteId()).append(",");
				sb.append(en.getStartDt()).append(",");
				sb.append(en.getEndDt()).append(",");
				sb.append(en.getUserDomainCnt()).append(",");
				sb.append(en.getInviteStatus()).append(",");
				sb.append(en.getResult()).append(System.getProperty("line.separator"));
				bw.write(sb.toString());
			}
			
			bw.flush();
			bw.close();
			flag = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
}
