package chicken.aggregates.dto;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class ZeitraumDto {
  private final LocalDate datum;
  private final LocalTime startUhrzeit;
  private final LocalTime endUhrzeit;

  private ZeitraumDto(LocalDate datum, LocalTime startUhrzeit, LocalTime endUhrzeit) {
    this.datum = datum;
    this.startUhrzeit = startUhrzeit;
    this.endUhrzeit = endUhrzeit;
  }

  public LocalDate getDatum() {
    return datum;
  }

  public LocalTime getStartUhrzeit() {
    return startUhrzeit;
  }

  public LocalTime getEndUhrzeit() {
    return endUhrzeit;
  }

  public static ZeitraumDto erstelleZeitraum(LocalDate datum, LocalTime startUhrzeit,
                                             LocalTime endUhrzeit) {
    if (!validierePraktikumszeitraum(datum)) return null;
    if (validiereObAnWochenende(datum)) return null;
    if (!validiereUhrzeiten(startUhrzeit, endUhrzeit)) return null;
    if (!validiereUhrzeitBlock(startUhrzeit, endUhrzeit)) return null;
    return new ZeitraumDto(datum, startUhrzeit, endUhrzeit);
  }

  @Deprecated
  private static boolean validierePraktikumszeitraum(LocalDate datum) {
    LocalDate startDatum = LocalDate.of(2022, 03, 07);
    LocalDate endDatum = LocalDate.of(2022, 03, 25);
    if (datum.isBefore(startDatum) || datum.isAfter(endDatum)) {
      return false;
    }
    return true;
  }

  private static boolean validiereObAnWochenende(LocalDate datum) {
    return datum.getDayOfWeek() == DayOfWeek.SATURDAY || datum.getDayOfWeek() == DayOfWeek.SUNDAY;
  }

  private static boolean validiereUhrzeiten(LocalTime startUhrzeit, LocalTime endUhrzeit) {
    return startUhrzeit.isBefore(endUhrzeit);
  }

  private static boolean validiereUhrzeitBlock(LocalTime startUhrzeit, LocalTime endUhrzeit) {
    if (startUhrzeit.getMinute() % 15 != 0) return false;
    if (endUhrzeit.getMinute() % 15 != 0) return false;
    return true;
  }

  public long dauerInMinuten() {
    long zeitraum = Duration.between(startUhrzeit, endUhrzeit).toMinutes();
    return zeitraum;
  }

}
