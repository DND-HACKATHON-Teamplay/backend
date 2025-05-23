package com.server.dndserver.domain.conversation.repository;

import com.server.dndserver.domain.conversation.domain.Conversation;
import com.server.dndserver.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    @Query("""
            SELECT conv FROM Conversation conv
            JOIN conv.call call
            JOIN call.elderly elderly
            WHERE elderly.member.id = :memberId
              AND DATE(call.createdDate) = :date
            """)
    List<Conversation> findByMemberIdAndCallDate(
            @Param("memberId") Long memberId,
            @Param("date") LocalDate date);
}
