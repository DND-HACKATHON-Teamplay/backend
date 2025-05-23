package com.server.dndserver.domain.elderly.dto;

import com.server.dndserver.domain.elderly.domain.Gender;
import com.server.dndserver.domain.elderly.domain.Relationship;

import java.time.LocalDate;

public record ElderlyRegisterRequest(String name, LocalDate birthDate,
                                     Gender gender,
                                     String phoneNumber,
                                     Relationship relationship) {

}