package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class UserTest {

    @Test
    public void casSetUsername() {

        final User user = new User();
        user.setUsername("username");

        assertEquals(user.getUsername(), "username");
    }

    @Test
    public void canSetPassword() {

        final User user = new User();
        user.setPassword("password");

        assertEquals(user.getPassword(), "password");
    }

    @Test
    public void canSetRoles() {

        final User user = new User();

        final Role role = new Role();
        role.setRolename("user");

        final List<Role> roles = new ArrayList<>();
        roles.add(role);

        user.setRoles(roles);

        assertEquals(1, user.getRoles().size());
        assertEquals(user.getRoles().get(0).getRolename(), "user");
    }
}
