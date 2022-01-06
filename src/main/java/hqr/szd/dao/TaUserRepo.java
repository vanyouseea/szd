package hqr.szd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import hqr.szd.domain.TaUser;

@Repository
public interface TaUserRepo extends JpaRepository<TaUser, Integer> {
	@Query(value="select count(1) from ta_user where user_id= :userid ", nativeQuery = true)
	int chkUserId(String userid);
	
	@Query(value="select * from ta_user where user_id= :userid ", nativeQuery = true)
	TaUser getUserById(String userid);
	
	@Query(value="select seq_no,user_id, '***' passwd,cf_auth_email,cf_auth_key,acct_role,acct_status,cur_sub_domain,max_sub_domain "
			+ "from (select A.*, rownum ro from (select * from ta_user order by acct_role desc) A  where rownum<= :endRow ) where ro>:startRow ", nativeQuery = true)
	List<TaUser> getSzdUsers(int startRow, int endRow);
	
}
