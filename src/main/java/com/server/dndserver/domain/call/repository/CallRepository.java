package com.server.dndserver.domain.call.repository;

import com.server.dndserver.domain.call.domain.Call;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CallRepository extends JpaRepository<Call, Long> {

    @Query("SELECT c FROM Call c WHERE c.elderly.id = :elderlyId AND FUNCTION('YEAR', c.createdDate) = :year AND FUNCTION('MONTH', c.createdDate) = :month")
    List<Call> findByElderlyAndMonth(@Param("elderlyId") Long elderlyId, @Param("year") int year, @Param("month") int month);
}