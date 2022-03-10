package dto;

import chicken.aggregates.dto.ZeitraumDto;
import java.time.LocalDate;
import java.time.LocalTime;

public record UrlaubZeitraumDto(LocalDate datum, LocalTime startUhrzeit, LocalTime endUhrzeit) {

  public ZeitraumDto konvertiereZuZeitraumDto() {
    return ZeitraumDto.erstelleZeitraum(datum, startUhrzeit, endUhrzeit);
  }

}
