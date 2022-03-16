package de.hhu.propra.chicken.repositories;

import de.hhu.propra.chicken.dao.StudentDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestKonfiguration {
  @Bean
  public StudentRepositoryImpl todoFacade(StudentDao service) {
    return new StudentRepositoryImpl(service);
  }


}
