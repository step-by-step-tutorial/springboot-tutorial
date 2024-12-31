package com.tutorial.springboot.securityoauth2server.entity;

import com.tutorial.springboot.securityoauth2server.util.CollectionUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Audited
public class User extends AbstractEntity<Long, User> implements UserDetails {

    @NotBlank(message = "Username is mandatory")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email is not valid")
    @Column(unique = true, nullable = false)
    private String email;

    private boolean enabled = true;

    @NotAudited
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"})
    )
    private List<Role> roles = new ArrayList<>();

    @Override
    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public User setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public User setRoles(List<Role> roles) {
        this.roles = roles;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public User updateFrom(User newOne) {
        super.updateFrom(newOne);
        this.username = newOne.username;
        this.email = newOne.email;
        this.enabled = newOne.enabled;

        return this;
    }

    @Override
    public User updateRelations(User newOne) {
        var compared = CollectionUtils.compareCollections(this.roles, newOne.roles);
        compared.commonItem().forEach(role -> role.updateRelations(newOne.roles.get(newOne.roles.indexOf(role))));
        compared.newItems().forEach(role -> role.updateRelations(role));

        this.roles.removeAll(compared.deletionItems());
        this.roles.addAll(compared.newItems());

        return this;
    }

    @Transient
    public List<String> getPermissions() {
        return getRoles()
                .stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .distinct()
                .toList();
    }

    @Override
    public String toString() {
        return username;
    }
}
