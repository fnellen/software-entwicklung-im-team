package dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record KlausurZeitraumDto(LocalDate datum, LocalTime startUhrzeit, LocalTime endUhrzeit) {
}
