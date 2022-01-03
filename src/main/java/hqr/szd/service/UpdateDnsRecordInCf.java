package hqr.szd.service;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Optional;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import hqr.szd.dao.TaMasterCdRepo;
import hqr.szd.domain.TaMasterCd;

/**
 *
PUT https://api.cloudflare.com/client/v4/zones/${zone_id}/dns_records/${sub_zone_id}
JSON:
{
	"type":"A",
	"name":"cgi",
	"content":"2.3.4.5",
	"ttl":3600,
	"proxied":false
}
 *
 */

@Service
public class UpdateDnsRecordInCf {
	@Autowired
	private TaMasterCdRepo tmc;
	
	@Value("${UA}")
    private String ua;
	
	private RestTemplate restTemplate;
	
	public void initlRestTemplate() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					return true;
				}
			}).build();
			
			SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
			CloseableHttpClient httpClient;
			
			Optional<TaMasterCd> opt = tmc.findById("HTTP_PROXY");
			if(opt.isPresent()) {
				TaMasterCd enti = opt.get();
				if("".equals( enti.getCd() ) ) {
					httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
				}
				else {
					try {
						String proxy[] = enti.getCd().split(":");
						httpClient = HttpClients.custom().setSSLSocketFactory(csf).setProxy(new HttpHost(proxy[0], Integer.parseInt(proxy[1]))).build();
					}
					catch (Exception e) {
						System.out.println("invalid proxy "+enti.getCd());
						httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
					}
				}
			}
			else {
				httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
			}

			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

			requestFactory.setHttpClient(httpClient);
			restTemplate = new RestTemplate(requestFactory);
			restTemplate.setErrorHandler(new ResponseErrorHandler() {
				@Override
				public boolean hasError(ClientHttpResponse response) throws IOException {
					return false;
				}
				@Override
				public void handleError(ClientHttpResponse response) throws IOException {}
			});
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String update(String zondId, String subZoneId, String type, String prefix, String ip, boolean proxied) {
		String res = "";
		initlRestTemplate();
		
		Optional<TaMasterCd> opt1 = tmc.findById("CF_AUTH_EMAIL");
		Optional<TaMasterCd> opt2 = tmc.findById("CF_AUTH_KEY");

		if(opt1.isPresent()&&opt2.isPresent()) {
			TaMasterCd en1 = opt1.get();
			TaMasterCd en2 = opt2.get();
			if(en1.getCd()!=null&&!"".equals(en1.getCd())&&en2.getCd()!=null&&!"".equals(en2.getCd())) {
				String cfAuthEmail = en1.getCd();
				String cfAuthKey = en2.getCd();
				
				String endpoint = "https://api.cloudflare.com/client/v4/zones/"+zondId+"/dns_records/"+subZoneId;
				HttpHeaders headers = new HttpHeaders();
				headers.set(HttpHeaders.USER_AGENT, ua);
				headers.add("X-Auth-Email", cfAuthEmail);
				headers.add("X-Auth-Key", cfAuthKey);
				headers.add("Content-Type", "application/json");
				String json = "{\"type\":\"A\",\"name\":\""+prefix+"\",\"content\":\""+ip+"\",\"ttl\":3600,\"proxied\":"+proxied+"}";
				System.out.println("UpdateDnsJson:"+json);
				HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
				try {
					ResponseEntity<String> response= restTemplate.exchange(endpoint, HttpMethod.PUT, requestEntity, String.class);
					if(response.getStatusCodeValue()==200) {
						//200 Succ
					}
					else {
						//400 Fail
						JSONObject jo = JSON.parseObject(response.getBody());
						JSONArray ja = jo.getJSONArray("errors");
						JSONObject erroObj = (JSONObject)ja.get(0);
						res = erroObj.getString("message");
						if("Record already exists.".equals(res)) {
							res = "此记录已存在，请选择另外选择";
						}
					}
				}
				catch (Exception e) {
					res = "与CF通信遇到问题 "+e.toString();
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
	
}
