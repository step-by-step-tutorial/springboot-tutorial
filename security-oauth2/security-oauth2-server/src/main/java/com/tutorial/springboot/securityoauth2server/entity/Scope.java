package com.tutorial.springboot.securityoauth2server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
public class Scope extends CodeTable<Long, Scope> {

    @ManyToMany(mappedBy = "scopes", fetch = LAZY)
    private List<Client> clients = new ArrayList<>();

    @ManyToMany(mappedBy = "scopes", fetch = LAZY)
    private List<Resource> resources = new ArrayList<>();

    public List<Client> getClients() {
        return clients;
    }

    public Scope setClients(List<Client> clients) {
        this.clients = clients;
        return this;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public Scope setResources(List<Resource> resources) {
        this.resources = resources;
        return this;
    }
}
