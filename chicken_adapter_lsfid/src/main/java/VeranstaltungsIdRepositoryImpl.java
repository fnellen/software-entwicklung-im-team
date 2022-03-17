import de.hhu.propra.chicken.repositories.VeranstaltungsIdRepository;

public class VeranstaltungsIdRepositoryImpl implements VeranstaltungsIdRepository {

  @Override
  public boolean webCheck(String veranstaltungsId) {
    return false;
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
