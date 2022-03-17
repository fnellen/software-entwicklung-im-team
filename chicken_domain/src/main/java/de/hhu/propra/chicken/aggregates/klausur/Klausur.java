package de.hhu.propra.chicken.aggregates.klausur;

import de.hhu.propra.chicken.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.stereotypes.AggregateRoot;
import de.hhu.propra.chicken.stereotypes.EntityObject;

/**
 * Darstellung einer Klausur.
 */
@AggregateRoot
@EntityObject
public record Klausur(Long id, VeranstaltungsId veranstaltungsId,
                      String veranstaltungsName,
                      ZeitraumDto zeitraumDto, Boolean praesenz) {


  public Klausur(Long id, String veranstaltungsId, String veranstaltungsName,
                 ZeitraumDto zeitraum, Boolean praesenz) {
    this(id, VeranstaltungsId.erstelle(veranstaltungsId), veranstaltungsName, zeitraum, praesenz);

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

    return veranstaltungsId.equals(klausur.veranstaltungsId);
  }

  public String getVeranstaltungsId() {
    return veranstaltungsId.getVeranstaltungsId();
  }

  @Override
  public int hashCode() {
    return veranstaltungsId.hashCode();
  }
}
