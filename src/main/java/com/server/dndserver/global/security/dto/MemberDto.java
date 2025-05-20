package com.server.dndserver.global.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {

    private String role;
    private String name;
    private String username;
    private String email;
}
