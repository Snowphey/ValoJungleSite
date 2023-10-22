package fr.cytech.pau.ValoJungleSite.controller;

import fr.cytech.pau.ValoJungleSite.entity.*;
import fr.cytech.pau.ValoJungleSite.repository.JoueurRepository;
import fr.cytech.pau.ValoJungleSite.repository.ModeDeJeuRepository;
import fr.cytech.pau.ValoJungleSite.repository.PartieRepository;
import fr.cytech.pau.ValoJungleSite.repository.UtilisateurRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PartieController {
    @Autowired
    PartieRepository partieRepository;

    @Autowired
    ModeDeJeuRepository modeDeJeuRepository;

    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Autowired
    JoueurRepository joueurRepository;

    @GetMapping(path = "/admin/game-dashboard")
    public String gameDashboard(Model model) {
        List<Partie> parties = partieRepository.findAll();

        model.addAttribute("parties", parties);

        return "game/gameDashboard";
    }

    @GetMapping(path = "/admin/game-dashboard/new-game")
    public String newGame(Model model) {
        List<ModeDeJeu> modesDeJeu = modeDeJeuRepository.findAll();

        model.addAttribute("modesDeJeu", modesDeJeu);

        return "game/newGameForm";
    }

    @PostMapping(path = "/admin/game-dashboard/new-game")
    public String postNewGame(@ModelAttribute Partie partie, HttpServletRequest request) {
        ModeDeJeu modeDeJeu = modeDeJeuRepository.findById(Long.valueOf(request.getParameter("modeDeJeu"))).orElse(null);

        partie.setModeDeJeu(modeDeJeu);

        partieRepository.save(partie);

        return "redirect:/admin/game-dashboard";
    }

    @GetMapping(path ="/admin/game-dashboard/edit-game/{id}")
    public String editGame(@PathVariable(value="id") String id, Model model) {
        Partie partie = partieRepository.findById(Long.valueOf(id)).orElse(null);

        List<ModeDeJeu> modesDeJeu = modeDeJeuRepository.findAll();

        model.addAttribute("partie", partie);
        model.addAttribute("modesDeJeu", modesDeJeu);

        return "game/editGameForm";
    }

    @PostMapping(path = "/admin/game-dashboard/edit-game/{id}")
    public String postEditGame(@PathVariable(value="id") String id, @ModelAttribute Partie partie, HttpServletRequest request) {
        Partie partieEnBD = partieRepository.findById(Long.valueOf(id)).orElse(null);

        if (partieEnBD != null) {
            partieEnBD.setNbJoueursMin(partie.getNbJoueursMin());
            partieEnBD.setNbJoueursMax(partie.getNbJoueursMax());
            partieEnBD.setDateHeureRDV(partie.getDateHeureRDV());
            partieEnBD.setInscriptionsOuvertes(partie.isInscriptionsOuvertes());
            partieEnBD.setModeDeJeu(partie.getModeDeJeu());

            partieRepository.save(partieEnBD);
        }

        return "redirect:/admin/game-dashboard";
    }

    @GetMapping(path ="/admin/game-dashboard/delete-game/{id}")
    public String deleteGame(@PathVariable(value="id") String id, Model model) {
        Partie partie = partieRepository.findById(Long.valueOf(id)).orElse(null);

        if(partie != null) {
            partieRepository.delete(partie);
        }

        return "redirect:/admin/game-dashboard";
    }

    @GetMapping("/player/join-game")
    public String joinGame(Model model) {
        List<Partie> partiesOuvertes = partieRepository.findByInscriptionsOuvertes(true);
        List<Partie> partiesRejointes = new ArrayList<>();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            User userConnecte = (User) principal;
            Utilisateur utilisateurConnecte = utilisateurRepository.findByUsername(userConnecte.getUsername());

            if (utilisateurConnecte != null) {
                Joueur joueur = utilisateurConnecte.getJoueur();
                model.addAttribute("joueur", joueur);

                if (joueur != null) {
                    partiesRejointes = joueur.getParties();
                }
            }
        }

        model.addAttribute("parties", partiesOuvertes);
        model.addAttribute("partiesRejointes", partiesRejointes);

        return "game/joinGameForm";
    }

    @PostMapping("/player/join-game")
    public String postJoinGame(Model model, HttpServletRequest request) {
        Partie partieRejointe = partieRepository.findById(Long.valueOf(request.getParameter("partie"))).orElse(null);

        if (partieRejointe != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if(principal instanceof User) {
                User userConnecte = (User) principal;
                Utilisateur utilisateurConnecte = utilisateurRepository.findByUsername(userConnecte.getUsername());

                Joueur joueur = joueurRepository.findById(utilisateurConnecte.getJoueur().getId()).orElse(null);

                if(joueur != null && partieRejointe.isInscriptionsOuvertes() && partieRejointe.getParticipants().size() < partieRejointe.getNbJoueursMax()) {
                    joueur.addPartie(partieRejointe);

                    partieRejointe.addParticipant(joueur);
                    partieRepository.save(partieRejointe);

                    joueurRepository.save(joueur);
                }
            }
        }

        return "redirect:/player/join-game";
    }

    @PostMapping("/player/leave-game")
    public String postLeaveGame(Model model, HttpServletRequest request) {
        Partie partieRejointe = partieRepository.findById(Long.valueOf(request.getParameter("partie"))).orElse(null);

        if (partieRejointe != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if(principal instanceof User) {
                User userConnecte = (User) principal;
                Utilisateur utilisateurConnecte = utilisateurRepository.findByUsername(userConnecte.getUsername());

                Joueur joueur = joueurRepository.findById(utilisateurConnecte.getJoueur().getId()).orElse(null);

                if(joueur != null && partieRejointe.isInscriptionsOuvertes()) {
                    joueur.removePartie(partieRejointe);

                    partieRejointe.removeParticipant(joueur);
                    partieRepository.save(partieRejointe);

                    joueurRepository.save(joueur);
                }
            }
        }

        return "redirect:/player/join-game";
    }
}
