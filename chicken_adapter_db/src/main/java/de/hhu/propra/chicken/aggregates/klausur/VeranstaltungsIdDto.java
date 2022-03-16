package de.hhu.propra.chicken.aggregates.klausur;

import de.hhu.propra.chicken.aggregates.klausur.VeranstaltungsId;
import org.springframework.data.relational.core.mapping.Table;

public record VeranstaltungsIdDto(String id) {

  public VeranstaltungsId konvertiereZuVeranstaltungsId() {
    return VeranstaltungsId.erstelle(this.id);
  }

}
