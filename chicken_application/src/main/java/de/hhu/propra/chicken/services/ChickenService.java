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

  public Student holeStudent(String githubHandle) throws StudentNichtGefundenException {
    Student student = studentRepository.findeStudentMitHandle(githubHandle);
    if (student == null) {
      throw new StudentNichtGefundenException(githubHandle);
    }
    return student;
  }

  public void belegeUrlaub(String githubHandle, ZeitraumDto beantragterUrlaub)
      throws StudentNichtGefundenException, UrlaubException {
    //Am Tag bezieht sich auf denselben Tag vom beantragterUrlaub.
    long dauerDesUrlaubs = beantragterUrlaub.dauerInMinuten();
    Student student = holeStudent(githubHandle);
    long resturlaub = student.berechneRestUrlaub();

    //Überprüfe, ob Student noch Resturlaub hat
    if (resturlaub <= 0 || dauerDesUrlaubs - resturlaub < 0) {
      throw new UrlaubException("Student hat keinen Resturlaub mehr.");
    }
    Set<Klausur> belegteKlausurenAmTag = getBelegteKlausurenAmTag(beantragterUrlaub, student);

    if (!belegteKlausurenAmTag.isEmpty()) {
      //An diesem Tag sind Klausuren belegt
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

      Set<ZeitraumDto> neuBerechneteUrlaube = neuBerechneteZeitraeume
          .stream()
          .flatMap(neuerUrlaub -> getUrlaubeAmTag(neuerUrlaub, student).stream()
              .filter(festerUrlaub -> ueberschneidenSichZeitraeume(festerUrlaub, neuerUrlaub))
              .flatMap(
                  festerUrlaub -> berechneNichtUeberlappendeZeitraeume(festerUrlaub, neuerUrlaub))
              .collect(Collectors.toSet()).stream())
          .collect(Collectors.toSet());
      neuBerechneteUrlaube.forEach(student::fuegeUrlaubHinzu);
    } else {
      //An diesem Tag sind keine Klausuren
      Set<ZeitraumDto> urlaubeAmTag = getUrlaubeAmTag(beantragterUrlaub, student);
      if (urlaubeAmTag.isEmpty()) {
        //Hat keinen Urlaub an dem Tag
        if (dauerDesUrlaubs <= MAXIMALER_URLAUB_AN_EINEM_TAG
            || dauerDesUrlaubs == PRAKTIKUMS_TAG_DAUER) {
          student.fuegeUrlaubHinzu(beantragterUrlaub);
        } else {
          throw new UrlaubException("Urlaubszeitraum nicht korrekt");
        }
      } else if (urlaubeAmTag.size() == 1) {
        // Es gibt schon einen Urlaub an diesem Tag
        ZeitraumDto urlaubAmTag = urlaubeAmTag.iterator().next();
        /*
         * Der bereits vorhandene Urlaub fängt um 9:30 Uhr an und der zu belegende Urlaub
         * hört um 13:30 Uhr auf.
         */
        ZeitraumDto urlaub1 =
            beantragterUrlaub.getStartUhrzeit()
                .isBefore(urlaubAmTag.getStartUhrzeit()) ? beantragterUrlaub : urlaubAmTag;
        ZeitraumDto urlaub2 =
            beantragterUrlaub.getStartUhrzeit()
                .isBefore(urlaubAmTag.getStartUhrzeit()) ? urlaubAmTag : beantragterUrlaub;

        if (urlaubsRegelnUeberpruefen(urlaub1, urlaub2)) {
          student.fuegeUrlaubHinzu(beantragterUrlaub);
        } else {
          throw new UrlaubException(
              "Zeit zwischen den zu bereits vorhandenen Urlauben ist weniger als 90 Minuten");
        }
      } else {
        throw new UrlaubException("Mehr als zwei Urlaube am Tag nicht möglich");
      }
    }
  }

