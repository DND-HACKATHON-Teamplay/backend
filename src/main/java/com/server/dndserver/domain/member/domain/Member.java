package com.server.dndserver.domain.member.domain;

import com.server.dndserver.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Builder
    public Member(String name, MemberRole role, String email) {
        this.name = name;
        this.role = role;
        this.email = email;
    }

    public static Member createDefaultMember(String name, MemberRole role, String email) {
        return Member.builder()
                .name(name)
                .role(role)
                .email(email)
                .build();
    }
}