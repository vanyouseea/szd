package hqr.szd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hqr.szd.dao.SzdUserDomainMapRepo;

@Service
public class DeleteMyDnsRecords {
	@Autowired
	private SzdUserDomainMapRepo sdmr;
	
	public void deleteRecords(int userSeqNo, String seqNos) {
		String strArr[] = seqNos.split(",");
		for (String str : strArr) {
			int seqNo = Integer.parseInt(str);
			int cnt = sdmr.deleteBySeqNoAndUserSeqNo(seqNo, userSeqNo);
		}
	}
}
