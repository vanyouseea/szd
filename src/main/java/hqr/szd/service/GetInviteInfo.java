package hqr.szd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import hqr.szd.dao.SzdInviteInfoRepo;
import hqr.szd.domain.SzdInviteInfo;


@Service
public class GetInviteInfo {
	@Autowired
	private SzdInviteInfoRepo repo;
	
	@Value("${UA}")
    private String ua;
	
	@Cacheable(value="cacheInviteInfo")
	public String getAllInviteInfo(int intRows, int intPage) {
		long total = repo.count();
		List<SzdInviteInfo> rows = new ArrayList<SzdInviteInfo>();
		if(total>0) {
			rows = repo.getInviteInfos(intRows * (intPage - 1), intRows * intPage );
		}
		
		HashMap map = new HashMap();
		map.put("total", total);
		map.put("rows", rows);
		return JSON.toJSON(map).toString();
	}
	
}
