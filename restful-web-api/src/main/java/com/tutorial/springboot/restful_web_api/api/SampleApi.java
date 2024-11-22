package com.tutorial.springboot.restful_web_api.api;

import com.tutorial.springboot.restful_web_api.dto.Page;
import com.tutorial.springboot.restful_web_api.dto.SampleDto;
import com.tutorial.springboot.restful_web_api.service.SampleService;
import com.tutorial.springboot.restful_web_api.validation.SaveValidation;
import com.tutorial.springboot.restful_web_api.validation.UpdateValidation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tutorial.springboot.restful_web_api.util.CleanUpUtils.clean;
import static com.tutorial.springboot.restful_web_api.util.HttpUtils.uriOf;
import static com.tutorial.springboot.restful_web_api.validation.ApiValidation.shouldBeValid;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("api/v1/samples")
public class SampleApi {

    private final Logger logger = LoggerFactory.getLogger(SampleApi.class.getSimpleName());

    private final SampleService<Long, SampleDto> service;

    public SampleApi(SampleService<Long, SampleDto> service) {
        this.service = service;
    }

    @PostMapping
    @Validated(SaveValidation.class)
    public ResponseEntity<Long> save(@RequestBody @Valid SampleDto dto, BindingResult bindingResult) {
        logger.info("Received an inbound request to save a {}", SampleDto.class.getSimpleName());
        shouldBeValid(bindingResult);

        return service.save(dto)
                .map(id -> created(uriOf(id)).body(id))
                .orElseThrow();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<SampleDto> findById(@PathVariable Long id) {
        logger.info("Received an inbound request to retrieve a {} by its unique ID[{}]", SampleDto.class.getSimpleName(), id);
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> notFound().build());
    }

    @PutMapping(path = "/{id}")
    @Validated(UpdateValidation.class)
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid SampleDto dto, BindingResult bindingResult) {
        logger.info("Received an inbound request to update a {} by its unique ID[{}]", SampleDto.class.getSimpleName(), id);
        shouldBeValid(bindingResult);
        service.update(id, dto);

        return noContent().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        logger.info("Received an inbound request to delete a {} by its unique ID[{}]", SampleDto.class.getSimpleName(), id);
        service.deleteById(id);

        return noContent().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> exists(@PathVariable Long id) {
        logger.info("Received an inbound request to check existence of a {} by its unique ID[{}]", SampleDto.class.getSimpleName(), id);
        return service.exists(id) ? ok().build() : notFound().build();
    }

    @RequestMapping(value = "/options", method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getHttpMethods() {
        logger.info("Received an inbound request to show supported HTTP verbs");
        return ResponseEntity.ok()
                .allow(HttpMethod.POST, HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.HEAD, HttpMethod.OPTIONS)
                .build();
    }

    @PostMapping(value = "/batch")
    public ResponseEntity<List<Long>> saveBatch(@RequestBody SampleDto[] items) {
        logger.info("Received an inbound request to save a batch[{}] of {}", items.length, SampleDto.class.getSimpleName());
        var identifiers = service.saveBatch(clean(items).toArray(SampleDto[]::new));

        return ResponseEntity.status(CREATED).body(identifiers);
    }

    @GetMapping(value = "/batch/{page}/{size}")
    public ResponseEntity<Page<SampleDto>> findBatch(@PathVariable int page, @PathVariable int size) {
        logger.info("Received an inbound request to find a page[{},{}] of {}", page, size, SampleDto.class.getSimpleName());
        return service.findByPage(page, size)
                .map(ResponseEntity::ok)
                .orElseThrow();
    }

    @DeleteMapping(value = "/batch")
    public ResponseEntity<Void> deleteBatch(@RequestBody Long[] identifiers) {
        logger.info("Received an inbound request to delete a batch[{}] of samples", identifiers.length);
        service.deleteBatch(identifiers);

        return noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SampleDto>> findAll() {
        logger.info("Received an inbound request to retrieve all {}", SampleDto.class.getSimpleName());
        final var items = service.selectAll();

        return ok(items);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        logger.info("Received an inbound request to delete all {}", SampleDto.class.getSimpleName());
        service.deleteAll();

        return noContent().build();
    }

}
