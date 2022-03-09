package chicken.aggregates.klausur;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;

/**
 * Darstellung einer gültigen VeranstaltungsId aus dem LSF.
 */
public class VeranstaltungsId {

  private final WebClient webClient;
  private final Long veranstaltungsId;

  private VeranstaltungsId(Long veranstaltungsId, WebClient webClient) {
    this.webClient = webClient;
    this.veranstaltungsId = veranstaltungsId;
  }

  /**
   * Getter für die veranstaltungsId.
   *
   * @return gibt eine gültige Veranstaltungs Id als Long zurück
   */
  public Long getVeranstaltungsId() {
    return veranstaltungsId;
  }

  /**
   * Statische Factory Methode zum erstellen einer Veranstaltungs ID.
   *
   * @param veranstaltungsId Id der Veranstaltung.
   * @return Gibt null zurück, wenn die Id der Veranstaltung nicht valide ist, ansonsten wird ein
   * VeranstaltungsId Objekt zurück gegeben.
   */
  public static VeranstaltungsId erstelle(Long veranstaltungsId, WebClient webClient) {
    if (istEchteVeranstaltungsId(veranstaltungsId, webClient)) {
      return new VeranstaltungsId(veranstaltungsId, webClient);
    }
    return null;
  }

  private static boolean istEchteVeranstaltungsId(Long veranstaltungsId, WebClient webClient) {
    return (webCheck(veranstaltungsId, webClient));
  }

  private static boolean webCheck(Long veranstaltungsId, WebClient webClient) {
    try (webClient) {
      final HtmlPage page1 = webClient.getPage(
          "https://lsf.hhu.de/qisserver/rds?state=verpublish&status=init&vmfile=no"
              + "&publishid=" + veranstaltungsId.toString()
              + "&moduleCall=webInfo&publishConfFile=webInfo&publishSubDir=veranstaltung");
      String htmlPageString = page1.getWebResponse().getContentAsString();
      return htmlPageString.contains("Veranstaltungsart");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

}
