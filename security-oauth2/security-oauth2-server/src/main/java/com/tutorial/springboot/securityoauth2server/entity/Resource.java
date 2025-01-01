package com.tutorial.springboot.securityoauth2server.entity;

import com.tutorial.springboot.securityoauth2server.util.CollectionUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Audited
public class Resource extends CodeTable<Long, Resource> {

    @NotAudited
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "resource_scopes",
            joinColumns = @JoinColumn(name = "resource_id"),
            inverseJoinColumns = @JoinColumn(name = "scope_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"resource_id", "scope_id"})
    )
    private List<Scope> scopes = new ArrayList<>();

    @Override
    public Resource updateJoinTableRelations(Resource newOne) {
        var scopesCompared = CollectionUtils.compareCollections(this.scopes, newOne.scopes);
        this.scopes.removeAll(scopesCompared.deletionItems());
        this.scopes.addAll(scopesCompared.newItems());
        return this;
    }

    public List<Scope> getScopes() {
        return scopes;
    }

    public Resource setScopes(List<Scope> scopes) {
        this.scopes = scopes;
        return this;
    }
}
