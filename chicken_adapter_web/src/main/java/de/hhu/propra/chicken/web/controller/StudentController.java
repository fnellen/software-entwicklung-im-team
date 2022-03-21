package de.hhu.propra.chicken.web.controller;

import de.hhu.propra.chicken.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.aggregates.fehler.ZeitraumDtoException;
import de.hhu.propra.chicken.aggregates.klausur.Klausur;
import de.hhu.propra.chicken.aggregates.student.Student;
import de.hhu.propra.chicken.services.ChickenService;
import de.hhu.propra.chicken.services.dto.StudentDetails;
import de.hhu.propra.chicken.services.fehler.KlausurException;
import de.hhu.propra.chicken.services.fehler.UrlaubException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StudentController {

  private final ChickenService service;

  public StudentController(ChickenService service) {
    this.service = service;
  }

  @ModelAttribute("handle")
  String handle(Principal user) {
    return user.getName();
  }

  @GetMapping("/")
  public String index(Model model, @ModelAttribute("handle") String handle) {
    try {
      service.holeStudent(handle);
    } catch (Exception e) {
      Student student = new Student(null, handle);
      service.studentSpeichern(student);
    }
    StudentDetails studentDetails = service.studentDetails(handle);
    model.addAttribute("details", studentDetails);
    model.addAttribute("fehler", "");
    return "index";
  }

  @PostMapping("/urlaubstornieren")
  public String urlaubStornieren(Model model,
                                 @ModelAttribute("handle") String handle,
                                 String urlaubdatum,
                                 String urlaubstart,
                                 String urlaubende) {

    LocalDate datum = LocalDate.parse(urlaubdatum);
    LocalTime start = LocalTime.parse(urlaubstart);
    LocalTime ende = LocalTime.parse(urlaubende);
    ZeitraumDto urlaub = ZeitraumDto.erstelleZeitraum(datum, start, ende);
    try {
      service.storniereUrlaub(handle, urlaub);
    } catch (Exception e) {
      StudentDetails studentDetails = service.studentDetails(handle);
      model.addAttribute("details", studentDetails);
      model.addAttribute("fehler", e.getMessage());
      return "index";
    }
    return "redirect:/";
  }

  @PostMapping("/klausurstornieren")
  public String klausurStornieren(Model model,
                                  @ModelAttribute("handle") String handle,
                                  String veranstaltungsId) {
    System.out.println(veranstaltungsId);
    Klausur klausur = service.holeKlausur(veranstaltungsId);
    try {
      service.storniereKlausur(handle, klausur);
    } catch (KlausurException e) {
      StudentDetails studentDetails = service.studentDetails(handle);
      model.addAttribute("details", studentDetails);
      model.addAttribute("fehler", e.getMessage());
      return "index";
    }
    return "redirect:/";
  }

  @GetMapping("/urlaubbelegen")
  public String urlaubBelegen(Model model) {
    model.addAttribute("fehler", "");
    return "urlaubbelegen";
  }

  @PostMapping("/urlaubbelegen")
  public String urlaubSpeichern(@ModelAttribute("handle") String handle,
                                Model model,
                                String urlaubdatum, String urlaubstart, String urlaubende) {
    if (urlaubdatum.isEmpty() || urlaubstart.isEmpty() || urlaubende.isEmpty()) {
      model.addAttribute("fehler", "Alle Felder müssen gesetzt sein!");
      return "urlaubbelegen";
    }
    LocalDate datum = LocalDate.parse(urlaubdatum);
    LocalTime start = LocalTime.parse(urlaubstart);
    LocalTime ende = LocalTime.parse(urlaubende);
    ZeitraumDto urlaub;
    try {
      urlaub = ZeitraumDto.erstelleZeitraum(datum, start, ende);
    } catch (ZeitraumDtoException e) {
      model.addAttribute("fehler", e.getMessage());
      return "urlaubbelegen";
    }
    try {
      service.belegeUrlaub(handle, urlaub);
      return "redirect:/";
    } catch (UrlaubException e) {
      model.addAttribute("fehler", e.getMessage());
      return "urlaubbelegen";
    }
  }

  @GetMapping("/klausurbelegen")
  public String klausurBelegen(Model model) {
    model.addAttribute("fehler", "");
    model.addAttribute("klausuren", service.alleKlausuren());
    return "klausurbelegen";
  }

  @PostMapping("/klausurbelegen")
  public String klausurBelegung(@ModelAttribute("handle") String handle,
                                Model model,
                                String veranstaltungsId) {
    try {
      Klausur klausur = service.holeKlausur(veranstaltungsId);
      service.belegeKlausur(handle, klausur);
    } catch (Exception e) {
      model.addAttribute("fehler", e.getMessage());
      model.addAttribute("klausuren", service.alleKlausuren());
      return "klausurbelegen";
    }
    return "redirect:/";
  }

  @GetMapping("/klausuranmelden")
  public String klausurAnmelden(Model model) {
    model.addAttribute("fehler", "");
    return "klausuranmelden";
  }

  @PostMapping("/klausuranmelden")
  public String klausurAnmeldenPost(Model model, String veranstaltungsId,
                                    String veranstaltungsName,
                                    Boolean praesenz,
                                    String klausurdatum,
                                    String klausurstart,
                                    String klausurende) {
    if (klausurdatum.isEmpty() || klausurstart.isEmpty() || klausurende.isEmpty()) {
      model.addAttribute("fehler", "Alle Felder müssen gesetzt sein!");
      return "urlaubbelegen";
    }
    LocalDate datum = LocalDate.parse(klausurdatum);
    LocalTime start = LocalTime.parse(klausurstart);
    LocalTime ende = LocalTime.parse(klausurende);
    try {
      ZeitraumDto klausurZeitraum = ZeitraumDto.erstelleZeitraum(datum, start, ende);
      service.klausurAnmelden(veranstaltungsId, veranstaltungsName, klausurZeitraum, praesenz);
    } catch (Exception e) {
      model.addAttribute("fehler", e.getMessage());
      return "klausuranmelden";
    }
    return "redirect:/";
  }

}
