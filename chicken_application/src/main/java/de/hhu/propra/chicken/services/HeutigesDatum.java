package de.hhu.propra.chicken.services;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface HeutigesDatum {

  LocalDate getDatum();

  LocalDateTime getDatumUndZeit();
}
