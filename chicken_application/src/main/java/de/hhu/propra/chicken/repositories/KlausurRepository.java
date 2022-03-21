package de.hhu.propra.chicken.repositories;

import de.hhu.propra.chicken.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.aggregates.klausur.Klausur;
import java.util.Set;

public interface KlausurRepository {


  Klausur findeKlausurMitVeranstaltungsId(String id);

  void speicherKlausur(Klausur klausur);

  Set<Klausur> findAll();

}
