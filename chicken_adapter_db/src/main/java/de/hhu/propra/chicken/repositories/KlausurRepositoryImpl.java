package de.hhu.propra.chicken.repositories;

import de.hhu.propra.chicken.aggregates.klausur.Klausur;
import org.springframework.stereotype.Repository;

@Repository
public class KlausurRepositoryImpl implements KlausurRepository {
  @Override
  public Klausur findeKlausurMitId(Long id) {
    return null;
  }

  @Override
  public Klausur findeKlausurMitName(String veranstaltungsName) {
    return null;
  }

  @Override
  public void speicherKlausur(Klausur klausur) {

  }
}
