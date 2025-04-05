package pillihuaman.com.pe.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.security.entity.user.User;
import pillihuaman.com.pe.security.repository.UserRepository;

import java.util.Optional;

@Component
public class UserInfoUserDetailsService implements UserDetailsService {
    private final UserRepository repository;
    public UserInfoUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Optional<User> userInfo = repository.findByEmail(username);
            return userInfo.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            //return userInfo.map(User::new)
            //      .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
        } catch (Exception ex) {
            throw ex;
        }

    }
}
