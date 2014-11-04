package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.app.App;
import fi.helsinki.cs.tmc.snapshot.api.model.Role;
import fi.helsinki.cs.tmc.snapshot.api.model.User;
import fi.helsinki.cs.tmc.snapshot.api.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class DefaultUserDetailsServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowExceptionOnNonExistentUsername() {

        userDetailsService.loadUserByUsername("username");
    }

    @Test
    public void shouldLoadUserByUsername() {

        User user = new User();
        user.setUsername("username");
        user.setPassword("passwordpassword");

        final Role role = new Role();
        role.setRolename("user");

        final List<Role> roles = new ArrayList<>();
        roles.add(role);

        user.setRoles(roles);

        user = userRepository.save(user);

        final UserDetails userDetails = userDetailsService.loadUserByUsername("username");

        assertEquals(userDetails.getUsername(), "username");
        assertEquals(userDetails.getAuthorities()
                                .iterator()
                                .next()
                                .getAuthority(), "user");

        userRepository.delete(user);
    }
}
