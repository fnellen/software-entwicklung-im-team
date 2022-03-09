package chicken.aggregates.student;

import chicken.aggregates.klausur.Klausur;
import chicken.aggregates.klausur.KlausurReferenz;
import chicken.stereotypes.AggregateRoot;
import java.util.HashSet;
import java.util.Set;

@AggregateRoot
public class Student {

  private final Long id;
  private final String githubHandle;
  private Set<UrlaubZeitraum> urlaube = new HashSet<>();
  private Set<KlausurReferenz> klausuren;

  private static final long GESAMT_URLAUBSZEIT_IN_MINUTEN = 240L;

  public Student(Long id, String githubHandle) {
    this.id = id;
    this.githubHandle = githubHandle;
  }


  public void fuegeUrlaubHinzufuegen(UrlaubZeitraum urlaubsZeitraum) {
    if (urlaubsZeitraum.dauerInMinuten() + this.berechneRestUrlaub() <=
        GESAMT_URLAUBSZEIT_IN_MINUTEN) {
      urlaube.add(urlaubsZeitraum);
    }
  }

  public void fuegeKlausurHinzufuegen(Klausur klausur) {
    klausuren.add(new KlausurReferenz(klausur.id()));
  }

  public long berechneRestUrlaub() {
    long urlaub = urlaube.stream().mapToLong(UrlaubZeitraum::dauerInMinuten).sum();
    return GESAMT_URLAUBSZEIT_IN_MINUTEN - urlaub;
  }


}
