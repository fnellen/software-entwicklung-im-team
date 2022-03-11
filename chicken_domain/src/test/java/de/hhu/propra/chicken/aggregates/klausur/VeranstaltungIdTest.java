package de.hhu.propra.chicken.aggregates.klausur;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class VeranstaltungIdTest {

  /*WebResponse webResponseMock = mock(WebResponse.class);
  WebClient webClientMock = mock(WebClient.class);
  HtmlPage htmlPageMock = mock(HtmlPage.class);

  @Test
  @DisplayName("Existierende Veranstaltung-id gibt true zurück.")
  void test_1() throws IOException {
    when(webResponseMock.getContentAsString()).thenReturn("Veranstaltungsart");
    when(htmlPageMock.getWebResponse()).thenReturn(webResponseMock);
    when(webClientMock.getPage(anyString())).thenReturn(htmlPageMock);
    VeranstaltungsId erstelle = VeranstaltungsId.erstelle("217419");

    assertThat(erstelle).isNotNull();
  }

  @Test
  @DisplayName("Nicht Existierende Veranstaltung-id gibt false zurück.")
  void test_2() throws IOException {
    when(webResponseMock.getContentAsString()).thenReturn("");
    when(htmlPageMock.getWebResponse()).thenReturn(webResponseMock);
    when(webClientMock.getPage(anyString())).thenReturn(htmlPageMock);
    VeranstaltungsId erstelle = VeranstaltungsId.erstelle("257419");

    assertThat(erstelle).isNull();
  }

  @Test
  @DisplayName("Veranstaltung-id mit nicht korrekter Länge gibt false zurück.")
  void test_3() throws IOException {
    when(webResponseMock.getContentAsString()).thenReturn("");
    when(htmlPageMock.getWebResponse()).thenReturn(webResponseMock);
    when(webClientMock.getPage(anyString())).thenReturn(htmlPageMock);
    VeranstaltungsId erstelle = VeranstaltungsId.erstelle("0");

    assertThat(erstelle).isNull();
  }*/
}
