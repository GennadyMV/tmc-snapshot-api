package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.List;

import javax.persistence.*;

import org.hibernate.validator.constraints.NotBlank;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "roles")
public class Role extends AbstractPersistable<Long> {

    @NotBlank
    private String rolename;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
    private List<User> users;

    public void setRolename(final String rolename) {

        this.rolename = rolename;
    }

    public String getRolename() {

        return rolename;
    }

    public void setUsers(final List<User> users) {

        this.users = users;
    }

    public List<User> getUsers() {

        return users;
    }
}
