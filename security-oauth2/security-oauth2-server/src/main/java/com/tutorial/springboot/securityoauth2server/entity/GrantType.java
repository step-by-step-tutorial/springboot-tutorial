package com.tutorial.springboot.securityoauth2server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
