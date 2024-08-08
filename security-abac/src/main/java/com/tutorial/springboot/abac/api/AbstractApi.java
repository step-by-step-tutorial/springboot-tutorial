package com.tutorial.springboot.abac.api;

import com.tutorial.springboot.abac.dto.AbstractDto;
import com.tutorial.springboot.abac.entity.AbstractEntity;
import com.tutorial.springboot.abac.service.AbstractService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

import static com.tutorial.springboot.abac.util.ApiErrorUtils.checkValidation;
import static com.tutorial.springboot.abac.util.CleanUpUtils.clean;
import static com.tutorial.springboot.abac.util.HttpUtils.createUriFromId;
import static com.tutorial.springboot.abac.util.ReflectionUtils.arrayFrom;
import static com.tutorial.springboot.abac.util.ReflectionUtils.identifyType;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.*;

public abstract class AbstractApi<ID, ENTITY extends AbstractEntity<ID, ENTITY>, DTO extends AbstractDto<ID, DTO>> {

    protected final Logger logger = LoggerFactory.getLogger(AbstractApi.class.getSimpleName());

    protected final AbstractService<ID, ENTITY, DTO> service;

    protected Class<ENTITY> entityClass;

    protected Class<DTO> dtoClass;

    protected AbstractApi(AbstractService<ID, ENTITY, DTO> service) {
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

    @PostMapping(value = "/batch")
    public ResponseEntity<List<ID>> saveBatch(@RequestBody DTO[] dtos) {
        logger.info("Received an inbound request to save a batch[{}] of permission", dtos.length);
        var identities = service.saveBatch(arrayFrom(clean(Stream.of(dtos)), dtoClass));

        return ResponseEntity.status(CREATED).body(identities);
    }

    @GetMapping
    public ResponseEntity<List<DTO>> findAll() {
        logger.info("Received an inbound request to retrieve all permissions");
        final var permissions = service.getAll();
        return ok(permissions);
    }

    @DeleteMapping(value = "/batch")
    public ResponseEntity<Void> deleteBatch(@RequestBody ID[] identities) {
        logger.info("Received an inbound request to delete a batch[{}] of permission", identities.length);
        service.deleteBatch(identities);

        return noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        logger.info("Received an inbound request to delete all permissions");
        service.deleteAll();
        return noContent().build();
    }

    @RequestMapping(value = "/options", method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getHttpMethods() {
        logger.info("Received an inbound request to show supported HTTP verbs");
        return ResponseEntity.ok()
                .allow(HttpMethod.POST, HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.HEAD, HttpMethod.OPTIONS)
                .build();
    }
}

