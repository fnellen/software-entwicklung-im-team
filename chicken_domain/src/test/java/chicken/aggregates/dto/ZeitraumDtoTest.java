package chicken.aggregates.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ZeitraumDtoTest {

  @Test
  @DisplayName("Ein Zeitraum soll valide sein, wenn das angegebene Datum innerhalb des "
      + "Praktikumszeitraums liegt.")
  void test_1() {
    ZeitraumDto
        zeitraumDto = ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 9),
        LocalTime.of(9, 15),
        LocalTime.of(10, 30));
    assertThat(zeitraumDto).isNotNull();
  }

  @Test
  @DisplayName("Ein Zeitraum soll valide sein, wenn das angegebene Datum dem Startdatum des "
      + "Praktikums entspricht.")
  void test_1a() {
    ZeitraumDto
        zeitraumDto = ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 7),
        LocalTime.of(9, 15),
        LocalTime.of(10, 30));
    assertThat(zeitraumDto).isNotNull();
  }

  @Test
  @DisplayName("Ein Zeitraum soll valide sein, wenn das angegebene Datum dem Enddatum des "
      + "Praktikums entspricht.")
  void test_1b() {
    ZeitraumDto
        zeitraumDto = ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 25),
        LocalTime.of(9, 15),
        LocalTime.of(10, 30));
    assertThat(zeitraumDto).isNotNull();
  }

  @Test
  @DisplayName("Ein Zeitraum soll nicht valide sein, wenn das angegeben Datum vor dem "
      + "Praktikumszeitraum liegt.")
  void test_2a() {
    ZeitraumDto
        zeitraumDto = ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 1),
        LocalTime.of(9, 15),
        LocalTime.of(10, 30));
    assertThat(zeitraumDto).isNull();
  }

  @Test
  @DisplayName("Ein Zeitraum soll nicht valide sein, wenn das angegeben Datum nach dem "
      + "Praktikumszeitraum liegt.")
  void test_2b() {
    ZeitraumDto
        zeitraumDto = ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 30),
        LocalTime.of(9, 15),
        LocalTime.of(10, 30));
    assertThat(zeitraumDto).isNull();
  }

  @Test
  @DisplayName("Ein Zeitraum soll nicht valide sein, wenn die angegebenen Zeiten nicht in 15er "
      + "Blöcken angegeben sind.")
  void test_3() {
    ZeitraumDto
        zeitraumDto = ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 9),
        LocalTime.of(9, 12),
        LocalTime.of(10, 30));
    assertThat(zeitraumDto).isNull();
  }

  @Test
  @DisplayName("Ein Zeitraum soll nicht valide sein, wenn die Startzeit nach der Endzeit liegt.")
  void test_4() {
    ZeitraumDto
        zeitraumDto = ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 9),
        LocalTime.of(14, 30),
        LocalTime.of(10, 30));
    assertThat(zeitraumDto).isNull();
  }

  @Test
  @DisplayName("Ein Zeitraum soll nicht valide sein, wenn das angegebene Datum am Wochenende "
      + "liegt.")
  void test_5() {
    ZeitraumDto
        zeitraumDto = ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 12),
        LocalTime.of(9, 30),
        LocalTime.of(10, 30));
    assertThat(zeitraumDto).isNull();
  }

  @Test
  @DisplayName("Ein Zeitraum soll nicht valide sein, wenn das angegebene Datum am Wochenende "
      + "liegt.")
  void test_6() {
    ZeitraumDto
        zeitraumDto = ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 13),
        LocalTime.of(9, 30),
        LocalTime.of(10, 30));
    assertThat(zeitraumDto).isNull();
  }

  @Test
  @DisplayName("Die Dauer eines Zeitraums wird korrekt berechnet.")
  void test_7() {
    ZeitraumDto
        zeitraumDto = ZeitraumDto.erstelleZeitraum(LocalDate.of(2022, 3, 7),
        LocalTime.of(9, 30),
        LocalTime.of(10, 30));
    long dauer = zeitraumDto.dauerInMinuten();
    assertThat(dauer).isEqualTo(60);
  }

}