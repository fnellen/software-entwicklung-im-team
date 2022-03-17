package de.hhu.propra.chicken.services;

import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class HeutigesDatumImpl implements HeutigesDatum {
  @Override
  public LocalDate getDatum() {
    return LocalDate.now();
  }
}
