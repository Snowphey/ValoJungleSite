package fr.cytech.pau.ValoJungleSite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping(path = "")
    public String dashboard(Model model)  {
        return "adminDashboard";
    }
}
