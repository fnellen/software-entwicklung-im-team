package de.hhu.propra.chicken.web.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

public record KlausurDto(
    @NotNull
    String veranstaltungsId,
    @NotNull
    String veranstaltungsName,
    @NotNull
    Boolean praesenz,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate klausurdatum,
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    LocalTime klausurstart,
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    LocalTime klausurende
) {

}
