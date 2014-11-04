package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class RoleTest {

    @Test
    public void casSetRolename() {

        final Role role = new Role();
        role.setRolename("user");

        assertEquals(role.getRolename(), "user");
    }

    @Test
    public void canSetUsers() {

        final Role role = new Role();

        final User user = new User();
        user.setUsername("username");

        final List<User> users = new ArrayList<>();
        users.add(user);

        role.setUsers(users);

        assertEquals(1, role.getUsers().size());
        assertEquals(role.getUsers().get(0).getUsername(), "username");
    }
}
