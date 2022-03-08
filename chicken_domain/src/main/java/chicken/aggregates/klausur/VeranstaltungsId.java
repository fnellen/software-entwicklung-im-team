package chicken.aggregates.klausur;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;

public class VeranstaltungsId {

  private final Long veranstaltungsId;

  private VeranstaltungsId(Long veranstaltungsId) {
    this.veranstaltungsId = veranstaltungsId;
  }

  public Long getVeranstaltungsId() {
    return veranstaltungsId;
  }

  public static VeranstaltungsId erstelle(Long veranstaltungsId) {
    if (istEchteVeranstaltungsId(veranstaltungsId)) {
      return new VeranstaltungsId(veranstaltungsId);
    }
    return null;
  }

  private static boolean istEchteVeranstaltungsId(Long veranstaltungsId) {
    return (!webCheck(veranstaltungsId));
  }

  private static boolean webCheck(Long veranstaltungsId) {
    try (final WebClient webClient = new WebClient()){
      final HtmlPage page1 = webClient.getPage("https://lsf.hhu.de/qisserver/rds?state=verpublish&status=init&vmfile=no" +
          "&publishid="+ veranstaltungsId.toString() +
          "&moduleCall=webInfo&publishConfFile=webInfo&publishSubDir=veranstaltung");
      String htmlPageString = page1.getWebResponse().getContentAsString();
      return htmlPageString.contains("Veranstaltungsart");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

}
