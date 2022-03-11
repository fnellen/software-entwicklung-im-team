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
  private Set<UrlaubZeitraum> urlaube = new HashSet<>();
  private Set<KlausurReferenz> klausuren;

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

  public Set<UrlaubZeitraum> getUrlaube() {
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
  public void fuegeUrlaubHinzufuegen(UrlaubZeitraum urlaubsZeitraum) {
    long minuten = urlaubsZeitraum.dauerInMinuten() + this.berechneBeantragtenUrlaub();
    if (minuten <= GESAMT_URLAUBSZEIT_IN_MINUTEN) {
      urlaube.add(urlaubsZeitraum);
    }
  }

  /*public void fuegeKlausurHinzufuegen(Klausur klausur) {
    klausuren.add(new KlausurReferenz(klausur.id()));
  }*/

  public long berechneBeantragtenUrlaub() {
    long urlaub = urlaube.stream().mapToLong(UrlaubZeitraum::dauerInMinuten).sum();
    return urlaub;
  }

  public long berechneRestUrlaub() {
    return GESAMT_URLAUBSZEIT_IN_MINUTEN - this.berechneBeantragtenUrlaub();
  }

  public void setzeUrlaube(Set<ZeitraumDto> urlaube) {
    Set<UrlaubZeitraum> urlaubeAusDatenbank = new HashSet<>();
    for (ZeitraumDto u : urlaube) {
      urlaubeAusDatenbank.add(new UrlaubZeitraum(u));
    }
    this.urlaube = urlaubeAusDatenbank;
  }

  public Set<KlausurReferenz> getKlausuren() {
    return Set.of(); // TODO: implement this
  }

  public void setzeKlausuren(Set<KlausurReferenz> klausurenReferenzen) {
    this.klausuren = klausurenReferenzen;
  }
}
