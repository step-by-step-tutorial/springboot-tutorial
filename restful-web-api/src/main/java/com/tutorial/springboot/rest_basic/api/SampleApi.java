package com.tutorial.springboot.rest_basic.api;

import com.tutorial.springboot.rest_basic.dto.SampleDto;
import com.tutorial.springboot.rest_basic.service.SampleService;
import com.tutorial.springboot.rest_basic.validation.SaveValidation;
import com.tutorial.springboot.rest_basic.validation.UpdateValidation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

import static com.tutorial.springboot.rest_basic.util.ApiErrorUtils.checkValidation;
import static com.tutorial.springboot.rest_basic.util.CleanUpUtils.clean;
import static com.tutorial.springboot.rest_basic.util.HttpUtils.createUriFromId;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("api/v1/samples")
public class SampleApi {

    private final Logger logger = LoggerFactory.getLogger(SampleApi.class.getSimpleName());

    private final SampleService sampleService;

    public SampleApi(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @PostMapping
    @Validated(SaveValidation.class)
    public ResponseEntity<Long> save(@RequestBody @Valid SampleDto dto, BindingResult bindingResult) {
        logger.info("Received an inbound request to save a sample");
        checkValidation(bindingResult);

        return sampleService.insert(dto)
                .map(id -> created(createUriFromId(id)).body(id))
                .orElseThrow();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<SampleDto> findById(@PathVariable Long id) {
        logger.info("Received an inbound request to retrieve a sample by its unique ID[{}]", id);
        return sampleService.selectById(id)
                .map(sample -> ok().body(sample))
                .orElseGet(() -> ok(SampleDto.builder().build()));
    }

    @PutMapping(path = "/{id}")
    @Validated(UpdateValidation.class)
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid SampleDto dto, BindingResult bindingResult) {
        logger.info("Received an inbound request to update a sample by its unique ID[{}]", id);
        checkValidation(bindingResult);
        sampleService.update(id, dto);

        return noContent().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        logger.info("Received an inbound request to delete a sample by its unique ID[{}]", id);
        sampleService.deleteById(id);

        return noContent().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> exists(@PathVariable Long id) {
        logger.info("Received an inbound request to check existence of a sample by its unique ID[{}]", id);
        return sampleService.exists(id) ? ok().build() : notFound().build();
    }

    @PostMapping(value = "/batch")
    public ResponseEntity<List<Long>> saveBatch(@RequestBody SampleDto[] samples) {
        logger.info("Received an inbound request to save a batch[{}] of samples", samples.length);
        var identities = sampleService.insertBatch(clean(Stream.of(samples)).toArray(SampleDto[]::new));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(identities);
    }

    @DeleteMapping(value = "/batch")
    public ResponseEntity<Void> deleteBatch(@RequestBody Long[] identities) {
        logger.info("Received an inbound request to delete a batch[{}] of samples", identities.length);
        sampleService.deleteBatch(identities);

        return noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SampleDto>> findAll() {
        logger.info("Received an inbound request to retrieve all samples");
        final var samples = sampleService.selectAll();

        return ok().body(samples);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        logger.info("Received an inbound request to delete all samples");
        sampleService.deleteAll();

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
