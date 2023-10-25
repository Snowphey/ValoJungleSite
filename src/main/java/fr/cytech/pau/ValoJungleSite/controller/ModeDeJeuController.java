package fr.cytech.pau.ValoJungleSite.controller;

import fr.cytech.pau.ValoJungleSite.entity.*;
import fr.cytech.pau.ValoJungleSite.repository.ModeDeJeuRepository;
import fr.cytech.pau.ValoJungleSite.repository.PartieRepository;
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
public class ModeDeJeuController {
    @Autowired
    ModeDeJeuRepository modeDeJeuRepository;

    @Autowired
    PartieRepository partieRepository;

    @GetMapping(path = "/admin/gamemode-dashboard")
    public String gamemodeDashboard(@RequestParam(value = "search", required = false) String search, Model model) {
        List<ModeDeJeu> modesDeJeu = new ArrayList<>();

        if (search != null && !search.isEmpty()) {
            // Recherche spécifiée, on utilise la méthode de recherche personnalisée
            modesDeJeu = modeDeJeuRepository.searchGamemodes(search);
            model.addAttribute("search", search);
        } else {
            // Aucune recherche spécifiée, on récupère tout
            modesDeJeu = modeDeJeuRepository.findAll();
        }

        model.addAttribute("modesDeJeu", modesDeJeu);

        return "gamemode/gamemodeDashboard";
    }

    @GetMapping(path = "/admin/gamemode-dashboard/new-gamemode")
    public String newGamemode(Model model) {
        return "gamemode/newGamemodeForm";
    }

    @PostMapping(path = "/admin/gamemode-dashboard/new-gamemode")
    public String postNewGamemode(@ModelAttribute ModeDeJeu modeDeJeu) {
        modeDeJeuRepository.save(modeDeJeu);
        return "redirect:/admin/gamemode-dashboard";
    }

    @GetMapping(path = "/admin/gamemode-dashboard/edit-gamemode/{id}")
    public String editGamemode(@PathVariable(value = "id") Long id, Model model) {
        ModeDeJeu modeDeJeu = modeDeJeuRepository.findById(id).orElse(null);
        model.addAttribute("modeDeJeu", modeDeJeu);
        return "gamemode/editGamemodeForm";
    }

    @PostMapping(path = "/admin/gamemode-dashboard/edit-gamemode/{id}")
    public String postEditGamemode(@PathVariable(value = "id") Long id, @ModelAttribute ModeDeJeu modeDeJeu) {
        ModeDeJeu modeDeJeuEnBD = modeDeJeuRepository.findById(id).orElse(null);
        if (modeDeJeuEnBD != null) {
            modeDeJeuEnBD.setNom(modeDeJeu.getNom());
            modeDeJeuEnBD.setDescription(modeDeJeu.getDescription());
            modeDeJeuEnBD.setLienGIF(modeDeJeu.getLienGIF());
            modeDeJeuRepository.save(modeDeJeuEnBD);
        }
        return "redirect:/admin/gamemode-dashboard";
    }

    @Transactional
    @GetMapping(path = "/admin/gamemode-dashboard/delete-gamemode/{id}")
    public String deleteGamemode(@PathVariable(value = "id") Long id) {
        ModeDeJeu modeDeJeu = modeDeJeuRepository.findById(id).orElse(null);
        if (modeDeJeu != null) {
            for(Partie partie : modeDeJeu.getParties()) {
                Organisateur organisateur = partie.getCreateur();

                if (organisateur != null) {
                    organisateur.removePartieCreee(partie);
                }

                for(Joueur participant : partie.getParticipants()) {
                    participant.removePartie(partie);
                }

                partieRepository.delete(partie);
            }

            modeDeJeuRepository.delete(modeDeJeu);
        }
        return "redirect:/admin/gamemode-dashboard";
    }
}
