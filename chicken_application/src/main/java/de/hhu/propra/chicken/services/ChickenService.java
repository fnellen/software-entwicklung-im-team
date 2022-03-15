package de.hhu.propra.chicken.services;

import de.hhu.propra.chicken.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.aggregates.klausur.Klausur;
import de.hhu.propra.chicken.aggregates.student.KlausurReferenz;
import de.hhu.propra.chicken.aggregates.student.Student;
import de.hhu.propra.chicken.repositories.KlausurRepository;
import de.hhu.propra.chicken.repositories.StudentRepository;
import de.hhu.propra.chicken.services.fehler.StudentNichtGefundenException;
import de.hhu.propra.chicken.services.fehler.UrlaubException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChickenService {
  public static final LocalTime PRAKTIKUMS_START_UHRZEIT = LocalTime.of(9, 30);
  public static final LocalTime PRAKTIKUMS_END_UHRZEIT = LocalTime.of(13, 30);
  public static final long PRAKTIKUMS_TAG_DAUER = 240L;
  public static final long MAXIMALER_URLAUB_AN_EINEM_TAG = 150L;
  public static final int MINDESTARBEITSAUFWAND = 90;
  private final StudentRepository studentRepository;
  private final KlausurRepository klausurRepository;


  public ChickenService(StudentRepository studentRepository,
                        KlausurRepository klausurRepository) {
    this.studentRepository = studentRepository;
    this.klausurRepository = klausurRepository;
  }

  public void belegeUrlaub(String githubHandle, ZeitraumDto beantragterUrlaub)
      throws StudentNichtGefundenException, UrlaubException {
    //Am Tag bezieht sich auf denselben Tag vom beantragterUrlaub.
    long dauerDesUrlaubs = beantragterUrlaub.dauerInMinuten();
    Student student = holeStudent(githubHandle);
    long resturlaub = student.berechneRestUrlaub();

    ueberpruefeResturlaub(dauerDesUrlaubs, resturlaub);
    Set<Klausur> belegteKlausurenAmTag = getBelegteKlausurenAmTag(beantragterUrlaub, student);
    Set<ZeitraumDto> gebuchteUrlaubeAmTag = getUrlaubeAmTag(beantragterUrlaub, student);

    if (!belegteKlausurenAmTag.isEmpty()) {
      belegeUrlaubMitKlausurAmTag(beantragterUrlaub, student, belegteKlausurenAmTag,
          gebuchteUrlaubeAmTag);
    } else {
      if (gebuchteUrlaubeAmTag.isEmpty()) {
        belegeUrlaubOhneUrlaubAmTag(beantragterUrlaub, dauerDesUrlaubs, student);
      } else if (gebuchteUrlaubeAmTag.size() == 1) {
        belegeUrlaubMitUrlaubAmTag(beantragterUrlaub, student, gebuchteUrlaubeAmTag);
      } else {
        throw new UrlaubException("Mehr als zwei Urlaube am Tag nicht möglich");
      }
    }
  }

  public Student holeStudent(String githubHandle) throws StudentNichtGefundenException {
    Student student = studentRepository.findeStudentMitHandle(githubHandle);
    if (student == null) {
      throw new StudentNichtGefundenException(githubHandle);
    }
    return student;
  }

  private void ueberpruefeResturlaub(long dauerDesUrlaubs, long resturlaub) {
    if (resturlaub <= 0 || resturlaub - dauerDesUrlaubs < 0) {
      throw new UrlaubException("Student hat keinen Resturlaub mehr.");
    }
  }

  private void belegeUrlaubMitUrlaubAmTag(ZeitraumDto beantragterUrlaub, Student student,
                                          Set<ZeitraumDto> gebuchteUrlaubeAmTag) {
    ZeitraumDto urlaubAmTag = gebuchteUrlaubeAmTag.iterator().next();
    ZeitraumDto urlaub1 =
        beantragterUrlaub.getStartUhrzeit()
            .isBefore(urlaubAmTag.getStartUhrzeit()) ? beantragterUrlaub : urlaubAmTag;
    ZeitraumDto urlaub2 =
        beantragterUrlaub.getStartUhrzeit()
            .isBefore(urlaubAmTag.getStartUhrzeit()) ? urlaubAmTag : beantragterUrlaub;

    if (urlaubsRegelnUeberpruefen(urlaub1, urlaub2)) {
      student.fuegeUrlaubHinzu(beantragterUrlaub);
      studentRepository.speicherStudent(student);
    } else {
      throw new UrlaubException(
          "Zeit zwischen den bereits vorhandenen Urlauben ist weniger als 90 Minuten");
    }
  }

  private void belegeUrlaubOhneUrlaubAmTag(ZeitraumDto beantragterUrlaub, long dauerDesUrlaubs,
                                           Student student) {
    if (dauerDesUrlaubs <= MAXIMALER_URLAUB_AN_EINEM_TAG
        || dauerDesUrlaubs == PRAKTIKUMS_TAG_DAUER) {
      student.fuegeUrlaubHinzu(beantragterUrlaub);
      studentRepository.speicherStudent(student);
    } else {
      throw new UrlaubException("Urlaubszeitraum nicht korrekt");
    }
  }

  private void belegeUrlaubMitKlausurAmTag(ZeitraumDto beantragterUrlaub, Student student,
                                           Set<Klausur> belegteKlausurenAmTag,
                                           Set<ZeitraumDto> gebuchteUrlaubeAmTag) {
    Set<ZeitraumDto> angepassteUrlaubzeitrauemeAnKlausuren = passeUrlaubAnKlausurZeitrauemeAn(
        beantragterUrlaub, belegteKlausurenAmTag);

    Set<ZeitraumDto> angepassteUrlaubszeitraumeAnVorhandenenUrlauben =
        passeUrlaubszeitraumeAnVorhandenenUrlaubenAn(gebuchteUrlaubeAmTag,
            angepassteUrlaubzeitrauemeAnKlausuren);

    angepassteUrlaubszeitraumeAnVorhandenenUrlauben.forEach(student::fuegeUrlaubHinzu);
    studentRepository.speicherStudent(student);
  }

  private Set<ZeitraumDto> passeUrlaubszeitraumeAnVorhandenenUrlaubenAn(
      Set<ZeitraumDto> gebuchteUrlaubeAmTag,
      Set<ZeitraumDto> neuBerechneteZeitraeume) {
    // Gucken ob ein bereits gebuchter Urlaub den Urlaubsantrag komplett überdeckt
    Set<ZeitraumDto> ueberdeckenSichUrlaubszeitraume =
        neuBerechneteZeitraeume.stream()
            .flatMap(neuerUrlaub -> gebuchteUrlaubeAmTag
                .stream()
                .filter(festerUrlaub -> liegtUrlaubInZeitraum(neuerUrlaub, festerUrlaub))
                .collect(Collectors.toSet()).stream()
            ).collect(Collectors.toSet());

    if (!ueberdeckenSichUrlaubszeitraume.isEmpty()) {
      throw new UrlaubException("Urlaubzeitraum wird komplett von festem Urlaub überdeckt");
    }

    // Berechne neue Zeiträume die außerhalb vom bereits gebuchten Urlaub liegen
    Set<ZeitraumDto> neuBerechneteUrlaube = neuBerechneteZeitraeume
        .stream()
        .flatMap(neuerUrlaub -> gebuchteUrlaubeAmTag.stream()
            .filter(festerUrlaub -> ueberschneidenSichZeitraeume(neuerUrlaub, festerUrlaub))
            .flatMap(
                festerUrlaub -> berechneNichtUeberlappendeZeitraeume(neuerUrlaub, festerUrlaub))
            .collect(Collectors.toSet()).stream())
        .collect(Collectors.toSet());

    // Falls fester Urlaub und berechnete Zeiträume nicht überschneiden, buche berechnete
    // Zeiträume
    if (neuBerechneteUrlaube.isEmpty()) {
      neuBerechneteUrlaube = neuBerechneteZeitraeume;
    }
    return neuBerechneteUrlaube;
  }

  private Set<ZeitraumDto> passeUrlaubAnKlausurZeitrauemeAn(
      ZeitraumDto beantragterUrlaub,
      Set<Klausur> belegteKlausurenAmTag) {
    // zu buchender Urlaub liegt in gebuchtem Klausurzeitraum -> lösche Urlaubantrag
    Set<Klausur> klausurenDieBeantragtenUrlaubUeberdecken = belegteKlausurenAmTag
        .stream()
        .filter(klausur -> liegtUrlaubInZeitraum(beantragterUrlaub, klausur.zeitraumDto()))
        .collect(Collectors.toSet());
    if (!klausurenDieBeantragtenUrlaubUeberdecken.isEmpty()) {
      throw new UrlaubException("Urlaubzeitraum wird komplett von Klausur ueberdeckt");
    }

    // Berechne neue Urlaubszeiträume außerhalb des vorher gebuchten Klausurzeitraums
    Set<ZeitraumDto> neuBerechneteZeitraeume = belegteKlausurenAmTag.stream()
        .filter(klausur -> ueberschneidenSichZeitraeume(beantragterUrlaub, klausur.zeitraumDto()))
        .flatMap(klausur -> berechneNichtUeberlappendeZeitraeume(beantragterUrlaub,
            klausur.zeitraumDto()))
        .collect(Collectors.toSet());

    // falls sich der zu beantragende Urlaub nicht mit der Klausur überschneidet, soll
    // trotzdem überprüft werden, ob sich schon vorhandene Urlaube schneiden
    if (neuBerechneteZeitraeume.isEmpty()) {
      neuBerechneteZeitraeume = Set.of(beantragterUrlaub);
    }
    return neuBerechneteZeitraeume;
  }

  Stream<ZeitraumDto> berechneNichtUeberlappendeZeitraeume(ZeitraumDto urlaub,
                                                           ZeitraumDto zeitraum) {
    // Fall 1: Urlaub liegt komplett vor Klausur, oder endet genau am Start der Klausur
    // Fall 2: Start von neuem Urlaub liegt außerhalb Klausur, Ende liegt in Klausur
    if (urlaub.getStartUhrzeit().isBefore(zeitraum.getStartUhrzeit())
        && urlaub.getEndUhrzeit().isBefore(zeitraum.getEndUhrzeit())) {
      ZeitraumDto neuerUrlaub = ZeitraumDto.erstelleZeitraum(
          urlaub.getDatum(),
          urlaub.getStartUhrzeit(),
          zeitraum.getStartUhrzeit());
      return Stream.of(neuerUrlaub);
    }
    // Fall 3: Urlaub Start in Klausur, Urlaub Ende nach Klausur
    if (urlaub.getStartUhrzeit().isAfter(zeitraum.getStartUhrzeit())
        && urlaub.getEndUhrzeit().isAfter(zeitraum.getEndUhrzeit())) {
      ZeitraumDto neuerUrlaub = ZeitraumDto.erstelleZeitraum(
          urlaub.getDatum(),
          zeitraum.getEndUhrzeit(),
          urlaub.getEndUhrzeit());
      return Stream.of(neuerUrlaub);
    }
    // Fall 4: Urlaub liegt komplett in Klausur
    // Fall 5: Urlaub liegt komplett nach Klausur oder startet genau am Ende der Klausur
    // Fall 6: Urlaub fängt vor Klausur an, endet nach Klausur
    if (urlaub.getStartUhrzeit().isBefore(zeitraum.getStartUhrzeit())
        && urlaub.getEndUhrzeit().isAfter(zeitraum.getEndUhrzeit())) {
      ZeitraumDto neuerUrlaubVorKlausur = ZeitraumDto.erstelleZeitraum(
          urlaub.getDatum(),
          urlaub.getStartUhrzeit(),
          zeitraum.getStartUhrzeit());
      ZeitraumDto neuerUrlaubNachKlausur = ZeitraumDto.erstelleZeitraum(
          urlaub.getDatum(),
          zeitraum.getEndUhrzeit(),
          urlaub.getEndUhrzeit());
      return Stream.of(neuerUrlaubVorKlausur, neuerUrlaubNachKlausur);
    }
    return Stream.empty();
  }

  // Fall 4: Urlaub liegt komplett in Klausur
  boolean liegtUrlaubInZeitraum(ZeitraumDto urlaub, ZeitraumDto zeitraum) {
    if (urlaub.getStartUhrzeit().isAfter(zeitraum.getStartUhrzeit())
        && urlaub.getEndUhrzeit().isBefore(zeitraum.getEndUhrzeit())) {
      return true;
    }
    return false;
  }

  boolean ueberschneidenSichZeitraeume(ZeitraumDto beantragterUrlaub,
                                       ZeitraumDto zeitraum) {
    // Fall 1
    if (beantragterUrlaub.getStartUhrzeit().isBefore(zeitraum.getStartUhrzeit())
        && (beantragterUrlaub.getEndUhrzeit().isBefore(zeitraum.getStartUhrzeit())
        || beantragterUrlaub.getEndUhrzeit().equals(zeitraum.getStartUhrzeit()))) {
      return false;
    }
    // Fall 2
    if (beantragterUrlaub.getStartUhrzeit().isBefore(zeitraum.getStartUhrzeit())
        && (beantragterUrlaub.getEndUhrzeit().isBefore(zeitraum.getEndUhrzeit())
            || beantragterUrlaub.getEndUhrzeit().equals(zeitraum.getEndUhrzeit()))) {
      return true;
    }
    // Fall 3
    if (beantragterUrlaub.getStartUhrzeit().isBefore(zeitraum.getEndUhrzeit())
        && beantragterUrlaub.getEndUhrzeit().isAfter(zeitraum.getEndUhrzeit())) {
      return true;
    }
    // Fall 5
    if (beantragterUrlaub.getStartUhrzeit().isAfter(zeitraum.getEndUhrzeit())
        || beantragterUrlaub.getStartUhrzeit().equals(zeitraum.getEndUhrzeit())) {
      return false;
    }
    return false;
  }

  Set<ZeitraumDto> getUrlaubeAmTag(ZeitraumDto zeitraumDto, Student student) {
    return student.getUrlaube().stream().filter(e -> e.getDatum().equals(zeitraumDto.getDatum()))
        .collect(
            Collectors.toSet());
  }

  Set<Klausur> getBelegteKlausurenAmTag(ZeitraumDto zeitraumDto, Student student) {
    Set<Klausur> belegteKlausurenVomStudenten =
        student.getKlausuren().stream().map(KlausurReferenz::id).map(
            klausurRepository::findeKlausurMitId).collect(Collectors.toSet());
    return belegteKlausurenVomStudenten.stream()
        .filter(e -> e.zeitraumDto().getDatum().equals(zeitraumDto.getDatum())).collect(
            Collectors.toSet());
  }

  boolean urlaubsRegelnUeberpruefen(ZeitraumDto urlaub1, ZeitraumDto urlaub2) {
    return istUrlaubsverteilungKorrekt(urlaub1, urlaub2) && istGenugZeitZwischen(urlaub1, urlaub2);
  }

  boolean istUrlaubsverteilungKorrekt(ZeitraumDto urlaub1, ZeitraumDto urlaub2) {
    return urlaub1.getStartUhrzeit().equals(PRAKTIKUMS_START_UHRZEIT)
        && urlaub2.getEndUhrzeit().equals(PRAKTIKUMS_END_UHRZEIT);
  }

  boolean istGenugZeitZwischen(ZeitraumDto urlaub1, ZeitraumDto urlaub2) {
    Duration zeitZwischenUrlauben =
        Duration.between(urlaub1.getEndUhrzeit(), urlaub2.getStartUhrzeit());
    return (zeitZwischenUrlauben.minus(Duration.of(MINDESTARBEITSAUFWAND, ChronoUnit.MINUTES))
        .toMinutes() >= 0);
  }


}
