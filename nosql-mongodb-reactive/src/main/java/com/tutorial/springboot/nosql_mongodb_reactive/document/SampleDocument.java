package com.tutorial.springboot.nosql_mongodb_reactive.document;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Objects;

@Document(collection = "samples")
public class SampleDocument {

    @Id
    private String id;

    private String name;

    private int code;

    private LocalDateTime datetime;

    public static SampleDocument create() {
        return new SampleDocument();
    }

    public String getId() {
        return id;
    }

    public SampleDocument setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public SampleDocument setName(String name) {
        this.name = name;
        return this;
    }

    public int getCode() {
        return code;
    }

    public SampleDocument setCode(int code) {
        this.code = code;
        return this;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public SampleDocument setDatetime(LocalDateTime datetime) {
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
        SampleDocument sampleDocument = (SampleDocument) o;
        return Objects.equals(id, sampleDocument.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Sample Model: {" + "id:" + id + ", username:" + name + ", code:" + code + ", date:" + datetime + "}";
    }
}
