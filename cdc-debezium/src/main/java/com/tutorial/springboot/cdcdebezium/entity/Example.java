package com.tutorial.springboot.cdcdebezium.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "EXAMPLE_TABLE")
public class Example {

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

    public static Example create() {
        return new Example();
    }

    public Long getId() {
        return id;
    }

    public Example setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Example setName(String name) {
        this.name = name;
        return this;
    }

    public int getCode() {
        return code;
    }

    public Example setCode(int code) {
        this.code = code;
        return this;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public Example setDatetime(LocalDateTime datetime) {
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
        Example example = (Example) o;
        return Objects.equals(id, example.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Example Entity: {" + "id:" + id + ", username:" + name + ", code:" + code + ", date:" + datetime + "}";
    }
}
