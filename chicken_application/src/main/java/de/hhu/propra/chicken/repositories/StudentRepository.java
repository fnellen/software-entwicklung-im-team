package de.hhu.propra.chicken.repositories;

import de.hhu.propra.chicken.aggregates.student.Student;

public interface StudentRepository {

  Student findeStudentMitId(Long id);

  Student findeStudentMitHandle(String githubHandle);

  void speicherStudent(Student student);

}
