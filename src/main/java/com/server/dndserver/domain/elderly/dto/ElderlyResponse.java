package com.server.dndserver.domain.elderly.dto;

import com.server.dndserver.domain.elderly.domain.Gender;
import com.server.dndserver.domain.elderly.domain.Relationship;

import java.time.LocalDate;
import java.time.LocalTime;

public record ElderlyResponse(Long id, String name, LocalDate birthDate, String phoneNumber, Gender gender,
                              Long memberId, Relationship relationshipWithGuardian, LocalTime timeToCall) {
}
