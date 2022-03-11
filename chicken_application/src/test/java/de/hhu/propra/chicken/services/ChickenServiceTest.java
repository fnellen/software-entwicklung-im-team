package de.hhu.propra.chicken.services;

import static org.assertj.core.api.Assertions.assertThat;

import de.hhu.propra.chicken.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.aggregates.student.Student;
import de.hhu.propra.chicken.repositories.KlausurRepository;
import de.hhu.propra.chicken.repositories.StudentRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class ChickenServiceTest {

  @Mock
  StudentRepository studentRepository;

  @Mock
  KlausurRepository klausurRepository;

  @Test
  @DisplayName("Gib die richtige Anzahl der belegten Urlaube an einem Tag zurück")
  void test_1() {
    Student student = new Student(1L, "");

    student.fuegeUrlaubHinzu(
        ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 03, 8), LocalTime.of(9, 30),
            LocalTime.of(10, 30)));
    student.fuegeUrlaubHinzu(
        ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 03, 8), LocalTime.of(11, 30),
            LocalTime.of(12, 30)));
    student.fuegeUrlaubHinzu(
        ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 03, 9), LocalTime.of(11, 30),
            LocalTime.of(12, 30)));

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository);

    Set<ZeitraumDto> urlaubeAmTag = applicationService.getUrlaubeAmTag(
        ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 03, 8), LocalTime.of(9, 30),
            LocalTime.of(10, 30)), student);

    assertThat(urlaubeAmTag).hasSize(2);


  }


}
