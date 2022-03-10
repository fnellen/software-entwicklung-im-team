package chicken.aggregates.klausur;

import chicken.aggregates.dto.ZeitraumDto;
import java.time.LocalDate;
import java.time.LocalTime;

public class KlausurZeitraum {
  private final LocalDate datum;
  private final LocalTime startUhrzeit;
  private final LocalTime endUhrzeit;

  public long dauerInMinuten() {
    return konvertiereInZeitraum().dauerInMinuten();
  }

  private ZeitraumDto konvertiereInZeitraum() {
    return ZeitraumDto.erstelleZeitraum(datum, startUhrzeit, endUhrzeit);
  }

  public KlausurZeitraum(ZeitraumDto zeitraumDto) {
    this.datum = zeitraumDto.getDatum();
    this.startUhrzeit = zeitraumDto.getStartUhrzeit();
    this.endUhrzeit = zeitraumDto.getEndUhrzeit();
  }
}
