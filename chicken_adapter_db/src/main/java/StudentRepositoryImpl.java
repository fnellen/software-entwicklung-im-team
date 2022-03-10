import chicken.aggregates.dto.ZeitraumDto;
import chicken.aggregates.student.KlausurReferenz;
import chicken.aggregates.student.Student;
import dao.KlausurDao;
import dao.StudentDao;
import dto.KlausurReferenzDto;
import dto.StudentDto;
import dto.UrlaubZeitraumDto;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import repositories.StudentRepository;

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
