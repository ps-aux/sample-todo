package sk.luce.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import sk.luce.data.repo.AccountRepo;
import sk.luce.data.UserAccount;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

@Component
public class DbUserDetailService implements UserDetailsService {

    private final AccountRepo userRepo;

    @Autowired
    public DbUserDetailService(AccountRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserAccount acc = userRepo.findByName(username);
        // When acc == null ends with exception and 401 is returned

        if (acc == null)
            throw new UsernameNotFoundException("User " + username + "not found");

        return new User(acc.getName(), acc.getPassword(),
                createAuthorityList("USER"));
    }
}
