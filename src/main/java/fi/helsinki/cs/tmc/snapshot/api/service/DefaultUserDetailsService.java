package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Role;
import fi.helsinki.cs.tmc.snapshot.api.model.User;
import fi.helsinki.cs.tmc.snapshot.api.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class DefaultUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    @Transactional
    public void executeFreely() {
        // populate db if needed
        if (userRepository.count() > 0) {
            return;
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin = userRepository.save(admin);

        final List<Role> roles = new ArrayList();

        Role role = new Role();
        role.setRolename("admin");
        roles.add(role);
        role = new Role();
        role.setRolename("user");
        roles.add(role);

        admin.setRoles(roles);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) {

        final User user = userRepository.findByUsername(username);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                getRolesAsGrantedAuthorities(user.getRoles()));

    }

    private List<GrantedAuthority> getRolesAsGrantedAuthorities(final List<Role> roles) {

        final List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRolename()));
        }

        return authorities;
    }
}
