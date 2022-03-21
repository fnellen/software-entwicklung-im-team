package de.hhu.propra.chicken.aggregates.fehler;

public class ZeitraumDtoException extends RuntimeException {
  public ZeitraumDtoException(String fehler) {
    super(fehler);
  }
}