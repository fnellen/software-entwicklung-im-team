package de.hhu.propra.chicken.services;

import java.time.LocalDate;
import org.springframework.stereotype.Component;

public interface HeutigesDatum {

  LocalDate getDatum();
}
