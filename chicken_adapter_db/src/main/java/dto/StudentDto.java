package dto;

import chicken.aggregates.student.KlausurReferenz;
import java.util.Set;
import org.springframework.data.annotation.Id;

public record StudentDto(@Id Long id,
                         String githubhandle,
                         Set<UrlaubZeitraumDto> urlaube,
                         Set<KlausurReferenzDto> klausuren) {
}
