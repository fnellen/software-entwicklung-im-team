package de.hhu.propra.chicken.repositories;

import de.hhu.propra.chicken.dao.StudentDao;
import de.hhu.propra.chicken.domain.aggregates.student.Student;
import de.hhu.propra.chicken.domain.aggregates.student.StudentDto;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Repository;

@Repository
public class StudentRepositoryImpl implements StudentRepository {
  private final StudentDao studentDao;

  public StudentRepositoryImpl(StudentDao studentDao) {
    this.studentDao = studentDao;
  }


  @Override
  public Student findeStudentMitHandle(String githubHandle) {
    StudentDto studentDto = studentDao.findeStudentMitHandle(githubHandle)
        .orElseThrow(() -> new NoSuchElementException("Student mit " + githubHandle + " konnte "
            + "nicht gefunden werden!"));
    return studentDto.konvertiereZuStudent();
  }


  @Override
  public void speicherStudent(Student student) {
    StudentDto studentDto = StudentDto.konvertiereZuStudentDto(student);
    StudentDto gespeichertesDto = studentDao.save(studentDto);
    student.setId(gespeichertesDto.id());
  }


}
