package dao;

import dto.StudentDto;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface StudentDao extends CrudRepository<StudentDto, Long> {
  @Query("""
          SELECT * from student_dto WHERE handle = :githubHandle
      """)
  Optional<StudentDto> findeStudentMitHandle(@Param("githubHandle") String githubHandle);

}
