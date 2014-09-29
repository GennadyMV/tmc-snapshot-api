package fi.helsinki.cs.tmc.snapshot.api.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity(name = "ROLES")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private String rolename;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public Long getId() {

        return id;
    }

    public void setId(final Long id) {

        this.id = id;
    }

    public List<User> getUsers() {

        return users;
    }

    public void setUsers(final List<User> users) {

        this.users = users;
    }

    public String getRolename() {

        return rolename;
    }

    public void setRolename(final String rolename) {

        this.rolename = rolename;
    }
}
