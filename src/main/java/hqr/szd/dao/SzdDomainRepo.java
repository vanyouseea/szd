package hqr.szd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hqr.szd.domain.SzdDomain;


public interface SzdDomainRepo extends JpaRepository<SzdDomain, Integer> {
	
	@Query(value="select seq_no,shared,sub_zone_id,sub_zone,zone_id,zone,create_dt from (select A.*, rownum ro from (select * from szd_domain) A  where rownum<= :endRow ) where ro>:startRow ", nativeQuery = true)
	List<SzdDomain> getSysRpt(int startRow, int endRow);
	
}
