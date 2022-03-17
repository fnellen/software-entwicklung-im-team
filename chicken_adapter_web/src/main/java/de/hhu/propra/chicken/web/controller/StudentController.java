package de.hhu.propra.chicken.web.controller;

import de.hhu.propra.chicken.aggregates.student.Student;
import de.hhu.propra.chicken.services.ChickenService;
import de.hhu.propra.chicken.services.fehler.StudentNichtGefundenException;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

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
    Student student;
    try {
      student = service.holeStudent(handle);
    } catch (Exception e) {
      student = new Student(null, handle);
    }
    System.out.println(student.getGithubHandle());

    return "index";
  }

}
