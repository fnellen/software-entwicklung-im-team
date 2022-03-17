package de.hhu.propra.chicken.repositories;

import de.hhu.propra.chicken.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.aggregates.klausur.Klausur;
import de.hhu.propra.chicken.aggregates.klausur.KlausurDto;
import de.hhu.propra.chicken.aggregates.student.StudentDto;
import de.hhu.propra.chicken.dao.KlausurDao;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class KlausurRepositoryImpl implements KlausurRepository {

  final KlausurDao klausurDao;

  public KlausurRepositoryImpl(KlausurDao klausurDao) {
    this.klausurDao = klausurDao;
  }

  @Override
  public Set<Klausur> findeKlausurenAmTag(ZeitraumDto zeitraumDto) {
    /*
    return klausurDao.findeKlausurenAmTag(zeitraumDto.getDatum()).stream()
        .map(KlausurDto::konvertiereZuKlausur).collect(
            Collectors.toSet());

     */
    return null;
  }

  @Override
  public Klausur findeKlausurMitId(String id) {
    KlausurDto klausurDto = klausurDao.findById(id)
        .orElseThrow(() -> new NoSuchElementException(id));
    return klausurDto.konvertiereZuKlausur();
  }


  @Override
  public void speicherKlausur(Klausur klausur) {
    KlausurDto klausurDto = KlausurDto.konvertiereZuKlausurDto(klausur);
    KlausurDto klausurDto1 = klausurDao.save(klausurDto);
  }
}
