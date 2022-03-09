package chicken.aggregates.student;

import static org.assertj.core.api.Assertions.assertThat;

import chicken.aggregates.utilities.Zeitraum;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StudentTest {

  @Test
  @DisplayName("Der Resturlaub wird korrekt berechnet.")
  void test_1() {
    Student student = new Student(1L, "");
    Zeitraum zeitraum1 = Zeitraum.erstelleZeitraum(LocalDate.of(2022, 3, 7), LocalTime.of(9, 30),
        LocalTime.of(10, 30));
    Zeitraum zeitraum2 = Zeitraum.erstelleZeitraum(LocalDate.of(2022, 3, 10), LocalTime.of(10, 30),
        LocalTime.of(11, 00));
    student.fuegeUrlaubHinzufuegen(zeitraum1);
    student.fuegeUrlaubHinzufuegen(zeitraum2);
    long restUrlaub = student.berechneRestUrlaub();

    assertThat(restUrlaub).isEqualTo(150);
  }
}
