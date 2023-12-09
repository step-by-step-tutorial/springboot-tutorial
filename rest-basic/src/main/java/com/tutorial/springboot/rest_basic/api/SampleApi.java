package com.tutorial.springboot.rest_basic.api;

import com.tutorial.springboot.rest_basic.dto.SampleDto;
import com.tutorial.springboot.rest_basic.dto.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

import static com.tutorial.springboot.rest_basic.dto.Storage.save;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/v1/samples")
public class SampleApi {

    private final Logger logger = LoggerFactory.getLogger(SampleApi.class.getSimpleName());

    @PostMapping
    public ResponseEntity<Long> save(@RequestBody SampleDto dto) {
        logger.info("Received an inbound request to save a sample");
        final var id = Storage.save(dto);
        final var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return created(uri).body(id);
    }

    @GetMapping
    public ResponseEntity<List<SampleDto>> findAll() {
        logger.info("Received an inbound request to retrieve all samples");
        final var values = Storage.SAMPLE_COLLECTION.values()
                .stream()
                .toList();

        return ok().body(values);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<SampleDto> findById(@PathVariable Long id) {
        logger.info("Received an inbound request to retrieve a sample by its unique ID[{}]", id);
        final var value = Storage.SAMPLE_COLLECTION.get(id);

        return ok().body(value);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody SampleDto dto) {
        logger.info("Received an inbound request to update a sample by its unique ID[{}]", id);
        Storage.update(id, dto);

        return noContent().build();
    }
}
