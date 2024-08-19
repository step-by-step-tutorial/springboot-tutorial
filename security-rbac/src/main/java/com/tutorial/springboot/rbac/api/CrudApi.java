package com.tutorial.springboot.rbac.api;

import com.tutorial.springboot.rbac.dto.AbstractDto;
import com.tutorial.springboot.rbac.entity.AbstractEntity;
import com.tutorial.springboot.rbac.service.AbstractService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tutorial.springboot.rbac.util.ApiErrorUtils.checkValidation;
import static com.tutorial.springboot.rbac.util.CleanUpUtils.clean;
import static com.tutorial.springboot.rbac.util.HttpUtils.createUriFromId;
import static com.tutorial.springboot.rbac.util.ReflectionUtils.identifyType;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.*;

public abstract class CrudApi<ID, ENTITY extends AbstractEntity<ID, ENTITY>, DTO extends AbstractDto<ID, DTO>> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    protected final AbstractService<ID, ENTITY, DTO> service;

    protected Class<ENTITY> entityClass;

    protected Class<DTO> dtoClass;

    protected CrudApi(AbstractService<ID, ENTITY, DTO> service) {
        this.service = service;
        entityClass = identifyType(1, getClass());
        dtoClass = identifyType(2, getClass());
    }

    @PostMapping
    public ResponseEntity<ID> save(@RequestBody @Valid DTO dto, BindingResult bindingResult) {
        logger.info("Received an inbound request to save a permission");
        checkValidation(bindingResult);
        return service.save(dto)
                .map(id -> created(createUriFromId(id)).body(id))
                .orElseThrow();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DTO> findById(@PathVariable ID id) {
        logger.info("Received an inbound request to retrieve a permission by its unique ID[{}]", id);
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable ID id, @RequestBody @Valid DTO dto, BindingResult bindingResult) {
        logger.info("Received an inbound request to update a permission by its unique ID[{}]", id);
        checkValidation(bindingResult);
        service.update(id, dto);
        return noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable ID id) {
        logger.info("Received an inbound request to delete a permission by its unique ID[{}]", id);
        service.delete(id);
        return noContent().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> exists(@PathVariable ID id) {
        logger.info("Received an inbound request to check existence of a sample by its unique ID[{}]", id);
        return service.exists(id) ? ok().build() : notFound().build();
    }

    @RequestMapping(value = "/options", method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getHttpMethods() {
        logger.info("Received an inbound request to show supported HTTP verbs");
        return ResponseEntity.ok()
                .allow(HttpMethod.POST, HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.HEAD, HttpMethod.OPTIONS)
                .build();
    }
}

