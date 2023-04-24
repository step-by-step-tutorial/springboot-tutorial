package com.tutorial.springboot.h2.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "SAMPLE_TABLE")
public class SampleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CODE")
    private int code;

    @Column(name = "DATETIME")
    private LocalDateTime datetime;

    public static SampleEntity create() {
        return new SampleEntity();
    }

    public Long getId() {
        return id;
    }

    public SampleEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public SampleEntity setName(String name) {
        this.name = name;
        return this;
    }

    public int getCode() {
        return code;
    }

    public SampleEntity setCode(int code) {
        this.code = code;
        return this;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public SampleEntity setDatetime(LocalDateTime datetime) {
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
        SampleEntity sampleEntity = (SampleEntity) o;
        return Objects.equals(id, sampleEntity.id);
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
