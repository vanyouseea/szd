package hqr.szd.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hqr.szd.domain.SzdUserDomainMap;

@Repository
public interface SzdUserDomainMapRepo extends JpaRepository<SzdUserDomainMap, Integer> {
	@Query(value="select seq_no,user_seq_no,domain_seq_no,type,prefix,ip,proxied,cur_domain,create_dt "
			+ "from (select A.*, rownum ro from (select * from szd_user_domain_map ) A  where rownum<= :endRow ) where ro>:startRow ", nativeQuery = true)
	List<SzdUserDomainMap> getUserDnsRecords(int startRow, int endRow);
	
	@Query(value="select seq_no,user_seq_no,domain_seq_no,type,prefix,ip,proxied,cur_domain,create_dt "
			+ "from (select A.*, rownum ro from (select * from szd_user_domain_map where user_seq_no=:userSeqNo) A  where rownum<= :endRow ) where ro>:startRow ", nativeQuery = true)
	List<SzdUserDomainMap> getMyDnsRecords(int userSeqNo, int startRow, int endRow);
	
	@Transactional
    @Modifying
	int deleteBySeqNoAndUserSeqNo(int seqNo, int userSeqNo);
	
}
