package com.server.dndserver.domain.call.repository;

import com.server.dndserver.domain.call.domain.Call;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallRepository extends JpaRepository<Call, Long> {
}
