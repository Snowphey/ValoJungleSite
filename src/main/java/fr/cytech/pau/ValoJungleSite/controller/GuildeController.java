package fr.cytech.pau.ValoJungleSite.controller;

import fr.cytech.pau.ValoJungleSite.entity.Guilde;
import fr.cytech.pau.ValoJungleSite.entity.Joueur;
import fr.cytech.pau.ValoJungleSite.entity.Utilisateur;
import fr.cytech.pau.ValoJungleSite.repository.GuildeRepository;
import fr.cytech.pau.ValoJungleSite.repository.JoueurRepository;
import fr.cytech.pau.ValoJungleSite.repository.UtilisateurRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GuildeController {
    @Autowired
    GuildeRepository guildeRepository;

    @Autowired
    JoueurRepository joueurRepository;

    @Autowired
    UtilisateurRepository utilisateurRepository;

    @GetMapping(path = "/admin/guild-dashboard")
    public String guildDashboard(Model model) {
        List<Guilde> guildes = guildeRepository.findAll();
        model.addAttribute("guildes", guildes);
        return "guild/guildDashboard";
    }

    @GetMapping(path = "/admin/guild-dashboard/new-guild")
    public String newGuildAdmin(Model model) {
        List<Joueur> joueurs = joueurRepository.findAll();

        List<Joueur> joueursSansGuilde = new ArrayList<>();

        for(Joueur joueur : joueurs) {
            if(joueur.getGuilde() == null) {
                joueursSansGuilde.add(joueur);
            }
        }

        model.addAttribute("joueursSansGuilde", joueursSansGuilde);

        return "guild/newGuildFormAdmin";
    }

    @Transactional
    @PostMapping(path = "/admin/guild-dashboard/new-guild")
    public String postNewGuildAdmin(@ModelAttribute Guilde guilde, HttpServletRequest request) {
        Joueur joueur = joueurRepository.findById(Long.valueOf(request.getParameter("chef"))).orElse(null);

        if(joueur != null) {
            joueur.setGuilde(guilde);
            joueur.setEstChef(true);

            guilde.addMembre(joueur);
            guildeRepository.save(guilde);

            joueurRepository.save(joueur);
        }

        return "redirect:/admin/guild-dashboard";
    }

    @GetMapping(path = "/admin/guild-dashboard/edit-guild/{id}")
    public String editGuildAdmin(@PathVariable(value = "id") Long id, Model model) {
        List<Joueur> joueurs = joueurRepository.findAll();

        List<Joueur> joueursSansGuildeEtChef = new ArrayList<>();

        for(Joueur joueur : joueurs) {
            if(joueur.getGuilde() == null) {
                joueursSansGuildeEtChef.add(joueur);
            }
        }

        Guilde guilde = guildeRepository.findById(id).orElse(null);

        for(Joueur joueur : guilde.getMembres()) {
            if(joueur.isEstChef()) {
                joueursSansGuildeEtChef.add(joueur);
            }
        }

        model.addAttribute("joueursSansGuildeEtChef", joueursSansGuildeEtChef);

        model.addAttribute("guilde", guilde);
        return "guild/editGuildFormAdmin";
    }

    @Transactional
    @PostMapping(path = "/admin/guild-dashboard/edit-guild/{id}")
    public String postEditGuildAdmin(@PathVariable(value = "id") Long id, @ModelAttribute Guilde guilde, HttpServletRequest request) {
        Guilde guildeEnBD = guildeRepository.findById(id).orElse(null);
        if (guildeEnBD != null) {
            guildeEnBD.setNom(guilde.getNom());
            guildeEnBD.setTag(guilde.getTag());
            guildeEnBD.setLienEmbleme(guilde.getLienEmbleme());
            guildeEnBD.setCouleurHex(guilde.getCouleurHex());

            Joueur chefEnBD = null;

            for(Joueur joueur : guildeEnBD.getMembres()) {
                if(joueur.isEstChef()) {
                    chefEnBD = joueur;
                    break;
                }
            }

            Joueur chef = joueurRepository.findById(Long.valueOf(request.getParameter("chef"))).orElse(null);

            if(!chefEnBD.getId().equals(chef.getId())) {
                // On a changé de chef pour un autre joueur sans guilde
                chefEnBD.setGuilde(null);
                chefEnBD.setEstChef(false);

                guildeEnBD.removeMembre(chefEnBD);

                chef.setGuilde(guildeEnBD);
                chef.setEstChef(true);

                guildeEnBD.addMembre(chef);
                guildeRepository.save(guildeEnBD);

                joueurRepository.save(chef);
                joueurRepository.save(chefEnBD);
            } else {
                guildeRepository.save(guildeEnBD);
            }
        }
        return "redirect:/admin/guild-dashboard";
    }

    @Transactional
    @GetMapping(path = "/admin/guild-dashboard/delete-guild/{id}")
    public String deleteGuild(@PathVariable(value = "id") Long id) {
        Guilde guilde = guildeRepository.findById(id).orElse(null);
        if (guilde != null) {
            for(Joueur joueur : guilde.getMembres()) {
                if(joueur.isEstChef()) {
                    joueur.setGuilde(null);
                    joueur.setEstChef(false);
                    joueurRepository.save(joueur);
                } else {
                    joueur.setGuilde(null);
                    joueurRepository.save(joueur);
                }
            }

            guildeRepository.delete(guilde);
        }
        return "redirect:/admin/guild-dashboard";
    }

    @GetMapping(path = "/player/guild")
    public String guild(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal instanceof User) {
            User userConnecte = (User) principal;
            Utilisateur utilisateurConnecte = utilisateurRepository.findByUsername(userConnecte.getUsername());

            Joueur joueur = joueurRepository.findById(utilisateurConnecte.getJoueur().getId()).orElse(null);

            if(joueur != null) {
                model.addAttribute("estChef", joueur.isEstChef());

                model.addAttribute("guilde", joueur.getGuilde());
            }
        }

        return "guild/guild";
    }

    @GetMapping(path = "/player/new-guild")
    public String newGuild(Model model) {
        return "guild/newGuildForm";
    }

    @Transactional
    @PostMapping(path = "/player/new-guild")
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

        return "redirect:/player/guild";
    }

    @GetMapping(path = "/player/edit-guild/{id}")
    public String editGuild(@PathVariable(value = "id") Long id, Model model) {
        Guilde guilde = guildeRepository.findById(id).orElse(null);
        model.addAttribute("guilde", guilde);
        return "guild/editGuildForm";
    }

    @PostMapping(path = "/player/edit-guild/{id}")
    public String postEditGuild(@PathVariable(value = "id") Long id, @ModelAttribute Guilde guilde) {
        Guilde guildeEnBD = guildeRepository.findById(id).orElse(null);
        if (guildeEnBD != null) {
            guildeEnBD.setNom(guilde.getNom());
            guildeEnBD.setTag(guilde.getTag());
            guildeEnBD.setLienEmbleme(guilde.getLienEmbleme());
            guildeEnBD.setCouleurHex(guilde.getCouleurHex());
            guildeRepository.save(guildeEnBD);
        }
        return "redirect:/player/guild";
    }

    @GetMapping("/player/join-guild")
    public String joinGuild(Model model) {
        List<Guilde> guildes = guildeRepository.findAll();

        model.addAttribute("guildes", guildes);

        return "guild/joinGuildForm";
    }

    @Transactional
    @PostMapping("/player/join-guild")
    public String postJoinGuild(Model model, HttpServletRequest request) {
        Guilde guildeRejointe = guildeRepository.findById(Long.valueOf(request.getParameter("guilde"))).orElse(null);

        if (guildeRejointe != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if(principal instanceof User) {
                User userConnecte = (User) principal;
                Utilisateur utilisateurConnecte = utilisateurRepository.findByUsername(userConnecte.getUsername());

                Joueur joueur = joueurRepository.findById(utilisateurConnecte.getJoueur().getId()).orElse(null);

                if(joueur != null) {
                    joueur.setGuilde(guildeRejointe);

                    guildeRejointe.addMembre(joueur);
                    guildeRepository.save(guildeRejointe);

                    joueurRepository.save(joueur);
                }
            }
        }

        return "redirect:/";
    }

    @Transactional
    @GetMapping("/player/leave-guild")
    public String leaveGuild(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal instanceof User) {
            User userConnecte = (User) principal;
            Utilisateur utilisateurConnecte = utilisateurRepository.findByUsername(userConnecte.getUsername());

            Joueur joueur = joueurRepository.findById(utilisateurConnecte.getJoueur().getId()).orElse(null);

            if(joueur != null) {
                if(joueur.isEstChef()) {
                    // Dissolution de la guilde
                    this.deleteGuild(joueur.getGuilde().getId());
                } else {
                    Guilde guilde = joueur.getGuilde();

                    guilde.removeMembre(joueur);
                    joueur.setGuilde(null);

                    guildeRepository.save(guilde);
                    joueurRepository.save(joueur);
                }
            }
        }

        return "redirect:/";
    }
}
