package com.tutorial.springboot.rbac.api;

import com.tutorial.springboot.rbac.dto.AbstractDto;
import com.tutorial.springboot.rbac.entity.AbstractEntity;
import com.tutorial.springboot.rbac.service.AbstractService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.tutorial.springboot.rbac.util.CleanUpUtils.clean;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.noContent;

public abstract class BatchApi<ID, ENTITY extends AbstractEntity<ID, ENTITY>, DTO extends AbstractDto<ID, DTO>>
        extends CrudApi<ID, ENTITY, DTO> {

    protected BatchApi(AbstractService<ID, ENTITY, DTO> service) {
        super(service);

    }

    @PostMapping(value = "/batch")
    public ResponseEntity<List<ID>> saveBatch(@RequestBody List<DTO> dtos) {
        logger.info("Received an inbound request to save a batch[{}] of permission", dtos.stream());
        var identities = service.saveBatch(clean(dtos.stream()).toList());

        return ResponseEntity.status(CREATED).body(identities);
    }

    @DeleteMapping(value = "/batch")
    public ResponseEntity<Void> deleteBatch(@RequestBody List<ID> identities) {
        logger.info("Received an inbound request to delete a batch[{}] of permission", identities.size());
        service.deleteBatch(identities);

        return noContent().build();
    }
}

