package de.hhu.propra.chicken.services;

import de.hhu.propra.chicken.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.aggregates.klausur.Klausur;
import de.hhu.propra.chicken.aggregates.student.KlausurReferenz;
import de.hhu.propra.chicken.aggregates.student.Student;
import de.hhu.propra.chicken.repositories.KlausurRepository;
import de.hhu.propra.chicken.repositories.StudentRepository;
import de.hhu.propra.chicken.repositories.VeranstaltungsIdRepository;
import de.hhu.propra.chicken.services.dto.StudentDetails;
import de.hhu.propra.chicken.services.fehler.KlausurException;
import de.hhu.propra.chicken.services.fehler.StudentNichtGefundenException;
import de.hhu.propra.chicken.services.fehler.UrlaubException;
import de.hhu.propra.chicken.services.fehler.VeranstaltungsIdException;
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
  private final HeutigesDatum heutigesDatum;
  private final VeranstaltungsIdRepository veranstaltungsIdRepository;

  public ChickenService(StudentRepository studentRepository,
                        KlausurRepository klausurRepository,
                        HeutigesDatum heutigesDatum,
                        VeranstaltungsIdRepository veranstaltungsIdRepository) {
    this.studentRepository = studentRepository;
    this.klausurRepository = klausurRepository;
    this.heutigesDatum = heutigesDatum;
    this.veranstaltungsIdRepository = veranstaltungsIdRepository;
  }

  public StudentDetails studentDetails(String handle) {
    Student student = studentRepository.findeStudentMitHandle(handle);
    if (student == null) {
      throw new StudentNichtGefundenException(handle);
    }
    Set<KlausurReferenz> klausuren = student.getKlausuren();
    Set<Klausur> klausuren1 = klausuren.stream()
        .map(KlausurReferenz::id)
        .map(klausurRepository::findeKlausurMitVeranstaltungsId).collect(Collectors.toSet());
    return StudentDetails.von(student, klausuren1);
  }

  public void studentSpeichern(Student student) {
    studentRepository.speicherStudent(student);
  }

  public void klausurAnmelden(String veranstaltungsId, String veranstaltungsName,
                              ZeitraumDto zeitraumDto, Boolean praesenz)
      throws VeranstaltungsIdException {
    boolean valideId = veranstaltungsIdRepository.webCheck(veranstaltungsId);
    if (!valideId) {
      throw new VeranstaltungsIdException("Veranstaltungs ID nicht gültig");
    } else {
      ZeitraumDto neuerZeitraum;
      if (praesenz) {
        LocalTime neueStartuhrzeit =
            zeitraumDto.getStartUhrzeit().minus(Duration.of(120, ChronoUnit.MINUTES));
        LocalTime neueEndUhrzeit =
            zeitraumDto.getEndUhrzeit().plus(Duration.of(120, ChronoUnit.MINUTES));
        if (neueStartuhrzeit.isBefore(LocalTime.of(9, 30))) {
          neueStartuhrzeit = LocalTime.of(9, 30);
        }
        if (neueEndUhrzeit.isAfter(LocalTime.of(13, 30))) {
          neueEndUhrzeit = LocalTime.of(13, 30);
        }
        neuerZeitraum =
            ZeitraumDto.erstelleZeitraum(zeitraumDto.getDatum(), neueStartuhrzeit, neueEndUhrzeit);
      } else {
        LocalTime neueStartuhrzeit = zeitraumDto.getStartUhrzeit().minus(Duration.of(30,
            ChronoUnit.MINUTES));
        if (neueStartuhrzeit.isBefore(LocalTime.of(9, 30))) {
          neueStartuhrzeit = LocalTime.of(9, 30);
        }
        neuerZeitraum = ZeitraumDto.erstelleZeitraum(zeitraumDto.getDatum(), neueStartuhrzeit,
            zeitraumDto.getEndUhrzeit());
      }
      Klausur klausur =
          new Klausur(null, veranstaltungsId, veranstaltungsName, neuerZeitraum, praesenz);
      klausurRepository.speicherKlausur(klausur);
    }
  }

  public void belegeUrlaub(String githubHandle, ZeitraumDto beantragterUrlaub)
      throws StudentNichtGefundenException, UrlaubException {

    istUrlaubsDatumKorrekt(beantragterUrlaub);

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

  public void storniereUrlaub(String githubHandle, ZeitraumDto urlaub) {
    istUrlaubsDatumKorrekt(urlaub);
    Student student = holeStudent(githubHandle);
    student.entferneUrlaub(urlaub);
    studentRepository.speicherStudent(student);
  }

  public void storniereKlausur(String githubHandle, Klausur klausur) {
    istKlausurDatumKorrekt(klausur);
    Student student = holeStudent(githubHandle);
    student.entferneKlausur(klausur);
    Set<ZeitraumDto> urlaubeAmTag = getUrlaubeAmTag(klausur.zeitraumDto(), student);
    urlaubeAmTag.forEach(student::entferneUrlaub);
    studentRepository.speicherStudent(student);
  }

  void istUrlaubsDatumKorrekt(ZeitraumDto beantragterUrlaub) {
    if (beantragterUrlaub.getDatum().equals(heutigesDatum.getDatum())) {
      throw new UrlaubException("Urlaub kann nicht am selben Tag geaendert werden.");
    }
    if (beantragterUrlaub.getDatum().isBefore(heutigesDatum.getDatum())) {
      throw new UrlaubException("Urlaub kann nicht im nachhinein geaendert werden");
    }
  }

  public void belegeKlausur(String githubHandle, Klausur klausur) throws KlausurException {

    istKlausurDatumKorrekt(klausur);

    Student student = holeStudent(githubHandle);
    ZeitraumDto beantragteKlausur = klausur.zeitraumDto();
    Set<Klausur> belegteKlausurenAmTag = getBelegteKlausurenAmTag(beantragteKlausur, student);
    Set<ZeitraumDto> gebuchteUrlaubeAmTag = getUrlaubeAmTag(beantragteKlausur, student);

    if (!belegteKlausurenAmTag.isEmpty()) {
      Set<Klausur> ueberschneidendeKlausuren = belegteKlausurenAmTag.stream().filter(
          belegteKlausurAmTag -> ueberschneidenSichZeitraeume(belegteKlausurAmTag.zeitraumDto(),
              beantragteKlausur)).collect(Collectors.toSet());
      if (ueberschneidendeKlausuren.isEmpty()) {
        student.fuegeKlausurHinzu(klausur);
        studentRepository.speicherStudent(student);
      } else {
        //Es wird außer Acht gelassen, dass eine Klausur zusätzlich Zeit angerechnet bekommt.
        throw new KlausurException("Es können keine zwei Klausuren am selben Zeitraum geschrieben"
            + " werden.");
      }
    }

    //**Fall 1**: Kein Urlaub an dem Tag
    if (gebuchteUrlaubeAmTag.isEmpty()) {
      student.fuegeKlausurHinzu(klausur);
      studentRepository.speicherStudent(student);
    } else {
      //**Fall 2**: Urlaub an dem Tag
      //Fall 5: Urlaub fängt innerhalb der Klausur an und hört innerhalb der Klausur auf
      Set<ZeitraumDto> urlaubeInnerhalbKlausur = gebuchteUrlaubeAmTag.stream()
          .filter(urlaub -> liegtUrlaubInZeitraum(urlaub, beantragteKlausur))
          .collect(Collectors.toSet());

      if (!urlaubeInnerhalbKlausur.isEmpty()) {
        urlaubeInnerhalbKlausur.stream().forEach(student::entferneUrlaub);
        student.fuegeKlausurHinzu(klausur);
        studentRepository.speicherStudent(student);
        return;
      }

      Set<ZeitraumDto> ueberschneidendeUrlaube = gebuchteUrlaubeAmTag.stream().filter(urlaub ->
          ueberschneidenSichZeitraeume(urlaub, beantragteKlausur)).collect(Collectors.toSet());

      //Fall 2: Urlaub fängt vor der Klausur an und hört vor der Klausur auf
      //Fall 6: Urlaub fängt nach der Klausur an und hört nach der Klausur auf
      if (ueberschneidendeUrlaube.isEmpty()) {
        student.fuegeKlausurHinzu(klausur);
        studentRepository.speicherStudent(student);
      } else {
        //Fall 1: Urlaub fängt vor der Klausur an und hört innerhalb des Klausurzeitraums auf
        //Fall 3: Urlaub fängt vor der Klausur an und hört nach der Klausur auf
        //Fall 4: Urlaub fängt innerhalb der Klausur an und hört nach der Klausur auf
        Set<ZeitraumDto> angepassteUrlaube =
            ueberschneidendeUrlaube.stream().flatMap(urlaub -> passeUrlaubAnKlausurAn(urlaub,
                beantragteKlausur, student)).collect(Collectors.toSet());
        angepassteUrlaube.forEach(student::fuegeUrlaubHinzu);
        student.fuegeKlausurHinzu(klausur);
        studentRepository.speicherStudent(student);
      }
    }
  }

  void istKlausurDatumKorrekt(Klausur klausur) {
    if (klausur.zeitraumDto().getDatum().equals(heutigesDatum.getDatum())) {
      throw new KlausurException("Klausur kann nicht am selben Tag geaendert werden.");
    }
    if (klausur.zeitraumDto().getDatum().isBefore(heutigesDatum.getDatum())) {
      throw new KlausurException("Klausur kann nicht im nachhinein geaendert werden.");
    }
  }

  Stream<ZeitraumDto> passeUrlaubAnKlausurAn(ZeitraumDto urlaub, ZeitraumDto klausur,
                                             Student student) {
    Stream<ZeitraumDto> zeitraumDtoStream = berechneNichtUeberlappendeZeitraeume(urlaub, klausur);
    student.entferneUrlaub(urlaub);
    return zeitraumDtoStream;
  }

  public Student holeStudent(String githubHandle) throws StudentNichtGefundenException {
    Student student = studentRepository.findeStudentMitHandle(githubHandle);
    if (student == null) {
      throw new StudentNichtGefundenException(githubHandle);
    }
    return student;
  }

  void ueberpruefeResturlaub(long dauerDesUrlaubs, long resturlaub) {
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

  Set<ZeitraumDto> passeUrlaubszeitraumeAnVorhandenenUrlaubenAn(
      Set<ZeitraumDto> gebuchteUrlaubeAmTag,
      Set<ZeitraumDto> neuBerechneteZeitraeume) throws UrlaubException {
    // Gucken ob ein bereits gebuchter Urlaub den Urlaubsantrag komplett überdeckt
    Set<ZeitraumDto> ueberdeckenSichUrlaubszeitraume =
        neuBerechneteZeitraeume.stream()
            .flatMap(neuerUrlaub -> gebuchteUrlaubeAmTag
                .stream()
                .filter(festerUrlaub -> liegtUrlaubInZeitraum(neuerUrlaub, festerUrlaub))
                .collect(Collectors.toSet()).stream()
            ).collect(Collectors.toSet());

    if (!ueberdeckenSichUrlaubszeitraume.isEmpty()) {
      throw new UrlaubException("Urlaubzeitraum wird komplett von festem Urlaub ueberdeckt");
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
    if ((urlaub.getStartUhrzeit().isAfter(zeitraum.getStartUhrzeit()) || urlaub.getStartUhrzeit()
        .equals(zeitraum.getStartUhrzeit()))
        && (urlaub.getEndUhrzeit().isBefore(zeitraum.getEndUhrzeit()) || urlaub.getEndUhrzeit()
        .equals(zeitraum.getEndUhrzeit()))) {
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
            klausurRepository::findeKlausurMitVeranstaltungsId).collect(Collectors.toSet());
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
