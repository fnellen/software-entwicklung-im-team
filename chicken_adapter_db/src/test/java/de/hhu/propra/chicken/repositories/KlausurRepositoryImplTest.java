package de.hhu.propra.chicken.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import de.hhu.propra.chicken.aggregates.klausur.Klausur;
import de.hhu.propra.chicken.aggregates.student.Student;
import de.hhu.propra.chicken.dao.KlausurDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@Sql({"classpath:db/migration/V1__tabelle_erstellen.sql",
    "classpath:db/migration/V2__testDaten.sql"})
@DataJdbcTest
@ActiveProfiles("test")
public class KlausurRepositoryImplTest {


  @Autowired
  KlausurDao klausurDao;

  @Test
  @DisplayName("Klausur wird mit richtiger VeranstaltungsId aus der Datenbank geladen")
  void test_1() {
    KlausurRepositoryImpl klausurRepository = new KlausurRepositoryImpl(klausurDao);
    Klausur klausur = klausurRepository.findeKlausurMitId("215783");
    assertThat(klausur).isNotNull();
    assertThat(klausur.getVeranstaltungsId()).isEqualTo("215783");

  }
}
