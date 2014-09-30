package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class User extends AbstractPersistable<Long> {

    @Column(unique = true)
    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 14)
    private String password;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Role> roles;

    public void setPassword(final String password) {

        this.password = password;
    }

    public String getPassword() {

        return password;
    }

    public void setRoles(final List<Role> roles) {

        this.roles = roles;
    }

    public List<Role> getRoles() {

        return roles;
    }

    public void setUsername(final String username) {

        this.username = username;
    }

    public String getUsername() {

        return username;
    }
}
