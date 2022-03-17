package de.hhu.propra.chicken.services;

import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_15_1045_1200;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hhu.propra.chicken.aggregates.student.Student;
import de.hhu.propra.chicken.repositories.KlausurRepository;
import de.hhu.propra.chicken.repositories.StudentRepository;
import de.hhu.propra.chicken.repositories.VeranstaltungsIdRepository;
import de.hhu.propra.chicken.services.fehler.UrlaubException;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class ChickeServiceStorniereUrlaubTest {

  @Mock
  StudentRepository studentRepository;

  @Mock
  KlausurRepository klausurRepository;
  Student dennis = new Student(1L, "dehus101");

  @Mock
  HeutigesDatum heutigesDatum;

  @Mock
  VeranstaltungsIdRepository veranstaltungsIdRepository;

  @Test
  @DisplayName("Der Urlaub kann im vorhinein bis zum Vortag storniert werden")
  void test_1() {

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_15_1045_1200);

    heutigesDatum = mock(HeutigesDatum.class);
    when(heutigesDatum.getDatum()).thenReturn(LocalDate.of(2022, 3, 7));
    klausurRepository = mock(KlausurRepository.class);
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatum,
            veranstaltungsIdRepository);

    applicationService.storniereUrlaub("dehus101", ZEITRAUM_03_15_1045_1200);

    assertThat(dennis.getUrlaube()).isEmpty();
  }

  @Test
  @DisplayName("Der Urlaub kann am Vortag storniert werden")
  void test_2() {

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_15_1045_1200);

    heutigesDatum = mock(HeutigesDatum.class);
    when(heutigesDatum.getDatum()).thenReturn(LocalDate.of(2022, 3, 14));
    klausurRepository = mock(KlausurRepository.class);
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatum,
            veranstaltungsIdRepository);

    applicationService.storniereUrlaub("dehus101", ZEITRAUM_03_15_1045_1200);

    assertThat(dennis.getUrlaube()).isEmpty();
  }

  @Test
  @DisplayName("Der Urlaub kann nicht am selben Tag storniert werden")
  void test_3() {

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_15_1045_1200);

    heutigesDatum = mock(HeutigesDatum.class);
    when(heutigesDatum.getDatum()).thenReturn(LocalDate.of(2022, 3, 15));
    klausurRepository = mock(KlausurRepository.class);
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatum,
            veranstaltungsIdRepository);
    assertThatExceptionOfType(UrlaubException.class).isThrownBy(() ->
        applicationService.storniereUrlaub("dehus101", ZEITRAUM_03_15_1045_1200)
    ).withMessageContaining("selben");

    assertThat(dennis.getUrlaube()).containsExactly(ZEITRAUM_03_15_1045_1200);
  }

  @Test
  @DisplayName("Der Urlaub kann nicht im nachhinein storniert werden")
  void test_4() {

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_15_1045_1200);

    heutigesDatum = mock(HeutigesDatum.class);
    when(heutigesDatum.getDatum()).thenReturn(LocalDate.of(2022, 3, 17));
    klausurRepository = mock(KlausurRepository.class);
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatum,
            veranstaltungsIdRepository);
    assertThatExceptionOfType(UrlaubException.class).isThrownBy(() ->
        applicationService.storniereUrlaub("dehus101", ZEITRAUM_03_15_1045_1200)
    ).withMessageContaining("nachhinein");

    assertThat(dennis.getUrlaube()).containsExactly(ZEITRAUM_03_15_1045_1200);
  }

}