package de.hhu.propra.chicken.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import de.hhu.propra.chicken.aggregates.student.Student;
import de.hhu.propra.chicken.dao.StudentDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

@ContextConfiguration(classes = {StudentRepositoryImpl.class, StudentDao.class})
@EnableAutoConfiguration
//@JdbcTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {
//    Repository.class}))
@DataJdbcTest
@ActiveProfiles("test")
public class DatenbankTest {

  @Autowired
  StudentDao studentDao;

  @Test
  @Sql({"classpath:db/migration/V1__tabelle_erstellen.sql",
      "classpath:db/migration/V2__testDaten.sql"})
  @DisplayName("Student wird mit richtigem githubHandle aus der Datenbank geladen")
  void test_1() {
    StudentRepositoryImpl studentRepository = new StudentRepositoryImpl(studentDao);
    Student student = studentRepository.findeStudentMitHandle("ernaz100");
    assertThat(student).isNotNull();
    assertThat(student.getGithubHandle()).isEqualTo("ernaz100");

  }
}
