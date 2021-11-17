package hqr.szd.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Service;
import hqr.szd.dao.TaMasterCdRepo;
import hqr.szd.domain.TaMasterCd;

@Service
public class ResetSystemInfo {
	
	@Autowired
	private JdbcTemplate jdbc;
	
	@Autowired
	private TaMasterCdRepo tmc;
	
	@CacheEvict(value= {"cacheSysInfo","cacheGlobalInd","cacheDefaultPwd"}, allEntries = true)
	public boolean executeSql() {
		boolean flag = false;
		
		//reload ta_master_cd.sql
		try(Connection conn = jdbc.getDataSource().getConnection();) {
			//Get GLOBAL_REG before delete all
			Optional<TaMasterCd> opt = tmc.findById("GLOBAL_REG");
			if(opt.isPresent()) {
				TaMasterCd globalReg = opt.get();
				loadFullSql(conn);
				tmc.saveAndFlush(globalReg);
			}
			//if not exist then load all
			else {
				loadFullSql(conn);
			}

			flag = true;
		} catch (SQLException e) {
			System.out.println("Got error:"+e.toString());
		}
		return flag;
	}
	
	private void loadFullSql(Connection conn) {
		tmc.deleteAll();
		tmc.flush();
		ScriptUtils.executeSqlScript(conn, new EncodedResource(new ClassPathResource("ta_master_cd.sql"), "utf-8"));
	}
	
}
