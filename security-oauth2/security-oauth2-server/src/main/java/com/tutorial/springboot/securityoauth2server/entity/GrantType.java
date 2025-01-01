package com.tutorial.springboot.securityoauth2server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
public class GrantType extends CodeTable<Long, GrantType> {

    @ManyToMany(mappedBy = "grantTypes", fetch = LAZY)
    private List<Client> clients = new ArrayList<>();

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
}
