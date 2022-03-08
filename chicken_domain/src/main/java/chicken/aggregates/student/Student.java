package chicken.aggregates.student;

import chicken.aggregates.klausur.Klausur;
import chicken.aggregates.klausur.KlausurReferenz;
import chicken.aggregates.utilities.Zeitraum;
import chicken.stereotypes.AggregateRoot;
import java.util.Set;

@AggregateRoot
public class Student {

  private final Long id;
  private final String githubHandle;
  private Set<Zeitraum> urlaube;
  private Set<KlausurReferenz> klausuren;

  public Student(Long id, String githubHandle) {
    this.id = id;
    this.githubHandle = githubHandle;
  }

  public void fuegeUrlaubHinzufuegen(Zeitraum urlaubsZeitraum){
    urlaube.add(urlaubsZeitraum);
  }

  public void fuegeKlausurHinzufuegen(Klausur klausur){
    klausuren.add(new KlausurReferenz(klausur.id()));
  }

}
