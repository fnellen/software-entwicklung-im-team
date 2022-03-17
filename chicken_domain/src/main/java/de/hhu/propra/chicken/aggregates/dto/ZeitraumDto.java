package de.hhu.propra.chicken.aggregates.dto;

import de.hhu.propra.chicken.stereotypes.ValueObject;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Daten Transfer Objekt für die Darstellung eines Zeitraums. Enthält einfache Validierung
 * des Zeitraums.
 */
@ValueObject
public class ZeitraumDto {
  private final LocalDate datum;
  private final LocalTime startUhrzeit;
  private final LocalTime endUhrzeit;

  private ZeitraumDto(LocalDate datum, LocalTime startUhrzeit, LocalTime endUhrzeit) {
    this.datum = datum;
    this.startUhrzeit = startUhrzeit;
    this.endUhrzeit = endUhrzeit;
  }

  /**
   * Statische factory Methode zur Erstellung eines Zeitraums mit Validierung der Daten.
   *
   * @param datum        Tag des Zeitraums.
   * @param startUhrzeit Start Uhrzeit des Zeitraums.
   * @param endUhrzeit   Ende Uhrzeit des Zeitraums.
   * @return gibt ein Zeitraums Objekt zurück, wenn die Validierung erfolgreich war. Ansonsten null.
   */
  public static ZeitraumDto erstelleZeitraum(LocalDate datum, LocalTime startUhrzeit,
                                             LocalTime endUhrzeit) {
    if (!validierePraktikumszeitraum(datum)) {
      return null;
    }
    if (validiereObAnWochenende(datum)) {
      return null;
    }
    if (!validiereUhrzeiten(startUhrzeit, endUhrzeit)) {
      return null;
    }
    if (!validiereUhrzeitBlock(startUhrzeit, endUhrzeit)) {
      return null;
    }
    if (!inPraktikumsZeit(startUhrzeit, endUhrzeit)) {
      return null;
    }
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

  private static boolean inPraktikumsZeit(LocalTime startUhrzeit, LocalTime endUhrzeit) {
    if (startUhrzeit.isBefore(LocalTime.of(9, 30)) || endUhrzeit.isAfter(LocalTime.of(13, 30))) {
      return false;
    } else {
      return true;
    }
  }

  private static boolean validiereObAnWochenende(LocalDate datum) {
    return datum.getDayOfWeek() == DayOfWeek.SATURDAY || datum.getDayOfWeek() == DayOfWeek.SUNDAY;
  }

  private static boolean validiereUhrzeiten(LocalTime startUhrzeit, LocalTime endUhrzeit) {
    return startUhrzeit.isBefore(endUhrzeit);
  }

  private static boolean validiereUhrzeitBlock(LocalTime startUhrzeit, LocalTime endUhrzeit) {
    if (startUhrzeit.getMinute() % 15 != 0) {
      return false;
    }
    if (endUhrzeit.getMinute() % 15 != 0) {
      return false;
    }
    return true;
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

  public long dauerInMinuten() {
    long zeitraum = Duration.between(startUhrzeit, endUhrzeit).toMinutes();
    return zeitraum;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ZeitraumDto that = (ZeitraumDto) o;

    if (!datum.equals(that.datum)) {
      return false;
    }
    if (!startUhrzeit.equals(that.startUhrzeit)) {
      return false;
    }
    return endUhrzeit.equals(that.endUhrzeit);
  }

  @Override
  public int hashCode() {
    int result = datum.hashCode();
    result = 31 * result + startUhrzeit.hashCode();
    result = 31 * result + endUhrzeit.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Datum: " + this.getDatum()
        + " Startzeit: " + this.getStartUhrzeit()
        + " Enduhrzeit: " + this.getEndUhrzeit();
  }
}
