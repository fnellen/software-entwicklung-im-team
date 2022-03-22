package de.hhu.propra.chicken.services;

import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_07_0930_1030;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_08_0930_1030;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_1000_1100;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_1000_1145;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_09_1130_1230;
import static de.hhu.propra.chicken.services.ZeitraumDtoTemplate.ZEITRAUM_03_24_1000_1300;

import de.hhu.propra.chicken.domain.aggregates.klausur.Klausur;

public class KlausurTemplate {

  static final Klausur KL_PROPRA_03_09_1130_1230 =
      new Klausur(null, "215783", "Propra2", ZEITRAUM_03_09_1130_1230, true);

  static final Klausur KL_03_09_1000_1100 =
      new Klausur(null, "Random", "Random", ZEITRAUM_03_09_1000_1100, true);

  static final Klausur KL_03_09_1000_1145 =
      new Klausur(null, "Random", "Random", ZEITRAUM_03_09_1000_1145, true);

  static final Klausur KL_STOCHASTIK_03_08_0930_1030 =
      new Klausur(null, "214613", "Stochastik", ZEITRAUM_03_08_0930_1030, true);

  static final Klausur KL_RECHNERNETZTE_03_07_0930_1030
      = new Klausur(null, "211234", "Rechnernetze", ZEITRAUM_03_07_0930_1030, true);

  static final Klausur KL_RANDOM10_03_24_1000_1300
      = new Klausur(null, "222222", "Random24", ZEITRAUM_03_24_1000_1300, true);
  static final Klausur KL_RANDOM11_03_24_1300_1330
      = new Klausur(null, "222222", "Random24", ZeitraumDtoTemplate.ZEITRAUM_03_24_1300_1330, true);
}
