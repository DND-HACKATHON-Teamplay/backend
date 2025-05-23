package com.server.dndserver.domain.conversation.repository;

import com.server.dndserver.domain.conversation.domain.Conversation;
import com.server.dndserver.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    @Query("""
        SELECT c FROM Conversation c
        JOIN c.call call
        WHERE call.elderly.id = :elderlyId
          AND call.createdDate BETWEEN :start AND :end
        ORDER BY c.createdDate ASC
    """)
    List<Conversation> findByElderlyIdAndCallDate(
            @Param("elderlyId") Long elderlyId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    List<Conversation> findByCallId(Long id);
}
