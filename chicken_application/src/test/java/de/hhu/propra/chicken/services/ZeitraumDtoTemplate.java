package de.hhu.propra.chicken.services;

import de.hhu.propra.chicken.aggregates.dto.ZeitraumDto;
import java.time.LocalDate;
import java.time.LocalTime;

public class ZeitraumDtoTemplate {


  static final ZeitraumDto ZEITRAUM_03_07_0930_1030 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 03, 7), LocalTime.of(9, 30),
      LocalTime.of(10, 30));

  static final ZeitraumDto ZEITRAUM_03_07_1230_1330 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 03, 7), LocalTime.of(12, 30),
      LocalTime.of(13, 30));

  static final ZeitraumDto ZEITRAUM_03_07_1130_1230 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 03, 7), LocalTime.of(11, 30),
      LocalTime.of(12, 30));

  static final ZeitraumDto ZEITRAUM_03_08_0930_1030 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 03, 8), LocalTime.of(9, 30),
      LocalTime.of(10, 30));

  static final ZeitraumDto ZEITRAUM_03_08_1130_1230 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 03, 8), LocalTime.of(11, 30),
      LocalTime.of(12, 30));

  static final ZeitraumDto ZEITRAUM_03_09_1130_1230 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 03, 9), LocalTime.of(11, 30),
      LocalTime.of(12, 30));

  static final ZeitraumDto ZEITRAUM_03_14_1130_1230 = ZeitraumDto.erstelleZeitraum(
      LocalDate.of(2022, 03, 14), LocalTime.of(11, 30),
      LocalTime.of(12, 30));
}
