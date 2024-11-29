package com.tutorial.springboot.securityoauth2client.api;

import com.tutorial.springboot.securityoauth2client.dto.AbstractDto;
import com.tutorial.springboot.securityoauth2client.entity.AbstractEntity;
import com.tutorial.springboot.securityoauth2client.service.AbstractService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

public abstract class AllApi<ID, ENTITY extends AbstractEntity<ID, ENTITY>, DTO extends AbstractDto<ID, DTO>>
        extends BatchApi<ID, ENTITY, DTO> {

    protected AllApi(AbstractService<ID, ENTITY, DTO> service) {
        super(service);
    }

    @GetMapping
    public ResponseEntity<List<DTO>> findAll() {
        logger.info("Received an inbound request to retrieve all {}", dtoClass.getSimpleName());
        final var dtoList = service.getAll();
        return ok(dtoList);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        logger.info("Received an inbound request to delete all {}", dtoClass.getSimpleName());
        service.deleteAll();
        return noContent().build();
    }

}

