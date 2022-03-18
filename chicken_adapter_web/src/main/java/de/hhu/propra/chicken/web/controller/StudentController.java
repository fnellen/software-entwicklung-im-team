package de.hhu.propra.chicken.web.controller;

import de.hhu.propra.chicken.aggregates.klausur.Klausur;
import de.hhu.propra.chicken.aggregates.student.Student;
import de.hhu.propra.chicken.services.ChickenService;
import de.hhu.propra.chicken.services.dto.StudentDetails;
import de.hhu.propra.chicken.services.fehler.KlausurException;
import de.hhu.propra.chicken.services.fehler.StudentNichtGefundenException;
import java.security.Principal;
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


    return "index";
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
      System.out.println(e.getMessage());
    }
    return "redirect:/";
  }


}
