package com.tutorial.springboot.rest_basic.api;

import com.tutorial.springboot.rest_basic.dto.SampleDto;
import com.tutorial.springboot.rest_basic.dto.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static com.tutorial.springboot.rest_basic.dto.Storage.save;

@RestController
@RequestMapping("/v1/samples")
public class SampleApi {

    private final Logger logger = LoggerFactory.getLogger(SampleApi.class.getSimpleName());

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody SampleDto dto) {
        logger.info("Received an inbound request to save a sample");

        final var id = save(dto);
        final var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(uri).build();
    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<SampleDto> findById(@PathVariable long id) {
        logger.info("Received an inbound request to retrieve a sample by its unique ID[{}]", id);
        return ResponseEntity.ok(Storage.SAMPLE_COLLECTION.get(id));
    }
}
