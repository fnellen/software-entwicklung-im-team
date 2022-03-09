package chicken.aggregates.klausur;

import chicken.aggregates.dto.ZeitraumDto;
import chicken.stereotypes.AggregateRoot;

/**
 * Darstellung einer Klausur.
 */
@AggregateRoot
public record Klausur(VeranstaltungsId id,
                      String veranstaltungsName,
                      ZeitraumDto zeitraumDto, Boolean praesenz) {
}
