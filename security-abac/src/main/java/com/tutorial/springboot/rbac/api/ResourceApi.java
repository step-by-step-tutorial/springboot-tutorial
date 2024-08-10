package com.tutorial.springboot.rbac.api;

import com.tutorial.springboot.rbac.dto.ResourceDto;
import com.tutorial.springboot.rbac.entity.Resource;
import com.tutorial.springboot.rbac.service.ResourceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/resources")
public class ResourceApi extends AbstractApi<Long, Resource, ResourceDto> {

    public ResourceApi(ResourceService service) {
        super(service);
    }
}
