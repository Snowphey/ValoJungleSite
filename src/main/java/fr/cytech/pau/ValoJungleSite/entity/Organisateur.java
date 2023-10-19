package fr.cytech.pau.ValoJungleSite.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class Organisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String discordUsername;

    private String email;

    @OneToOne
    private Utilisateur utilisateur;

    @OneToMany
    private List<Evenement> evenementsCrees;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDiscordUsername() {
        return discordUsername;
    }

    public void setDiscordUsername(String discordUsername) {
        this.discordUsername = discordUsername;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public List<Evenement> getEvenementsCrees() {
        return evenementsCrees;
    }

    public void setEvenementsCrees(List<Evenement> evenementsCrees) {
        this.evenementsCrees = evenementsCrees;
    }
}
