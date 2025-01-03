package com.tutorial.springboot.security_rbac_jwt.api;

import com.tutorial.springboot.security_rbac_jwt.dto.AbstractDto;
import com.tutorial.springboot.security_rbac_jwt.entity.AbstractEntity;
import com.tutorial.springboot.security_rbac_jwt.service.AbstractService;
import com.tutorial.springboot.security_rbac_jwt.util.CleanUpUtils;
import com.tutorial.springboot.security_rbac_jwt.validation.SaveValidation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

public abstract class BatchApi<ID, ENTITY extends AbstractEntity<ID, ENTITY>, DTO extends AbstractDto<ID, DTO>>
        extends CrudApi<ID, ENTITY, DTO> {

    protected BatchApi(AbstractService<ID, ENTITY, DTO> service) {
        super(service);
    }

    @PostMapping(value = "/batch")
    public ResponseEntity<List<ID>> saveBatch(@RequestBody List<DTO> dtoList) {
        logger.info("Received an inbound request to save a batch[{}] of {}", dtoList.size(), dtoClass.getSimpleName());
        CleanUpUtils.validate(dtoList.stream(), SaveValidation.class);
        var identities = service.saveBatch(dtoList);
        return ResponseEntity.status(CREATED).body(identities);
    }

    @DeleteMapping(value = "/batch")
    public ResponseEntity<Void> deleteBatch(@RequestBody List<ID> identities) {
        logger.info("Received an inbound request to delete a batch[{}] of {}", identities.size(), dtoClass.getSimpleName());
        service.deleteBatch(identities);
        return noContent().build();
    }

    @GetMapping(value = "/batch/{page}/{size}")
    public ResponseEntity<Page<DTO>> findByPage(@PathVariable int page, @PathVariable int size) {
        logger.info("Received an inbound request to find a page[{},{}] of {}", page, size, dtoClass.getSimpleName());
        var pageOfDto = service.findByPage(PageRequest.of(page, size));
        return ok(pageOfDto);
    }
}

