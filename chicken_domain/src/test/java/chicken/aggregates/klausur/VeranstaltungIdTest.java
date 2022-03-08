package chicken.aggregates.klausur;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class VeranstaltungIdTest {

  @Test
  @DisplayName("Existierende Veranstaltung-id gibt true zurück.")
  void test_1(){
    VeranstaltungsId erstelle = VeranstaltungsId.erstelle(217419L);

    assertThat(erstelle).isNotNull();
  }

  @Test
  @DisplayName("Nicht Existierende Veranstaltung-id gibt false zurück.")
  void test_2(){
    VeranstaltungsId erstelle = VeranstaltungsId.erstelle(257419L);

    assertThat(erstelle).isNull();
  }

  @Test
  @DisplayName("Veranstaltung-id mit nicht korrekter Länge gibt false zurück.")
  void test_3(){
    VeranstaltungsId erstelle = VeranstaltungsId.erstelle(0L);

    assertThat(erstelle).isNull();
  }
}
