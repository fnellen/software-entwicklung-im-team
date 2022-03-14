package de.hhu.propra.chicken.aggregates.klausur;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;

/**
 * Darstellung einer gültigen VeranstaltungsId aus dem LSF.
 */
class VeranstaltungsId {

  private final String veranstaltungsId;

  private VeranstaltungsId(String veranstaltungsId) {
    this.veranstaltungsId = veranstaltungsId;
  }


  public String getVeranstaltungsId() {
    return veranstaltungsId;
  }

  public static VeranstaltungsId erstelle(String veranstaltungsId) {
    return new VeranstaltungsId(veranstaltungsId);
  }

  /*private static boolean webCheck(String veranstaltungsId) {
    try (WebClient webClient = new WebClient()) {
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
  }*/

}
