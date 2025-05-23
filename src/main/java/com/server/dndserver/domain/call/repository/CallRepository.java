package com.server.dndserver.domain.call.repository;

import com.server.dndserver.domain.call.domain.Call;
import com.server.dndserver.domain.elderly.domain.Elderly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface CallRepository extends JpaRepository<Call, Long> {

    @Query("SELECT c FROM Call c WHERE c.elderly.id = :elderlyId AND FUNCTION('YEAR', c.createdDate) = :year AND FUNCTION('MONTH', c.createdDate) = :month")
    List<Call> findByElderlyAndMonth(@Param("elderlyId") Long elderlyId, @Param("year") int year, @Param("month") int month);

    // 해당 노인 + 날짜 범위 안에서 가장 최신 Call 1건
    Optional<Call> findFirstByElderlyIdAndCreatedDateBetweenOrderByCreatedDateDesc(
            Long elderlyId,
            LocalDateTime start,
            LocalDateTime end);

    void deleteAllByElderly(Elderly elderly);
}