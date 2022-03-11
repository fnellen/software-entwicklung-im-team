package de.hhu.propra.chicken.services;

import de.hhu.propra.chicken.aggregates.student.Student;
import de.hhu.propra.chicken.repositories.KlausurRepository;
import de.hhu.propra.chicken.repositories.StudentRepository;
import de.hhu.propra.chicken.services.fehler.StudentNichtGefundenException;

public class ApplicationService {
  private final StudentRepository studentRepository;
  private final KlausurRepository klausurRepository;


  public ApplicationService(StudentRepository studentRepository,
                            KlausurRepository klausurRepository) {
    this.studentRepository = studentRepository;
    this.klausurRepository = klausurRepository;
  }

  public Student holeStudent(String githubHandle) throws StudentNichtGefundenException {
    Student student = studentRepository.findeStudentMitHandle(githubHandle);
    if (student == null) {
      throw new StudentNichtGefundenException(githubHandle);
    }
    return student;
  }


}
