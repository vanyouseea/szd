package hqr.szd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hqr.szd.domain.SzdInviteInfo;


@Repository
public interface SzdInviteInfoRepo extends JpaRepository<SzdInviteInfo, String> {
	@Query(value="select invite_id, start_dt, end_dt,invite_status,user_domain_cnt,result "
			+ "from (select A.*, rownum ro from (select * from szd_invite_info order by start_dt desc) A  where rownum<= :endRow ) where ro>:startRow ", nativeQuery = true)
	List<SzdInviteInfo> getInviteInfos(int startRow, int endRow);
}
