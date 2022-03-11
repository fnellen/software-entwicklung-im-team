package de.hhu.propra.chicken.aggregates.student;

import static org.assertj.core.api.Assertions.assertThat;

import de.hhu.propra.chicken.aggregates.dto.ZeitraumDto;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StudentTest {

  @Test
  @DisplayName("Der Belegte Urlaub wird korrekt berechnet.")
  void test_1() {
    Student student = new Student(1L, "");
    UrlaubZeitraum zeitraum1 = new UrlaubZeitraum(
        ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 7), LocalTime.of(9, 30),
            LocalTime.of(10, 30)));
    UrlaubZeitraum zeitraum2 = new UrlaubZeitraum(
        ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 10), LocalTime.of(10, 30),
            LocalTime.of(11, 00)));
    student.fuegeUrlaubHinzufuegen(zeitraum1);
    student.fuegeUrlaubHinzufuegen(zeitraum2);
    long restUrlaub = student.berechneBeantragtenUrlaub();

    assertThat(restUrlaub).isEqualTo(90);
  }

  @Test
  @DisplayName("Der restliche Urlaub wird korrekt berechnet.")
  void test_2() {
    Student student = new Student(1L, "");
    UrlaubZeitraum zeitraum1 = new UrlaubZeitraum(
        ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 7), LocalTime.of(9, 30),
            LocalTime.of(10, 30)));
    UrlaubZeitraum zeitraum2 = new UrlaubZeitraum(
        ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 10), LocalTime.of(10, 30),
            LocalTime.of(11, 00)));
    student.fuegeUrlaubHinzufuegen(zeitraum1);
    student.fuegeUrlaubHinzufuegen(zeitraum2);
    long restUrlaub = student.berechneRestUrlaub();

    assertThat(restUrlaub).isEqualTo(150);
  }
}
