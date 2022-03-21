package de.hhu.propra.chicken.web.configuration;

import de.hhu.propra.chicken.repositories.HeutigesDatumRepository;
import de.hhu.propra.chicken.repositories.KlausurRepository;
import de.hhu.propra.chicken.repositories.StudentRepository;
import de.hhu.propra.chicken.repositories.VeranstaltungsIdRepository;
import de.hhu.propra.chicken.services.ChickenService;
import de.hhu.propra.chicken.services.logging.LoggingRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfig {
  @Bean
  ChickenService chickenService(StudentRepository studentRepository,
                                KlausurRepository klausurRepository,
                                HeutigesDatumRepository heutigesDatumRepository,
                                VeranstaltungsIdRepository veranstaltungsIdRepository,
                                LoggingRepositoryImpl loggingRepositoryImpl) {

    return new ChickenService(studentRepository, klausurRepository,
        heutigesDatumRepository, veranstaltungsIdRepository, loggingRepositoryImpl);
  }

}
