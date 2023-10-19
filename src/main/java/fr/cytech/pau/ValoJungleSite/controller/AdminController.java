package fr.cytech.pau.ValoJungleSite.controller;

import fr.cytech.pau.ValoJungleSite.entity.Utilisateur;
import fr.cytech.pau.ValoJungleSite.repository.UtilisateurRepository;
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
    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping(path = "/welcome")
    public String welcome(Model model) { return "welcome"; }

    @GetMapping(path = "")
    public String dashboard(Model model)  {
        return "dashboard";
    }

    @GetMapping(path = "/user-dashboard")
    public String userDashboard(Model model) {
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();

        model.addAttribute("utilisateurs", utilisateurs);

        return "userDashboard";
    }

    @GetMapping(path = "/user-dashboard/new-user")
    public String newUser(Model model) {
        return "newUserForm";
    }

    @GetMapping(path ="/user-dashboard/edit-user/{id}")
    public String editUser(@PathVariable(value="id") String id, Model model) {
        Utilisateur utilisateur = utilisateurRepository.findById(Long.valueOf(id)).orElse(null);

        model.addAttribute("utilisateur", utilisateur);

        return "editUserForm";
    }

    @PostMapping(path = "/user-dashboard/edit-user/{id}")
    public String postEditUser(@PathVariable(value="id") String id, @ModelAttribute Utilisateur utilisateur) {
        Utilisateur utilisateurEnBD = utilisateurRepository.getReferenceById(Long.valueOf(id));

        if(!utilisateur.getPassword().equals("")) {
            utilisateur.setPassword(bCryptPasswordEncoder.encode(utilisateur.getPassword()));
            utilisateurEnBD.setPassword(utilisateur.getPassword());
        }

        utilisateurEnBD.setUsername(utilisateur.getUsername());
        utilisateurEnBD.setRole(utilisateur.getRole());

        utilisateurRepository.save(utilisateurEnBD);

        return "redirect:/admin/user-dashboard";
    }

    @PostMapping(path = "/user-dashboard/new-user")
    public String postNewUser(@ModelAttribute Utilisateur utilisateur) {
        utilisateur.setPassword(bCryptPasswordEncoder.encode(utilisateur.getPassword()));

        utilisateurRepository.save(utilisateur);

        return "redirect:/admin/user-dashboard";
    }
}
