package fr.cytech.pau.ValoJungleSite.controller;

import fr.cytech.pau.ValoJungleSite.entity.Joueur;
import fr.cytech.pau.ValoJungleSite.entity.Organisateur;
import fr.cytech.pau.ValoJungleSite.entity.Utilisateur;
import fr.cytech.pau.ValoJungleSite.repository.JoueurRepository;
import fr.cytech.pau.ValoJungleSite.repository.OrganisateurRepository;
import fr.cytech.pau.ValoJungleSite.repository.UtilisateurRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping(path = "")
    public String dashboard(Model model)  {
        return "dashboard";
    }
}
