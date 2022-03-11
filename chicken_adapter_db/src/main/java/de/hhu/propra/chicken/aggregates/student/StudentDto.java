package de.hhu.propra.chicken.aggregates.student;

import de.hhu.propra.chicken.aggregates.dto.ZeitraumDto;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.annotation.Id;

public record StudentDto(@Id Long id,
                         String githubhandle,
                         Set<UrlaubZeitraumDto> urlaube,
                         Set<KlausurReferenzDto> klausuren) {

  public Student konvertiereZuStudent() {
    Student student = new Student(this.id(), this.githubhandle());
    Set<UrlaubZeitraumDto> urlaubZeitraumDtos = this.urlaube();
    Set<KlausurReferenzDto> klausuren = this.klausuren();

    Set<ZeitraumDto> zeitraumDtos = urlaubZeitraumDtos.stream()
        .map(UrlaubZeitraumDto::konvertiereZuZeitraumDto)
        .collect(Collectors.toSet());
    Set<KlausurReferenz> klausurenReferenzen =
        klausuren.stream().map(KlausurReferenzDto::konvertiereZuKlausurReferenz)
            .collect(Collectors.toSet());
    student.setzeUrlaube(zeitraumDtos);
    student.setzeKlausuren(klausurenReferenzen);
    return student;
  }
}
