package de.hhu.propra.chicken.domain.aggregates.klausur;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

//@Table("klausur")
public record KlausurDto(@Id Long id,
                         @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
                         VeranstaltungsId veranstaltungsId,
                         String veranstaltungsName,
                         @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
                         KlausurZeitraumDto klausurZeitraum,
                         boolean praesenz) {

  public static KlausurDto konvertiereZuKlausurDto(Klausur klausur) {
    return new KlausurDto(klausur.id(), klausur.veranstaltungsId(), klausur.veranstaltungsName(),
        new KlausurZeitraumDto(klausur.zeitraumDto().getDatum(),
            klausur.zeitraumDto().getStartUhrzeit(), klausur.zeitraumDto().getEndUhrzeit()),
        klausur.praesenz());
  }
}
