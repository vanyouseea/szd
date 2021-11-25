package hqr.szd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;

import hqr.szd.dao.TaUserRepo;
import hqr.szd.domain.TaUser;


@Service
public class GetUserInfo {
	@Autowired
	private TaUserRepo repo;
	
	@Value("${UA}")
    private String ua;
	
	@Cacheable(value="cacheInviteInfo")
	public String getUsers(int intPage, int intRows) {
		long total = repo.count();
		List<TaUser> rows = new ArrayList<TaUser>();
		if(total>0) {
			rows = repo.getSzdUsers(intRows * (intPage - 1), intRows * intPage );
		}
		
		HashMap map = new HashMap();
		map.put("total", total);
		map.put("rows", rows);
		
		
		return JSON.toJSON(map).toString();
	}
	
}
