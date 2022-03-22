package de.hhu.propra.chicken.web.controller;

import static de.hhu.propra.chicken.web.StudentTemplate.DENNIS;
import static de.hhu.propra.chicken.web.StudentTemplate.FEDERICO;
import static de.hhu.propra.chicken.web.StudentTemplate.KL_PROPRA_03_09_1130_1230;
import static de.hhu.propra.chicken.web.StudentTemplate.ZEITRAUM_03_10_1030_1300;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.hhu.propra.chicken.services.ChickenService;
import de.hhu.propra.chicken.services.dto.StudentDetailsDto;
import de.hhu.propra.chicken.services.fehler.StudentNichtGefundenException;
import de.hhu.propra.chicken.web.StudentTemplate;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
public class StudentControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  ChickenService chickenService;

  StudentTemplate template = new StudentTemplate();

  @Test
  @DisplayName("Startseite aufrufen und Klausur wird richtig angezeigt")
  void test_01() throws Exception {
    when(chickenService.holeStudent(DENNIS.getGithubHandle())).thenReturn(DENNIS);
    when(chickenService.studentDetails(DENNIS.getGithubHandle())).thenReturn(
        template.getDennisDetails());
    MockHttpServletRequestBuilder getRequest =
        get("/").flashAttr("handle", DENNIS.getGithubHandle());

    MvcResult mvcResult = mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(model().attribute("details", template.getDennisDetails()))
        .andExpect(model().attribute("fehler", "")).andReturn();

    assertThat(mvcResult.getResponse().getContentAsString()).contains("Propra2");
  }

  @Test
  @DisplayName("Startseite aufrufen und Urlaub wird richtig angezeigt")
  void test_02() throws Exception {
    DENNIS.fuegeUrlaubHinzu(ZEITRAUM_03_10_1030_1300);
    when(chickenService.holeStudent(DENNIS.getGithubHandle())).thenReturn(DENNIS);
    when(chickenService.studentDetails(DENNIS.getGithubHandle())).thenReturn(
        new StudentDetailsDto(DENNIS, Set.of(KL_PROPRA_03_09_1130_1230)));
    MockHttpServletRequestBuilder getRequest =
        get("/").flashAttr("handle", DENNIS.getGithubHandle());

    MvcResult result = mockMvc.perform(getRequest).andReturn();

    String html = result.getResponse().getContentAsString();

    assertThat(html).contains("2022-03-10");
    assertThat(html).contains("10:30");
    assertThat(html).contains("13:00");
  }

  @Test
  @DisplayName("Student noch nicht in Datenbank. Student wird in Datenbank angelegt.")
  void test_03() throws Exception {
    when(chickenService.holeStudent(FEDERICO.getGithubHandle()))
        .thenThrow(StudentNichtGefundenException.class)
        .thenReturn(FEDERICO);
    when(chickenService.studentDetails(FEDERICO.getGithubHandle())).thenReturn(
        template.getFedericoDetails());

    MockHttpServletRequestBuilder getRequest =
        get("/").flashAttr("handle", FEDERICO.getGithubHandle());

    mockMvc.perform(getRequest).andExpect(status().isOk())
        .andReturn();

    verify(chickenService).holeStudent(FEDERICO.getGithubHandle());
    verify(chickenService).studentSpeichern(FEDERICO);
    verify(chickenService).studentDetails(FEDERICO.getGithubHandle());
  }

  @Test
  @DisplayName("Urlaub wird storniert")
  void test_04() throws Exception {
    FEDERICO.fuegeUrlaubHinzu(ZEITRAUM_03_10_1030_1300);
    when(chickenService.holeStudent(FEDERICO.getGithubHandle()))
        .thenThrow(StudentNichtGefundenException.class)
        .thenReturn(FEDERICO);
    when(chickenService.studentDetails(FEDERICO.getGithubHandle())).thenReturn(
        template.getFedericoDetails());

    MockHttpServletRequestBuilder getRequest =
        post("/urlaubstornieren").flashAttr("handle", FEDERICO.getGithubHandle());

    mockMvc.perform(getRequest).andExpect(status().isOk())
        .andReturn();

    verify(chickenService).holeStudent(FEDERICO.getGithubHandle());
    verify(chickenService).studentSpeichern(FEDERICO);
    verify(chickenService).studentDetails(FEDERICO.getGithubHandle());
  }
}
