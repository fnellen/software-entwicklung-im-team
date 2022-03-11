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

public class ApplicationService {
  public static final LocalTime START_UHRZEIT = LocalTime.of(9, 30);
  public static final LocalTime END_UHRZEIT = LocalTime.of(13, 30);
  public static final long PRAKTIKUMS_TAG_DAUER = 240L;
  public static final long MAXIMALER_URLAUB_AN_EINEM_TAG = 150L;
  private final StudentRepository studentRepository;
  private final KlausurRepository klausurRepository;


  public ApplicationService(StudentRepository studentRepository,
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

    Student student = holeStudent(githubHandle);
    //Überprüfe, ob Student noch Resturlaub hat
    if (student.berechneRestUrlaub() <= 0) {
      throw new UrlaubException("Student hat keinen Resturlaub mehr.");
    }
    Set<Klausur> belegteKlausurenAmTag = getBelegteKlausurenAmTag(beantragterUrlaub, student);

    if (!belegteKlausurenAmTag.isEmpty()) {
      //An diesem Tag sind Klausuren belegt
      // TODO: andere Validierung
    } else {
      //An diesem Tag sind keine Klausuren
      long dauerDesUrlaubs = beantragterUrlaub.dauerInMinuten();

      //Student hat genug Resturlaub TODO: Schöner machen
      if (student.berechneRestUrlaub() - dauerDesUrlaubs < 0) {
        throw new UrlaubException("Student hat nicht genug Resturlaub.");
      }
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
        if (!ueberpruefeUrlaubsAnfaenge(beantragterUrlaub, beantragterUrlaub, urlaubAmTag,
            student)) {
          throw new UrlaubException(
              "Zeit zwischen den zu bereits vorhandenen Urlauben ist weniger als 90 Minuten");
        }
        if (!ueberpruefeUrlaubsAnfaenge(beantragterUrlaub, urlaubAmTag, beantragterUrlaub,
            student)) {
          throw new UrlaubException(
              "Zeit zwischen den zu bereits vorhandenen Urlauben ist weniger als 90 Minuten");
        }
      } else {
        throw new UrlaubException("Zu viele Urlaube an diesem Tag");
      }
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
        .filter(e -> e.zeitraumDto().getDatum() == zeitraumDto.getDatum()).collect(
            Collectors.toSet());
  }

  boolean ueberpruefeUrlaubsAnfaenge(ZeitraumDto urlaub, ZeitraumDto anfangsZeit,
                                             ZeitraumDto endZeit, Student student) {
    if (anfangsZeit.getStartUhrzeit().equals(START_UHRZEIT)
        && endZeit.getEndUhrzeit().equals(END_UHRZEIT)) {
      if (berechneZeitZwischenZeitraeumen(anfangsZeit, endZeit)) {
        student.fuegeUrlaubHinzu(urlaub);
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  /**
   * Überprüft, ob der Zeitraum zwischen dem Ende des ersten Urlaubs und dem
   * Anfang des zweiten Urlaubs mindestens 90 Minuten ist.
   */
  boolean berechneZeitZwischenZeitraeumen(ZeitraumDto urlaub1,
                                                  ZeitraumDto urlaub2
  ) {
    Duration zeitZwischenUrlauben =
        Duration.between(urlaub1.getEndUhrzeit(), urlaub2.getStartUhrzeit());
    return (zeitZwischenUrlauben.toMinutes() >= Duration.of(90, ChronoUnit.MINUTES).toMinutes());
  }


}
