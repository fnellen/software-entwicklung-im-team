package de.hhu.propra.chicken.domain.aggregates.student;

import de.hhu.propra.chicken.dao.StudentDao;
import de.hhu.propra.chicken.domain.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.repositories.StudentRepository;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

  private final StudentDao studentDao;
  private final LocalDate praktikumsstart;
  private final LocalDate praktikumsende;

  public StudentRepositoryImpl(StudentDao studentDao,
                               @Value("${praktikumszeitraum.praktikumsstart}")
                                   String praktikumsstart,
                               @Value("${praktikumszeitraum.praktikumsende}")
                                   String praktikumsende) {
    this.studentDao = studentDao;
    this.praktikumsstart = LocalDate.parse(praktikumsstart);
    this.praktikumsende = LocalDate.parse(praktikumsende);
  }

  private Student konvertiereZuStudent(StudentDto studentDto) {
    Student student = new Student(studentDto.id(), studentDto.githubhandle());
    Set<ZeitraumDto> zeitraumDtos =
        studentDto.urlaube().stream().map(e -> ZeitraumDto.erstelleZeitraum(e.datum(),
                e.startUhrzeit(), e.endUhrzeit(), praktikumsstart, praktikumsende))
            .collect(Collectors.toSet());
    Set<KlausurReferenzDto> klausuren = studentDto.klausuren();
    Set<KlausurReferenz> klausurenReferenzen =
        klausuren.stream().map(KlausurReferenzDto::konvertiereZuKlausurReferenz)
            .collect(Collectors.toSet());
    student.setzeUrlaube(zeitraumDtos);
    student.setzeKlausuren(klausurenReferenzen);
    return student;
  }

  @Override
  public Student findeStudentMitHandle(String githubHandle) {
    StudentDto studentDto = studentDao.findeStudentMitHandle(githubHandle)
        .orElseThrow(() -> new NoSuchElementException("Student mit " + githubHandle + " konnte "
            + "nicht gefunden werden!"));
    return konvertiereZuStudent(studentDto);
  }


  @Override
  public void speicherStudent(Student student) {
    StudentDto studentDto = StudentDto.konvertiereZuStudentDto(student);
    StudentDto gespeichertesDto = studentDao.save(studentDto);
    student.setId(gespeichertesDto.id());
  }


}
