package dto;

import chicken.aggregates.student.KlausurReferenz;
import org.springframework.data.relational.core.mapping.Embedded;

public record KlausurReferenzDto(
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL) VeranstaltungsIdDto id) {

  public KlausurReferenz konvertiereZuKlausurReferenz() {
    return new KlausurReferenz(id.konvertiereZuVeranstaltungsId());
  }
}
