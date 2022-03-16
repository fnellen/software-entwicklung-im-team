package de.hhu.propra.chicken.dao;

import de.hhu.propra.chicken.aggregates.student.StudentDto;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Component
public interface StudentDao extends CrudRepository<StudentDto, Long> {
  @Query("""
          SELECT * from student_dto WHERE githubhandle = :githubHandle
      """)
  Optional<StudentDto> findeStudentMitHandle(@Param("githubHandle") String githubHandle);

}
