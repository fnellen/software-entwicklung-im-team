package de.hhu.propra.chicken.repository;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import de.hhu.propra.chicken.repositories.VeranstaltungsIdRepository;
import java.io.IOException;

public class VeranstaltungsIdRepositoryImpl implements VeranstaltungsIdRepository {

  private final WebPageContentProvider provider;

  public VeranstaltungsIdRepositoryImpl(WebPageContentProvider provider) {
    this.provider = provider;
  }


  @Override
  public boolean webCheck(String veranstaltungsId) {
    String webPageContent = provider.getWebPageContent(veranstaltungsId);
    return webPageContent.contains("Veranstaltungsart");
  }

}
