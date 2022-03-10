package dto;

import chicken.aggregates.klausur.VeranstaltungsId;

public record VeranstaltungsIdDto(Long id) {

  public VeranstaltungsId konvertiereZuVeranstaltungsId() {
    return VeranstaltungsId.erstelle(this.id);
  }
}
