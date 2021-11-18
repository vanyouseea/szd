package hqr.szd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import hqr.szd.dao.SzdDomainRepo;
import hqr.szd.domain.SzdDomain;

@Service
public class GetDomain {
	@Autowired
	private SzdDomainRepo sdr;
	
	public String getAllDomainInfo(int intRows, int intPage) {
		long total = sdr.count();
		List<SzdDomain> rows = new ArrayList<SzdDomain>();
		if(total>0) {
			rows = sdr.getSysRpt(intRows * (intPage - 1), intRows * intPage );
		}
		
		HashMap map = new HashMap();
		map.put("total", total);
		map.put("rows", rows);

		return JSON.toJSON(map).toString();
		
	}
	
}
