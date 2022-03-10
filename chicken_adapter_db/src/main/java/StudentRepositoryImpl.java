import chicken.aggregates.dto.ZeitraumDto;
import chicken.aggregates.student.Student;
import dao.StudentDao;
import dto.StudentDto;
import dto.UrlaubZeitraumDto;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
    return konvertiereZuStudent(studentDto);
  }

  private Student konvertiereZuStudent(StudentDto studentDto) {
    Student student = new Student(studentDto.id(), studentDto.githubhandle());
    Set<UrlaubZeitraumDto> urlaubZeitraumDtos = studentDto.urlaube();
    Set<ZeitraumDto> zeitraumDtos =
        urlaubZeitraumDtos.stream().map(UrlaubZeitraumDto::konvertiereZuZeitraumDto)
            .collect(Collectors.toSet());
    student.setzeUrlaube(zeitraumDtos);
    //student.setzeKlausuren(studentDto.klausuren());
    return student;
  }

  @Override
  public void speicherStudent(Student student) {

  }
}
