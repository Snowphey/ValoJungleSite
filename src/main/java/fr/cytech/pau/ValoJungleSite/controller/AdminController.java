package fr.cytech.pau.ValoJungleSite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping(path = "/welcome")
    public String welcome(Model model) { return "welcome"; }
}
