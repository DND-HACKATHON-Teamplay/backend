package com.server.dndserver.domain.conversation.domain;

import com.server.dndserver.domain.call.domain.Call;
import com.server.dndserver.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Conversation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Call call;

    private boolean isElderly;

    public Conversation(String content, boolean elderly) {
        this.content = content;
        this.isElderly = elderly;
    }

    public void setCall(Call call) {
        this.call = call;
    }
}
