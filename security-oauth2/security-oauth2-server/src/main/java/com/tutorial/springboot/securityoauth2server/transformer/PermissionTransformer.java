package com.tutorial.springboot.securityoauth2server.transformer;

import com.tutorial.springboot.securityoauth2server.dto.PermissionDto;
import com.tutorial.springboot.securityoauth2server.entity.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionTransformer extends CodeTableTransformer<Long, Permission, PermissionDto> {
}
