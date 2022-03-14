package de.hhu.propra.chicken.aggregates.klausur;

import de.hhu.propra.chicken.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.stereotypes.AggregateRoot;

/**
 * Darstellung einer Klausur.
 */
@AggregateRoot
public record Klausur(VeranstaltungsId id,
                      String veranstaltungsName,
                      ZeitraumDto zeitraumDto, Boolean praesenz) {

  public Klausur(String id, String veranstaltungsName, ZeitraumDto zeitraum, Boolean praesenz) {
    this(VeranstaltungsId.erstelle(id), veranstaltungsName, zeitraum, praesenz);

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Klausur klausur = (Klausur) o;

    return id.equals(klausur.id);
  }

  public String getVeranstaltungsId() {
    return id.getVeranstaltungsId();
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