//  Stream<ZeitraumDto> berechneNichtUeberlappendeZeitraeume(ZeitraumDto zeitraumDto1,
//                                                           ZeitraumDto zeitraumDto2) {
//    if (zeitraumDto1.getStartUhrzeit().isBefore(zeitraumDto2.getStartUhrzeit())
//        && zeitraumDto1.getEndUhrzeit().isAfter(zeitraumDto2.getEndUhrzeit())) {
//      ZeitraumDto zeitraum1 = ZeitraumDto.erstelleZeitraum(zeitraumDto1.getDatum(),
//          zeitraumDto1.getStartUhrzeit(), zeitraumDto2.getStartUhrzeit());
//      ZeitraumDto zeitraum2 = ZeitraumDto.erstelleZeitraum(zeitraumDto1.getDatum(),
//          zeitraumDto2.getEndUhrzeit(), zeitraumDto1.getEndUhrzeit());
//      return Stream.of(zeitraum1, zeitraum2);
//    } else if (zeitraumDto2.getStartUhrzeit().isBefore(zeitraumDto1.getStartUhrzeit())
//        && zeitraumDto2.getEndUhrzeit().isAfter(zeitraumDto1.getEndUhrzeit())) {
//      ZeitraumDto zeitraum1 = ZeitraumDto.erstelleZeitraum(zeitraumDto2.getDatum(),
//          zeitraumDto2.getStartUhrzeit(), zeitraumDto1.getStartUhrzeit());
//      ZeitraumDto zeitraum2 = ZeitraumDto.erstelleZeitraum(zeitraumDto2.getDatum(),
//          zeitraumDto1.getEndUhrzeit(), zeitraumDto2.getEndUhrzeit());
//      return Stream.of(zeitraum1, zeitraum2);
//    } else {
//      ZeitraumDto zeitraum;
//      if (zeitraumDto2.getStartUhrzeit()
//          .isBefore(zeitraumDto1.getStartUhrzeit())) {
//        zeitraum = ZeitraumDto.erstelleZeitraum(zeitraumDto1.getDatum(),
//            zeitraumDto2.getEndUhrzeit(), zeitraumDto1.getEndUhrzeit());
//      } else {
//        zeitraum = ZeitraumDto.erstelleZeitraum(zeitraumDto1.getDatum(),
//            zeitraumDto1.getStartUhrzeit(), zeitraumDto2.getStartUhrzeit());
//      }
//      return Stream.of(zeitraum);
//    }
//  }

  Stream<ZeitraumDto> berechneNichtUeberlappendeZeitraeume(ZeitraumDto urlaub,
                                                           ZeitraumDto klausur) {
    // Fall 1: Urlaub liegt komplett außerhalb vor Klausur, wird nicht von Methode gehandhabt
    // Fall 2: Start von neuem Urlaub liegt außerhalb Klausur, Ende liegt in Klausur
    if (urlaub.getStartUhrzeit().isBefore(klausur.getStartUhrzeit())
        && urlaub.getEndUhrzeit().isBefore(klausur.getEndUhrzeit())) {
      ZeitraumDto neuerUrlaub = ZeitraumDto.erstelleZeitraum(
          urlaub.getDatum(),
          urlaub.getStartUhrzeit(),
          klausur.getStartUhrzeit());
      return Stream.of(neuerUrlaub);
    }
    // Fall 3: Start in Klausur, Ende nach Klausur
    if (urlaub.getStartUhrzeit().isAfter(klausur.getStartUhrzeit())
        && urlaub.getEndUhrzeit().isAfter(klausur.getEndUhrzeit())) {
      ZeitraumDto neuerUrlaub = ZeitraumDto.erstelleZeitraum(
          urlaub.getDatum(),
          klausur.getEndUhrzeit(),
          urlaub.getEndUhrzeit());
      return Stream.of(neuerUrlaub);
    }
    // Fall 4: Urlaub liegt komplett in Klausur, wird nicht von Methode gehandhabt (s. Z 184)
    // Fall 5: Urlaub liegt komplett außerhalb nach Klausur, wird nicht von Methode gehandhabt
    // Fall 6: Urlaub fängt vor Klausur an, endet nach Klausur
    if (urlaub.getStartUhrzeit().isBefore(klausur.getStartUhrzeit())
        && urlaub.getEndUhrzeit().isAfter(klausur.getEndUhrzeit())) {
      ZeitraumDto neuerUrlaubVorKlausur = ZeitraumDto.erstelleZeitraum(
          urlaub.getDatum(),
          urlaub.getStartUhrzeit(),
          klausur.getStartUhrzeit());
      ZeitraumDto neuerUrlaubNachKlausur = ZeitraumDto.erstelleZeitraum(
          urlaub.getDatum(),
          klausur.getEndUhrzeit(),
          urlaub.getEndUhrzeit());
      return Stream.of(neuerUrlaubVorKlausur, neuerUrlaubNachKlausur);
    }
    return Stream.empty();
  }

  // Fall 4: Urlaub liegt komplett in Klausur
  boolean liegtUrlaubInKlausurZeitraum(ZeitraumDto urlaub, ZeitraumDto klausur) {
    if (urlaub.getStartUhrzeit().isAfter(klausur.getStartUhrzeit())
        && urlaub.getEndUhrzeit().isBefore(klausur.getEndUhrzeit())) {
      return true;
    }
    return false;
  }

  boolean ueberschneidenSichZeitraeume(ZeitraumDto beantragterUrlaub,
                                       ZeitraumDto zeitraumDto) {
    //beantragterUrlaub liegt teilweise rechts in zeitraumDto z.B:
    //beantragterUrlaub = 11:00-12:00, zeitraumDto = 10:30-11:30 ->> Überschneidung
    if (zeitraumDto.getStartUhrzeit().isBefore(beantragterUrlaub.getStartUhrzeit())
        && beantragterUrlaub.getStartUhrzeit().isBefore(zeitraumDto.getEndUhrzeit())) {
      return true;
      //beantragterUrlaub liegt nicht zwischen zeitraumDtoStart und zeitraumDtoStart z.B:
      //beantragterUrlaub = 11:45-12:00, zeitraumDto = 10:30-11:30 ->> keine Überschneidung
    } else if (zeitraumDto.getStartUhrzeit().isBefore(beantragterUrlaub.getStartUhrzeit())
        && beantragterUrlaub.getStartUhrzeit().isAfter(zeitraumDto.getEndUhrzeit())) {
      return false;
      //beantragterUrlaub liegt teilweise links in zeitraumDto z.B:
      //beantragterUrlaub = 10:15-11:00, zeitraumDto = 10:30-11:30 ->> Überschneidung
    } else if (
        zeitraumDto.getStartUhrzeit().isAfter(beantragterUrlaub.getStartUhrzeit())
            && beantragterUrlaub.getEndUhrzeit().isAfter(zeitraumDto.getStartUhrzeit())) {
      return true;
    } else {
      return false;
    }
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
