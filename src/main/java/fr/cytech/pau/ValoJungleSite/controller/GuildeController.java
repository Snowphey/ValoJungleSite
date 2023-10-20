package fr.cytech.pau.ValoJungleSite.controller;

import fr.cytech.pau.ValoJungleSite.entity.Guilde;
import fr.cytech.pau.ValoJungleSite.entity.Joueur;
import fr.cytech.pau.ValoJungleSite.entity.Utilisateur;
import fr.cytech.pau.ValoJungleSite.repository.GuildeRepository;
import fr.cytech.pau.ValoJungleSite.repository.JoueurRepository;
import fr.cytech.pau.ValoJungleSite.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GuildeController {
    @Autowired
    GuildeRepository guildeRepository;

    @Autowired
    JoueurRepository joueurRepository;

    @Autowired
    UtilisateurRepository utilisateurRepository;

    @GetMapping(path = "/player/guild-dashboard")
    public String guildDashboard(Model model) {
        List<Guilde> guildes = guildeRepository.findAll();
        model.addAttribute("guildes", guildes);
        return "guild/guildDashboard";
    }

    @GetMapping(path = "/player/guild-dashboard/new-guild")
    public String newGuild(Model model) {
        return "guild/newGuildForm";
    }

    @PostMapping(path = "/player/guild-dashboard/new-guild")
    public String postNewGuild(@ModelAttribute Guilde guilde) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal instanceof User) {
            User userConnecte = (User) principal;
            Utilisateur utilisateurConnecte = utilisateurRepository.findByUsername(userConnecte.getUsername());

            Joueur joueur = joueurRepository.findById(utilisateurConnecte.getJoueur().getId()).orElse(null);

            if(joueur != null) {
                joueur.setGuilde(guilde);
                joueur.setEstChef(true);

                guilde.addMembre(joueur);
                guildeRepository.save(guilde);

                joueurRepository.save(joueur);
            }
        }

        return "redirect:/player/guild-dashboard";
    }

    @GetMapping(path = "/player/guild-dashboard/edit-guild/{id}")
    public String editGuild(@PathVariable(value = "id") Long id, Model model) {
        Guilde guilde = guildeRepository.findById(id).orElse(null);
        model.addAttribute("guilde", guilde);
        return "guild/editGuildForm";
    }

    @PostMapping(path = "/player/guild-dashboard/edit-guild/{id}")
    public String postEditGuild(@PathVariable(value = "id") Long id, @ModelAttribute Guilde guilde) {
        Guilde guildeEnBD = guildeRepository.findById(id).orElse(null);
        if (guildeEnBD != null) {
            guildeEnBD.setNom(guilde.getNom());
            guildeEnBD.setTag(guilde.getTag());
            guildeEnBD.setLienEmbleme(guilde.getLienEmbleme());
            guildeEnBD.setCouleurHex(guilde.getCouleurHex());
            guildeRepository.save(guildeEnBD);
        }
        return "redirect:/player/guild-dashboard";
    }

    @GetMapping(path = "/player/guild-dashboard/delete-guild/{id}")
    public String deleteGuild(@PathVariable(value = "id") Long id) {
        Guilde guilde = guildeRepository.findById(id).orElse(null);
        if (guilde != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if(principal instanceof User) {
                User userConnecte = (User) principal;
                Utilisateur utilisateurConnecte = utilisateurRepository.findByUsername(userConnecte.getUsername());

                Joueur joueur = joueurRepository.findById(utilisateurConnecte.getJoueur().getId()).orElse(null);

                if(joueur != null) {
                    joueur.setGuilde(null);
                    joueur.setEstChef(false);

                    joueurRepository.save(joueur);
                }
            }

            guildeRepository.delete(guilde);
        }
        return "redirect:/player/guild-dashboard";
    }
}
