package fr.cytech.pau.ValoJungleSite.controller;

import fr.cytech.pau.ValoJungleSite.entity.*;
import fr.cytech.pau.ValoJungleSite.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
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
    PartieRepository partieRepository;

    @Autowired
    GuildeRepository guildeRepository;

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

        if(utilisateur.getRole().equals("player")) {
            Joueur joueur = new Joueur();
            joueur.setNom(request.getParameter("nom"));
            joueur.setPrenom(request.getParameter("prenom"));
            joueur.setPseudo(request.getParameter("pseudo"));

            utilisateur.setJoueur(joueur);
            joueur.setUtilisateur(utilisateur);

            utilisateurRepository.save(utilisateur);
        } else {
            Organisateur organisateur = new Organisateur();
            organisateur.setDiscordUsername(request.getParameter("discordUsername"));
            organisateur.setEmail(request.getParameter("email"));

            utilisateur.setOrganisateur(organisateur);
            organisateur.setUtilisateur(utilisateur);

            utilisateurRepository.save(utilisateur);
        }

        return "redirect:/admin/user-dashboard";
    }

    @GetMapping(path ="/admin/user-dashboard/edit-user/{id}")
    public String editUser(@PathVariable(value="id") String id, Model model) {
        Utilisateur utilisateur = utilisateurRepository.findById(Long.valueOf(id)).orElse(null);

        model.addAttribute("utilisateur", utilisateur);

        return "user/editUserForm";
    }

    @PostMapping(path = "/admin/user-dashboard/edit-user/{id}")
    public String postEditUser(@PathVariable(value="id") String id, @ModelAttribute Utilisateur utilisateur, HttpServletRequest request) {
        Utilisateur utilisateurEnBD = utilisateurRepository.getReferenceById(Long.valueOf(id));

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

        if(!utilisateur.getPassword().isEmpty()) {
            utilisateur.setPassword(bCryptPasswordEncoder.encode(utilisateur.getPassword()));
            utilisateurEnBD.setPassword(utilisateur.getPassword());
        }

        utilisateurEnBD.setUsername(utilisateur.getUsername());

        utilisateurRepository.save(utilisateurEnBD);

        return "redirect:/admin/user-dashboard";
    }

    @Transactional
    @GetMapping(path = "/admin/user-dashboard/delete-user/{id}")
    public String deleteUser(@PathVariable(value = "id") String id, Model model) {
        Utilisateur utilisateur = utilisateurRepository.findById(Long.valueOf(id)).orElse(null);

        if (utilisateur != null) {
            if (utilisateur.getRole().equals("player")) {
                Joueur joueur = utilisateur.getJoueur();

                Guilde guilde = joueur.getGuilde();

                joueur.setGuilde(null);

                // Si le joueur était chef de sa guilde, elle est supprimée
                if(joueur.isEstChef()) {
                    // Tous les membres de la guilde du joueur n'ont plus de guilde
                    for(Joueur player : guilde.getMembres()) {
                        player.setGuilde(null);
                    }

                    guildeRepository.delete(guilde);
                }

                // On retire la participation à toutes les parties du joueur
                for(Partie partie : joueur.getParties()) {
                    partie.removeParticipant(joueur);
                }

                joueur.setParties(new ArrayList<>());

                joueurRepository.delete(joueur);
            } else {
                Organisateur organisateur = utilisateur.getOrganisateur();

                for (Partie partie : organisateur.getPartiesCreees()) {
                    for(Joueur participant : partie.getParticipants()) {
                        participant.removePartie(partie);
                    }

                    partie.setParticipants(new ArrayList<>());
                    partie.setCreateur(null);

                    partieRepository.delete(partie);
                }

                organisateurRepository.delete(organisateur);
            }

            utilisateurRepository.delete(utilisateur);
        }

        return "redirect:/admin/user-dashboard";
    }
}
