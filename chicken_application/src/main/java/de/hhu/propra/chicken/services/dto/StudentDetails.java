package de.hhu.propra.chicken.services.dto;

import de.hhu.propra.chicken.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.aggregates.klausur.Klausur;
import de.hhu.propra.chicken.aggregates.student.Student;
import java.util.Set;

public record StudentDetails(Student student,
                             Set<Klausur> klausuren) {

  public static StudentDetails von(Student student, Set<Klausur> klausuren) {

    return new StudentDetails(student, klausuren);
  }


}
