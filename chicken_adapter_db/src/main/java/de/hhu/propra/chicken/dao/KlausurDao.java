package de.hhu.propra.chicken.dao;

import de.hhu.propra.chicken.aggregates.klausur.KlausurDto;
import java.time.LocalDate;
import java.util.Set;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface KlausurDao extends CrudRepository<KlausurDto, String> {

  @Query(
      """
          SELECT * FROM klausur
          WHERE date = :datum;
          """
  )
  Set<KlausurDto> findeKlausurenAmTag(@Param("datum") LocalDate datum);

}
