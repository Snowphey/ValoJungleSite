package fr.cytech.pau.ValoJungleSite.controller;

import fr.cytech.pau.ValoJungleSite.entity.Joueur;
import fr.cytech.pau.ValoJungleSite.entity.Organisateur;
import fr.cytech.pau.ValoJungleSite.entity.Utilisateur;
import fr.cytech.pau.ValoJungleSite.repository.JoueurRepository;
import fr.cytech.pau.ValoJungleSite.repository.UtilisateurRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    JoueurRepository joueurRepository;

    @GetMapping(path = "/")
    public String home(Model model) {
        return "home";
    }

    @GetMapping(path = "/register")
    public String register() { return "register"; }

    @PostMapping(path = "/register")
    public String postRegister(@ModelAttribute Utilisateur utilisateur, HttpServletRequest request) {
        utilisateur.setPassword(bCryptPasswordEncoder.encode(utilisateur.getPassword()));

        utilisateur.setRole("player");

        Joueur joueur = new Joueur();
        joueur.setNom(request.getParameter("nom"));
        joueur.setPrenom(request.getParameter("prenom"));
        joueur.setPseudo(request.getParameter("pseudo"));

        utilisateur.setJoueur(joueur);
        joueur.setUtilisateur(utilisateur);

        utilisateurRepository.save(utilisateur);

        return "redirect:/";
    }

    @GetMapping(path = "/admin")
    public String dashboard(Model model)  {
        return "adminDashboard";
    }
}