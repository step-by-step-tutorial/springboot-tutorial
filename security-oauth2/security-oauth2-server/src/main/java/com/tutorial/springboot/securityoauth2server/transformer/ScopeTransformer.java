package com.tutorial.springboot.securityoauth2server.transformer;

import com.tutorial.springboot.securityoauth2server.dto.ScopeDto;
import com.tutorial.springboot.securityoauth2server.entity.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class ScopeTransformer extends CodeTableTransformer<Long, Scope, ScopeDto> {
}
