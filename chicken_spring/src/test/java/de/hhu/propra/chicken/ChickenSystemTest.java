package de.hhu.propra.chicken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.javascript.SilentJavaScriptErrorListener;
import de.hhu.propra.chicken.repositories.HeutigesDatumRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;

@SpringBootTest()
@ActiveProfiles("test")
@AutoConfigureMockMvc()
public class ChickenSystemTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  HeutigesDatumRepository heutigesDatumRepository;

  WebClient webClient;

  @BeforeEach
  void setupWebClient() {
    when(heutigesDatumRepository.getDatum()).thenReturn(LocalDate.of(2022, 3, 8));
    webClient = MockMvcWebClientBuilder
        .mockMvcSetup(mvc)
        .build();
    webClient.getOptions().setThrowExceptionOnScriptError(false);
    webClient.setJavaScriptErrorListener(new SilentJavaScriptErrorListener());
  }

  @Test
  @DisplayName("Nachdem eine Bestellung verschickt wurde, ist sie nicht mehr in der Übersicht")
  @WithMockUser(username = "fnellen", roles = "STUDENT")
  void test_1() throws Exception {

    HtmlPage page = webClient.getPage("http://localhost:8080/");
    HtmlAnchor urlaubBelegenNavButton = page.getElementByName("urlaub-belegen-a");
    page = urlaubBelegenNavButton.click();
    HtmlForm urlaubBelegenForm = page.getFormByName("urlaub_anmelden");
    HtmlInput urlaubsDatum = urlaubBelegenForm.getInputByName("urlaubsDatum");
    urlaubsDatum.setValueAttribute("2022-03-10");
    HtmlInput urlaubsStart = urlaubBelegenForm.getInputByName("urlaubsStart");
    urlaubsStart.setValueAttribute("10:00");
    HtmlInput urlaubsEnde = urlaubBelegenForm.getInputByName("urlaubsEnde");
    urlaubsEnde.setValueAttribute("11:00");

    Iterable<DomElement> childElements = urlaubBelegenForm.getChildElements();
    childElements.forEach(System.out::println);

    HtmlSubmitInput button = urlaubBelegenForm.getInputByName("abschicken");

    //urlaubBelegenForm.submit(button);

    page = button.click();
    System.out.println(page);
    HtmlTable urlaubsTabelle = page.getElementByName("urlaub_table");
    HtmlTableCell urlaubsDatumZelle = urlaubsTabelle.getCellAt(1, 0);
    System.out.println(urlaubsDatumZelle);
    assertThat(urlaubsDatumZelle.getTextContent()).contains("2022-03-10");

//    HtmlForm versandmethodenwahl = page.getFormByName("urlaubStornieren");
//    HtmlInput direktfahrt = versandmethodenwahl.getInputByValue("DIREKTFAHRT");
//    page = (HtmlPage) direktfahrt.setChecked(true);
  }


}
