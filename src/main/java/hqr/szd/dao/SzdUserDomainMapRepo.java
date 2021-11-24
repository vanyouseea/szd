package hqr.szd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hqr.szd.domain.SzdUserDomainMap;

@Repository
public interface SzdUserDomainMapRepo extends JpaRepository<SzdUserDomainMap, Integer> {
	@Query(value="select seq_no,user_seq_no,domain_seq_no,type,prefix,ip,proxied,create_dt "
			+ "from (select A.*, rownum ro from (select * from szd_user_domain_map ) A  where rownum<= :endRow ) where ro>:startRow ", nativeQuery = true)
	List<SzdUserDomainMap> getUserDnsRecords(int startRow, int endRow);
}
