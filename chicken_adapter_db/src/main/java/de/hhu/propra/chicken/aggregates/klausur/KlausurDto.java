package de.hhu.propra.chicken.aggregates.klausur;

import de.hhu.propra.chicken.aggregates.dto.ZeitraumDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

//@Table("klausur")
public record KlausurDto(@Id
                         @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
                         VeranstaltungsId veranstaltungsId,
                         String veranstaltungsName,
                         @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
                         ZeitraumDto klausurZeitraum,
                         boolean praesenz) {

  public static KlausurDto konvertiereZuKlausurDto(Klausur klausur) {
    return new KlausurDto(klausur.id(), klausur.veranstaltungsName(),
        klausur.zeitraumDto(), klausur.praesenz());
  }

  public Klausur konvertiereZuKlausur() {
    return new Klausur(this.veranstaltungsId, this.veranstaltungsName, this.klausurZeitraum(),
        this.praesenz);
  }
}
