package com.tutorial.springboot.h2.repository;

import com.tutorial.springboot.h2.domain.DomainEntity;
import org.springframework.data.repository.CrudRepository;

public interface DomainRepository extends CrudRepository<DomainEntity, Long> {
}
