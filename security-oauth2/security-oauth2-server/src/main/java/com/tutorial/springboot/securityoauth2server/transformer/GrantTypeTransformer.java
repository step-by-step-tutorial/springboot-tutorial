package com.tutorial.springboot.securityoauth2server.transformer;

import com.tutorial.springboot.securityoauth2server.dto.GrantTypeDto;
import com.tutorial.springboot.securityoauth2server.entity.GrantType;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class GrantTypeTransformer extends CodeTableTransformer<Long, GrantType, GrantTypeDto> {
}
