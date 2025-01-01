package com.tutorial.springboot.securityoauth2server.transformer;

import com.tutorial.springboot.securityoauth2server.dto.PermissionDto;
import com.tutorial.springboot.securityoauth2server.entity.GrantType;
import com.tutorial.springboot.securityoauth2server.entity.Permission;
import com.tutorial.springboot.securityoauth2server.transformer.AbstractTransformer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class PermissionTransformer extends CodeTableTransformer<Long, Permission, PermissionDto> {
}
