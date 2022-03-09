package chicken.aggregates.student;

import chicken.aggregates.dto.ZeitraumDto;
import java.time.LocalDate;
import java.time.LocalTime;

class UrlaubZeitraum {
  private LocalDate datum;
  private LocalTime startUhrzeit;
  private LocalTime endUhrzeit;

  public long dauerInMinuten() {
    return konvertiereInZeitraum().dauerInMinuten();
  }

  private ZeitraumDto konvertiereInZeitraum(){
    return ZeitraumDto.erstelleZeitraum(datum, startUhrzeit, endUhrzeit);
  }

  public UrlaubZeitraum(ZeitraumDto zeitraumDto){
    this.datum = zeitraumDto.getDatum();
    this.startUhrzeit = zeitraumDto.getStartUhrzeit();
    this.endUhrzeit = zeitraumDto.getEndUhrzeit();
  }
}
