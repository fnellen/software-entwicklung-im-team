package de.hhu.propra.chicken.web.configuration;

import de.hhu.propra.chicken.repositories.KlausurRepository;
import de.hhu.propra.chicken.repositories.StudentRepository;
import de.hhu.propra.chicken.repositories.VeranstaltungsIdRepository;
import de.hhu.propra.chicken.services.ChickenService;
import de.hhu.propra.chicken.services.HeutigesDatum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfig {
  @Bean
  ChickenService chickenService(StudentRepository studentRepository,
                                KlausurRepository klausurRepository,
                                HeutigesDatum heutigesDatum,
                                VeranstaltungsIdRepository veranstaltungsIdRepository) {
    
    return new ChickenService(studentRepository, klausurRepository,
        heutigesDatum, veranstaltungsIdRepository);
  }

}
