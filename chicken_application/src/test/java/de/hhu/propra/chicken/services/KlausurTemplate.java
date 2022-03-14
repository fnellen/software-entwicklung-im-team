package de.hhu.propra.chicken.services;

import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_07_0930_1030;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_08_0930_1030;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_1130_1230;

import de.hhu.propra.chicken.aggregates.klausur.Klausur;

public class KlausurTemplate {

  static final Klausur klausur1 = new Klausur("215783", "Propra2", ZEITRAUM_03_09_1130_1230, true);
  static final Klausur klausur2 = new Klausur("214613", "Stochastik", ZEITRAUM_03_08_0930_1030,
      true);
  static final Klausur klausur3 = new Klausur("211234", "Rechnernetze", ZEITRAUM_03_07_0930_1030,
      true);

}
