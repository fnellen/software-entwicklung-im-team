package de.hhu.propra.chicken.aggregates.klausur;

import de.hhu.propra.chicken.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.dao.KlausurDao;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

//@Table("klausur")
public record KlausurDto(@Id
                         @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL) VeranstaltungsId id,
                         String veranstaltungsName,
                         @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
                         ZeitraumDto klausurZeitraum,
                         boolean praesenz) {

  public Klausur konvertiereZuKlausur() {
    return new Klausur(this.id, this.veranstaltungsName, this.klausurZeitraum(), this.praesenz);
  }

  public static KlausurDto konvertiereZuKlausurDto(Klausur klausur) {
    return new KlausurDto(klausur.id(), klausur.veranstaltungsName(),
        klausur.zeitraumDto(), klausur.praesenz());
  }
}
