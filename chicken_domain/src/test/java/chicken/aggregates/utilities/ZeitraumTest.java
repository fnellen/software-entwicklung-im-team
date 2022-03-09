package chicken.aggregates.utilities;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import chicken.aggregates.student.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ZeitraumTest {

  @Test
  @DisplayName("Ein Zeitraum soll valide sein, wenn das angegebene Datum innerhalb des Praktikumszeitraums liegt.")
  void test_1() {
    Zeitraum zeitraum = Zeitraum.erstelleZeitraum(LocalDate.of(2022, 3, 9), LocalTime.of(9, 15),
        LocalTime.of(10, 30));
    assertThat(zeitraum).isNotNull();
  }

  @Test
  @DisplayName("Ein Zeitraum soll valide sein, wenn das angegebene Datum dem Startdatum des Praktikums entspricht.")
  void test_1a() {
    Zeitraum zeitraum = Zeitraum.erstelleZeitraum(LocalDate.of(2022, 3, 7), LocalTime.of(9, 15),
        LocalTime.of(10, 30));
    assertThat(zeitraum).isNotNull();
  }

  @Test
  @DisplayName("Ein Zeitraum soll valide sein, wenn das angegebene Datum dem Enddatum des Praktikums entspricht.")
  void test_1b() {
    Zeitraum zeitraum = Zeitraum.erstelleZeitraum(LocalDate.of(2022, 3, 25), LocalTime.of(9, 15),
        LocalTime.of(10, 30));
    assertThat(zeitraum).isNotNull();
  }

  @Test
  @DisplayName("Ein Zeitraum soll nicht valide sein, wenn das angegeben Datum vor dem Praktikumszeitraum liegt.")
  void test_2a() {
    Zeitraum zeitraum = Zeitraum.erstelleZeitraum(LocalDate.of(2022, 3, 1), LocalTime.of(9, 15),
        LocalTime.of(10, 30));
    assertThat(zeitraum).isNull();
  }

  @Test
  @DisplayName("Ein Zeitraum soll nicht valide sein, wenn das angegeben Datum nach dem Praktikumszeitraum liegt.")
  void test_2b() {
    Zeitraum zeitraum = Zeitraum.erstelleZeitraum(LocalDate.of(2022, 3, 30), LocalTime.of(9, 15),
        LocalTime.of(10, 30));
    assertThat(zeitraum).isNull();
  }

  @Test
  @DisplayName("Ein Zeitraum soll nicht valide sein, wenn die angegebenen Zeiten nicht in 15er Blöcken angegeben sind.")
  void test_3() {
    Zeitraum zeitraum = Zeitraum.erstelleZeitraum(LocalDate.of(2022, 3, 9), LocalTime.of(9, 12),
        LocalTime.of(10, 30));
    assertThat(zeitraum).isNull();
  }

  @Test
  @DisplayName("Ein Zeitraum soll nicht valide sein, wenn die Startzeit nach der Endzeit liegt.")
  void test_4() {
    Zeitraum zeitraum = Zeitraum.erstelleZeitraum(LocalDate.of(2022, 3, 9), LocalTime.of(14, 30),
        LocalTime.of(10, 30));
    assertThat(zeitraum).isNull();
  }

  @Test
  @DisplayName("Ein Zeitraum soll nicht valide sein, wenn das angegebene Datum am Wochenende liegt.")
  void test_5() {
    Zeitraum zeitraum = Zeitraum.erstelleZeitraum(LocalDate.of(2022, 3, 12), LocalTime.of(9, 30),
        LocalTime.of(10, 30));
    assertThat(zeitraum).isNull();
  }

  @Test
  @DisplayName("Ein Zeitraum soll nicht valide sein, wenn das angegebene Datum am Wochenende liegt.")
  void test_6() {
    Zeitraum zeitraum = Zeitraum.erstelleZeitraum(LocalDate.of(2022, 3, 13), LocalTime.of(9, 30),
        LocalTime.of(10, 30));
    assertThat(zeitraum).isNull();
  }

  @Test
  @DisplayName("Die Dauer eines Zeitraums wird korrekt berechnet.")
  void test_7() {
    Zeitraum zeitraum = Zeitraum.erstelleZeitraum(LocalDate.of(2022, 3, 7), LocalTime.of(9, 30),
        LocalTime.of(10, 30));
    long dauer = zeitraum.dauerInMinuten();
    assertThat(dauer).isEqualTo(60);
  }

}
