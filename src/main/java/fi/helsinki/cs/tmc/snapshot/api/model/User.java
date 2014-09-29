package fi.helsinki.cs.tmc.snapshot.api.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity(name = "USERS")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Role> roles;

    public Long getId() {

        return id;
    }

    public void setId(final Long id) {

        this.id = id;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(final String password) {

        this.password = password;
    }

    public List<Role> getRoles() {

        return roles;
    }

    public void setRoles(final List<Role> roles) {

        this.roles = roles;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(final String username) {

        this.username = username;
    }
}
