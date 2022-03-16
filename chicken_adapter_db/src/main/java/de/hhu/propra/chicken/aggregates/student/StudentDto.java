package de.hhu.propra.chicken.aggregates.student;

import de.hhu.propra.chicken.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.aggregates.klausur.VeranstaltungsIdDto;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("student")
public record StudentDto(@Id Long id,
                         String githubhandle,
                         Set<ZeitraumDto> urlaube,
                         Set<KlausurReferenzDto> klausuren) {

  public Student konvertiereZuStudent() {
    Student student = new Student(this.id(), this.githubhandle());
    Set<ZeitraumDto> zeitraumDtos = this.urlaube();
    Set<KlausurReferenzDto> klausuren = this.klausuren();
    Set<KlausurReferenz> klausurenReferenzen =
        klausuren.stream().map(KlausurReferenzDto::konvertiereZuKlausurReferenz)
            .collect(Collectors.toSet());
    student.setzeUrlaube(zeitraumDtos);
    student.setzeKlausuren(klausurenReferenzen);
    return student;
  }

  public static StudentDto konvertiereZuStudentDto(Student student) {
    Set<KlausurReferenzDto> klausurReferenzDtos = student.getKlausuren().stream()
        .map(e -> new KlausurReferenzDto(new VeranstaltungsIdDto(e.id())))
        .collect(Collectors.toSet());
    return new StudentDto(student.getId(), student.getGithubHandle(), student.getUrlaube(),
        klausurReferenzDtos);
  }


}
