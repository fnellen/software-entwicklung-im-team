package repositories;

import chicken.aggregates.klausur.Klausur;
import chicken.aggregates.klausur.VeranstaltungsId;

public interface KlausurRepository {

  Klausur findeKlausurMitId(VeranstaltungsId id);

  Klausur findeKlausurMitName(String veranstaltungsName);

  void speicherKlausur(Klausur klausur);

}
