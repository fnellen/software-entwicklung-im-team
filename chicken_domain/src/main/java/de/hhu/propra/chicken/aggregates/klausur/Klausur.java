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
}
