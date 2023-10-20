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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UtilisateurController {
    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Autowired
    JoueurRepository joueurRepository;

    @Autowired
    OrganisateurRepository organisateurRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping(path = "/admin/user-dashboard")
    public String userDashboard(Model model) {
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();

        model.addAttribute("utilisateurs", utilisateurs);

        return "user/userDashboard";
    }

    @GetMapping(path = "/admin/user-dashboard/new-user")
    public String newUser(Model model) {
        return "user/newUserForm";
    }

    @PostMapping(path = "/admin/user-dashboard/new-user")
    public String postNewUser(@ModelAttribute Utilisateur utilisateur, HttpServletRequest request) {
        utilisateur.setPassword(bCryptPasswordEncoder.encode(utilisateur.getPassword()));

        utilisateur = utilisateurRepository.save(utilisateur);

        if(utilisateur.getRole().equals("player")) {
            Joueur joueur = new Joueur();
            joueur.setNom(request.getParameter("nom"));
            joueur.setPrenom(request.getParameter("prenom"));
            joueur.setPseudo(request.getParameter("pseudo"));

            utilisateur.setJoueur(joueur);

            utilisateurRepository.save(utilisateur);
        } else {
            Organisateur organisateur = new Organisateur();
            organisateur.setDiscordUsername(request.getParameter("discordUsername"));
            organisateur.setEmail(request.getParameter("email"));

            utilisateur.setOrganisateur(organisateur);

            utilisateurRepository.save(utilisateur);
        }

        return "redirect:/admin/user-dashboard";
    }

    @GetMapping(path ="/user-dashboard/edit-user/{id}")
    public String editUser(@PathVariable(value="id") String id, Model model) {
        Utilisateur utilisateur = utilisateurRepository.findById(Long.valueOf(id)).orElse(null);

        model.addAttribute("utilisateur", utilisateur);

        return "user/editUserForm";
    }

    @PostMapping(path = "/admin/user-dashboard/edit-user/{id}")
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

                utilisateurEnBD.setJoueur(joueur);
            } else {
                // Si le nouveau rôle est organisateur, on supprime l'ancien joueur associé à l'utilisateur
                utilisateurEnBD.setJoueur(null);

                Organisateur organisateur = new Organisateur();
                organisateur.setDiscordUsername(request.getParameter("discordUsername"));
                organisateur.setEmail(request.getParameter("email"));

                utilisateurEnBD.setOrganisateur(organisateur);
            }
        } else {
            if(utilisateurEnBD.getRole().equals("player")) {
                Joueur joueur = utilisateurEnBD.getJoueur();
                joueur.setNom(request.getParameter("nom"));
                joueur.setPrenom(request.getParameter("prenom"));
                joueur.setPseudo(request.getParameter("pseudo"));

                utilisateurEnBD.setJoueur(joueur);
            } else {
                Organisateur organisateur = utilisateurEnBD.getOrganisateur();
                organisateur.setDiscordUsername(request.getParameter("discordUsername"));
                organisateur.setEmail(request.getParameter("email"));

                utilisateurEnBD.setOrganisateur(organisateur);
            }
        }

        if(!utilisateur.getPassword().isEmpty()) {
            utilisateur.setPassword(bCryptPasswordEncoder.encode(utilisateur.getPassword()));
            utilisateurEnBD.setPassword(utilisateur.getPassword());
        }

        utilisateurEnBD.setUsername(utilisateur.getUsername());
        utilisateurEnBD.setRole(utilisateur.getRole());

        utilisateurRepository.save(utilisateurEnBD);

        return "redirect:/admin/user-dashboard";
    }

    @GetMapping(path ="/admin/user-dashboard/delete-user/{id}")
    public String deleteUser(@PathVariable(value="id") String id, Model model) {
        Utilisateur utilisateur = utilisateurRepository.findById(Long.valueOf(id)).orElse(null);

        if(utilisateur != null) {
            utilisateurRepository.delete(utilisateur);
        }

        return "redirect:/admin/user-dashboard";
    }
}
