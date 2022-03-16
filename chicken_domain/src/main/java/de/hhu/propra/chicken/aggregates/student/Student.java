package de.hhu.propra.chicken.aggregates.student;

import de.hhu.propra.chicken.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.aggregates.klausur.Klausur;
import de.hhu.propra.chicken.stereotypes.AggregateRoot;
import java.util.HashSet;
import java.util.Set;

/**
 * Darstellung eines Studenten, der Urlaub nehmen kann und sich für Klausuren anmelden kann.
 * Bildet den Stakeholder Student ab.
 */
@AggregateRoot
public class Student {

  private Long id;
  private final String githubHandle;
  private Set<ZeitraumDto> urlaube = new HashSet<>();
  private Set<KlausurReferenz> klausuren = new HashSet<>();

  private static final long GESAMT_URLAUBSZEIT_IN_MINUTEN = 240L;

  /**
   * Konstruktor zur Erstellung eines Studenten.
   *
   * @param id           Eindeutige Identifikation des Studenten in der Datenbank.
   * @param githubHandle Eindeutige Identifikation des Studenten durch GitHub-Authentifizierung.
   */
  public Student(Long id, String githubHandle) {
    this.id = id;
    this.githubHandle = githubHandle;
  }

  public Long getId() {
    return id;
  }

  public String getGithubHandle() {
    return githubHandle;
  }

  public Set<ZeitraumDto> getUrlaube() {
    return urlaube;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Fügt dem Studenten Urlaub hinzu.
   *
   * @param urlaubsZeitraum Ein Zeitraum, indem der Student Urlaub hat.
   */
  public void fuegeUrlaubHinzu(ZeitraumDto urlaubsZeitraum) {
    long minuten = urlaubsZeitraum.dauerInMinuten() + this.berechneBeantragtenUrlaub();
    if (minuten <= GESAMT_URLAUBSZEIT_IN_MINUTEN) {
      urlaube.add(urlaubsZeitraum);
    }
  }

  public void fuegeKlausurHinzu(Klausur klausur) {
    klausuren.add(new KlausurReferenz(klausur.getVeranstaltungsId()));
  }

  public long berechneBeantragtenUrlaub() {
    long urlaub = urlaube.stream().mapToLong(ZeitraumDto::dauerInMinuten).sum();
    return urlaub;
  }

  public long berechneRestUrlaub() {
    return GESAMT_URLAUBSZEIT_IN_MINUTEN - this.berechneBeantragtenUrlaub();
  }

  void setzeUrlaube(Set<ZeitraumDto> urlaube) {
    this.urlaube = urlaube;
  }

  public Set<KlausurReferenz> getKlausuren() {
    return klausuren;
  }

  void setzeKlausuren(Set<KlausurReferenz> klausurenReferenzen) {
    this.klausuren = klausurenReferenzen;
  }

  public void entferneUrlaub(ZeitraumDto zeitraumDto) {
    urlaube.remove(zeitraumDto);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Student student = (Student) o;

    return githubHandle.equals(student.githubHandle);
  }

  @Override
  public int hashCode() {
    return githubHandle.hashCode();
  }
}
