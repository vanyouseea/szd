package hqr.szd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hqr.szd.domain.SzdDomain;

@Repository
public interface SzdDomainRepo extends JpaRepository<SzdDomain, Integer> {
	
	@Query(value="select seq_no,shared,user_seq_no,sub_zone_id,sub_zone,zone_id,zone,create_dt from (select A.*, rownum ro from (select * from szd_domain) A  where rownum<= :endRow ) where ro>:startRow ", nativeQuery = true)
	List<SzdDomain> getDomains(int startRow, int endRow);
	
	//hidden other user info
	@Query(value="select seq_no,shared, -1 user_seq_no ,sub_zone_id,sub_zone,zone_id,zone,create_dt from (select A.*, rownum ro from (select * from szd_domain where shared='是') A  where rownum<= :endRow ) where ro>:startRow ", nativeQuery = true)
	List<SzdDomain> getSharedDomains(int startRow, int endRow);
	
	List<SzdDomain> findBySubZoneAndUserSeqNo(String subZone, int userSeqNo);
	
	List<SzdDomain> findBySubZone(String subZone);
	
}
