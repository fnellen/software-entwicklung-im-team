package de.hhu.propra.chicken.services;

import java.time.LocalDate;

public interface HeutigesDatum {

  default LocalDate getDatum() {
    return LocalDate.now();
  }

}
