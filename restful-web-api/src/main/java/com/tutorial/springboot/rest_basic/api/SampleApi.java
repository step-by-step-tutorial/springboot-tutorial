package com.tutorial.springboot.rest_basic.api;

import com.tutorial.springboot.rest_basic.dao.SampleRepository;
import com.tutorial.springboot.rest_basic.dto.SampleDto;
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

import static com.tutorial.springboot.rest_basic.util.ErrorUtils.checkValidation;
import static com.tutorial.springboot.rest_basic.util.HttpUtils.createUriFromId;
import static com.tutorial.springboot.rest_basic.util.StringUtils.stringToLongArray;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("api/v1/samples")
public class SampleApi {

    private final Logger logger = LoggerFactory.getLogger(SampleApi.class.getSimpleName());

    private final SampleRepository sampleRepository;

    public SampleApi(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    @PostMapping
    @Validated(SaveValidation.class)
    public ResponseEntity<Long> save(@RequestBody @Valid SampleDto dto, BindingResult bindingResult) {
        logger.info("Received an inbound request to save a sample");
        checkValidation(bindingResult);

        return sampleRepository.insert(dto)
                .map(id -> created(createUriFromId(id)).body(id))
                .orElseThrow();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<SampleDto> findById(@PathVariable Long id) {
        logger.info("Received an inbound request to retrieve a sample by its unique ID[{}]", id);
        return sampleRepository.selectById(id)
                .map(sample -> ok().body(sample))
                .orElseThrow();
    }

    @PutMapping(path = "/{id}")
    @Validated(UpdateValidation.class)
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid SampleDto dto, BindingResult bindingResult) {
        logger.info("Received an inbound request to update a sample by its unique ID[{}]", id);
        checkValidation(bindingResult);
        sampleRepository.update(id, dto);

        return noContent().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        logger.info("Received an inbound request to delete a sample by its unique ID[{}]", id);
        sampleRepository.deleteById(id);

        return noContent().build();
    }

    @PostMapping(value = "/saveAll")
    public ResponseEntity<List<Long>> saveAll(@RequestBody SampleDto[] samples) {
        logger.info("Received an inbound request to save all samples");
        var identities = sampleRepository.insertAll(samples);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(identities);
    }

    @GetMapping(value = "/findAll/{identities}")
    public ResponseEntity<List<SampleDto>> findAll(@PathVariable(value = "identities") String identities) {
        logger.info("Received an inbound request to retrieve all samples");
        final var samples = sampleRepository.selectAll(stringToLongArray(identities));
        return ok().body(samples);
    }

    @GetMapping(value = "/findAll")
    public ResponseEntity<List<SampleDto>> findAll() {
        logger.info("Received an inbound request to retrieve all samples");
        final var samples = sampleRepository.selectAll();
        return ok().body(samples);
    }

    @DeleteMapping(value = "/deleteAll/{identities}")
    public ResponseEntity<Void> deleteAll(@PathVariable(value = "identities") String identities) {
        logger.info("Received an inbound request to delete all samples");
        sampleRepository.deleteAll(stringToLongArray(identities));
        return noContent().build();
    }

    @DeleteMapping(value = "/truncate")
    public ResponseEntity<Void> truncate() {
        logger.info("Received an inbound request to delete all samples");
        sampleRepository.truncate();
        return noContent().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> exists(@PathVariable Long id) {
        logger.info("Received an inbound request to check existence of a sample by its unique ID[{}]", id);
        return sampleRepository.exists(id) ? ok().build() : notFound().build();
    }

    @RequestMapping(value = "/verbs", method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getHttpVerbs() {
        logger.info("Received an inbound request to show supported HTTP verbs");
        return ResponseEntity.ok()
                .allow(HttpMethod.POST, HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.HEAD, HttpMethod.OPTIONS)
                .build();
    }
}
