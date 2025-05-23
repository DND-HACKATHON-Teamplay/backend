package com.server.dndserver.domain.call.domain;

import com.server.dndserver.domain.elderly.domain.Elderly;
import com.server.dndserver.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Call extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private HealthStatus healthStatus;

    private Long sleepTime;

    @Enumerated(EnumType.STRING)
    private MindStatus mindStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private Elderly elderly;
}
