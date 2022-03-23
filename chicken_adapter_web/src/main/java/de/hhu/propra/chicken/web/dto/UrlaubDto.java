package de.hhu.propra.chicken.web.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

public record UrlaubDto(
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    LocalDate urlaubsDatum,
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull
    LocalTime urlaubsStart,
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @NotNull
    LocalTime urlaubsEnde) {
}
