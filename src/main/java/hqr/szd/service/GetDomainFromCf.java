package hqr.szd.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import hqr.szd.dao.SzdDomainRepo;
import hqr.szd.dao.TaMasterCdRepo;
import hqr.szd.domain.SzdDomain;
import hqr.szd.domain.TaMasterCd;

@Service
public class GetDomainFromCf {
	
	@Autowired
	private TaMasterCdRepo tmc;
	
	@Autowired
	private SzdDomainRepo sdr;
	
	@Value("${UA}")
    private String ua;
	
	private RestTemplate restTemplate = new RestTemplate();
	
	public String initDomainInfo() {
		//什么都不返回代表成功
		String res = "";
		
		Optional<TaMasterCd> opt1 = tmc.findById("CF_AUTH_EMAIL");
		Optional<TaMasterCd> opt2 = tmc.findById("CF_AUTH_KEY");

		if(opt1.isPresent()&&opt2.isPresent()) {
			TaMasterCd en1 = opt1.get();
			TaMasterCd en2 = opt2.get();
			if(en1.getCd()!=null&&!"".equals(en1.getCd())&&en2.getCd()!=null&&!"".equals(en2.getCd())) {
				sdr.deleteAll();
				HashMap<String, Object> map = getZoneIds(en1.getCd(), en2.getCd());
				String status = (String)map.get("status");
				if("succ".equals(status)) {
					ArrayList<String> list = (ArrayList<String>)map.get("res");
					getSubZoneIds(list);
				}
				else {
					res = (String)map.get("res");
				}
			}
			else {
				res = "请先前往系统配置中配置CF_AUTH_EMAIL和CF_AUTH_KEY";
			}
		}
		else {
			res = "CF_AUTH_EMAIL或CF_AUTH_KEY不存在，请检查";
		}
		
		return res;
	}
	
	private HashMap<String, Object> getZoneIds(String cfAuthEmail, String cfAuthKey){
		HashMap<String,Object> map = new HashMap<String, Object>();
		ArrayList<String> list = new ArrayList<String>();
		
		String endpoint = "https://api.cloudflare.com/client/v4/zones";
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.USER_AGENT, ua);
		headers.add("X-Auth-Email", cfAuthEmail);
		headers.add("X-Auth-Key", cfAuthKey);
		headers.add("Content-Type", "application/json");
		String body="";
		HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
		try {
			ResponseEntity<String> response= restTemplate.exchange(endpoint, HttpMethod.GET, requestEntity, String.class);
			JSONObject jo = JSON.parseObject(response.getBody());
			boolean flag = jo.getBoolean("success");
			if(flag) {
				map.put("status", "succ");
				JSONArray ja = jo.getJSONArray("result");
				for (Object obj : ja) {
					JSONObject jb = (JSONObject)obj;
					String zoneStatus = jb.getString("status");
					//只加入active的域名
					if("active".equals(zoneStatus)) {
						String zoneId = jb.getString("id");
						String zoneName = jb.getString("name");
						list.add(zoneId);
						
						SzdDomain enti = new SzdDomain();
						enti.setZone(zoneName);
						enti.setZoneId(zoneId);
						enti.setCreateDt(new Date());
						sdr.saveAndFlush(enti);
					}
				}
				map.put("res", list);
			}
			else {
				JSONArray ja = jo.getJSONArray("errors");
				map.put("status", "fail");
				map.put("res", ja);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("status", "fail");
			map.put("res", "CF_AUTH_EMAIL或CF_AUTH_KEY配置不正确");
		}
		return map;
	}
	
	private void getSubZoneIds(ArrayList<String> list) {
		System.out.println("Go here");
	}
	
}