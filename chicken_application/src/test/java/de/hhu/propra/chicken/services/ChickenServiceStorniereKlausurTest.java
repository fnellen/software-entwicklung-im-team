package de.hhu.propra.chicken.services;

import static de.hhu.propra.chicken.services.KlausurTemplate.KL_PROPRA_03_09_1130_1230;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_0930_0945;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_1300_1330;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hhu.propra.chicken.aggregates.student.KlausurReferenz;
import de.hhu.propra.chicken.aggregates.student.Student;
import de.hhu.propra.chicken.repositories.KlausurRepository;
import de.hhu.propra.chicken.repositories.StudentRepository;
import de.hhu.propra.chicken.repositories.VeranstaltungsIdRepository;
import de.hhu.propra.chicken.services.fehler.KlausurException;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class ChickenServiceStorniereKlausurTest {

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
  @DisplayName("Eine Klausur kann im vorhinein bis zum Vortag storniert werden")
  void test_1() {

    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230);

    heutigesDatum = mock(HeutigesDatum.class);
    when(heutigesDatum.getDatum()).thenReturn(LocalDate.of(2022, 3, 7));
    klausurRepository = mock(KlausurRepository.class);
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatum,
            veranstaltungsIdRepository);

    applicationService.storniereKlausur("dehus101", KL_PROPRA_03_09_1130_1230);

    assertThat(dennis.getKlausuren()).isEmpty();
  }

  @Test
  @DisplayName("Eine Klausur kann am Vortag storniert werden")
  void test_2() {

    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230);

    heutigesDatum = mock(HeutigesDatum.class);
    when(heutigesDatum.getDatum()).thenReturn(LocalDate.of(2022, 3, 8));
    klausurRepository = mock(KlausurRepository.class);
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatum,
            veranstaltungsIdRepository);

    applicationService.storniereKlausur("dehus101", KL_PROPRA_03_09_1130_1230);

    assertThat(dennis.getKlausuren()).isEmpty();
  }

  @Test
  @DisplayName("Eine Klausur kann nicht am selben Tag storniert werden")
  void test_3() {

    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230);

    heutigesDatum = mock(HeutigesDatum.class);
    when(heutigesDatum.getDatum()).thenReturn(LocalDate.of(2022, 3, 9));
    klausurRepository = mock(KlausurRepository.class);
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatum,
            veranstaltungsIdRepository);
    assertThatExceptionOfType(KlausurException.class).isThrownBy(() ->
        applicationService.storniereKlausur("dehus101", KL_PROPRA_03_09_1130_1230)
    ).withMessageContaining("selben");

    assertThat(dennis.getKlausuren()).containsExactly(
        new KlausurReferenz(KL_PROPRA_03_09_1130_1230.getVeranstaltungsId()));
  }

  @Test
  @DisplayName("Eine Klausur kann nicht im nachhinein storniert werden")
  void test_4() {

    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230);

    heutigesDatum = mock(HeutigesDatum.class);
    when(heutigesDatum.getDatum()).thenReturn(LocalDate.of(2022, 3, 17));
    klausurRepository = mock(KlausurRepository.class);
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatum,
            veranstaltungsIdRepository);
    assertThatExceptionOfType(KlausurException.class).isThrownBy(() ->
        applicationService.storniereKlausur("dehus101", KL_PROPRA_03_09_1130_1230)
    ).withMessageContaining("nachhinein");

    assertThat(dennis.getKlausuren()).containsExactly(
        new KlausurReferenz(KL_PROPRA_03_09_1130_1230.getVeranstaltungsId()));
  }

  @Test
  @DisplayName("Eine Klausur kann bis zum Vortag storniert werden. Urlaube an dem Tag werden "
      + "storniert.")
  void test_5() {

    dennis.fuegeKlausurHinzu(KL_PROPRA_03_09_1130_1230);
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_0930_0945);
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_1300_1330);

    heutigesDatum = mock(HeutigesDatum.class);
    when(heutigesDatum.getDatum()).thenReturn(LocalDate.of(2022, 3, 7));
    klausurRepository = mock(KlausurRepository.class);
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository, heutigesDatum,
            veranstaltungsIdRepository);
    applicationService.storniereKlausur("dehus101", KL_PROPRA_03_09_1130_1230);

    assertThat(dennis.getKlausuren()).isEmpty();
    assertThat(dennis.getUrlaube()).isEmpty();
  }
}
