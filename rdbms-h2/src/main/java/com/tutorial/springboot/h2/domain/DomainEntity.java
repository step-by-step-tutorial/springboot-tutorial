package com.tutorial.springboot.h2.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "DOMAIN_TABLE")
public class DomainEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private Long id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "AGE")
  private int age;

  @Column(name = "EMAIL")
  private String email;

  public static DomainEntity create() {
    return new DomainEntity();
  }

  public Long getId() {
    return id;
  }

  public DomainEntity setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public DomainEntity setName(String name) {
    this.name = name;
    return this;
  }

  public int getAge() {
    return age;
  }

  public DomainEntity setAge(int age) {
    this.age = age;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public DomainEntity setEmail(String email) {
    this.email = email;
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
    DomainEntity domainEntity = (DomainEntity) o;
    return Objects.equals(id, domainEntity.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Domain Entity: {"
        + "id:" + id
        + ", username:" + name
        + ", password:" + age
        + ", email:" + email
        + "}";
  }
}
