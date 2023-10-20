package fr.cytech.pau.ValoJungleSite.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity

public class Joueur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String prenom;

    private String pseudo;

    private boolean estChef = false;

    @OneToOne
    private Utilisateur utilisateur;

    @ManyToOne
    private Guilde guilde;

    @OneToMany
    private List<Partie> parties;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public boolean isEstChef() {
        return estChef;
    }

    public void setEstChef(boolean estChef) {
        this.estChef = estChef;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Guilde getGuilde() {
        return guilde;
    }

    public void setGuilde(Guilde guilde) {
        this.guilde = guilde;
    }

    public List<Partie> getParties() {
        return parties;
    }

    public void setParties(List<Partie> parties) {
        this.parties = parties;
    }
}
