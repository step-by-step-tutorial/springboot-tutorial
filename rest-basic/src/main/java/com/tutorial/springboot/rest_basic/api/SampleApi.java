package com.tutorial.springboot.rest_basic.api;

import com.tutorial.springboot.rest_basic.dto.SampleDto;
import com.tutorial.springboot.rest_basic.dto.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static com.tutorial.springboot.rest_basic.dto.Storage.save;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/samples")
public class SampleApi {

    private final Logger logger = LoggerFactory.getLogger(SampleApi.class.getSimpleName());

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody SampleDto dto) {
        logger.info("Received an inbound request to save a sample");

        final var id = save(dto);
        final var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return created(uri).body(id);
    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<SampleDto> readById(@PathVariable Long id) {
        logger.info("Received an inbound request to retrieve a sample by its unique ID[{}]", id);
        final var dto = Storage.SAMPLE_COLLECTION.get(id);
        return ok().body(dto);
    }
}
