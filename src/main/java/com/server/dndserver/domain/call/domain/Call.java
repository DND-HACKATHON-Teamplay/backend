package com.server.dndserver.domain.call.domain;

import com.server.dndserver.domain.conversation.domain.Conversation;
import com.server.dndserver.domain.elderly.domain.Elderly;
import com.server.dndserver.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @OneToMany(mappedBy = "call",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private final List<Conversation> conversations = new ArrayList<>();

    public void updateStatus(HealthStatus healthStatus, Long sleepTime, MindStatus mindStatus) {
        this.healthStatus = healthStatus;
        this.sleepTime = sleepTime;
        this.mindStatus = mindStatus;
    }

    public void addConversation(Conversation conv) {
        conversations.add(conv);
        conv.setCall(this);
    }
}
