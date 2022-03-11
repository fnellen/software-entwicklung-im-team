package de.hhu.propra.chicken.aggregates.student;

import de.hhu.propra.chicken.aggregates.dto.ZeitraumDto;
import java.time.LocalDate;
import java.time.LocalTime;

class UrlaubZeitraum {
  private final LocalDate datum;
  private final LocalTime startUhrzeit;
  private final LocalTime endUhrzeit;

  public long dauerInMinuten() {
    return konvertiereInZeitraum().dauerInMinuten();
  }

  private ZeitraumDto konvertiereInZeitraum() {
    return ZeitraumDto.erstelleZeitraum(datum, startUhrzeit, endUhrzeit);
  }

  LocalDate getDatum() {
    return datum;
  }

  LocalTime getStartUhrzeit() {
    return startUhrzeit;
  }

  LocalTime getEndUhrzeit() {
    return endUhrzeit;
  }

  public UrlaubZeitraum(ZeitraumDto zeitraumDto) {
    this.datum = zeitraumDto.getDatum();
    this.startUhrzeit = zeitraumDto.getStartUhrzeit();
    this.endUhrzeit = zeitraumDto.getEndUhrzeit();
  }
}
