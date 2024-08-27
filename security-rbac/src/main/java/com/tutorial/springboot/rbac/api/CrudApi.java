package com.tutorial.springboot.rbac.api;

import com.tutorial.springboot.rbac.dto.AbstractDto;
import com.tutorial.springboot.rbac.entity.AbstractEntity;
import com.tutorial.springboot.rbac.service.AbstractService;
import com.tutorial.springboot.rbac.validation.SaveValidation;
import com.tutorial.springboot.rbac.validation.UpdateValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.tutorial.springboot.rbac.util.ApiErrorUtils.checkValidation;
import static com.tutorial.springboot.rbac.util.HttpUtils.createUriFromId;
import static com.tutorial.springboot.rbac.util.ReflectionUtils.identifyType;
import static org.springframework.http.ResponseEntity.*;

public abstract class CrudApi<ID, ENTITY extends AbstractEntity<ID, ENTITY>, DTO extends AbstractDto<ID, DTO>> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    protected final AbstractService<ID, ENTITY, DTO> service;

    protected Class<ENTITY> entityClass;

    protected Class<DTO> dtoClass;

    protected CrudApi(AbstractService<ID, ENTITY, DTO> service) {
        this.service = service;
        this.entityClass = identifyType(1, getClass());
        this.dtoClass = identifyType(2, getClass());
    }

    @PostMapping
    public ResponseEntity<ID> save(@RequestBody @Validated(value = SaveValidation.class) DTO dto, BindingResult bindingResult) {
        logger.info("Received an inbound request to save a {}", dtoClass.getSimpleName());
        checkValidation(bindingResult);
        return service.save(dto)
                .map(id -> created(createUriFromId(id)).body(id))
                .orElseThrow();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DTO> findById(@PathVariable ID id) {
        logger.info("Received an inbound request to retrieve a {} by its unique ID[{}]", dtoClass.getSimpleName(), id);
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable ID id,
                                       @RequestBody @Validated(value = UpdateValidation.class) DTO dto,
                                       BindingResult bindingResult) {
        logger.info("Received an inbound request to update a {} by its unique ID[{}]", dtoClass.getSimpleName(), id);
        checkValidation(bindingResult);
        service.update(id, dto);
        return noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable ID id) {
        logger.info("Received an inbound request to delete a {} by its unique ID[{}]", dtoClass.getSimpleName(), id);
        service.deleteById(id);
        return noContent().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> exists(@PathVariable ID id) {
        logger.info("Received an inbound request to check existence of a {} by its unique ID[{}]", dtoClass.getSimpleName(), id);
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

