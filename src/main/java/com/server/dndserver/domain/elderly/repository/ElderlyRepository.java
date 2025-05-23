package com.server.dndserver.domain.elderly.repository;

import com.server.dndserver.domain.elderly.domain.Elderly;
import com.server.dndserver.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ElderlyRepository extends JpaRepository<Elderly, Long> {
    Optional<Elderly> findByMemberId(Long memberId);

    Optional<Elderly> findByPhoneNumber(String phoneNumber);

    void deleteAllByMember(Member member);
}
