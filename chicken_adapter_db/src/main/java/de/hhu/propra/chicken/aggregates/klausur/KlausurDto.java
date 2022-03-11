package de.hhu.propra.chicken.aggregates.klausur;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

public record KlausurDto(@Id @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL) VeranstaltungsId id,
                         String veranstaltungsName,
                         @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
                         KlausurZeitraumDto klausurZeitraum,
                         boolean praesenz) {
}
