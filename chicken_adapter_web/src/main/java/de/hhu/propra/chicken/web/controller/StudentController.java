package de.hhu.propra.chicken.web.controller;

import de.hhu.propra.chicken.aggregates.dto.ZeitraumDto;
import de.hhu.propra.chicken.aggregates.klausur.Klausur;
import de.hhu.propra.chicken.aggregates.student.Student;
import de.hhu.propra.chicken.services.ChickenService;
import de.hhu.propra.chicken.services.dto.StudentDetails;
import de.hhu.propra.chicken.services.fehler.KlausurException;
import de.hhu.propra.chicken.web.dto.KlausurDto;
import de.hhu.propra.chicken.web.dto.UrlaubDto;
import java.security.Principal;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
  public String index(Model model, @ModelAttribute("handle") String handle, UrlaubDto urlaubDto) {
    try {
      service.holeStudent(handle);
    } catch (Exception e) {
      Student student = new Student(null, handle);
      service.studentSpeichern(student);
    }
    StudentDetails studentDetails = service.studentDetails(handle);
    model.addAttribute("details", studentDetails);
    model.addAttribute("urlaubDto", urlaubDto);
    model.addAttribute("fehler", "");
    return "index";
  }

  @PostMapping("/urlaubstornieren")
  public String urlaubStornieren(Model model, @ModelAttribute("handle") String handle,
                                 @Valid UrlaubDto urlaubDto) {
    System.out.println(urlaubDto);
    ZeitraumDto urlaub = ZeitraumDto.erstelleZeitraum(urlaubDto.urlaubsDatum(),
        urlaubDto.urlaubsStart(), urlaubDto.urlaubsEnde());
    try {
      service.storniereUrlaub(handle, urlaub);
    } catch (Exception e) {
      StudentDetails studentDetails = service.studentDetails(handle);
      model.addAttribute("details", studentDetails);
      model.addAttribute("urlaubDto", new UrlaubDto(null, null, null));
      model.addAttribute("fehler", e.getMessage());
      return "index";
    }
    return "redirect:/";
  }

  @PostMapping("/klausurstornieren")
  public String klausurStornieren(Model model,
                                  @ModelAttribute("handle") String handle,
                                  @NotNull @NotBlank @NotEmpty
                                      String veranstaltungsId) {
    System.out.println(veranstaltungsId);
    Klausur klausur = service.holeKlausur(veranstaltungsId);
    try {
      service.storniereKlausur(handle, klausur);
    } catch (KlausurException e) {
      StudentDetails studentDetails = service.studentDetails(handle);
      model.addAttribute("details", studentDetails);
      model.addAttribute("urlaubDto", new UrlaubDto(null, null, null));
      model.addAttribute("fehler", e.getMessage());
      return "index";
    }
    return "redirect:/";
  }

  @GetMapping("/urlaubbelegen")
  public String urlaubBelegen(Model model, UrlaubDto urlaubDto) {
    model.addAttribute("fehler", "");
    model.addAttribute("urlaubDto", urlaubDto);
    return "urlaubbelegen";
  }

  @PostMapping("/urlaubbelegen")
  public String urlaubSpeichern(@ModelAttribute("handle") String handle,
                                Model model, @Valid UrlaubDto urlaubDto, BindingResult result) {
    if (result.hasErrors()) {
      model.addAttribute("fehler", "");
      return "urlaubbelegen";
    }
    try {
      ZeitraumDto urlaub = ZeitraumDto.erstelleZeitraum(urlaubDto.urlaubsDatum(),
          urlaubDto.urlaubsStart(),
          urlaubDto.urlaubsEnde());
      service.belegeUrlaub(handle, urlaub);
      return "redirect:/";
    } catch (Exception e) {
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
  public String klausurBelegung(@ModelAttribute("handle") String handle, Model model,
                                @NotNull @NotEmpty @NotBlank String veranstaltungsId,
                                BindingResult result) {
    if (result.hasErrors()) {
      model.addAttribute("fehler", "");
      return "klausurbelegen";
    }
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
  public String klausurAnmelden(Model model, KlausurDto klausurDto) {
    model.addAttribute("fehler", "");
    model.addAttribute("klausurDto", klausurDto);
    return "klausuranmelden";
  }

  @PostMapping("/klausuranmelden")
  public String klausurAnmeldenPost(@Valid KlausurDto klausurDto, BindingResult result, Model model
  ) {
    System.out.println(klausurDto);
    if (result.hasErrors()) {
      model.addAttribute("fehler", "");
      return "klausuranmelden";
    }
    try {
      ZeitraumDto klausurZeitraum = ZeitraumDto.erstelleZeitraum(klausurDto.klausurdatum(),
          klausurDto.klausurstart(), klausurDto.klausurende());
      service.klausurAnmelden(klausurDto.veranstaltungsId(), klausurDto.veranstaltungsName(),
          klausurZeitraum,
          klausurDto.praesenz());
    } catch (Exception e) {
      model.addAttribute("fehler", e.getMessage());
      return "klausuranmelden";
    }
    return "redirect:/";
  }

}
