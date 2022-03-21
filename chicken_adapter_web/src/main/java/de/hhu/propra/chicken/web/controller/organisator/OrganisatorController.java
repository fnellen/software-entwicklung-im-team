package de.hhu.propra.chicken.web.controller.organisator;

import de.hhu.propra.chicken.web.annotations.OrganisatorRoute;
import java.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@OrganisatorRoute
@RequestMapping("/organisator")
public class OrganisatorController {

  @GetMapping("logs")
  public String logs(@ModelAttribute("handle") String organisatorHandle) {
    return "index";
  }

  @ModelAttribute("handle")
  String handle(Principal user) {
    return user.getName();
  }
}
