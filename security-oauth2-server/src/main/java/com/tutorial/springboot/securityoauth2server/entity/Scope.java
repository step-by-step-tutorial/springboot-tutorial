package com.tutorial.springboot.securityoauth2server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Scope extends AbstractEntity<Long, Scope> {

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @ManyToMany(mappedBy = "scopes", fetch = FetchType.EAGER)
    private List<Client> clients = new ArrayList<>();

    public String getName() {
        return name;
    }

    public Scope setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Scope setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
}
