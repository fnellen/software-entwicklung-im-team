package de.hhu.propra.chicken.services;

import static de.hhu.propra.chicken.services.KlausurTemplate.klausur1;
import static de.hhu.propra.chicken.services.KlausurTemplate.klausur2;
import static de.hhu.propra.chicken.services.KlausurTemplate.klausur3;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_07_0930_1030;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_07_1130_1230;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_07_1230_1330;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_08_0930_1030;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_08_1130_1230;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_1130_1230;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_14_1130_1230;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_15_1000_1030;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_15_1000_1100;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_15_1000_1200;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_15_1015_1100;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_15_1030_1130;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_15_1045_1200;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_15_1100_1200;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_15_1130_1200;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_15_1145_1200;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hhu.propra.chicken.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.aggregates.klausur.Klausur;
import de.hhu.propra.chicken.aggregates.student.Student;
import de.hhu.propra.chicken.repositories.KlausurRepository;
import de.hhu.propra.chicken.repositories.StudentRepository;
import de.hhu.propra.chicken.services.fehler.StudentNichtGefundenException;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class ChickenServiceTest {

  @Mock
  StudentRepository studentRepository;

  @Mock
  KlausurRepository klausurRepository;
  Student dennis = new Student(1L, "dehus101");

  @Test
  @DisplayName("GetUrlaubeAmTag gibt die richtige Anzahl der belegten Urlaube an einem Tag zurück")
  void test_1() {

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_08_0930_1030);
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_08_1130_1230);
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_1130_1230);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository);

    Set<ZeitraumDto> urlaubeAmTag = applicationService
        .getUrlaubeAmTag(ZEITRAUM_03_08_0930_1030, dennis);

    assertThat(urlaubeAmTag).hasSize(2);
  }

  @Test
  @DisplayName("GetUrlaubeAmTag gibt leere Menge zurück bei keinem Urlaub am Tag")
  void test_2() {
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_08_0930_1030);
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_08_1130_1230);
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_1130_1230);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository);

    Set<ZeitraumDto> urlaubeAmTag = applicationService
        .getUrlaubeAmTag(ZEITRAUM_03_07_0930_1030, dennis);

    assertThat(urlaubeAmTag).hasSize(0);
  }

  @Test
  @DisplayName("GetUrlaubeAmTag gibt die richtige Anzahl der belegten Urlaube an einem Tag zurück")
  void test_3() {

    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_08_0930_1030);
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_08_1130_1230);
    dennis.fuegeUrlaubHinzu(ZEITRAUM_03_09_1130_1230);

    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository);

    Set<ZeitraumDto> urlaubeAmTag = applicationService
        .getUrlaubeAmTag(ZEITRAUM_03_09_1130_1230, dennis);

    assertThat(urlaubeAmTag).hasSize(1);
  }

  @Test
  @DisplayName("getBelegteKlausurenAmTag gibt die richtige Anzahl der belegten Klausuren an einem"
      + " Tag zurück")
  void test_4() {

    dennis.fuegeKlausurHinzufuegen(klausur1);
    dennis.fuegeKlausurHinzufuegen(klausur2);
    dennis.fuegeKlausurHinzufuegen(klausur3);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitId(klausur1.getVeranstaltungsId())).thenReturn(klausur1);
    when(klausurRepository.findeKlausurMitId(klausur2.getVeranstaltungsId())).thenReturn(klausur2);
    when(klausurRepository.findeKlausurMitId(klausur3.getVeranstaltungsId())).thenReturn(klausur3);
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository);

    Set<Klausur> klausurenAmTag = applicationService
        .getBelegteKlausurenAmTag(ZEITRAUM_03_08_0930_1030, dennis);

    assertThat(klausurenAmTag).hasSize(1);
  }

  @Test
  @DisplayName("getBelegteKlausurenAmTag gibt keine Klausuren an einem Tag ohne belegte Klausuren"
      + " zurück")
  void test_5() {
    dennis.fuegeKlausurHinzufuegen(klausur1);
    dennis.fuegeKlausurHinzufuegen(klausur2);
    dennis.fuegeKlausurHinzufuegen(klausur3);

    klausurRepository = mock(KlausurRepository.class);
    when(klausurRepository.findeKlausurMitId(klausur1.getVeranstaltungsId())).thenReturn(klausur1);
    when(klausurRepository.findeKlausurMitId(klausur2.getVeranstaltungsId())).thenReturn(klausur2);
    when(klausurRepository.findeKlausurMitId(klausur3.getVeranstaltungsId())).thenReturn(klausur3);
    ChickenService applicationService =
        new ChickenService(studentRepository, klausurRepository);

    Set<Klausur> klausurenAmTag = applicationService
        .getBelegteKlausurenAmTag(ZEITRAUM_03_14_1130_1230, dennis);

    assertThat(klausurenAmTag).hasSize(0);
  }

  @Test
  @DisplayName("istUrlaubsverteilungKorrekt gibt true bei korrekter Verteilung der "
      + "Urlaubszeiten")
  void test_6() {
    ChickenService appService = new ChickenService(studentRepository, klausurRepository);
    boolean verteilung = appService.istUrlaubsverteilungKorrekt(ZEITRAUM_03_07_0930_1030,
        ZEITRAUM_03_07_1230_1330);
    assertThat(verteilung).isTrue();
  }

  @Test
  @DisplayName("istUrlaubsverteilungKorrekt gibt false bei nicht konformer Verteilung der "
      + "Urlaubszeiten")
  void test_7() {
    ChickenService appService = new ChickenService(studentRepository, klausurRepository);
    boolean verteilung = appService.istUrlaubsverteilungKorrekt(ZEITRAUM_03_07_0930_1030,
        ZEITRAUM_03_07_1130_1230);
    assertThat(verteilung).isFalse();
  }

  @Test
  @DisplayName("istGenugZeitZwischen gibt true bei genügend Abstand zwischen den beiden "
      + "Urlaubszeiten")
  void test_8() {
    ChickenService appService = new ChickenService(studentRepository, klausurRepository);
    boolean abstand = appService.istGenugZeitZwischen(ZEITRAUM_03_07_0930_1030,
        ZEITRAUM_03_07_1230_1330);
    assertThat(abstand).isTrue();
  }

  @Test
  @DisplayName("istGenugZeitZwischen gibt false bei zu wenig Abstand zwischen den beiden "
      + "Urlaubszeiten")
  void test_9() {
    ChickenService appService = new ChickenService(studentRepository, klausurRepository);
    boolean abstand = appService.istGenugZeitZwischen(ZEITRAUM_03_07_1130_1230,
        ZEITRAUM_03_07_1230_1330);
    assertThat(abstand).isFalse();
  }

  @Test
  @DisplayName("holeStudent gibt den Student mit dem jeweiligen GithubHandle zurück")
  void test_10() throws StudentNichtGefundenException {
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle("dehus101")).thenReturn(dennis);

    ChickenService appService = new ChickenService(studentRepository, klausurRepository);

    Student geholt = appService.holeStudent("dehus101");

    assertThat(geholt).isEqualTo(dennis);
  }

  @Test
  @DisplayName("holeStudent wirft Exception bei keinem gefundenen Studenten")
  void test_11() throws StudentNichtGefundenException {
    studentRepository = mock(StudentRepository.class);
    when(studentRepository.findeStudentMitHandle(anyString())).thenReturn(null);

    ChickenService appService = new ChickenService(studentRepository, klausurRepository);

    assertThatExceptionOfType(StudentNichtGefundenException.class)
        .isThrownBy(() -> appService.holeStudent("dehus101"));
  }

  @Test
  @DisplayName("ueberschneidenSichZeitraeume gibt true bei rechter Überschneidung zurück")
  void test_12() throws StudentNichtGefundenException {
    ChickenService appService = new ChickenService(studentRepository, klausurRepository);

    boolean ueberschneidung =
        appService.ueberschneidenSichZeitraeume(ZEITRAUM_03_15_1100_1200, ZEITRAUM_03_15_1030_1130);

    assertThat(ueberschneidung).isTrue();

  }

  @Test
  @DisplayName("ueberschneidenSichZeitraeume gibt bei keiner Überschneidung false zurück")
  void test_13() throws StudentNichtGefundenException {
    ChickenService appService = new ChickenService(studentRepository, klausurRepository);

    boolean ueberschneidung =
        appService.ueberschneidenSichZeitraeume(ZEITRAUM_03_15_1145_1200, ZEITRAUM_03_15_1030_1130);

    assertThat(ueberschneidung).isFalse();

  }

  @Test
  @DisplayName("Reihenfolge ist nicht entscheidend über das Ergebnis")
  void test_14() throws StudentNichtGefundenException {
    ChickenService appService = new ChickenService(studentRepository, klausurRepository);

    boolean ueberschneidung =
        appService.ueberschneidenSichZeitraeume(ZEITRAUM_03_15_1030_1130, ZEITRAUM_03_15_1145_1200);

    assertThat(ueberschneidung).isFalse();

  }

  @Test
  @DisplayName("ueberschneidenSichZeitraeume gibt true bei linker Überschneidung zurück")
  void test_15() throws StudentNichtGefundenException {
    ChickenService appService = new ChickenService(studentRepository, klausurRepository);

    boolean ueberschneidung =
        appService.ueberschneidenSichZeitraeume(ZEITRAUM_03_15_1015_1100, ZEITRAUM_03_15_1030_1130);

    assertThat(ueberschneidung).isTrue();

  }

  @Test
  @DisplayName("ueberschneidenSichZeitraeume gibt true bei kompletter Überlappung")
  void test_16() throws StudentNichtGefundenException {
    ChickenService appService = new ChickenService(studentRepository, klausurRepository);

    boolean ueberschneidung =
        appService.ueberschneidenSichZeitraeume(ZEITRAUM_03_15_1000_1200, ZEITRAUM_03_15_1030_1130);

    assertThat(ueberschneidung).isTrue();

  }

  @Test
  @DisplayName("ueberschneidenSichZeitraeume gibt false bei identischen Zeiträumen")
  void test_17() throws StudentNichtGefundenException {
    ChickenService appService = new ChickenService(studentRepository, klausurRepository);

    boolean ueberschneidung =
        appService.ueberschneidenSichZeitraeume(ZEITRAUM_03_15_1000_1200, ZEITRAUM_03_15_1000_1200);

    assertThat(ueberschneidung).isFalse();

  }

  @Test
  @DisplayName("berechneZeitraeume gibt nicht ueberlappende Zeiträume zurück. Zeitraum1 beinhaltet Zeitraum2")
  void test_18() throws StudentNichtGefundenException {
    ChickenService appService = new ChickenService(studentRepository, klausurRepository);

    Set<ZeitraumDto> neueZeitraeume =
        appService.berechneNichtUeberlappendeZeitraeume(ZEITRAUM_03_15_1000_1200,
            ZEITRAUM_03_15_1030_1130).collect(
            Collectors.toSet());

    assertThat(neueZeitraeume).containsExactlyInAnyOrder(ZEITRAUM_03_15_1000_1030,
        ZEITRAUM_03_15_1130_1200);

  }


  @Test
  @DisplayName("berechneZeitraeume gibt ueberlappenden Zeitraum rechts zurück.")
  void test_20() throws StudentNichtGefundenException {
    ChickenService appService = new ChickenService(studentRepository, klausurRepository);

    Set<ZeitraumDto> neueZeitraeume =
        appService.berechneNichtUeberlappendeZeitraeume(ZEITRAUM_03_15_1045_1200,
            ZEITRAUM_03_15_1030_1130)
            .collect(Collectors.toSet());

    assertThat(neueZeitraeume).containsExactlyInAnyOrder(ZEITRAUM_03_15_1130_1200);

  }

  @Test
  @DisplayName("berechneZeitraeume gibt ueberlappenden Zeitraum links zurück.")
  void test_21() throws StudentNichtGefundenException {
    ChickenService appService = new ChickenService(studentRepository, klausurRepository);

    Set<ZeitraumDto> neueZeitraeume =
        appService.berechneNichtUeberlappendeZeitraeume(ZEITRAUM_03_15_1000_1100,
            ZEITRAUM_03_15_1030_1130)
            .collect(Collectors.toSet());

    assertThat(neueZeitraeume).containsExactlyInAnyOrder(ZEITRAUM_03_15_1000_1030);

  }

  @Test
  @DisplayName("liegtUrlaubInZeitraum gibt true bei Zeitraum2 beinhaltet Zeitraum1 zurück")
  void test_19() throws StudentNichtGefundenException {
    ChickenService appService = new ChickenService(studentRepository, klausurRepository);

    boolean b = appService.liegtUrlaubInZeitraum(ZEITRAUM_03_15_1030_1130,
        ZEITRAUM_03_15_1000_1200);

    assertThat(b).isTrue();

  }

  @Test
  @DisplayName("liegtUrlaubInZeitraum gibt false bei Zeitraum2 beinhaltet nicht Zeitraum1 zurück")
  void test_22() throws StudentNichtGefundenException {
    ChickenService appService = new ChickenService(studentRepository, klausurRepository);

    boolean b = appService.liegtUrlaubInZeitraum(ZEITRAUM_03_15_1030_1130,
        ZEITRAUM_03_15_1130_1200);

    assertThat(b).isFalse();

  }
}
