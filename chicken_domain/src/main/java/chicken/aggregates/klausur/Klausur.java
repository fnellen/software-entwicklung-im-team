package chicken.aggregates.klausur;

import chicken.aggregates.utilities.Zeitraum;
import chicken.stereotypes.AggregateRoot;

@AggregateRoot
public record Klausur(VeranstaltungsId id,
                      String veranstaltungsName,
                      Zeitraum zeitraum, Boolean praesenz) {
}
