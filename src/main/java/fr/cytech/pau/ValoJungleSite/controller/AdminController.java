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
    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Autowired
    JoueurRepository joueurRepository;

    @Autowired
    OrganisateurRepository organisateurRepository;

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
    public String postEditUser(@PathVariable(value="id") String id, @ModelAttribute Utilisateur utilisateur, HttpServletRequest request) {
        Utilisateur utilisateurEnBD = utilisateurRepository.getReferenceById(Long.valueOf(id));

        // Si le rôle de l'utilisateur a été changé
        if(!utilisateurEnBD.getRole().equals(utilisateur.getRole())) {
            if(utilisateur.getRole().equals("player")) {
                // Si le nouveau rôle est joueur, on supprime l'ancien organisateur associé à l'utilisateur
                utilisateurEnBD.setOrganisateur(null);

                Joueur joueur = new Joueur();
                joueur.setNom(request.getParameter("nom"));
                joueur.setPrenom(request.getParameter("prenom"));
                joueur.setPseudo(request.getParameter("pseudo"));
                joueur.setUtilisateur(utilisateurEnBD);

                joueur = joueurRepository.save(joueur);

                utilisateurEnBD.setJoueur(joueur);
            } else {
                // Si le nouveau rôle est organisateur, on supprime l'ancien joueur associé à l'utilisateur
                utilisateurEnBD.setJoueur(null);

                Organisateur organisateur = new Organisateur();
                organisateur.setDiscordUsername(request.getParameter("discordUsername"));
                organisateur.setEmail(request.getParameter("email"));
                organisateur.setUtilisateur(utilisateurEnBD);

                organisateur = organisateurRepository.save(organisateur);

                utilisateurEnBD.setOrganisateur(organisateur);
            }
        } else {
            if(utilisateurEnBD.getRole().equals("player")) {
                Joueur joueur = utilisateurEnBD.getJoueur();
                joueur.setNom(request.getParameter("nom"));
                joueur.setPrenom(request.getParameter("prenom"));
                joueur.setPseudo(request.getParameter("pseudo"));

                joueurRepository.save(joueur);
            } else {
                Organisateur organisateur = utilisateurEnBD.getOrganisateur();
                organisateur.setDiscordUsername(request.getParameter("discordUsername"));
                organisateur.setEmail(request.getParameter("email"));

                organisateurRepository.save(organisateur);
            }
        }

        if(!utilisateur.getPassword().equals("")) {
            utilisateur.setPassword(bCryptPasswordEncoder.encode(utilisateur.getPassword()));
            utilisateurEnBD.setPassword(utilisateur.getPassword());
        }

        utilisateurEnBD.setUsername(utilisateur.getUsername());
        utilisateurEnBD.setRole(utilisateur.getRole());

        utilisateurRepository.save(utilisateurEnBD);

        return "redirect:/admin/user-dashboard";
    }

    @GetMapping(path ="/user-dashboard/delete-user/{id}")
    public String deleteUser(@PathVariable(value="id") String id, Model model) {
        Utilisateur utilisateur = utilisateurRepository.findById(Long.valueOf(id)).orElse(null);

        if(utilisateur != null) {
            utilisateurRepository.delete(utilisateur);
        }

        return "redirect:/admin/user-dashboard";
    }

    @PostMapping(path = "/user-dashboard/new-user")
    public String postNewUser(@ModelAttribute Utilisateur utilisateur, HttpServletRequest request) {
        utilisateur.setPassword(bCryptPasswordEncoder.encode(utilisateur.getPassword()));

        utilisateur = utilisateurRepository.save(utilisateur);

        if(utilisateur.getRole().equals("player")) {
            Joueur joueur = new Joueur();
            joueur.setNom(request.getParameter("nom"));
            joueur.setPrenom(request.getParameter("prenom"));
            joueur.setPseudo(request.getParameter("pseudo"));
            joueur.setUtilisateur(utilisateur);

            joueur = joueurRepository.save(joueur);

            utilisateur.setJoueur(joueur);

            utilisateurRepository.save(utilisateur);
        } else {
            Organisateur organisateur = new Organisateur();
            organisateur.setDiscordUsername(request.getParameter("discordUsername"));
            organisateur.setEmail(request.getParameter("email"));
            organisateur.setUtilisateur(utilisateur);

            organisateur = organisateurRepository.save(organisateur);

            utilisateur.setOrganisateur(organisateur);

            utilisateurRepository.save(utilisateur);
        }

        return "redirect:/admin/user-dashboard";
    }
}
