package de.hhu.propra.chicken.repositories;

import de.hhu.propra.chicken.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.aggregates.klausur.Klausur;
import de.hhu.propra.chicken.aggregates.klausur.KlausurDto;
import de.hhu.propra.chicken.aggregates.student.StudentDto;
import de.hhu.propra.chicken.dao.KlausurDao;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Repository;

@Repository
public class KlausurRepositoryImpl implements KlausurRepository {

  final KlausurDao klausurDao;

  public KlausurRepositoryImpl(KlausurDao klausurDao) {
    this.klausurDao = klausurDao;
  }



  @Override
  public Klausur findeKlausurMitVeranstaltungsId(String id) {
    KlausurDto klausurDto = klausurDao.findeKlausurMitVeranstaltungsId(id)
        .orElseThrow(() -> new NoSuchElementException(id));
    return klausurDto.konvertiereZuKlausur();
  }


  @Override
  public void speicherKlausur(Klausur klausur) {
    try {
      findeKlausurMitVeranstaltungsId(klausur.getVeranstaltungsId());
      throw new RuntimeException("fehler");
    } catch (NoSuchElementException e) {
      KlausurDto klausurDto = KlausurDto.konvertiereZuKlausurDto(klausur);
      klausurDao.save(klausurDto);
    }
  }

  @Override
  public Set<Klausur> findAll() {
    return klausurDao.findAll()
        .stream()
        .map(KlausurDto::konvertiereZuKlausur)
        .collect(Collectors.toSet());
  }


}
