package de.hhu.propra.chicken.repositories;

import de.hhu.propra.chicken.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.aggregates.klausur.Klausur;
import java.util.Set;

public interface KlausurRepository {

  Set<Klausur> findeKlausurenAmTag(ZeitraumDto zeitraumDto);

  Klausur findeKlausurMitId(String id);

  Klausur findeKlausurMitName(String veranstaltungsName);

  void speicherKlausur(Klausur klausur);

}
