package de.hhu.propra.chicken.domain.aggregates.student;

import de.hhu.propra.chicken.domain.aggregates.klausur.VeranstaltungsIdDto;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.annotation.Id;

public record StudentDto(@Id Long id,
                         String githubhandle,
                         Set<UrlaubZeitraumDto> urlaube,
                         Set<KlausurReferenzDto> klausuren) {

  public StudentDto(Long id, String githubhandle, Set<UrlaubZeitraumDto> urlaube,
                    Set<KlausurReferenzDto> klausuren) {
    this.id = id;
    this.githubhandle = githubhandle;
    this.urlaube = Set.copyOf(urlaube);
    this.klausuren = Set.copyOf(klausuren);
  }

  public static StudentDto konvertiereZuStudentDto(Student student) {
    Set<KlausurReferenzDto> klausurReferenzDtos = student.getKlausuren().stream()
        .map(e -> new KlausurReferenzDto(new VeranstaltungsIdDto(e.id())))
        .collect(Collectors.toSet());
    return new StudentDto(student.getId(), student.getGithubHandle(),
        student.getUrlaube().stream().map(e -> new UrlaubZeitraumDto(e.getDatum(),
            e.getStartUhrzeit(), e.getEndUhrzeit())).collect(Collectors.toSet()),
        klausurReferenzDtos);
  }


}
