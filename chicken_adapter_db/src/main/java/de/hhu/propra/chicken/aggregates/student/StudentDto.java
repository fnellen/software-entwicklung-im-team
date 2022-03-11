package de.hhu.propra.chicken.aggregates.student;

import de.hhu.propra.chicken.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.aggregates.klausur.VeranstaltungsIdDto;
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

  public static StudentDto konvertiereZuStudentDto(Student student) {
    Set<UrlaubZeitraumDto> urlaubZeitraumDtos = student.getUrlaube().stream()
        .map(e -> new UrlaubZeitraumDto(e.getDatum(), e.getStartUhrzeit(), e.getEndUhrzeit()))
        .collect(Collectors.toSet());
    Set<KlausurReferenzDto> klausurReferenzDtos = student.getKlausuren().stream()
        .map(e -> new KlausurReferenzDto(new VeranstaltungsIdDto(e.id())))
        .collect(Collectors.toSet());
    return new StudentDto(student.getId(), student.getGithubHandle(), urlaubZeitraumDtos,
        klausurReferenzDtos);
  }


}
