package de.hhu.propra.chicken.web.controller;

import static de.hhu.propra.chicken.web.StudentTemplate.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.hhu.propra.chicken.services.ChickenService;
import de.hhu.propra.chicken.web.dto.UrlaubDto;
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

  @Test
  @DisplayName("Startseite aufrufen und Klausur wird richtig angezeigt")
  void test_01() throws Exception {
    when(chickenService.holeStudent(DENNIS.getGithubHandle())).thenReturn(DENNIS);
    when(chickenService.studentDetails(DENNIS.getGithubHandle())).thenReturn(DENNIS_DETAILS);
    MockHttpServletRequestBuilder getRequest = get("/")
        .flashAttr("handle", DENNIS.getGithubHandle());

    MvcResult mvcResult = mockMvc.perform(getRequest)
        .andExpect(status().isOk())
        .andExpect(model().attribute("details", DENNIS_DETAILS))
        .andExpect(model().attribute("urlaubDto", new UrlaubDto(null, null, null)))
        .andExpect(model().attribute("fehler", ""))
        .andReturn();

    assertThat(mvcResult.getResponse().getContentAsString().contains("Propra2"));
  }

  @Test
  @DisplayName("Startseite aufrufen und Urlaub wird richtig angezeigt")
  void test_02() throws Exception {
    when(chickenService.holeStudent(DENNIS.getGithubHandle())).thenReturn(DENNIS);
    DENNIS.fuegeUrlaubHinzu(ZEITRAUM_03_10_1030_1300);
    when(chickenService.studentDetails(DENNIS.getGithubHandle())).thenReturn(DENNIS_DETAILS);
    MockHttpServletRequestBuilder getRequest = get("/")
        .flashAttr("handle", DENNIS.getGithubHandle());

    MvcResult mvcResult = mockMvc.perform(getRequest)
        .andExpect(status().isOk())
        .andExpect(model().attribute("details", DENNIS_DETAILS))
        .andExpect(model().attribute("urlaubDto", new UrlaubDto(null, null, null)))
        .andExpect(model().attribute("fehler", ""))
        .andReturn();

    assertThat(mvcResult.getResponse().getContentAsString().contains("10.03.2022"));
    assertThat(mvcResult.getResponse().getContentAsString().contains("10:30"));
    assertThat(mvcResult.getResponse().getContentAsString().contains("13:00"));
  }
}
