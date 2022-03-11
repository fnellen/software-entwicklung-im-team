package de.hhu.propra.chicken.repositories;

import de.hhu.propra.chicken.aggregates.klausur.Klausur;

public interface KlausurRepository {

  Klausur findeKlausurMitId(Long id);

  Klausur findeKlausurMitName(String veranstaltungsName);

  void speicherKlausur(Klausur klausur);

}
