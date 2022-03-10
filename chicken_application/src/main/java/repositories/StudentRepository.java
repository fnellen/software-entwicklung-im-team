package repositories;

import chicken.aggregates.student.Student;

public interface StudentRepository {

  Student findeStudentMitId(Long id);

  Student findeStudentMitHandle(String githubHandle);

  void speicherStudent(Student student);

}
