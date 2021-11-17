package hqr.szd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import hqr.szd.dao.TaUserRepo;
import hqr.szd.domain.TaUser;

@Component
public class TaUserDetailsService implements UserDetailsService{

	@Autowired
	private TaUserRepo tup;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		TaUser taUser = tup.getUserById(username);
        return new User(username, taUser.getPasswd(),AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
	}

}
