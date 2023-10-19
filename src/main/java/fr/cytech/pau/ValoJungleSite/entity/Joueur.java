package fr.cytech.pau.ValoJungleSite.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.Set;

@Entity

public class Joueur {

    @Id
    private long id;
    private String nom;
    private String prenom;
    private String pseudo;


    private void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
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

    @OneToOne
    private Utilisateur utilisateurJoueur;

    @OneToMany
    Set<Evenement> evenementInscrit;

    @OneToMany
    Set<Guilde> guildeCree;

}
