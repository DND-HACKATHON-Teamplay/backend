package com.server.dndserver.domain.elderly.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.dndserver.domain.elderly.domain.Gender;
import com.server.dndserver.domain.elderly.domain.Relationship;

import java.time.LocalDate;
import java.time.LocalTime;

public record ElderlyRegisterRequest(String name, LocalDate birthDate,
                                     Gender gender,
                                     String phoneNumber,
                                     Relationship relationship,
                                     @JsonFormat(pattern = "HH:mm:ss")
                                     LocalTime timeToCall
) {

}