package com.server.dndserver.domain.elderly.repository;

import com.server.dndserver.domain.elderly.domain.Elderly;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElderlyRepository extends JpaRepository<Elderly, Long> {
}
