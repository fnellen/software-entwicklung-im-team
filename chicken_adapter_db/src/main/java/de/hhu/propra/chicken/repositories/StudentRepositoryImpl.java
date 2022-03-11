package de.hhu.propra.chicken.repositories;

import de.hhu.propra.chicken.aggregates.student.Student;
import de.hhu.propra.chicken.aggregates.student.StudentDto;
import de.hhu.propra.chicken.dao.StudentDao;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Repository;

@Repository
public class StudentRepositoryImpl implements StudentRepository {
  private final StudentDao studentDao;

  public StudentRepositoryImpl(StudentDao studentDao) {
    this.studentDao = studentDao;
  }

  @Override
  public Student findeStudentMitId(Long id) {
    return null;
  }

  @Override
  public Student findeStudentMitHandle(String githubHandle) {
    StudentDto studentDto = studentDao.findeStudentMitHandle(githubHandle)
        .orElseThrow(() -> new NoSuchElementException(githubHandle));
    return studentDto.konvertiereZuStudent();
  }


  @Override
  public void speicherStudent(Student student) {

  }


}
