package hqr.szd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hqr.szd.dao.SzdDomainRepo;

@Service
public class DeleteDomain {
	
	@Autowired
	private SzdDomainRepo sdr;
	
	public void deleteDomains(String seqNos) {
		String strArr[] = seqNos.split(",");
		for (String str : strArr) {
			int seqNo = Integer.parseInt(str);
			sdr.deleteById(seqNo);
		}
	}
	
}
