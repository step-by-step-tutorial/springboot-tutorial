package com.tutorial.springboot.newsqlcockroachdb.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "EXAMPLE_TABLE")
public class ExampleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EXAMPLE_GEN")
    @SequenceGenerator(name = "EXAMPLE_GEN", sequenceName = "EXAMPLE_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CODE")
    private int code;

    @Column(name = "DATETIME")
    private LocalDateTime datetime;

    public static ExampleEntity create() {
        return new ExampleEntity();
    }

    public Long getId() {
        return id;
    }

    public ExampleEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ExampleEntity setName(String name) {
        this.name = name;
        return this;
    }

    public int getCode() {
        return code;
    }

    public ExampleEntity setCode(int code) {
        this.code = code;
        return this;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public ExampleEntity setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExampleEntity exampleEntity = (ExampleEntity) o;
        return Objects.equals(id, exampleEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Sample Entity: {"
               + "id:" + id
               + ", username:" + name
               + ", code:" + code
               + ", date:" + datetime
               + "}";
    }
}
