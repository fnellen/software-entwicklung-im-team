package de.hhu.propra.chicken.web.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.format.annotation.DateTimeFormat;

public record UrlaubDto(
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate urlaubsDatum,
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    LocalTime urlaubsStart,
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    LocalTime urlaubsEnde) {
}
