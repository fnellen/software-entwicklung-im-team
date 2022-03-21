package de.hhu.propra.chicken.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class HeutigesDatumImpl implements HeutigesDatum {
  @Override
  public LocalDate getDatum() {
    return LocalDate.now();
  }

  @Override
  public LocalDateTime getDatumUndZeit() {
    return LocalDateTime.now();
  }
}
